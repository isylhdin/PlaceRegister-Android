package com.placeregister.asynchtask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.placeregister.R;
import com.placeregister.constants.PlaceConstant;
import com.placeregister.places.Place;
import com.placeregister.places.PlaceType;
import com.placeregister.search.parameters.SearchGooglePlaceParam;
import com.placeregister.utils.PropertiesUtil;
import com.placeregister.utils.TypesUtil;

/**
 * Retrieve places in background and display them on the map 
 * @author yoann
 *
 */
public class DisplayPlaceService extends AsyncTask<SearchGooglePlaceParam, String, List<Place>>{

	/**
	 * Main activity context
	 */
	private Activity activity;

	/**
	 * Interesting place types contained in PlaceType Enum class
	 */
	private List<String> existingTypes;

	/**
	 * Google map object
	 */
	private GoogleMap mMap;
	
	
	private Map<Marker, Place> placeMap;

	/**
	 * Consume Google map webservice to retrieve interesting places around the user
	 */
	@Override
	protected List<Place> doInBackground(SearchGooglePlaceParam... arg) {
		List<Place> places = new ArrayList<Place>();
		try {
			activity = arg[0].getActivityContext();
			places = searchPlaces(arg[0].getLocation(), arg[0].getRadius());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return places;
	}

	/**
	 * Displays every retrieved place on the map, with a color depending on the type
	 */
	@Override
	protected void onPostExecute(List<Place> result) {
		super.onPostExecute(result);

		mMap = ((MapFragment) activity.getFragmentManager().findFragmentById(R.id.map)).getMap();
		existingTypes = PlaceType.getTypes();
		placeMap = new HashMap<Marker, Place>();

		for(Place place : result){
			place.removeUnsupportedTypes();
			addMarker(place);
		}
	}

	/**
	 * Construct a marker for a given place
	 * @param place
	 */
	public void addMarker(Place place){
		float color = TypesUtil.getColor(place.getTypes(), existingTypes);

		LatLng coord = new LatLng(place.getLatitude(), place.getLongitude());
		Marker currentMarker = mMap.addMarker(new MarkerOptions()
		.position(coord)
		.title(place.getName())
		.snippet(place.getTypes().toString() + " : " + TypesUtil.getMaxEarnablePoint(place.getTypes())+ " points")
		.icon(BitmapDescriptorFactory.defaultMarker(color)));

		placeMap.put(currentMarker, place);
		addMarkerListener(currentMarker);

	}

	/**
	 * Add a listener on the marker window info
	 * @param marker
	 * @param place
	 */
	public void addMarkerListener(Marker marker){
		mMap.setOnInfoWindowClickListener(
				new OnInfoWindowClickListener(){
					public void onInfoWindowClick(Marker marker){
						Toast t = Toast.makeText(activity, "Click info : " + marker.getTitle(), Toast.LENGTH_SHORT);
						t.show();
						
						new RegisterUserPlaceService().execute(placeMap.get(marker));
					}
				}
				);
	}

	/**
	 * POST request to google places web service, in order to retrieve interesting places around user
	 * @param location of user
	 * @param radius. The distance between user and the limit where we want places to display
	 * @return List of places we want to display on the map
	 * @throws Exception
	 */
	public List<Place> searchPlaces(Location location, double radius)
			throws Exception {

		List<Place> places = new ArrayList<Place>();
		List<String> types = PlaceType.getTypes();

		Uri uri = new Uri.Builder()
		.scheme("https")
		.authority(PlaceConstant.PLACES_SEARCH_URL)
		.path("maps/api/place/search/json")
		.appendQueryParameter("key", PropertiesUtil.getProperty(activity, "API_KEY"))
		.appendQueryParameter("location", location.getLatitude() + "," + location.getLongitude())
		.appendQueryParameter("radius", String.valueOf(radius))
		.appendQueryParameter("types", appendTypes(types))
		.appendQueryParameter("sensor", String.valueOf(true))
		.build();

		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(uri.toString()); 

		try {
			HttpResponse response = httpclient.execute(httpget);
			final int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				Log.e("Status Code", "Bad status code" + statusCode);
				return null;
			}
			places = getPlacesFromJSON(EntityUtils.toString(response.getEntity()));
		}catch(Exception e){
			e.printStackTrace();
		}
		return places;
	}

	/**
	 * Convert the JSON object returned by the google web service into a list of Places
	 * @param httpResult
	 * @return
	 * @throws JSONException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Place> getPlacesFromJSON(String httpResult) throws JSONException{

		List<Place> places = new ArrayList<Place>();

		JSONObject placesObject = new JSONObject(httpResult);
		JSONArray placesArray = (JSONArray) placesObject.get("results");

		for(int i=0; i<placesArray.length(); i++) {
			JSONObject json = placesArray.getJSONObject(i);
			Place place = new Place();
			place.setName(json.getString("name"));
			place.setId(json.getString("id"));
			place.setAddress(json.getString("vicinity"));

			String types = json.getString("types");
			types = types.replaceAll("[\\[\\]\"]", "");
			place.setTypes(new ArrayList(Arrays.asList(types.split(","))));

			JSONObject geometry = json.getJSONObject("geometry");
			JSONObject location = geometry.getJSONObject("location");
			place.setLatitude((Double) location.get("lat"));
			place.setLongitude((Double) location.get("lng"));

			places.add(place);
		}
		return places;
	}

	/**
	 * Append all types with | between them.
	 * 
	 * @param types
	 * @return
	 */
	private String appendTypes(List<String> types){
		StringBuilder allTypes = new StringBuilder();

		String prefix = "";
		for (String type : types) {
			allTypes.append(prefix);
			prefix = "|";
			allTypes.append(type);
		}
		return allTypes.toString();
	}
}

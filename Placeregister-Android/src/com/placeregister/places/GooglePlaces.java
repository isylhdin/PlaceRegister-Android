package com.placeregister.places;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.location.Location;
import android.net.Uri;
import android.util.Log;

import com.placeregister.utils.PropertiesUtil;

/**
 * TODO : Ce fichier doit contenir les classes utilitaires à la manipulation des données de google map
 * ==> Renommer la classe
 * @author yoann
 *
 */
public class GooglePlaces {

	private static final String PLACES_SEARCH_URL = "maps.googleapis.com";

	private Context context;

	public GooglePlaces(Context context){
		this.context = context;
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
		.authority(PLACES_SEARCH_URL)
		.path("maps/api/place/search/json")
		.appendQueryParameter("key", PropertiesUtil.getProperty(context, "API_KEY"))
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
			place.setReference(json.getString("reference"));

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
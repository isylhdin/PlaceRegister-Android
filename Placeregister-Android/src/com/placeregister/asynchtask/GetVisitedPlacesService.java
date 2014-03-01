package com.placeregister.asynchtask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

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
import com.placeregister.places.VisitedPlace;
import com.placeregister.search.parameters.PlaceMarkerParam;
import com.placeregister.search.parameters.SearchBDPlaceParam;
import com.placeregister.utils.TypesUtil;

public class GetVisitedPlacesService extends
		AsyncTask<SearchBDPlaceParam, String, List<VisitedPlace>> {

	/**
	 * Main activity context
	 */
	private Activity activity;

	/**
	 * Google map object
	 */
	private GoogleMap mMap;

	/**
	 * Mapping google marker <-> Place object
	 */
	private Map<Marker, Place> placeMap;

	/**
	 * Interesting place types contained in PlaceType Enum class
	 */
	private List<String> existingTypes;

	/**
	 * User position
	 */
	private Location userLocation;

	/**
	 * Places already visited by the user. Returned by our server
	 */
	private List<VisitedPlace> alreadyVisitedPlaces;

	/**
	 * Google places found in the previous asynckTask. @see
	 * GetGooglePlacesService
	 */
	private List<Place> googlePlaces;

	@Override
	protected List<VisitedPlace> doInBackground(SearchBDPlaceParam... params) {

		try {
			this.userLocation = params[0].getLocation();
			this.activity = params[0].getActivityContext();
			this.googlePlaces = params[0].getGooglePlaces();

			this.alreadyVisitedPlaces = searchVisitedPlaces(
					params[0].getUserTag(), userLocation);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return alreadyVisitedPlaces;
	}

	@Override
	protected void onPostExecute(List<VisitedPlace> alreadyVisitedPlaces) {
		super.onPostExecute(alreadyVisitedPlaces);

		mMap = ((MapFragment) this.activity.getFragmentManager()
				.findFragmentById(R.id.map)).getMap();

		// Display place markers on map
		displayMarkers(mMap);

	}

	/**
	 * 
	 * Distinguishes already visited places from new places and add them on map
	 * 
	 * @param mMap
	 * @param alreadyVisitedPlaces
	 */
	private void displayMarkers(GoogleMap mMap) {

		List<Place> newPlaces = new ArrayList<Place>();
		reshapeGooglePlaces();
		reshapeVisitedPlaces();

		// Each google place that don't have a corresponding place returned by
		// our server is a new place
		for (Place googlePlace : this.googlePlaces) {

			boolean hasFoundCorrespondingPlace = false;

			for (VisitedPlace visitedPlace : this.alreadyVisitedPlaces) {

				if (visitedPlace.getLatitude() == googlePlace.getLatitude()
						&& visitedPlace.getLongitude() == googlePlace
								.getLongitude()) {
					// Visited place returned by our server corresponds to a
					// google place
					hasFoundCorrespondingPlace = true;
					break;
				}
			}

			if (!hasFoundCorrespondingPlace) {
				newPlaces.add(googlePlace);
			}
		}

		// Displays new places on map
		for (Place newPlace : newPlaces) {
			addNewPlaceMarker(newPlace);
		}

		// Displays already visited places on map
		for (VisitedPlace visitedPlace : alreadyVisitedPlaces) {
			addVisitedPlaceMarker(visitedPlace);
		}
	}

	/**
	 * Change types to lower-case
	 */
	private void reshapeVisitedPlaces() {
		for (int index = 0; index < this.alreadyVisitedPlaces.size(); index++) {
			List<String> lowerCaseTypes = new ArrayList<String>();

			for (String placeType : this.alreadyVisitedPlaces.get(index)
					.getTypes()) {
				lowerCaseTypes.add(placeType.toLowerCase());
			}

			this.alreadyVisitedPlaces.get(index).setTypes(lowerCaseTypes);
		}
	}

	/**
	 * Remove unsupported types of the application to the google places
	 * 
	 * @param googlePlaces
	 * @return
	 */
	private void reshapeGooglePlaces() {
		existingTypes = PlaceType.getTypes();
		placeMap = new HashMap<Marker, Place>();

		for (Place place : this.googlePlaces) {
			place.removeUnsupportedTypes();
		}
	}

	/**
	 * Constructs a marker for a new place
	 * 
	 * @param place
	 */
	private void addNewPlaceMarker(Place place) {
		float color = TypesUtil.getColor(place.getTypes(), existingTypes);

		LatLng coord = new LatLng(place.getLatitude(), place.getLongitude());
		Marker currentMarker = mMap.addMarker(new MarkerOptions()
				.position(coord)
				.title(place.getName())
				.snippet(
						place.getTypes().toString()
								+ " : "
								+ TypesUtil.getMaxEarnablePoint(place
										.getTypes()) + " points")
				.icon(BitmapDescriptorFactory.defaultMarker(color)));

		placeMap.put(currentMarker, place);
		addMarkerListener(currentMarker);

	}

	/**
	 * Constructs a marker for a visited place
	 * 
	 * @param visitedPlace
	 */
	private void addVisitedPlaceMarker(VisitedPlace visitedPlace) {
		LatLng coord = new LatLng(visitedPlace.getLatitude(),
				visitedPlace.getLongitude());

		int markerResourceId = TypesUtil.getVisitedMarkerTypeId(visitedPlace
				.getTypes());

		Marker currentMarker = mMap.addMarker(new MarkerOptions()
				.position(coord)
				.title(visitedPlace.getName())
				.snippet(
						visitedPlace.getTypes().toString()
								+ " : "
								+ TypesUtil.getMaxEarnablePoint(visitedPlace
										.getTypes()) + " points")
				.icon(BitmapDescriptorFactory.fromResource(markerResourceId)));

		placeMap.put(currentMarker, visitedPlace);
		addMarkerListener(currentMarker);

	}

	/**
	 * Add a listener on the marker window info
	 * 
	 * @param marker
	 * @param place
	 */
	public void addMarkerListener(Marker marker) {
		mMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
			public void onInfoWindowClick(Marker marker) {

				// Sends a request to the server to register the visit
				PlaceMarkerParam markerParam = new PlaceMarkerParam(marker,
						placeMap.get(marker), null, activity);
				new GetTimeService().execute(markerParam);

			}
		});
	}

	/**
	 * Call our REST web service to retrieve visited places
	 * 
	 * @param userTag
	 * @param location
	 * @return
	 */
	private List<VisitedPlace> searchVisitedPlaces(String userTag,
			Location location) {
		String url = PlaceConstant.GET_VISITED_PLACES_URL;
		int statusCode = 0;
		List<VisitedPlace> places = new ArrayList<VisitedPlace>();

		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(url);

			Map<String, String> params = new HashMap<String, String>();
			params.put("longitude", String.valueOf(location.getLongitude()));
			params.put("latitude", String.valueOf(location.getLatitude()));

			// FIXME : Stub to transmit user info
			params.put("tag", "nickname2#241113");

			JSONObject holder = new JSONObject(params);

			StringEntity se = new StringEntity(holder.toString());
			httppost.setEntity(se);
			httppost.setHeader("Content-type", "application/json");

			HttpResponse response = httpclient.execute(httppost);
			statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				Log.e("Status Code", "Bad status code" + statusCode);
			}

			places = getPlacesFromJSON(EntityUtils.toString(response
					.getEntity()));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return places;
	}

	/**
	 * Parse JSON response to retrieve visited places
	 * 
	 * @param httpResult
	 * @return
	 * @throws JSONException
	 */
	public List<VisitedPlace> getPlacesFromJSON(String httpResult)
			throws JSONException {

		List<VisitedPlace> places = new ArrayList<VisitedPlace>();

		JSONArray placesArray = new JSONArray(httpResult);

		for (int i = 0; i < placesArray.length(); i++) {
			JSONObject json = placesArray.getJSONObject(i);
			VisitedPlace place = new VisitedPlace();
			place.setName(json.getString("name"));
			place.setId(json.getString("id"));
			place.setAddress(json.getString("address"));

			String types = json.getString("types");
			types = types.replaceAll("[\\[\\]\"]", "");
			place.setTypes(new ArrayList(Arrays.asList(types.split(","))));

			JSONObject location = json.getJSONObject("location");
			place.setLatitude((Double) location.get("latitude"));
			place.setLongitude((Double) location.get("longitude"));

			places.add(place);
		}
		return places;
	}

}

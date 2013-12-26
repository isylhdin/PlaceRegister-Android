package com.placeregister.places;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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

public class GooglePlaces {

	private static final String PLACES_SEARCH_URL = "maps.googleapis.com";

	private Context context;

	public GooglePlaces(Context context){
		this.context = context;
	}

	private String buildURL(Location location, double radius) throws UnsupportedEncodingException{
		StringBuilder url = new StringBuilder(PLACES_SEARCH_URL);
		url.append("key=" + PropertiesUtil.getProperty(context, "API_KEY"));
		url.append("&");
		url.append("location=" + location.getLatitude() + "," + location.getLongitude());
		url.append("&");
		url.append("radius=" + radius); // in meters.
		url.append("&");
		url.append("types=" + URLEncoder.encode(PropertiesUtil.getProperty(context, "types"),"UTF-8"));
		url.append("&");
		url.append("sensor=true");

		return url.toString();
	}

	/**
	 * 
	 * @param location
	 * @param radius
	 * @return
	 * @throws Exception
	 */
	public List<Place> searchPlaces(Location location, double radius)
			throws Exception {

		List<Place> places = new ArrayList<Place>();

		Uri uri = new Uri.Builder()
		.scheme("https")
		.authority(PLACES_SEARCH_URL)
		.path("maps/api/place/search/json")
		.appendQueryParameter("key", PropertiesUtil.getProperty(context, "API_KEY"))
		.appendQueryParameter("location", location.getLatitude() + "," + location.getLongitude())
		.appendQueryParameter("radius", String.valueOf(radius))
		.appendQueryParameter("types", PropertiesUtil.getProperty(context, "types"))
		.appendQueryParameter("sensor", String.valueOf(true))
		.build();


//		URI uri = new URI (buildURL(location, radius));

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
			types = types.replace("[", "");
			types= types.replace("]", "");
			place.setTypes(new ArrayList(Arrays.asList(types)));

			JSONObject geometry = json.getJSONObject("geometry");
			JSONObject location = geometry.getJSONObject("location");
			place.setLatitude((Double) location.get("lat"));
			place.setLongitude((Double) location.get("lng"));

			places.add(place);
		}

		return places;
	}


}
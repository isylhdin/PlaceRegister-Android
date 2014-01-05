package com.placeregister.asynchtask;

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
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.Marker;
import com.placeregister.R;
import com.placeregister.places.Place;
import com.placeregister.places.PlaceType;

public class RegisterUserPlaceService extends
		AsyncTask<Place, String, String> {
	
	/**
	 * Back end url to add a new place in DataBase
	 */
	private static final String ADD_PLACE_URL = "http://192.168.0.4:8080/rest/place/service/add/place";
	
	@Override
	protected String doInBackground(Place... place) {
		return addPlace(place[0]);
	}
	
	@Override
	protected void onPostExecute(String placeId) {
		super.onPostExecute(placeId);
		
	}

	/**
	 * POST request to add a new place to DataBase
	 * @param place
	 * @return TODO
	 */
	public String addPlace(Place place){

		String url = ADD_PLACE_URL;
		String id = null;
		int statusCode = 0;
		
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(url);

			Map<String,String> params = new HashMap<String, String>();
			params.put("name", place.getName());
			params.put("id", place.getId());
			params.put("types", place.getTypes().toString());
			params.put("address", place.getAddress());
			params.put("longitude", String.valueOf(place.getLongitude()));
			params.put("latitude", String.valueOf(place.getLatitude()));
			
			// FIXME : Stub to transmit user info
			params.put("tag", "nickname1#241113");
			
			JSONObject holder = new JSONObject(params);

			StringEntity se = new StringEntity(holder.toString());
			httppost.setEntity(se);
			httppost.setHeader("Content-type", "application/json");

			HttpResponse response = httpclient.execute(httppost);
			statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				Log.e("Status Code", "Bad status code" + statusCode);
			}
			id = getId(EntityUtils.toString(response.getEntity()));
		}catch(Exception e){
			e.printStackTrace();
		}
		return id;
	}

	private String getId(String httpResult) {
		try {
			JSONObject placeObject = new JSONObject(httpResult);
			return placeObject.getString("id");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
}
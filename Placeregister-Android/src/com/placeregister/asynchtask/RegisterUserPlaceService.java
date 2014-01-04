package com.placeregister.asynchtask;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import com.placeregister.places.Place;

import android.os.AsyncTask;
import android.util.Log;

public class RegisterUserPlaceService extends
		AsyncTask<Place, String, String> {
	
	/**
	 * Back end url to add a new place in DataBase
	 */
	private static final String ADD_PLACE_URL = "http://192.168.0.4:8080/rest/service/add/place";
	
	@Override
	protected String doInBackground(Place... place) {
		addPlace(place[0]);
		return null;
	}

	/**
	 * POST request to add a new place to DataBase
	 * @param place
	 */
	public void addPlace(Place place){

		String url = ADD_PLACE_URL;
		int statusCode = 0;
		
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(url);

			Map<String,String> params = new HashMap<String, String>();
			params.put("name", place.getName());
			params.put("reference", place.getReference());
			params.put("types", place.getTypes().toString());
			params.put("address", place.getAddress());
			JSONObject holder = new JSONObject(params);

			StringEntity se = new StringEntity(holder.toString());
			httppost.setEntity(se);
			httppost.setHeader("Content-type", "application/json");

			HttpResponse response = httpclient.execute(httppost);
			statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				Log.e("Status Code", "Bad status code" + statusCode);
			}

		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
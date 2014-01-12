package com.placeregister.asynchtask;

import java.util.ArrayList;
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
import org.json.JSONObject;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.placeregister.constants.PlaceConstant;
import com.placeregister.places.Place;
import com.placeregister.search.parameters.SearchBDPlaceParam;

public class GetVisitedPlacesService extends AsyncTask<SearchBDPlaceParam, String, List<Place>>{



	@Override
	protected List<Place> doInBackground(SearchBDPlaceParam... params) {
		List<Place> places = new ArrayList<Place>();
		try {
			places = searchVisitedPlaces(params[0].getUserTag(), params[0].getLocation());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return places;
	}
	
	@Override
	protected void onPostExecute(List<Place> result) {
		super.onPostExecute(result);
	}
	
	private List<Place> searchVisitedPlaces(String userTag, Location location) {
		String url = PlaceConstant.GET_VISITED_PLACES_URL;
		int statusCode = 0;
		List<Place> places = new ArrayList<Place>();
		
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(url);

			Map<String,String> params = new HashMap<String, String>();
			params.put("longitude", String.valueOf(location.getLongitude()));
			params.put("latitude", String.valueOf(location.getLatitude()));
			
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
			
			EntityUtils.toString(response.getEntity());
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}


}

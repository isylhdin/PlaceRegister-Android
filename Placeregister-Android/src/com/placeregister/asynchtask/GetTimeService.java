package com.placeregister.asynchtask;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

import com.placeregister.places.Place;
import com.placeregister.search.parameters.PlaceMarkerParam;

public class GetTimeService extends AsyncTask<PlaceMarkerParam, String, String> {

	/**
	 * Object containing marker reference, the place to register and its
	 * timeZone
	 */
	private PlaceMarkerParam placeMarkerParam;

	@Override
	protected String doInBackground(PlaceMarkerParam... placeMarkerParam) {
		this.placeMarkerParam = placeMarkerParam[0];
		return getLocalTime(this.placeMarkerParam.getPlace());
	}

	@Override
	protected void onPostExecute(String timeZone) {

		this.placeMarkerParam.setTimeZone(timeZone);

		// Once we have the place's timeZone we can register the user's visit
		new RegisterUserPlaceService().execute(this.placeMarkerParam);
	}

	private String getLocalTime(Place place) {

		double latitude = place.getLatitude();
		double longitude = place.getLongitude();

		String timeZone = null;

		long timeStamp = System.currentTimeMillis() / 1000;

		String url = "https://maps.googleapis.com/maps/api/timezone/json?location="
				+ latitude
				+ ","
				+ longitude
				+ "&timestamp="
				+ timeStamp
				+ "&sensor=true";
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet httpget = new HttpGet(url);

			HttpResponse response = httpclient.execute(httpget);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				Log.e("Status Code", "Bad status code" + statusCode);
			}

			timeZone = getTimeZoneId(EntityUtils.toString(response.getEntity()));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return timeZone;
	}

	private String getTimeZoneId(String timeZoneResponse) {
		String timeZone = null;

		try {
			JSONObject json = new JSONObject(timeZoneResponse);
			timeZone = (String) json.get("timeZoneId");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return timeZone;
	}

}

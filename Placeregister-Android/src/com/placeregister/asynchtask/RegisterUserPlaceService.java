package com.placeregister.asynchtask;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.placeregister.constants.AddPlaceStatusConstant;
import com.placeregister.constants.PlaceConstant;
import com.placeregister.places.Place;
import com.placeregister.search.parameters.PlaceMarkerParam;
import com.placeregister.utils.TypesUtil;

/**
 * Submit a place to our server, to register it
 * 
 * @author yoann
 * 
 */
public class RegisterUserPlaceService extends
		AsyncTask<PlaceMarkerParam, String, Integer> {

	/**
	 * Marker represented on map
	 */
	private Marker marker;

	/**
	 * The corresponding place to register
	 */
	private Place place;

	/**
	 * TimeZone of the given place
	 */
	private String timeZone;

	/**
	 * Calling activity
	 */
	private Activity activity;

	@Override
	protected Integer doInBackground(PlaceMarkerParam... placeMarkerParam) {
		this.marker = placeMarkerParam[0].getMarker();
		this.place = placeMarkerParam[0].getPlace();
		this.timeZone = placeMarkerParam[0].getTimeZone();
		this.activity = placeMarkerParam[0].getContext();

		return addPlace(place);
	}

	@Override
	protected void onPostExecute(Integer serverStatus) {
		super.onPostExecute(serverStatus);

		int resourceId = activity.getResources().getIdentifier(
				"status_" + serverStatus, "string", activity.getPackageName());
		Toast t = Toast.makeText(this.activity, resourceId, Toast.LENGTH_SHORT);
		t.show();

		if (AddPlaceStatusConstant.OK == serverStatus) {
			int markerId = TypesUtil.getVisitedMarkerTypeId(place.getTypes());
			marker.setIcon(BitmapDescriptorFactory.fromResource(markerId));
		}

	}

	/**
	 * POST request to add a new place to DataBase
	 * 
	 * @param place
	 * @return the new place id
	 */
	public int addPlace(Place place) {

		String url = PlaceConstant.ADD_PLACE_URL;
		int applicationStatus = 0;

		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(url);

			Map<String, String> params = new HashMap<String, String>();
			params.put("name", place.getName());
			params.put("id", place.getId());
			params.put("types", place.getTypes().toString());
			params.put("address", place.getAddress());
			params.put("longitude", String.valueOf(place.getLongitude()));
			params.put("latitude", String.valueOf(place.getLatitude()));
			params.put("timeZone", this.timeZone);

			// FIXME : Stub to transmit user info
			params.put("tag", "nickname2#241113");

			JSONObject holder = new JSONObject(params);

			StringEntity se = new StringEntity(holder.toString());
			httppost.setEntity(se);
			httppost.addHeader("Content-Type", "application/json");
			httppost.addHeader("Accept", "application/json");

			HttpResponse response = httpclient.execute(httppost);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				Log.e("Status Code", "Bad status code" + statusCode);
			}

			applicationStatus = Integer.valueOf(EntityUtils.toString(response
					.getEntity()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return applicationStatus;
	}
}
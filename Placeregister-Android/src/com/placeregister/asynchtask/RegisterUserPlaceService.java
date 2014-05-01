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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.placeregister.R;
import com.placeregister.application.ApplicationInfo;
import com.placeregister.constants.AddPlaceStatusConstant;
import com.placeregister.constants.URLConstant;
import com.placeregister.model.Achievement;
import com.placeregister.model.UserInfo;
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
		AsyncTask<PlaceMarkerParam, String, UserInfo> {

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

	/**
	 * Selected type
	 */
	private String selectedType;

	@Override
	protected UserInfo doInBackground(PlaceMarkerParam... placeMarkerParam) {
		this.marker = placeMarkerParam[0].getMarker();
		this.place = placeMarkerParam[0].getPlace();
		this.timeZone = placeMarkerParam[0].getTimeZone();
		this.activity = placeMarkerParam[0].getContext();
		this.selectedType = placeMarkerParam[0].getSelectedType();

		return addPlace(place);
	}

	@Override
	protected void onPostExecute(UserInfo userInfo) {
		super.onPostExecute(userInfo);

		int resourceId = activity.getResources().getIdentifier(
				"status_" + userInfo.getCodeStatus(), "string",
				activity.getPackageName());
		Toast t = Toast.makeText(this.activity, resourceId, Toast.LENGTH_SHORT);
		t.show();

		// Update global variable : score
		ApplicationInfo appInfo = (ApplicationInfo) activity.getApplication();
		// compute how many points user has earned submitting his place
		int computedEarnedPoints = Integer.parseInt(userInfo.getScore())
				- Integer.parseInt(appInfo.getUser().getScore());
		appInfo.getUser().setScore(userInfo.getScore());

		// TODO refactor
		if (AddPlaceStatusConstant.OK == userInfo.getCodeStatus()) {
			// Change submited place icon to another icon representing a visited
			// place
			int markerId = TypesUtil.getVisitedMarkerTypeId(place.getTypes());
			marker.setIcon(BitmapDescriptorFactory.fromResource(markerId));

			// Display a custom dialog to show unlocked achievement
			if (userInfo.getAchievements() != null
					&& !userInfo.getAchievements().isEmpty()) {

				for (Achievement unlockedAchievement : userInfo
						.getAchievements()) {

					StringBuilder sb = new StringBuilder();
					sb.append(unlockedAchievement.getTitle());
					sb.append(" : ");
					sb.append(unlockedAchievement.getDescription());

					AlertDialog.Builder achievementDialogBuilder = new AlertDialog.Builder(
							this.activity);
					achievementDialogBuilder
							.setTitle(R.string.unlocked_achievement)
							.setMessage(sb).setCancelable(false)
							.setPositiveButton(R.string.OK, null);
					AlertDialog achievementAlert = achievementDialogBuilder
							.create();
					achievementAlert.show();
				}
			}

			// Display a custom dialog to show how much points user has won
			AlertDialog.Builder scoreDialogBuilder = new AlertDialog.Builder(
					this.activity);
			scoreDialogBuilder
					.setTitle(R.string.congratulation)
					.setMessage(
							this.activity.getString(
									R.string.you_earned_some_points,
									computedEarnedPoints)).setCancelable(false)
					.setPositiveButton(R.string.OK, null);
			AlertDialog scoreAlert = scoreDialogBuilder.create();
			scoreAlert.show();

		}

	}

	/**
	 * POST request to add a new place to DataBase
	 * 
	 * @param place
	 * @return the new place id
	 */
	public UserInfo addPlace(Place place) {

		String url = URLConstant.ADD_PLACE_URL;
		UserInfo userInfo = null;

		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(url);

			Map<String, String> params = new HashMap<String, String>();
			params.put("name", place.getName());
			params.put("id", place.getId());
			params.put("types", place.getTypes().toString());
			params.put("selectedType", this.selectedType);
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

			userInfo = getUserInfoFromJSON(EntityUtils.toString(response
					.getEntity()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userInfo;
	}

	/**
	 * Parse JSON response to retrieve user info
	 * 
	 * @param httpResult
	 * @return
	 * @throws JSONException
	 */
	public UserInfo getUserInfoFromJSON(String httpResult) throws JSONException {

		UserInfo info = new UserInfo();
		JSONObject json = new JSONObject(httpResult);

		JSONArray achievements = json.getJSONArray("achievements");
		List<Achievement> unlockedAchievements = new ArrayList<Achievement>();
		for (int i = 0; i < achievements.length(); i++) {
			JSONObject achievementJSON = achievements.getJSONObject(i);
			Achievement achievement = new Achievement();
			achievement
					.setDescription(achievementJSON.getString("description"));
			achievement.setTitle(achievementJSON.getString("title"));
			achievement.setPoints(achievementJSON.getLong("points"));

			unlockedAchievements.add(achievement);
		}

		info.setAchievements(unlockedAchievements);
		info.setScore(json.getString("score"));
		info.setCodeStatus(Integer.parseInt(json.getString("codeStatus")));

		return info;
	}

}
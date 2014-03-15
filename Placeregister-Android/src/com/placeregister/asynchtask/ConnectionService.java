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
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.placeregister.activity.MapActivity;
import com.placeregister.application.ApplicationInfo;
import com.placeregister.constants.URLConstant;
import com.placeregister.model.User;

public class ConnectionService extends AsyncTask<Activity, String, Integer> {

	private Activity activity;

	private int connectionStub() {
		return 200;
	}

	@Override
	protected Integer doInBackground(Activity... activity) {

		int statusCode = 0;
		String url = URLConstant.LOGIN_URL;
		this.activity = activity[0];

		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);

		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("mail", "user2");
			params.put("password", "user2");
			JSONObject holder = new JSONObject(params);

			StringEntity se = new StringEntity(holder.toString());
			httppost.setEntity(se);
			httppost.setHeader("Content-type", "application/json");

			HttpResponse response = httpclient.execute(httppost);
			statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				Log.e("Status Code", "Bad status code" + statusCode);
				return statusCode;
			}

			User userInfo = getUserInfoFromJSON(EntityUtils.toString(response
					.getEntity()));

			// Save user info as a global property
			ApplicationInfo appInfo = (ApplicationInfo) this.activity
					.getApplicationContext();
			appInfo.setUser(userInfo);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return statusCode;

	}

	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);

		String state = (result == 200) ? "You are connected"
				: "Bad login/password";

		Toast t = Toast.makeText(activity, state, Toast.LENGTH_SHORT);
		t.show();

		if (result.equals(200)) {
			Intent intent = new Intent(activity, MapActivity.class);
			activity.startActivity(intent);
		}

	}

	/**
	 * Parse JSON response to retrieve user info
	 * 
	 * @param httpResult
	 * @return
	 * @throws JSONException
	 */
	public User getUserInfoFromJSON(String httpResult) throws JSONException {

		User user = new User();
		JSONObject json = new JSONObject(httpResult);

		user.setTag(json.getString("tag"));
		user.setNickName(json.getString("nickName"));
		user.setAvatarRef(json.getString("avatarRef"));
		user.setScore(json.getString("score"));

		return user;
	}

}

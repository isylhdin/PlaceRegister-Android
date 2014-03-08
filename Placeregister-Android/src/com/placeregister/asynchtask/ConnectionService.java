package com.placeregister.asynchtask;

import com.placeregister.activity.MapActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

public class ConnectionService extends AsyncTask<Activity, String, Integer>{

	private final String CONNECTION_URL ="http://192.168.0.4:8080/rest/service/login";
	private Activity activity;
	
	
	
	private int connectionStub(){
		return 200;
	}
	
	
	@Override
	protected Integer doInBackground(Activity... activity) {
		
//		int statusCode = 0;
//		String url = CONNECTION_URL;
//		this.activity = activity[0];
//
//		HttpClient httpclient = new DefaultHttpClient();
//		HttpPost httppost = new HttpPost(url);
//
//		try {
//			Map<String,String> params = new HashMap<String, String>();
//			params.put("mail", "user1");
//			params.put("password", "user1");
//		    JSONObject holder = new JSONObject(params);
//		    
//		    StringEntity se = new StringEntity(holder.toString());
//			httppost.setEntity(se);
//			httppost.setHeader("Content-type", "application/json");
//
//			HttpResponse response = httpclient.execute(httppost);
//			statusCode = response.getStatusLine().getStatusCode();
//			if (statusCode != HttpStatus.SC_OK) {
//				Log.e("Status Code", "Bad status code" + statusCode);
//			}
//
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		return statusCode;
		
		this.activity = activity[0];
		return connectionStub();
	}
	
	
	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);
		
		String state = (result == 200) ? "You are connected": "Bad login/password";
		
		Toast t = Toast.makeText(activity, state, Toast.LENGTH_SHORT);
		t.show();
		
		if (result.equals(200)) {
			Intent intent = new Intent(activity, MapActivity.class);
			activity.startActivity(intent);
		}
		 
	}

}

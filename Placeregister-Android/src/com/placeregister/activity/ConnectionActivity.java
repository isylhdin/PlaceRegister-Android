package com.placeregister.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.placeregister.R;
import com.placeregister.asynchtask.ConnectionService;
import com.placeregister.utils.ConnectionUtils;

public class ConnectionActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connection);

		String hasInternet = (ConnectionUtils.hasConnection(this)) ? "You are connected to the Internet"
				: "Please connect to the Internet";
		TextView textViewInternet = (TextView) findViewById(R.id.hasInternet);
		textViewInternet.setText(hasInternet);

		String hasGPSEnabled = (ConnectionUtils.hasGPSEnabled(this)) ? "Your GPS is enabled"
				: "Please enable your GPS";
		// TODO : POPUP local settings Activity to activate GPS
		TextView textViewGPS = (TextView) findViewById(R.id.hasGPSEnabled);
		textViewGPS.setText(hasGPSEnabled);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.connection, menu);
		return true;
	}

	public void connect(View view) {
		new ConnectionService().execute(this);
	}

}

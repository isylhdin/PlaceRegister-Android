package com.placeregister;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends FragmentActivity {

	GoogleMap mMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		getCurrentPosition();
		setPositionOnMap();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	/**
	 * Displays the user's current position on the map
	 */
	public void getCurrentPosition(){

		if (mMap == null) {
			mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
					.getMap();

			if (mMap != null) {
				mMap.setMyLocationEnabled(true);
			} else {
				Log.e("MainActivity", "GoogleMap object has not been instantiated");
			}
		}
	}
	
	/**
	 * Centers the view on the user's position and zoom on it  
	 */
	private void setPositionOnMap() {
		Criteria criteria = new Criteria();
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		String provider = locationManager.getBestProvider(criteria, false);
		Location location = locationManager.getLastKnownLocation(provider);
		double lat =  location.getLatitude();
		double lng = location.getLongitude();
		LatLng coordinate = new LatLng(lat, lng);

		CameraUpdate center=
				CameraUpdateFactory.newLatLng(coordinate);
		CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);

		mMap.moveCamera(center);
		mMap.animateCamera(zoom);
	}

}

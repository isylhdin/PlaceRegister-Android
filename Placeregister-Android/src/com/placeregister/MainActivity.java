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

public class MainActivity extends FragmentActivity 
{
	// The maximum time that should pass before the user gets a location update.
	private GoogleMap mMap;
	private final int zoomLevel = 16;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		getMapObject();
		setPositionOnMap(retrieveLocation(), zoomLevel);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * Get the GoogleMap object
	 */
	public void getMapObject(){
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
	 * Retrieves the user location
	 * @return
	 */
	public Location retrieveLocation(){
		LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		Location myLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		if (myLocation == null) {
			Criteria criteria = new Criteria();
			criteria.setAccuracy(Criteria.ACCURACY_FINE);
			String provider = lm.getBestProvider(criteria, true);
			myLocation = lm.getLastKnownLocation(provider);
		}
		return myLocation;
	}
	
	/**
	 * Centers the view on the user's position and zoom on it  
	 */
	private void setPositionOnMap(Location location, int zoomLevel) {

		double lat =  location.getLatitude();
		double lng = location.getLongitude();
		LatLng coordinate = new LatLng(lat, lng);
		CameraUpdate center=
				CameraUpdateFactory.newLatLng(coordinate);
		CameraUpdate zoom=CameraUpdateFactory.zoomTo(zoomLevel);

		mMap.moveCamera(center);
		mMap.animateCamera(zoom);

	}
}

package com.placeregister.activity;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.placeregister.R;
import com.placeregister.asynchtask.PlaceService;
import com.placeregister.places.PlaceParam;

public class MainActivity extends FragmentActivity 
implements
GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener,
LocationListener 
{

	// Milliseconds per second
	private static final int MILLISECONDS_PER_SECOND = 500;
	// Update frequency in seconds
	public static final int UPDATE_INTERVAL_IN_SECONDS = 5;
	// Update frequency in milliseconds
	private static final long UPDATE_INTERVAL =
			MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;
	// The fastest update frequency, in seconds
	private static final int FASTEST_INTERVAL_IN_SECONDS = 1;
	// A fast frequency ceiling in milliseconds
	private static final long FASTEST_INTERVAL =
			MILLISECONDS_PER_SECOND * FASTEST_INTERVAL_IN_SECONDS;

	private GoogleMap mMap;
	private final int zoomLevel = 14;
	private final int radius = 2000;

	private LocationClient mLocationClient;
	private LocationRequest mLocationRequest;
	private LocationManager locationManager;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Acquire a reference to the system Location Manager
		locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

		// Create the LocationRequest object
		mLocationRequest = LocationRequest.create();
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		mLocationRequest.setInterval(UPDATE_INTERVAL);
		mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

		mLocationClient = new LocationClient(this, this, this);

		getMapObject();
	}

	@Override
	protected void onStart() {
		super.onStart();
		mLocationClient.connect();
	}

	@Override
	protected void onStop() {
		if (mLocationClient.isConnected()) {
			mLocationClient.removeLocationUpdates(this);
		}
		mLocationClient.disconnect();
		super.onStop();
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

	public void onLocationChanged(Location location) {
	}

	public void onConnectionFailed(ConnectionResult result) {

	}
	public void onConnected(Bundle connectionHint) {
		Criteria crit = new Criteria();
		crit.setAccuracy(Criteria.ACCURACY_FINE);
		crit.setPowerRequirement(Criteria.POWER_LOW);   

		boolean gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		if (gps_enabled) {
			Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if (location == null) {
				mLocationClient.requestLocationUpdates(mLocationRequest, this);
			} else {
				Toast t = Toast.makeText(this, "La position a été retrouvé à partir de LastKnownLocation", Toast.LENGTH_SHORT);
				t.show();
				
				try {
					PlaceParam params = new PlaceParam(this, location, radius);
					new PlaceService().execute(params);
				} catch (Exception e) {
					e.printStackTrace();
				}
			

				setPositionOnMap(location, zoomLevel);
			}
		}

	}

	public void onDisconnected() {

	}
}

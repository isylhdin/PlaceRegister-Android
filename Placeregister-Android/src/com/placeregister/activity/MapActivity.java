package com.placeregister.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.FragmentManager;
import android.content.Context;
import android.content.res.Configuration;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

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
import com.placeregister.adapter.NavDrawerListAdapter;
import com.placeregister.asynchtask.GetGooglePlacesService;
import com.placeregister.constants.MapConstant;
import com.placeregister.model.NavDrawerItem;
import com.placeregister.search.parameters.SearchGooglePlaceParam;

/**
 * 
 * FIXME : Externalize Navigation drawer methods + create Activities instead of
 * fragments when an item is clicked
 * 
 * @author yoann
 * 
 */
public class MapActivity extends FragmentActivity implements
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener, LocationListener {

	/** Map reference */
	private GoogleMap mMap;

	private LocationClient mLocationClient;
	private LocationRequest mLocationRequest;
	private LocationManager locationManager;

	/** Navigation drawer attributes */
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	// nav drawer title
	private CharSequence mDrawerTitle;

	// used to store app title
	private CharSequence mTitle;

	// slide menu items
	private List<String> navMenuTitles;
	private List<Integer> navMenuIcons;

	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		/** Navigation drawer initialization */

		mTitle = mDrawerTitle = getTitle();

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		navMenuTitles = getNavigationDrawerTexts();
		navMenuIcons = getNavigationDrawerIcons();

		navDrawerItems = new ArrayList<NavDrawerItem>();

		// adding nav drawer items to array
		navDrawerItems.add(new NavDrawerItem(navMenuTitles.get(0), navMenuIcons
				.get(0)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles.get(1), navMenuIcons
				.get(1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles.get(2), navMenuIcons
				.get(2)));

		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);
		mDrawerList.setAdapter(adapter);
		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		// enabling action bar app icon and behaving it as toggle button
		getActionBar().setDisplayHomeAsUpEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.app_name, R.string.app_name) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		/** Object initialization to update user position on map */
		locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		mLocationRequest = LocationRequest.create();
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		mLocationRequest.setInterval(MapConstant.UPDATE_INTERVAL);
		mLocationRequest.setFastestInterval(MapConstant.FASTEST_INTERVAL);

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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action bar actions click
		switch (item.getItemId()) {
		case R.id.action_settings:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			displayView(position);
		}
	}

	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	private void displayView(int position) {
		// update the main content by replacing fragments
		android.app.Fragment fragment = null;
		switch (position) {
		case 0:
			fragment = new HomeFragment();
			break;
		case 1:
			fragment = new FindPeopleFragment();
			break;
		case 2:
			fragment = new PhotosFragment();
			break;
		default:
			break;
		}

		if (fragment != null) {
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.map, fragment)
					.commit();

			// update selected item and title, then close the drawer
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(navMenuTitles.get(position));
			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
	}

	/***
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * Get the GoogleMap object
	 */
	public void getMapObject() {
		if (mMap == null) {
			mMap = ((MapFragment) getFragmentManager().findFragmentById(
					R.id.map)).getMap();

			if (mMap != null) {
				mMap.setMyLocationEnabled(true);
			} else {
				Log.e("MainActivity",
						"GoogleMap object has not been instantiated");
			}
		}
	}

	/**
	 * Centers the view on the user's position and zoom on it
	 */
	private void setPositionOnMap(Location location, int zoomLevel) {

		double lat = location.getLatitude();
		double lng = location.getLongitude();
		LatLng coordinate = new LatLng(lat, lng);
		CameraUpdate center = CameraUpdateFactory.newLatLng(coordinate);
		CameraUpdate zoom = CameraUpdateFactory.zoomTo(zoomLevel);

		mMap.moveCamera(center);
		mMap.animateCamera(zoom);
	}

	public List<String> getNavigationDrawerTexts() {
		List<String> items = new ArrayList<String>();
		items.add(getResources().getString(R.string.navigation_countdown));
		items.add(getResources().getString(
				R.string.navigation_last_achievements));
		items.add(getResources()
				.getString(R.string.navigation_all_achievements));
		return items;
	}

	public List<Integer> getNavigationDrawerIcons() {
		List<Integer> icons = new ArrayList<Integer>();
		icons.add(R.drawable.ic_home);
		icons.add(R.drawable.ic_people);
		icons.add(R.drawable.ic_photos);

		return icons;
	}

	public void onLocationChanged(Location location) {
		// CircleOptions circleOptions = new CircleOptions()
		// .center(new LatLng(location.getLatitude(), location.getLongitude()))
		// .radius(1000);
		//
		// Circle circle = mMap.addCircle(circleOptions);

	}

	public void onConnectionFailed(ConnectionResult result) {

	}

	public void onConnected(Bundle connectionHint) {
		Criteria crit = new Criteria();
		crit.setAccuracy(Criteria.ACCURACY_FINE);
		crit.setPowerRequirement(Criteria.POWER_LOW);

		boolean gps_enabled = locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);
		if (gps_enabled) {
			Location location = locationManager
					.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if (location == null) {
				mLocationClient.requestLocationUpdates(mLocationRequest, this);
			} else {
				setPositionOnMap(location, MapConstant.ZOOM_LEVEL);
			}

			try {
				SearchGooglePlaceParam googleParams = new SearchGooglePlaceParam(
						this, location, MapConstant.RADIUS);
				new GetGooglePlacesService().execute(googleParams);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void onDisconnected() {

	}
}

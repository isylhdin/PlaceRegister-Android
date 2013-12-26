package com.placeregister.asynchtask;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.os.AsyncTask;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.placeregister.R;
import com.placeregister.places.GooglePlaces;
import com.placeregister.places.Place;
import com.placeregister.places.PlaceParam;

public class PlaceService extends AsyncTask<PlaceParam, String, List<Place>>{

	GooglePlaces googlePlaces;
	Activity activity;

	@Override
	protected List<Place> doInBackground(PlaceParam... arg) {
		List<Place> places = new ArrayList<Place>();
		try {
			activity = arg[0].getActivityContext();
			googlePlaces = new GooglePlaces(activity.getApplicationContext());
			places = googlePlaces.searchPlaces(arg[0].getLocation(), arg[0].getRadius());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return places;
	}

	@Override
	protected void onPostExecute(List<Place> result) {
		super.onPostExecute(result);

		GoogleMap mMap = ((MapFragment) activity.getFragmentManager().findFragmentById(R.id.map))
				.getMap();

		for(Place place : result){
			LatLng coord = new LatLng(place.getLatitude(), place.getLongitute());
			mMap.addMarker(new MarkerOptions()
			.position(coord)
			.title(place.getName()));

		}
	}
}

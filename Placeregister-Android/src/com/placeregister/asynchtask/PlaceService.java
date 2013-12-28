package com.placeregister.asynchtask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.placeregister.R;
import com.placeregister.places.GooglePlaces;
import com.placeregister.places.Place;
import com.placeregister.places.PlaceParam;
import com.placeregister.places.PlaceType;

/**
 * Retrieve places in background and display them on the map 
 * @author yoann
 *
 */
public class PlaceService extends AsyncTask<PlaceParam, String, List<Place>>{

	/**
	 * Google places object
	 */
	private GooglePlaces googlePlaces;
	
	/**
	 * Main activity context
	 */
	private Activity activity;
	
	/**
	 * Interesting place types contained in PlaceType Enum class
	 */
	private List<String> existingTypes;

	/**
	 * Consumme Google map webservice to retrieve interesting places around the user
	 */
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

	/**
	 * Displays every retrieved place on the map, with a color depending on the type
	 */
	@Override
	protected void onPostExecute(List<Place> result) {
		super.onPostExecute(result);

		GoogleMap mMap = ((MapFragment) activity.getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		
		existingTypes = new ArrayList<String>();
		for (PlaceType place : Arrays.asList(PlaceType.values())) {
			existingTypes.add(place.getType());
		}
		
		for(Place place : result){
			
			float color = getColor(place.getTypes());
			
			LatLng coord = new LatLng(place.getLatitude(), place.getLongitute());
			mMap.addMarker(new MarkerOptions()
			.position(coord)
			.title(place.getName())
			.snippet(place.getTypes().toString())
			.icon(BitmapDescriptorFactory.defaultMarker(color)));
		}
	}
	
	
	// TODO move these function into another utility class
	
	/**
	 * Returns the color to display for a place, giving its different types
	 * @param types Different types belonging to a place
	 * @return a color (as a float)
	 */
	public float getColor(List<String> types) {
		List<PlaceType> colors = new ArrayList<PlaceType>();
		
		for (String type : types) {
			if(existingTypes.contains(type)){
				colors.add(PlaceType.getPlaceByType(type));
			}
		}
		
		float color;
		if (colors.size() == 1){
			color = colors.get(0).getColor();
		} else {
			color = getColorOfPlaceGivingMaxPoint(colors);
		}
		
		return color;
	}
	
	
	/**
	 * Returns the type's color giving the maximum amount of point in the list.
	 *
	 * @param different types returned by google map web services. A place can have several types
	 * @return the max element
	 */
	public static float getColorOfPlaceGivingMaxPoint(List<PlaceType> places){
		Iterator<PlaceType> typeIterator = places.iterator();
		PlaceType candidate = typeIterator.next();

		while (typeIterator.hasNext() ){
			PlaceType next = typeIterator.next();
			if (next.getPoints() > candidate.getPoints()){
				candidate = next;
			}
		}
		return candidate.getColor();
	}
}

package com.placeregister.places;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.placeregister.R;

public enum PlaceType {

	MUSEUM("museum", 4, BitmapDescriptorFactory.HUE_CYAN, R.drawable.footprint_museum),
	AMUSEMENT_PARK("amusement_park", 1, BitmapDescriptorFactory.HUE_AZURE, R.drawable.footprint_park),
	NIGHT_CLUB("night_club", 2, BitmapDescriptorFactory.HUE_ROSE, R.drawable.footprint_night),
	AQUARIUM("aquarium", 4, BitmapDescriptorFactory.HUE_GREEN, R.drawable.footprint_park),
	ART_GALLERY("art_gallery", 3, BitmapDescriptorFactory.HUE_CYAN, R.drawable.footprint_museum),
	PARK("park", 2, BitmapDescriptorFactory.HUE_GREEN, R.drawable.footprint_park),
	BAR("bar", 1, BitmapDescriptorFactory.HUE_RED, R.drawable.footprint_restaurant),
	GYM("gym", 3, BitmapDescriptorFactory.HUE_VIOLET, R.drawable.footprint_gym),
	CAFE("cafe", 1, BitmapDescriptorFactory.HUE_RED, R.drawable.footprint_restaurant),
	RESTAURANT("restaurant", 1, BitmapDescriptorFactory.HUE_RED, R.drawable.footprint_restaurant),
	CAMPGROUND("campground", 4, BitmapDescriptorFactory.HUE_GREEN, R.drawable.footprint_park),
	CASINO("casino", 4, BitmapDescriptorFactory.HUE_CYAN, R.drawable.footprint_museum),
	STADIUM("stadium", 4, BitmapDescriptorFactory.HUE_GREEN, R.drawable.footprint_park),
	MOVIE_THEATER("movie_theater", 2, BitmapDescriptorFactory.HUE_YELLOW, R.drawable.footprint_yellow),
	ZOO("zoo", 3, BitmapDescriptorFactory.HUE_CYAN, R.drawable.footprint_museum);

	private final String type;
	private final int points;
	private final float color;
	private final int markerId;

	private PlaceType(final String type, final int points, final float color, final int markerId) { this.type = type; this.points = points; this.color = color; this.markerId = markerId;}
	public String toString() { return this.type + ", color : " + this.color + ", points : " + this.points; }
	public String getType() { return this.type; }
	public int getPoints() { return this.points; }
	public float getColor() { return this.color;}
	public int getMarkerId() { return this.markerId;}

	public static PlaceType getPlaceByType(String type) {
		for(PlaceType place: PlaceType.values()) {
			if(place.type.equals(type)) {
				return place;
			}
		}
		return null;
	}
	
	public static List<String> getTypes() {
		List<String> types = new ArrayList<String>();
		for (PlaceType place : Arrays.asList(PlaceType.values())) {
			types.add(place.getType());
		}
		return types;
	}

}

package com.placeregister.places;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;

public enum PlaceType {

	MUSEUM("museum", 4, BitmapDescriptorFactory.HUE_CYAN),
	AMUSEMENT_PARK("amusement_park", 1, BitmapDescriptorFactory.HUE_AZURE),
	NIGHT_CLUB("night_club", 2, BitmapDescriptorFactory.HUE_ROSE),
	AQUARIUM("aquarium", 4, BitmapDescriptorFactory.HUE_GREEN),
	ART_GALLERY("art_gallery", 3, BitmapDescriptorFactory.HUE_CYAN),
	PARK("park", 2, BitmapDescriptorFactory.HUE_GREEN),
	BAR("bar", 1, BitmapDescriptorFactory.HUE_RED),
	GYM("gym", 3, BitmapDescriptorFactory.HUE_VIOLET),
	CAFE("cafe", 1, BitmapDescriptorFactory.HUE_RED),
	RESTAURANT("restaurant", 1, BitmapDescriptorFactory.HUE_RED),
	CAMPGROUND("campground", 4, BitmapDescriptorFactory.HUE_GREEN),
	CASINO("casino", 4, BitmapDescriptorFactory.HUE_CYAN),
	STADIUM("stadium", 4, BitmapDescriptorFactory.HUE_GREEN),
	MOVIE_THEATER("movie_theater", 2, BitmapDescriptorFactory.HUE_YELLOW),
	ZOO("zoo", 3, BitmapDescriptorFactory.HUE_CYAN);

	private final String type;
	private final int points;
	private final float color;

	private PlaceType(final String type, final int points, final float color) { this.type = type; this.points = points; this.color = color; }
	public String toString() { return this.type + ", color : " + this.color + ", points : " + this.points; }
	public String getType() { return this.type; }
	public int getPoints() { return this.points; }
	public float getColor() { return this.color;}

	public static PlaceType getPlaceByType(String type) {
		for(PlaceType place: PlaceType.values()) {
			if(place.type.equals(type)) {
				return place;
			}
		}
		return null;
	}

}

package com.placeregister.search.parameters;

import com.google.android.gms.maps.model.Marker;
import com.placeregister.places.Place;

public class PlaceMarkerParam {

	private Marker marker;
	private Place place;
	private String timeZone;

	public PlaceMarkerParam() {

	}

	public PlaceMarkerParam(Marker marker, Place place, String timeZone) {
		super();
		this.marker = marker;
		this.place = place;
		this.timeZone = timeZone;
	}

	public Marker getMarker() {
		return marker;
	}

	public void setMarker(Marker marker) {
		this.marker = marker;
	}

	public Place getPlace() {
		return place;
	}

	public void setPlace(Place place) {
		this.place = place;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

}

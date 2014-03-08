package com.placeregister.search.parameters;

import android.app.Activity;

import com.google.android.gms.maps.model.Marker;
import com.placeregister.places.Place;

public class PlaceMarkerParam {

	private Marker marker;
	private Place place;
	private String selectedType;
	private String timeZone;
	private Activity context;

	public PlaceMarkerParam() {

	}

	public PlaceMarkerParam(Marker marker, Place place, String selectedType,
			String timeZone, Activity context) {
		super();
		this.marker = marker;
		this.place = place;
		this.selectedType = selectedType;
		this.timeZone = timeZone;
		this.context = context;
	}

	public String getSelectedType() {
		return selectedType;
	}

	public void setSelectedType(String selectedType) {
		this.selectedType = selectedType;
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

	public Activity getContext() {
		return context;
	}

	public void setContext(Activity context) {
		this.context = context;
	}

}

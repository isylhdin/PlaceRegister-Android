package com.placeregister.search.parameters;

import java.util.List;

import android.app.Activity;
import android.location.Location;

import com.placeregister.places.Place;

public class SearchBDPlaceParam {

	private String userTag;
	private Location location;
	private Activity activityContext;
	private List<Place> googlePlaces;

	public SearchBDPlaceParam() {
		super();
	}

	public SearchBDPlaceParam(String userTag, Location location,
			Activity activityContext, List<Place> googlePlaces) {
		super();
		this.userTag = userTag;
		this.location = location;
		this.activityContext = activityContext;
		this.googlePlaces = googlePlaces;
	}

	public String getUserTag() {
		return userTag;
	}

	public void setUserTag(String userTag) {
		this.userTag = userTag;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Activity getActivityContext() {
		return activityContext;
	}

	public void setActivityContext(Activity activityContext) {
		this.activityContext = activityContext;
	}

	public List<Place> getGooglePlaces() {
		return googlePlaces;
	}

	public void setGooglePlaces(List<Place> googlePlaces) {
		this.googlePlaces = googlePlaces;
	}

}

package com.placeregister.search.parameters;

import android.app.Activity;
import android.location.Location;

public class SearchGooglePlaceParam {
	
	private Activity activityContext;
	private Location location;
	private double radius;
	
	public SearchGooglePlaceParam() {
		super();
	}
	

	public SearchGooglePlaceParam(Activity activityContext, Location location, double radius) {
		super();
		this.activityContext = activityContext;
		this.location = location;
		this.radius = radius;
	}

	public Activity getActivityContext() {
		return activityContext;
	}

	public void setActivityContext(Activity activityContext) {
		this.activityContext = activityContext;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}
	
}

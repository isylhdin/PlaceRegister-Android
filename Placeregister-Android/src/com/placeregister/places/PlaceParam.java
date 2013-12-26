package com.placeregister.places;

import android.app.Activity;
import android.location.Location;

public class PlaceParam {
	
	private Activity activityContext;
	private Location location;
	private double radius;
	
	public PlaceParam() {
		super();
	}
	

	public PlaceParam(Activity activityContext, Location location, double radius) {
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

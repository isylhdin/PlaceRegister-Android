package com.placeregister.search.parameters;

import android.location.Location;

public class SearchBDPlaceParam {
	
	private String userTag;
	private Location location;

	public SearchBDPlaceParam() {
		super();
	}
	
	public SearchBDPlaceParam(String userTag, Location location) {
		super();
		this.userTag = userTag;
		this.location = location;
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
	
	

}

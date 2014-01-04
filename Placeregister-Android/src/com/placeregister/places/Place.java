package com.placeregister.places;

import java.util.ArrayList;
import java.util.List;

public class Place {
	
	private String name;
	private String reference;
	private List<String> types;
	private String address;
	private double latitude;
	private double longitude;

	
	public Place() {
	}


	public Place(String name, String reference, ArrayList<String> types, String address,
			double latitude, double longitute) {
		super();
		this.name = name;
		this.reference = reference;
		this.types = types;
		this.address = address;
		this.latitude = latitude;
		this.longitude = longitute;
	}
	
	public void removeUnsupportedTypes(){
		List<String> authorizedTypes = PlaceType.getTypes();
		List<String> types = this.types;
		List<String> usedTypes = new ArrayList<String>();
		
		for (String type : types) {
			if (authorizedTypes.contains(type)) {
				usedTypes.add(type);
			}
		}
		this.types = usedTypes;
	}

	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getReference() {
		return reference;
	}


	public void setReference(String reference) {
		this.reference = reference;
	}


	public List<String> getTypes() {
		return types;
	}


	public void setTypes(List<String> types) {
		this.types = types;
	}
	
	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public double getLatitude() {
		return latitude;
	}


	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}


	public double getLongitude() {
		return longitude;
	}


	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}


}

package com.placeregister.places;

import java.util.ArrayList;

public class Place {
	
	private String name;
	private String reference;
	private ArrayList<String> types;
	private double latitude;
	private double longitute;

	
	public Place() {
	}


	public Place(String name, String reference, ArrayList<String> types,
			double latitude, double longitute) {
		super();
		this.name = name;
		this.reference = reference;
		this.types = types;
		this.latitude = latitude;
		this.longitute = longitute;
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


	public ArrayList<String> getTypes() {
		return types;
	}


	public void setTypes(ArrayList<String> types) {
		this.types = types;
	}


	public double getLatitude() {
		return latitude;
	}


	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}


	public double getLongitute() {
		return longitute;
	}


	public void setLongitude(double longitute) {
		this.longitute = longitute;
	}
	
}

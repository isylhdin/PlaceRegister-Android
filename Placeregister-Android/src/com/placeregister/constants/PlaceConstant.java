package com.placeregister.constants;

public class PlaceConstant {

	/**
	 * Raspberry local jetty server url. Another context is added by default (ie war's name)
	 */
	private static final String BACK_END_RASPBERRY_LOCAL_URL = "http://192.168.0.11:8080/Placeregister-backend-0.0.1-SNAPSHOT/rest/place/service/";
	
	/**
	 * Embedded jetty server launched with maven/eclipse
	 */
	private static final String BACK_END_URL = "http://192.168.0.4:8080/rest/place/service/";
	
	/**
	 * Google places web service url
	 */
	public static final String PLACES_SEARCH_URL = "maps.googleapis.com";
	
	/**
	 * Back end url to retrieve already visited places
	 */
	public static final String GET_VISITED_PLACES_URL =  BACK_END_URL + "get/visited/places";
	
	/**
	 * Back end url to add a new place in DataBase
	 */
	public static final String ADD_PLACE_URL = BACK_END_URL + "add/place";
	
}

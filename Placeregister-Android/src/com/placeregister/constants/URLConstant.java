package com.placeregister.constants;

public class URLConstant {

	/**
	 * Raspberry local jetty server url. Another context is added by default (ie
	 * war's name)
	 */
	private static final String BACK_END_RASPBERRY_LOCAL_URL = "http://192.168.0.11:8080/Placeregister-backend-0.0.1-SNAPSHOT/rest/place/service/";

	/**
	 * Embedded jetty server launched with maven/eclipse
	 */
	private static final String BACK_END_URL = "http://192.168.0.7:8080/rest/";

	/**
	 * Heroku url
	 */
	private static final String BACK_END_HEROKU_URL = "http://sheltered-wildwood-2400.herokuapp.com/rest/place/service/";

	/**
	 * Back end url to login to the app
	 */
	public static final String LOGIN_URL = BACK_END_URL + "user/service/login";

	/**
	 * Google places web service url
	 */
	public static final String PLACES_SEARCH_URL = "maps.googleapis.com";

	/**
	 * Back end url to retrieve already visited places
	 */
	public static final String GET_VISITED_PLACES_URL = BACK_END_URL
			+ "place/service/get/visited/places";

	/**
	 * Back end url to add a new place in DataBase
	 */
	public static final String ADD_PLACE_URL = BACK_END_URL
			+ "place/service/add/place";

}

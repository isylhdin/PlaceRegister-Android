package com.placeregister.constants;

/**
 * Status code returned by the server when a user submit a visit
 * 
 * @author yoann
 * 
 */
public class AddPlaceStatusConstant {

	/************************************************************/
	/*********************** SUCCESS CODES **********************/
	/************************************************************/

	public static final int OK = 200;

	public static final int NEW_PLACE_ADDED = 2000;

	public static final int NEW_VISIT_REGISTERED = 2001;

	/************************************************************/
	/************************ ERROR CODES ***********************/
	/************************************************************/

	public static final int INTERNAL_ERROR = 500;

	/********************** AQUARIUM **************************/

	public static final int AQUARIUM_LIMIT_OF_TIME_NOT_RESPECTED = 5000;

	/********************* ART GALLERY ************************/

	public static final int ART_GALLERY_LIMIT_OF_TIME_NOT_RESPECTED = 5010;

	/************************* BAR ****************************/

	public static final int BAR_ALREADY_VISITED_TODAY = 5020;

	public static final int BAR_NOT_ALLOWED_MORNING = 5021;

	public static final int BAR_NOT_HAPPY_HOUR = 5022;

	/************************* CAFE ***************************/

	public static final int CAFE_LIMIT_OF_TIME_NOT_RESPECTED = 5030;

	/*********************** CAMPGROUND ************************/

	public static final int CAMPGROUND_LIMIT_OF_TIME_NOT_RESPECTED = 5040;

	/************************* CASINO **************************/

	public static final int CASINO_LIMIT_OF_TIME_NOT_RESPECTED = 5050;

	/*************************** GYM ***************************/

	public static final int GYM_LIMIT_OF_TIME_NOT_RESPECTED = 5060;

	public static final int GYM_ONLY_ONE_LOCATION_PER_DAY = 5061;

	/************************** MOVIE **************************/

	public static final int MOVIE_THEATER_LIMIT_OF_TIME_NOT_RESPECTED = 5070;

	/************************ MUSEUM ****************************/

	public static final int MUSEUM_LIMIT_OF_TIME_NOT_RESPECTED = 5080;

	/********************** NIGHT CLUB **************************/

	public static final int NIGHT_CLUB_TOO_EARLY = 5090;

	public static final int NIGHT_CLUB_ALREADY_VISITED_TODAY = 5091;

	/****************** PARK / AMUSEMENT PARK *******************/

	public static final int PARK_LIMIT_OF_TIME_NOT_RESPECTED = 5100;

	public static final int PARK_ALREADY_VISITED_TODAY = 5101;

	/************************ RESTAURANT ************************/

	public static final int RESTAURANT_LIMIT_OF_TIME_NOT_RESPECTED = 5110;

	/************************* STADIUM *************************/

	public static final int STADIUM_LIMIT_OF_TIME_NOT_RESPECTED = 5120;

	/*************************** ZOO ***************************/

	public static final int ZOO_LIMIT_OF_TIME_NOT_RESPECTED = 5130;

}

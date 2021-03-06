package com.placeregister.constants;

public class MapConstant {

	/**
	 * Milliseconds per second
	 */
	public static final int MILLISECONDS_PER_SECOND = 500;

	/**
	 * Update frequency in seconds
	 */
	public static final int UPDATE_INTERVAL_IN_SECONDS = 5;

	/**
	 * Update frequency in milliseconds
	 */
	public static final long UPDATE_INTERVAL = MILLISECONDS_PER_SECOND
			* UPDATE_INTERVAL_IN_SECONDS;

	/**
	 * The fastest update frequency, in seconds
	 */
	public static final int FASTEST_INTERVAL_IN_SECONDS = 1;

	/**
	 * A fast frequency ceiling in milliseconds
	 */
	public static final long FASTEST_INTERVAL = MILLISECONDS_PER_SECOND
			* FASTEST_INTERVAL_IN_SECONDS;

	public static final int ZOOM_LEVEL = 14;

	/**
	 * Distance from where we want to retrieve places according to user's
	 * position
	 */
	public static final int RADIUS = 2000;
}

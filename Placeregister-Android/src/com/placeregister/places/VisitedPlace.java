package com.placeregister.places;

import java.util.List;

/**
 * This is how a place is stored in the User collection
 * 
 * @author yoann
 * 
 */
public class VisitedPlace extends Place {

	/**
	 * Represent every visit of the user for this place
	 */
	private List<String> dates;

	public VisitedPlace() {
		super();
	}

	public VisitedPlace(List<String> dates) {
		super();
		this.dates = dates;
	}

	public List<String> getDates() {
		return dates;
	}

	public void setDates(List<String> dates) {
		this.dates = dates;
	}
}

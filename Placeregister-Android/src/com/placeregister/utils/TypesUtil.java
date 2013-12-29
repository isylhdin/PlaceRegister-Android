package com.placeregister.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.placeregister.places.Place;
import com.placeregister.places.PlaceType;

public class TypesUtil {
	
	/**
	 * Returns the color to display for a place, given its different types
	 * @param types Different types belonging to a place
	 * @param existingTypes types supported by the application
	 * @return a color (as a float)
	 */
	public static float getColor(List<String> types, List<String> existingTypes) {
		List<PlaceType> places = new ArrayList<PlaceType>();
		
		for (String type : types) {
			if(existingTypes.contains(type)){
				places.add(PlaceType.getPlaceByType(type));
			}
		}
		
		float color;
		if (places.size() == 1){
			color = places.get(0).getColor();
		} else {
			color = getColorOfPlaceGivingMaxPoint(places);
		}
		
		return color;
	}
	
	
	/**
	 * Returns the type's color giving the maximum amount of point in the list.
	 *
	 * @param different types returned by google map web services. A place can have several types
	 * @return the max element
	 */
	public static float getColorOfPlaceGivingMaxPoint(List<PlaceType> places){
		Iterator<PlaceType> typeIterator = places.iterator();
		PlaceType candidate = typeIterator.next();

		while (typeIterator.hasNext() ){
			PlaceType next = typeIterator.next();
			if (next.getPoints() > candidate.getPoints()){
				candidate = next;
			}
		}
		return candidate.getColor();
	}
	
	/**
	 * Returns the maximum point in the list.
	 *
	 * @param different types returned by google map web services. A place can have several types
	 * @return the max element
	 */
	public static int getMaxEarnablePoint(List<String> types){
		int maxPoint = 0;
		
		for (String type : types) {
			PlaceType place = PlaceType.getPlaceByType(type);
			if (place.getPoints() > maxPoint){
				maxPoint = place.getPoints();
			}
		}
		return maxPoint;
	}
}

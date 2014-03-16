package com.placeregister.model;

import java.util.List;

/**
 * Response to send to Android mobile when a user tries to register a place
 * 
 * @author yoann
 * 
 */
public class UserInfo {

	/**
	 * Unlocked achievements due to the place he registered
	 */
	private List<Achievement> achievements;

	/**
	 * The status to return indicating if the place registration was successful
	 */
	private int codeStatus;

	/**
	 * User score after the registration
	 */
	private String score;

	public UserInfo() {

	}

	public List<Achievement> getAchievements() {
		return achievements;
	}

	public void setAchievements(List<Achievement> achievements) {
		this.achievements = achievements;
	}

	public int getCodeStatus() {
		return codeStatus;
	}

	public void setCodeStatus(int codeStatus) {
		this.codeStatus = codeStatus;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

}

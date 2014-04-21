package com.placeregister.model;

public class Achievement {

	private String id;
	private String title;
	private String description;
	private long points;
	private Achievement achievementExtension;
	private String category;

	public Achievement() {

	}

	public Achievement(String title, String description, long points,
			Achievement achievementExtension, String category) {
		super();
		this.title = title;
		this.description = description;
		this.points = points;
		this.achievementExtension = achievementExtension;
		this.category = category;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getPoints() {
		return points;
	}

	public void setPoints(long points) {
		this.points = points;
	}

	public Achievement getAchievementExtension() {
		return achievementExtension;
	}

	public void setAchievementExtension(Achievement achievementExtension) {
		this.achievementExtension = achievementExtension;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return "Achievement [id=" + id + ", title=" + title + ", description="
				+ description + ", points=" + points
				+ ", achievementExtension=" + achievementExtension
				+ ", category=" + category + "]";
	}
}

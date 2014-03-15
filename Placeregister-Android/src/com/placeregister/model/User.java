package com.placeregister.model;

public class User {

	private String tag;
	private String nickName;
	private String avatarRef;
	private String score;

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getAvatarRef() {
		return avatarRef;
	}

	public void setAvatarRef(String avatarRef) {
		this.avatarRef = avatarRef;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getScore() {
		return score;
	}

}

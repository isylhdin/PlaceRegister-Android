package com.placeregister.application;

import android.app.Application;

import com.placeregister.model.User;

public class ApplicationInfo extends Application {

	private User user;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}

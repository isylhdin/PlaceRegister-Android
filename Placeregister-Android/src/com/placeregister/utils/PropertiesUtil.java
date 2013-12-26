package com.placeregister.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.Log;

public class PropertiesUtil {

	
	public static String getProperty(Context context, String key){   	
		Resources resources = context.getResources();
		AssetManager assetManager = resources.getAssets();
		Properties properties = new Properties();

		// Read from the /assets directory
		try {
			InputStream inputStream = assetManager.open("util.properties");
			properties.load(inputStream);
			Log.i("GooglePlaces", properties.toString());
		} catch (IOException e) {
			Log.e("GooglePlaces", "Failed to load properties file");
			e.printStackTrace();
		}
		return properties.getProperty(key);
	}
}

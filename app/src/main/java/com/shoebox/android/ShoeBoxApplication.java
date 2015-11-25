package com.shoebox.android;

import android.app.Application;

import com.firebase.client.Config;
import com.firebase.client.Firebase;
import com.firebase.client.Logger;

public class ShoeBoxApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		Firebase.setAndroidContext(this);

		Config config = new Config();
		config.setLogLevel(Logger.Level.INFO);
		config.setPersistenceEnabled(true);

		Firebase.setDefaultConfig(config);
	}
}

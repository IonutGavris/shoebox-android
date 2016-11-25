package com.shoebox.android;

import android.app.Application;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

public class ShoeBoxApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();

		// Configure Fabric Kits
		Fabric.with(this, new Crashlytics());

		// configure Firebase
//		Firebase.setAndroidContext(this);
//
//		Config config = new Config();
//		config.setLogLevel(Logger.Level.INFO);
//		config.setPersistenceEnabled(true);
//
//		Firebase.setDefaultConfig(config);

		// add JakeWharton's Timber logging library if debug build type
		if (BuildConfig.DEBUG) {
			Timber.plant(new Timber.DebugTree());
		}
	}
}

package com.shoebox.android;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.FirebaseDatabase;

import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

public class ShoeBoxApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();

		// Configure Fabric Kits
		Fabric.with(this, new Crashlytics());

		FirebaseApp.initializeApp(getApplicationContext());
		FirebaseDatabase.getInstance().setPersistenceEnabled(true);

		if (BuildConfig.DEBUG) {
			// add JakeWharton's Timber logging library if debug build type
			Timber.plant(new Timber.DebugTree());

			// disable analytics if debug build type
			FirebaseAnalytics.getInstance(this).setAnalyticsCollectionEnabled(false);
		}
	}

}
package com.shoebox.android;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.FirebaseDatabase;
import com.shoebox.android.injection.component.ApplicationComponent;
import com.shoebox.android.injection.component.DaggerApplicationComponent;
import com.shoebox.android.injection.module.ShoeBoxAndroidInjector;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.HasActivityInjector;
import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

public class ShoeBoxApplication extends Application implements HasActivityInjector {

	@Inject
	protected ShoeBoxAndroidInjector<Activity> dispatchingAndroidInjector;

	private ApplicationComponent component;

	public static ApplicationComponent getComponent(Context context) {
		return ((ShoeBoxApplication) context.getApplicationContext()).component;
	}

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

		component.inject(this);
	}

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);

		// Required init
		component = DaggerApplicationComponent.builder()
				.application(this)
				.build();
	}

	@Override
	public AndroidInjector<Activity> activityInjector() {
		return dispatchingAndroidInjector;
	}

}
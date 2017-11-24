package com.shoebox.android;

import android.app.Application;
import android.content.res.Resources;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.FirebaseDatabase;
import com.shoebox.android.util.HelperClass;
import com.shoebox.android.util.UIUtils;

import java.util.Locale;

import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

public class ShoeBoxApplication extends Application {
	private Locale phoneLocale;

	@Override
	public void onCreate() {
		super.onCreate();
		phoneLocale = getResources().getConfiguration().locale;

		// Configure Fabric Kits
		Fabric.with(this, new Crashlytics());

		FirebaseApp.initializeApp(getApplicationContext());
		// enable persistence for the data fetched from Firebase
		FirebaseDatabase.getInstance().setPersistenceEnabled(true);

		if (BuildConfig.DEBUG) {
			// add JakeWharton's Timber logging library if debug build type
			Timber.plant(new Timber.DebugTree());

			// disable analytics if debug build type
			FirebaseAnalytics.getInstance(this).setAnalyticsCollectionEnabled(false);
		}

		if (UIUtils.LANG_RO.equals(phoneLocale.getLanguage())) {
			HelperClass.addStringValueInSharedPreference(getApplicationContext(), HelperClass.keyAppLanguage, UIUtils.LANG_RO);
		}

		String localeCode = HelperClass.getStringValueInSharedPreference(getApplicationContext(), HelperClass.keyAppLanguage, UIUtils.LANG_RO);
		Resources res = getApplicationContext().getResources();
		android.content.res.Configuration conf = res.getConfiguration();
		conf.locale = new Locale(localeCode);
		res.updateConfiguration(conf, res.getDisplayMetrics());
	}

	public Locale getPhoneLocale() {
		return phoneLocale;
	}
}

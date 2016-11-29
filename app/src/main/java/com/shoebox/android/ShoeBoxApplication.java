package com.shoebox.android;

import android.app.Application;
import android.content.res.Resources;

import com.crashlytics.android.Crashlytics;
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

		// add JakeWharton's Timber logging library if debug build type
		if (BuildConfig.DEBUG) {
			Timber.plant(new Timber.DebugTree());
		}

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

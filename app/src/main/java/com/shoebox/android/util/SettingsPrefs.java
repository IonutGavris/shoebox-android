package com.shoebox.android.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * magdabadita on 30/11/15.
 */
public class SettingsPrefs {

	private static final String KEY_IS_FIRST_TIME = "firsttime";
	private static final String SHARED_PREFERENCES = "SHARED_PREFERENCES";

	public static void setIsNotFirstTime(Context context) {
		SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putBoolean(KEY_IS_FIRST_TIME, false);
		editor.apply();
	}

	public static boolean isFirstTime(Context context) {
		SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		return sharedPref.getBoolean(KEY_IS_FIRST_TIME, true);
	}

}

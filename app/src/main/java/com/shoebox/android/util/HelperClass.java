package com.shoebox.android.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by magdabadita on 30/11/15.
 */
public class HelperClass {

    public static final String keyIsFirstTime = "firsttime";

    public static final String urlFacebook = "https://www.facebook.com/ShoeBox.ro";
    public static final String urlTwitter = "https://twitter.com/ShoeBoxRomania";
    public static final String contactPhoneNumber = "+40745 900 851";
    public static final String contactEmail = "valvesa@gmail.com";

    private static final String SHARED_PREFERENCES = "SHARED_PREFERENCES";

    public static void addBooleanValueInSharedPreference(Context context, String key, boolean value) {
        SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean getBooleanValueInSharedPreference(Context context, String key, boolean defaultValue) {
        SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPref.getBoolean(key, defaultValue);
    }
}

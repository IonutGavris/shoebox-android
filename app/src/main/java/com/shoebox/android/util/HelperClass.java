package com.shoebox.android.util;

import android.app.Activity;
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

    public static void addBooleanValueInSharedPreference(Activity context, String key, boolean value) {
        SharedPreferences sharedPref = context.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean getBooleanValueInSharedPreference(Activity context, String key, boolean defaultValue) {
        SharedPreferences sharedPref = context.getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getBoolean(key, defaultValue);
    }
}

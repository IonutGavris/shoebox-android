package com.shoebox.android;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by vasile.mihalca on 24/11/15.
 */
public class UIUtils {
	public static void hideKeyboard(Context context, View input) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
	}

	public static void hideCurrentFocusKeyboard(Activity activity) {
		if (activity != null && activity.getCurrentFocus() != null) {
			UIUtils.hideKeyboard(activity, activity.getCurrentFocus());
		}
	}
}

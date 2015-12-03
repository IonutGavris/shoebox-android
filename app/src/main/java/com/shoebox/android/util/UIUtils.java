package com.shoebox.android.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.shoebox.android.BaseActivity;
import com.shoebox.android.R;

/**
 * Created by vasile.mihalca on 24/11/15.
 */
public class UIUtils {
	public static final String DIAL_PREFIX = "tel:";

	public static void hideKeyboard(Context context, View input) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
	}

	public static void hideCurrentFocusKeyboard(Activity activity) {
		if (activity != null && activity.getCurrentFocus() != null) {
			UIUtils.hideKeyboard(activity, activity.getCurrentFocus());
		}
	}

	public static void showMessage(BaseActivity activity, int resId) {
		if (!activity.isFinishing()) {
			View view = activity.coordinatorLayout;
			if (activity.coordinatorLayout == null) {
				view = activity.getWindow().getDecorView().findViewById(android.R.id.content);
			}
			Snackbar.make(view, resId, Snackbar.LENGTH_LONG).show();
		}
	}

	public static void launchDial(BaseActivity activity, String phoneNumber) {
		Intent launchDial = new Intent(Intent.ACTION_DIAL, Uri.parse(DIAL_PREFIX + phoneNumber));
		try {
			activity.startActivity(launchDial);
		} catch (android.content.ActivityNotFoundException ex) {
			UIUtils.showMessage(activity, R.string.msg_no_dial);
		}
	}
}

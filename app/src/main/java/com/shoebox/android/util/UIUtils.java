package com.shoebox.android.util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.shoebox.android.BaseActivity;
import com.shoebox.android.R;

import de.cketti.mailto.EmailIntentBuilder;
import timber.log.Timber;

public class UIUtils {
	public static final String LANG_RO = "ro";
	public static final String CONTACT_EMAIL = "contact@shoebox.ro";
	public static final String CONTACT_EMAIL_SUBJECT = "ShoeBox Android";

//	public static final String urlFacebook = "https://www.facebook.com/ShoeBox.ro";
//	public static final String urlTwitter = "https://twitter.com/ShoeBoxRomania";
//	public static final String contactPhoneNumber = "+40745 900 851";

	private static final String DIAL_PREFIX = "tel:";

	private static void hideKeyboard(Context context, View input) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm != null) {
			imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
		}
	}

	public static void hideCurrentFocusKeyboard(Activity activity) {
		if (activity != null && activity.getCurrentFocus() != null) {
			UIUtils.hideKeyboard(activity, activity.getCurrentFocus());
		}
	}

	public static void showMessage(BaseActivity activity, int resId) {
		showMessage(activity, activity.getString(resId));
	}

	public static void showMessage(BaseActivity activity, String message) {
		if (!activity.isFinishing()) {
			View view = activity.coordinatorLayout;
			if (activity.coordinatorLayout == null) {
				view = activity.getWindow().getDecorView().findViewById(android.R.id.content);
			}
			Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
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

	public static void setListStatus(View listStatusView, String statusText, boolean isError) {
		// TODO create custom view for listStatusView
		TextView statusTextView = listStatusView.findViewById(R.id.statusText);
		View statusProgress = listStatusView.findViewById(R.id.statusProgress);
		statusTextView.setText(statusText);
		statusProgress.setVisibility(isError || statusText == null ? View.GONE : View.VISIBLE);
	}

	public static void launchEmail(BaseActivity activity, String to, String subject, String content) {
		Intent emailIntent = EmailIntentBuilder.from(activity)
				.to(to)
				.subject(subject)
				.body(content)
				.build();
		try {
			activity.startActivity(emailIntent);
		} catch (android.content.ActivityNotFoundException ex) {
			UIUtils.showMessage(activity, R.string.msg_no_email_app);
		}
	}

	public static String getDeviceData(Context context) {
		return "ShoeBox " + getAppVersionName(context) + " on "
				+ Build.MANUFACTURER + " " + Build.MODEL + " (" + Build.VERSION.RELEASE + ")\n";
	}

	private static String getAppVersionName(Context context) {
		try {
			PackageInfo pinfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return pinfo.versionName;
		} catch (PackageManager.NameNotFoundException e) {
			Timber.e(e, "getAppVersionName: Version name not found");
		}
		return "";
	}

	public static void showMessageWithAction(@NonNull BaseActivity activity, @StringRes int messageResId, @Snackbar
			.Duration int duration, @StringRes int btnResId, @NonNull View.OnClickListener onClickListener) {
		showMessageWithAction(activity, activity.getString(messageResId), duration, btnResId, onClickListener, null);
	}

	public static void showMessageWithAction(@NonNull BaseActivity activity, @NonNull String message, @Snackbar.Duration int duration,
	                                         @StringRes int btnResId, @NonNull View.OnClickListener onClickListener, Snackbar.Callback showDismissCallback) {
		if (!activity.isFinishing()) {
			View view = activity.coordinatorLayout;
			if (activity.coordinatorLayout == null) {
				view = activity.getWindow().getDecorView().findViewById(android.R.id.content);
			}
			Snackbar snackbar = Snackbar.make(view, message, duration);
			snackbar.setActionTextColor(ContextCompat.getColor(activity, R.color.colorAccent));
			snackbar.setAction(btnResId, onClickListener);
			snackbar.addCallback(showDismissCallback);
			snackbar.show();
		}
	}

	public static void launchSystemAppSettings(Activity activity) {
		try {
			//Open the specific App Info page:
			Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
			intent.setData(Uri.parse("package:" + activity.getPackageName()));
			activity.startActivity(intent);
		} catch (ActivityNotFoundException ignored) {
			//Open the generic Apps page:
			Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
			activity.startActivity(intent);
		}
	}

	public static boolean useRomanianLanguage(Context context) {
		String phoneLocale = context.getResources().getConfiguration().locale.getLanguage();
		Timber.d("useRomanianLanguage: phoneLocale=%s", phoneLocale);
		return UIUtils.LANG_RO.equals(phoneLocale);
	}

}

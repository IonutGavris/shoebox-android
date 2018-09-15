package com.shoebox.android.util.permission;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import com.shoebox.android.BaseActivity;
import com.shoebox.android.R;
import com.shoebox.android.util.UIUtils;

public final class PermissionUtil {

	private PermissionUtil() {
	}

	/**
	 * Gets whether the application has the provided permissions granted.
	 *
	 * @return Returns true if all the permissions are granted, false otherwise.
	 */
	public static boolean hasPermissions(Context context, @NonNull String[] permissions) {
		for (String permission : permissions) {
			if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Gets the permission group for a provided permission.
	 * This currently works only for Camera and Location permission groups.
	 *
	 * @return The permission group or null.
	 */
	static String getPermissionGroup(@NonNull Context context, @NonNull String permission) {
		switch (permission) {
			case Manifest.permission.ACCESS_FINE_LOCATION:
			case Manifest.permission.ACCESS_COARSE_LOCATION:
				return context.getString(R.string.permission_location);
		}
		return null;
	}

	/**
	 * If user has selected "Don't show again" in the permission dialog, show a SnackBar directing the user to
	 * Settings so he can manually enable app permissions.
	 */
	public static void showSettingsSnackbar(Activity activity, PermissionRequest permissionRequest) {
		UIUtils.showMessageWithAction(
				(BaseActivity) activity,
				getSettingsMessage(activity, permissionRequest.getPermission()), Snackbar.LENGTH_LONG, R.string.btn_settings,
				view -> UIUtils.launchSystemAppSettings(activity), null);

	}

	private static String getSettingsMessage(@NonNull Context context, @NonNull String permission) {
		String permissionGroup = PermissionUtil.getPermissionGroup(context, permission);
		return context.getString(R.string.permission_denied_single, permissionGroup);
	}

	public static void showSnackbarWithAction(Activity activity, PermissionRequest permissionRequest, View.OnClickListener onClickListener) {
		UIUtils.showMessageWithAction((BaseActivity) activity, permissionRequest.getRationaleMessageId(), Snackbar.LENGTH_LONG, R.string.btn_ok, onClickListener);
	}

}

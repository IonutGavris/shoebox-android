package com.shoebox.android.util.permission;

import android.Manifest;
import android.support.annotation.StringRes;

import com.shoebox.android.R;

public enum PermissionRequest {
	DETECT_LOCATION(11, Manifest.permission.ACCESS_FINE_LOCATION, R.string.permission_rationale_location);

	private int requestCode;
	private String permission;
	@StringRes
	private int rationaleMessageId;

	PermissionRequest(int requestCode, String permission, int rationaleMessageId) {
		this.requestCode = requestCode;
		this.permission = permission;
		this.rationaleMessageId = rationaleMessageId;
	}

	public int getRequestCode() {
		return requestCode;
	}

	public String getPermission() {
		return permission;
	}

	public int getRationaleMessageId() {
		return rationaleMessageId;
	}
}

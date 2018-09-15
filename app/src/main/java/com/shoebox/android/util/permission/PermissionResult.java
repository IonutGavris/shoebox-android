package com.shoebox.android.util.permission;

public class PermissionResult {

	private final boolean granted;
	private final boolean shouldShowRequestPermissionRationale;
	private final PermissionRequest permissionRequest;
	private final RequestType requestType;

	private PermissionResult(boolean granted, boolean shouldShowRequestPermissionRationale,
	                         PermissionRequest permissionRequest, RequestType requestType) {
		this.granted = granted;
		this.shouldShowRequestPermissionRationale = shouldShowRequestPermissionRationale;
		this.permissionRequest = permissionRequest;
		this.requestType = requestType;
	}

	public static PermissionResult simple(boolean granted, PermissionRequest permissionRequest) {
		return new PermissionResult(granted, false, permissionRequest, RequestType.SIMPLE);
	}

	public static PermissionResult detailed(boolean granted, boolean shouldShowRequestPermissionRationale, PermissionRequest permissionRequest) {
		return new PermissionResult(granted, shouldShowRequestPermissionRationale, permissionRequest, RequestType.DETAILED);
	}

	public boolean isGranted() {
		return granted;
	}

	public boolean isShouldShowRequestPermissionRationale() {
		return shouldShowRequestPermissionRationale;
	}

	public PermissionRequest getPermissionRequest() {
		return permissionRequest;
	}

	public RequestType getRequestType() {
		return requestType;
	}

	public enum RequestType {
		DETAILED, SIMPLE
	}
}

package com.shoebox.android.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.shoebox.android.LocationsActivity;
import com.shoebox.android.beans.Location;
import com.shoebox.android.util.PermissionUtils;

import java.util.List;

import timber.log.Timber;

public class LocationsMapFragment extends com.google.android.gms.maps.SupportMapFragment implements
		GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationChangeListener, GoogleMap
		.OnMarkerClickListener, GoogleMap.OnMapClickListener,
		OnMapReadyCallback, LocationsActivity.LocationsListener {

	private static final double ROMANIA_CENTER_LATITUDE = 45.867063;
	private static final double ROMANIA_CENTER_LONGITUDE = 24.91699199999993;

	/**
	 * Request code for location permission request.
	 *
	 * @see #onRequestPermissionsResult(int, String[], int[])
	 */
	private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

	/**
	 * Flag indicating whether a requested permission has been denied after returning in
	 * {@link #onRequestPermissionsResult(int, String[], int[])}.
	 */
	private boolean mPermissionDenied = false;

	private View fragmentView;
	private GoogleMap map;

	public LocationsMapFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getMapAsync(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		fragmentView = super.onCreateView(inflater, container, savedInstanceState);
		return fragmentView;
	}

	@Override
	public void onResume() {
		super.onResume();
		if (mPermissionDenied) {
			Timber.d("onResume: Permission was not granted, display error dialog");
			// Displays a dialog with error message explaining that the location permission is missing.
			PermissionUtils.PermissionDeniedDialog
					.newInstance(false).show(getActivity().getSupportFragmentManager(), "dialog");
			mPermissionDenied = false;
		}
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		map = googleMap;
		map.setOnMyLocationButtonClickListener(this);
		enableMyLocation();
		map.setOnMarkerClickListener(this);
		map.setOnMapClickListener(this);
		UiSettings mapUiSettings = map.getUiSettings();
		mapUiSettings.setZoomControlsEnabled(false);
		mapUiSettings.setMapToolbarEnabled(false);
	}

	public void onRequestPermissionsEnded(int requestCode, @NonNull String[] permissions, @NonNull int[]
			grantResults) {
		if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
			return;
		}

		if (PermissionUtils.isPermissionGranted(permissions, grantResults,
				Manifest.permission.ACCESS_FINE_LOCATION)) {
			Timber.d("onRequestPermissionsResult: Enable the my location layer <- the permission has been granted.");
			enableMyLocation();
		} else {
			Timber.d("onRequestPermissionsResult: Display the missing permission error dialog when the fragment " +
					"resumes");
			mPermissionDenied = true;
		}
	}

	@Override
	public void onMapClick(LatLng latLng) {
		// TODO
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		// TODO
		return true;
	}

	@Override
	public void setStatus(String status) {
		// TODO
	}

	@Override
	public void setLocationsResult(List<Location> locations) {
		// TODO
	}

	@Override
	public boolean onMyLocationButtonClick() {
		Timber.d("MyLocation button clicked");
		// Return false so that we don't consume the event and the default behavior still occurs
		// (the camera animates to the user's current position).
		return false;
	}

	@Override
	public void onMyLocationChange(android.location.Location myLocation) {
		Timber.d("onMyLocationChange");
		map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(myLocation.getLatitude(), myLocation.getLongitude())));
	}

	/**
	 * Enables the My Location layer if the fine location permission has been granted.
	 */
	private void enableMyLocation() {
		if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
				!= PackageManager.PERMISSION_GRANTED) {
			Timber.d("enableMyLocation: Permission to access the location is missing");
			PermissionUtils.requestPermission(getActivity(), LOCATION_PERMISSION_REQUEST_CODE,
					Manifest.permission.ACCESS_FINE_LOCATION, true);
		} else if (map != null) {
			Timber.d("enableMyLocation: Access to the location has been granted to the app");
			map.setMyLocationEnabled(true);
			// need to delay in order for the map to have the "my location" set
			fragmentView.postDelayed(new Runnable() {
				@Override
				public void run() {
					android.location.Location location = map.getMyLocation();
					Timber.d("enableMyLocation: myLocation=%s", location);
					float zoom = 15;
					if (location == null) { // if we don't have my location yet, then go to the center of Romania
						location = new android.location.Location("");
						location.setLatitude(ROMANIA_CENTER_LATITUDE);
						location.setLongitude(ROMANIA_CENTER_LONGITUDE);
						zoom = 7;
					}
					CameraPosition position = new CameraPosition.Builder()
							.target(new LatLng(location.getLatitude(), location.getLongitude()))
							.zoom(zoom).build();
					map.animateCamera(CameraUpdateFactory.newCameraPosition(position));
				}
			}, 500);
		}
	}


}

package com.shoebox.android.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.shoebox.android.beans.Location;
import com.shoebox.android.event.DistanceCalculatedEvent;

import org.greenrobot.eventbus.EventBus;

import timber.log.Timber;

public class LocationDetailsMapFragment extends com.google.android.gms.maps.SupportMapFragment implements
		OnMapReadyCallback, GoogleMap.OnMyLocationChangeListener {

	private static final String LOCATION = "location";
	private static final String BUNDLE_MAP_CENTERED = "map_centered";
	private final EventBus bus = EventBus.getDefault();
	boolean mapCentered = false;

	private View fragmentView;
	private GoogleMap map;
	private Location location;

	public static LocationDetailsMapFragment newInstance(Location location) {
		LocationDetailsMapFragment fragment = new LocationDetailsMapFragment();
		Bundle bundle = new Bundle();
		bundle.putParcelable(LOCATION, location);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle args = getArguments();
		if (args != null) {
			location = args.getParcelable(LOCATION);
		} else {
			throw new IllegalStateException("LocationDetailsMapFragment needs arguments with location");
		}
		getMapAsync(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		fragmentView = super.onCreateView(inflater, container, savedInstanceState);
		return fragmentView;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		Timber.d("onSaveInstanceState zoom=%s", mapCentered);
		outState.putBoolean(BUNDLE_MAP_CENTERED, mapCentered);
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onViewStateRestored(Bundle savedInstanceState) {
		super.onViewStateRestored(savedInstanceState);
		if (savedInstanceState != null) {
			mapCentered = savedInstanceState.getBoolean(BUNDLE_MAP_CENTERED);
		}
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		map = googleMap;
		enableMyLocation();
		UiSettings mapUiSettings = map.getUiSettings();
		mapUiSettings.setZoomControlsEnabled(false);
		mapUiSettings.setMapToolbarEnabled(false);
		map.setOnMyLocationChangeListener(this);
		LatLng latLng = new LatLng(location.latitude, location.longitude);
		map.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory
				.HUE_AZURE)).position(latLng));
		if (!mapCentered) { // center map only once
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
		}
	}

	/**
	 * Enables the My Location layer if the fine location permission has been granted.
	 */
	private void enableMyLocation() {
		if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
				== PackageManager.PERMISSION_GRANTED && map != null) {
			Timber.d("enableMyLocation: Access to the location has been granted to the app");
			map.setMyLocationEnabled(true);
		} else {
			bus.post(new DistanceCalculatedEvent(-1));
		}
	}

	@Override
	public void onMyLocationChange(android.location.Location myLocation) {
		Timber.d("onMyLocationChange: myLocation=%s", myLocation);
		double distance = -1;
		if (myLocation != null) { // if we have my location than calculate distance to selected location
			android.location.Location selectedLocation = new android.location.Location("");
			selectedLocation.setLatitude(location.latitude);
			selectedLocation.setLongitude(location.longitude);
			distance = myLocation.distanceTo(selectedLocation) * 0.001; // in km
		}
		bus.post(new DistanceCalculatedEvent(distance));
		// we need the my location found callback only once
		map.setOnMyLocationChangeListener(null);
	}
}

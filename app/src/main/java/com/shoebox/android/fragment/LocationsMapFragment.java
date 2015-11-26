package com.shoebox.android.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.shoebox.android.LocationsActivity;
import com.shoebox.android.beans.Location;

import java.util.List;

public class LocationsMapFragment extends com.google.android.gms.maps.SupportMapFragment implements
		GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener, OnMapReadyCallback, LocationsActivity.LocationsListener {

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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		map = googleMap;
		map.setMyLocationEnabled(false);
		map.setOnMarkerClickListener(this);
		map.setOnMapClickListener(this);
		UiSettings mapUiSettings = map.getUiSettings();
		mapUiSettings.setZoomControlsEnabled(false);
		mapUiSettings.setMapToolbarEnabled(false);
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

}

package com.shoebox.android.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
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
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.shoebox.android.LocationsActivity;
import com.shoebox.android.beans.Location;
import com.shoebox.android.event.LocationClickedEvent;
import com.shoebox.android.util.BusProvider;
import com.shoebox.android.util.PermissionUtils;
import com.squareup.otto.Bus;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class LocationsMapFragment extends com.google.android.gms.maps.SupportMapFragment implements
		GoogleMap.OnMyLocationButtonClickListener, ClusterManager.OnClusterItemInfoWindowClickListener<Location>,
		ClusterManager.OnClusterClickListener<Location>, OnMapReadyCallback, LocationsActivity.LocationsListener {

	private static final double ROMANIA_CENTER_LATITUDE = 45.867063;
	private static final double ROMANIA_CENTER_LONGITUDE = 24.91699199999993;

	private static final String BUNDLE_MAP_CENTERED = "map_centered";
	/**
	 * Request code for location permission request.
	 *
	 * @see #onRequestPermissionsResult(int, String[], int[])
	 */
	private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
	private final Bus bus = BusProvider.get();
	boolean mapCentered = false;
	/**
	 * Flag indicating whether a requested permission has been denied after returning in
	 * {@link #onRequestPermissionsResult(int, String[], int[])}.
	 */
	private boolean mPermissionDenied = false;
	private View fragmentView;
	private GoogleMap map;
	private List<Location> locations;
	private ClusterManager<Location> clusterManager;
	private SetLocationMarkersTask setMarkersTask;

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
	public void onResume() {
		super.onResume();
		if (mPermissionDenied) {
			Timber.d("onResume: Permission was not granted, display error dialog");
			// Displays a dialog with error message explaining that the location permission is missing.
			PermissionUtils.PermissionDeniedDialog
					.newInstance(false).show(getActivity().getSupportFragmentManager(), "dialog");
			mPermissionDenied = false;
		}
		setUpMapIfNeeded();
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		map = googleMap;
		map.setOnMyLocationButtonClickListener(this);
		enableMyLocation();
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
			Timber.d("onRequestPermissionsResult: Display the missing permission error dialog when the fragment resumes");
			mPermissionDenied = true;
		}
	}

	@Override
	public void setStatus(String status) {
		// TODO
	}

	@Override
	public void setLocationsResult(List<Location> locations) {
		Timber.d("setLocationsResult map=%s  &  locations=%s", map, locations);
		if (map == null)
			return;
		if (this.locations != null && locations != null && this.locations.containsAll(locations))
			return;

		this.locations = locations;
		// Attempt to cancel the in-flight request.
		if (setMarkersTask != null) {
			setMarkersTask.cancel(true);
		}

		if (locations == null || locations.isEmpty()) {
			// clear the map when we don't have locations
			map.clear();
			clusterManager.clearItems();
		} else {
			setMarkersTask = new SetLocationMarkersTask();
			setMarkersTask.execute(locations);
		}
	}

	@Override
	public boolean onMyLocationButtonClick() {
		Timber.d("MyLocation button clicked");
		// Return false so that we don't consume the event and the default behavior still occurs
		// (the camera animates to the user's current position).
		return false;
	}

	@Override
	public boolean onClusterClick(Cluster<Location> cluster) {
		Timber.d("onClusterClick: cluster count = %s ", cluster.getSize());
		// TODO case when 2 or more locations have same position
		return true;
	}

	@Override
	public void onClusterItemInfoWindowClick(Location item) {
		// go to location details
		bus.post(new LocationClickedEvent(item));
	}

	private void setUpMapIfNeeded() {
		Timber.i("setUpMapIfNeeded: map = %s ", map);
		// Do a null check to confirm that we have not already instantiated the map.
		if (map == null) {
			// Try to obtain the map from the SupportMapFragment.
			map = getMap();
			// Check if we were successful in obtaining the map.
			if (map != null) {
				// setup cluster manager and needed listeners
				clusterManager = new ClusterManager<>(getActivity(), map);
				clusterManager.setRenderer(new LocationRenderer());
				map.setOnCameraChangeListener(clusterManager);
				map.setOnMarkerClickListener(clusterManager);
				map.setOnInfoWindowClickListener(clusterManager);
				clusterManager.setOnClusterItemInfoWindowClickListener(this);
				clusterManager.setOnClusterClickListener(this);
				setLocationsResult(((LocationsActivity) getActivity()).getLocations());
			}
		}
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
			if (mapCentered) // try to center map only once
				return;
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
					mapCentered = true;
				}
			}, 500);
		}
	}

	private class SetLocationMarkersTask extends AsyncTask<List<Location>, Void, Void> {

		public SetLocationMarkersTask() {
			super();
		}

		@Override
		protected Void doInBackground(List<Location>... locationsList) {
			List<Location> locations = locationsList[0];
			if (locations != null && locations.size() > 0) {
				List<Location> locationsWitGeoPosition = new ArrayList<>();
				for (Location location : locations) {
					// don't show locations which don't have geo position
					if (location.hasGeoLocation()) {
						locationsWitGeoPosition.add(location);
					}
				}

				if (!isCancelled()) {
					clusterManager.addItems(locationsWitGeoPosition);
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			Timber.d("SetLocationMarkersTask - onPostExecute");
			// force a re-cluster after adding new markers
			clusterManager.cluster();
		}
	}

	/**
	 * Show location title and address when the marker is tapped
	 */
	private class LocationRenderer extends DefaultClusterRenderer<Location> {

		public LocationRenderer() {
			super(getActivity().getApplicationContext(), getMap(), clusterManager);
		}

		@Override
		protected void onBeforeClusterItemRendered(Location shop, MarkerOptions markerOptions) {
			// Set the info window to show the shop name and address
			markerOptions.title(shop.title).snippet(shop.address);
			super.onBeforeClusterItemRendered(shop, markerOptions);
		}

		@Override
		protected void onBeforeClusterRendered(Cluster<Location> cluster, MarkerOptions markerOptions) {
			super.onBeforeClusterRendered(cluster, markerOptions);
		}

		@Override
		protected boolean shouldRenderAsCluster(Cluster<Location> cluster) {
			// Always render clusters.
			return cluster.getSize() > 1;
		}
	}
}

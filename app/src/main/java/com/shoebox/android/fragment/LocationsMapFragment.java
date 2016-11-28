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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.shoebox.android.LocationsActivity;
import com.shoebox.android.beans.Location;
import com.shoebox.android.event.LocationClickedEvent;
import com.shoebox.android.util.PermissionUtils;
import com.shoebox.android.util.ShoeBoxAnalytics;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class LocationsMapFragment extends com.google.android.gms.maps.SupportMapFragment implements
		GoogleMap.OnMyLocationButtonClickListener, ClusterManager.OnClusterItemInfoWindowClickListener<Location>,
		ClusterManager.OnClusterClickListener<Location>, OnMapReadyCallback, LocationsActivity.LocationsListener,
		GoogleMap.OnMyLocationChangeListener {

	private static final double ROMANIA_CENTER_LATITUDE = 45.867063;
	private static final double ROMANIA_CENTER_LONGITUDE = 24.91699199999993;

	private static final String BUNDLE_MAP_CENTERED = "map_centered";
	/**
	 * Request code for location permission request.
	 *
	 * @see #onRequestPermissionsResult(int, String[], int[])
	 */
	private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
	protected FirebaseAnalytics firebaseAnalytics;
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
		firebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
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
		centerMapToCoordinate(ROMANIA_CENTER_LATITUDE, ROMANIA_CENTER_LONGITUDE, 6, false);
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
			String errorMsg = "onRequestPermissionsResult: Display the missing permission error dialog when the fragment resumes";
			ShoeBoxAnalytics.sendErrorState(firebaseAnalytics, errorMsg);
			Timber.d(errorMsg);
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
	public void onClusterItemInfoWindowClick(Location item) { // go to location details
		EventBus.getDefault().post(new LocationClickedEvent(item));
	}

	private void setUpMapIfNeeded() {
		Timber.i("setUpMapIfNeeded: map = %s ", map);
		// Do a null check to confirm that we have not already instantiated the map.
		if (map == null) {
			// Try to obtain the map from the SupportMapFragment.
			getMapAsync(new OnMapReadyCallback() {
				@Override
				public void onMapReady(GoogleMap googleMap) {
					map = googleMap;
					// Check if we were successful in obtaining the map.
					if (map != null) {
						// setup cluster manager and needed listeners
						clusterManager = new ClusterManager<>(getActivity(), map);
						clusterManager.setRenderer(new LocationRenderer(map));
						map.setOnCameraChangeListener(clusterManager);
						map.setOnMarkerClickListener(clusterManager);
						map.setOnInfoWindowClickListener(clusterManager);
						map.setOnMyLocationChangeListener(LocationsMapFragment.this);
						clusterManager.setOnClusterItemInfoWindowClickListener(LocationsMapFragment.this);
						clusterManager.setOnClusterClickListener(LocationsMapFragment.this);
						setLocationsResult(((LocationsActivity) getActivity()).getLocations());
					}
				}
			});
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
					Manifest.permission.ACCESS_FINE_LOCATION, false);
		} else if (map != null) {
			Timber.d("enableMyLocation: Access to the location has been granted to the app");
			map.setMyLocationEnabled(true);
		}
	}

	@Override
	public void onMyLocationChange(android.location.Location location) {
		if (mapCentered) // try to center map only once
			return;

		Timber.d("onMyLocationChange: myLocation=%s", location);
		if (location != null) {
			centerMapToCoordinate(location.getLatitude(), location.getLongitude(), 12, true);
			mapCentered = true;
			// we need the my location found callback only once
			map.setOnMyLocationChangeListener(null);
		}
	}

	private void centerMapToCoordinate(double lat, double lng, float zoom, boolean withAnimation) {
		CameraPosition position = new CameraPosition.Builder()
				.target(new LatLng(lat, lng))
				.zoom(zoom).build();
		if (withAnimation) {
			map.animateCamera(CameraUpdateFactory.newCameraPosition(position));
		} else {
			map.moveCamera(CameraUpdateFactory.newCameraPosition(position));
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

		public LocationRenderer(GoogleMap map) {
			super(getActivity().getApplicationContext(), map, clusterManager);
		}

		@Override
		protected void onBeforeClusterItemRendered(Location location, MarkerOptions markerOptions) {
			markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
			// Set the info window to show the location title and address
			markerOptions.title(location.title).snippet(location.address);
			super.onBeforeClusterItemRendered(location, markerOptions);
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

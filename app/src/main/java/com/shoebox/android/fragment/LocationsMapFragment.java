package com.shoebox.android.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.shoebox.android.bean.Location;
import com.shoebox.android.event.LocationClickedEvent;
import com.shoebox.android.util.ShoeBoxAnalytics;
import com.shoebox.android.util.permission.PermissionRequest;
import com.shoebox.android.util.permission.PermissionResult;
import com.shoebox.android.util.permission.PermissionUtil;
import com.shoebox.android.viewmodel.PermissionsViewModel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.Lazy;
import dagger.android.support.AndroidSupportInjection;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class LocationsMapFragment extends com.google.android.gms.maps.SupportMapFragment implements
		GoogleMap.OnMyLocationButtonClickListener, ClusterManager.OnClusterItemInfoWindowClickListener<Location>,
		ClusterManager.OnClusterClickListener<Location>, OnMapReadyCallback, LocationsActivity.LocationsListener,
		GoogleMap.OnMyLocationChangeListener {

	private static final double ROMANIA_CENTER_LATITUDE = 45.867063;
	private static final double ROMANIA_CENTER_LONGITUDE = 24.91699199999993;

	private static final String BUNDLE_MAP_CENTERED = "map_centered";

	@Inject
	protected Lazy<FirebaseAnalytics> firebaseAnalytics;

	boolean mapCentered = false;
	private GoogleMap map;
	private List<Location> locations;
	private ClusterManager<Location> clusterManager;
	private SetLocationMarkersTask setMarkersTask;

	private PermissionsViewModel permissionsViewModel;
	private Disposable permissionDisposable;

	public LocationsMapFragment() {
		// Required empty public constructor
	}

	@Override
	public void onAttach(Activity activity) {
		AndroidSupportInjection.inject(this);
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getMapAsync(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		permissionsViewModel = ViewModelProviders.of(this).get(PermissionsViewModel.class);
		permissionsViewModel.init(getActivity());

		permissionDisposable = permissionsViewModel.getPermissionResult()
				.doOnNext(result -> Timber.d(result.toString()))
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(this::onResult, Timber::e);
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		Timber.d("onSaveInstanceState zoom=%s", mapCentered);
		outState.putBoolean(BUNDLE_MAP_CENTERED, mapCentered);
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onDestroyView() {
		if (permissionsViewModel != null) {
			permissionsViewModel.clear();
		}
		if (permissionDisposable != null && !permissionDisposable.isDisposed()) {
			permissionDisposable.dispose();
		}
		super.onDestroyView();
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

	@Override
	public void setStatus(String status) {
		// TODO
	}

	@Override
	public void setLocationsResult(List<Location> locations) {
		Timber.d("setLocationsResult: map=%s & size=%d", map, locations == null ? -1 : locations.size());
		if (map == null)
			return;

		boolean filtering = false;
		if (this.locations != null && locations != null) {
			filtering = this.locations.size() > locations.size();
			if (!filtering && this.locations.containsAll(locations)) return;
		}
		Timber.d("setLocationsResult: filtering=%b", filtering);

		this.locations = locations;
		// Attempt to cancel the in-flight request.
		if (setMarkersTask != null) {
			setMarkersTask.cancel(true);
		}

		if (locations == null || locations.isEmpty() || filtering) {
			// clear the map when we don't have locations
			map.clear();
			clusterManager.clearItems();
		}
		setMarkersTask = new SetLocationMarkersTask();
		setMarkersTask.execute(locations);

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

	private void requestPermission(PermissionRequest permissionRequest) {
		if (permissionsViewModel != null) {
			permissionsViewModel.requestPermissionDetailed(permissionRequest);
		}
	}

	private void onResult(PermissionResult permissionResult) {
		onPermissionResult(permissionResult.isGranted(), permissionResult.getPermissionRequest());

		if (permissionResult.getRequestType() == PermissionResult.RequestType.DETAILED) {
			if (permissionResult.isShouldShowRequestPermissionRationale()) {
				PermissionUtil.showSnackbarWithAction(getActivity(), permissionResult.getPermissionRequest(),
						view -> permissionsViewModel.requestPermission(permissionResult.getPermissionRequest()));
			} else if (!permissionResult.isGranted()) {
				PermissionUtil.showSettingsSnackbar(getActivity(), permissionResult.getPermissionRequest());
			}
		}
	}

	private void onPermissionResult(boolean granted, PermissionRequest permissionRequest) {
		if (granted) {
			Timber.d("onPermissionResult: Enable the my location layer <- the permission has been granted.");
			enableMyLocation();
		} else {
			String errorMsg = "onPermissionResult: Missing permission for: " + permissionRequest.getPermission();
			ShoeBoxAnalytics.sendErrorState(firebaseAnalytics.get(), errorMsg);
			Timber.d(errorMsg);
		}
	}

	private void setUpMapIfNeeded() {
		Timber.i("setUpMapIfNeeded: map = %s ", map);
		// Do a null check to confirm that we have not already instantiated the map.
		if (map == null) {
			// Try to obtain the map from the SupportMapFragment.
			getMapAsync(googleMap -> {
				map = googleMap;
				Context context = getActivity();
				// Check if we were successful in obtaining the map.
				if (map != null && context != null) {
					// setup cluster manager and needed listeners
					clusterManager = new ClusterManager<>(context, map);
					clusterManager.setRenderer(new LocationRenderer(context, map));
					map.setOnCameraChangeListener(clusterManager);
					map.setOnMarkerClickListener(clusterManager);
					map.setOnInfoWindowClickListener(clusterManager);
					map.setOnMyLocationChangeListener(LocationsMapFragment.this);
					clusterManager.setOnClusterItemInfoWindowClickListener(LocationsMapFragment.this);
					clusterManager.setOnClusterClickListener(LocationsMapFragment.this);
					setLocationsResult(((LocationsActivity) getActivity()).getLocations());
				}
			});
		}
	}

	/**
	 * Enables the My Location layer if the fine location permission has been granted.
	 */
	@SuppressLint("MissingPermission")
	private void enableMyLocation() {
		if (getActivity() == null) {
			Timber.e("enableMyLocation: getActivity() returned NULL!!!");
			return;
		}
		final String[] locationPermissions = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
		if (!PermissionUtil.hasPermissions(getActivity(), locationPermissions)) {
			Timber.d("enableMyLocation: Permission to access the location is missing");
			requestPermission(PermissionRequest.DETECT_LOCATION);
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

		SetLocationMarkersTask() {
			super();
		}

		@SafeVarargs
		@Override
		protected final Void doInBackground(List<Location>... locationsList) {
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

		LocationRenderer(Context context, GoogleMap map) {
			super(context, map, clusterManager);
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

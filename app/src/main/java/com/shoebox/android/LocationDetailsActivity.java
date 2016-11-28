package com.shoebox.android;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.shoebox.android.adapter.LocationDetailsRecyclerAdapter;
import com.shoebox.android.beans.Location;
import com.shoebox.android.event.ContactCallClickedEvent;
import com.shoebox.android.event.DistanceCalculatedEvent;
import com.shoebox.android.fragment.LocationDetailsMapFragment;
import com.shoebox.android.util.BusProvider;
import com.shoebox.android.util.ShoeBoxAnalytics;
import com.shoebox.android.util.UIUtils;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import butterknife.BindView;
import butterknife.OnClick;
import timber.log.Timber;

public class LocationDetailsActivity extends BaseActivity {

	public static final String EXTRA_LOCATION = "location";
	private static final String FRAGMENT_TAG_MAP = "map_locations";
	private static final String GET_DIRECTIONS_PREFIX = "google.navigation:q=";
	private final Bus bus = BusProvider.get();
	@BindView(R.id.locationDetailsItems)
	RecyclerView recyclerView;

	private LocationDetailsMapFragment mapFragment;
	private Location location;

	private LocationDetailsRecyclerAdapter detailsRecyclerAdapter;

	public static Intent getLaunchingIntent(Context context, Location location) {
		Intent intent = new Intent(context, LocationDetailsActivity.class);
		intent.putExtra(EXTRA_LOCATION, location);
		return intent;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location_details);

		location = getIntent().getParcelableExtra(EXTRA_LOCATION);

		setTitle(location.title);

		initMapFragment();

		recyclerView.setHasFixedSize(true);
		LinearLayoutManager layoutManager = new LinearLayoutManager(this);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		recyclerView.setLayoutManager(layoutManager);
		recyclerView.setItemAnimator(new DefaultItemAnimator());

		detailsRecyclerAdapter = new LocationDetailsRecyclerAdapter(location);
		recyclerView.setAdapter(detailsRecyclerAdapter);

		Bundle bundle = new Bundle(3);
		bundle.putString(ShoeBoxAnalytics.Param.LOCATION_TITLE, location.title);
		bundle.putString(ShoeBoxAnalytics.Param.LOCATION_CITY, location.city);
		bundle.putString(ShoeBoxAnalytics.Param.LOCATION_COUNTRY, location.country);
		firebaseAnalytics.logEvent(ShoeBoxAnalytics.State.LOCATION_DETAILS, bundle);
	}

	@Override
	public void onResume() {
		super.onResume();
		bus.register(this);
	}

	@Override
	public void onPause() {
		bus.unregister(this);
		super.onPause();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private void initMapFragment() {
		FragmentManager fm = getSupportFragmentManager();
		mapFragment = (LocationDetailsMapFragment) fm.findFragmentByTag(FRAGMENT_TAG_MAP);

		FragmentTransaction ft = fm.beginTransaction();
		if (mapFragment == null) {
			mapFragment = LocationDetailsMapFragment.newInstance(location);
			ft.add(R.id.fragmentHolder, mapFragment, FRAGMENT_TAG_MAP);
		}

		ft.commit();
	}

	@OnClick(R.id.fab)
	public void doGetDirections() {
		Uri uri;
		if (location.hasGeoLocation()) {
			uri = Uri.parse(GET_DIRECTIONS_PREFIX + location.latitude + ", " + location.longitude);
		} else {
			String address = "";
			try {
				address = URLEncoder.encode(location.addressFull, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				Timber.e(e, "doGetDirections");
			}
			uri = Uri.parse(GET_DIRECTIONS_PREFIX + address);
		}
		Timber.d("doGetDirections: uri = %s", uri.toString());
		firebaseAnalytics.logEvent(ShoeBoxAnalytics.Action.SHOW_DIRECTIONS, null);
		try {
			Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
			startActivity(intent);
		} catch (ActivityNotFoundException e) {
			ShoeBoxAnalytics.sendErrorState(firebaseAnalytics, getString(R.string.msg_no_navigation));
			UIUtils.showMessage(this, R.string.msg_no_navigation);
		}
	}

	@Subscribe
	public void onDistanceCalculated(DistanceCalculatedEvent event) {
		detailsRecyclerAdapter.setDistance(event.distance);
	}

	@Subscribe
	public void onContactCallClicked(ContactCallClickedEvent event) {
		firebaseAnalytics.logEvent(ShoeBoxAnalytics.Action.CALL_CONTACT, null);
		UIUtils.launchDial(this, event.phoneNumber);
	}
}

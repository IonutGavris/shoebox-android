package com.shoebox.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;
import com.shoebox.android.bean.Location;
import com.shoebox.android.event.LocationClickedEvent;
import com.shoebox.android.fragment.LocationsListFragment;
import com.shoebox.android.fragment.LocationsMapFragment;
import com.shoebox.android.util.ShoeBoxAnalytics;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

public class LocationsActivity extends BaseActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

	private static final String BUNDLE_CURRENT_VIEW_MODE = "current_view_mode";

	private static final String FRAGMENT_TAG_MAP = "map_locations";
	private static final String FRAGMENT_TAG_LIST = "list_locations";

	private static final String dataPath = "locations";

	private final EventBus bus = EventBus.getDefault();

	@BindView(R.id.filterLayout)
	View filterLayout;
	@BindView(R.id.filterShopsView)
	EditText filterShopsView;

	private Fragment mapFragment;
	private Fragment listFragment;
	private ViewMode currentViewMode = ViewMode.MAP;
	private List<Location> locations;

	private DatabaseReference locationsRef;
	private ValueEventListener valueEventListener;
	private CompositeSubscription compositeSubscription;

	public static Intent getLaunchingIntent(Context context) {
		return new Intent(context, LocationsActivity.class);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_locations);

		if (savedInstanceState != null) {
			currentViewMode = ViewMode.valueOf(savedInstanceState.getInt(BUNDLE_CURRENT_VIEW_MODE, ViewMode.LIST.ordinal()));
		}

		filterLayout.setVisibility(View.GONE);

		initFragments(currentViewMode);

		valueEventListener = new ValueEventListener() {
			// TODO try to use orderByChild for sorting
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				Timber.d("The %s read was successful :)", dataPath);

				GenericTypeIndicator<List<Location>> t = new GenericTypeIndicator<List<Location>>() {
				};
				List<Location> locations = dataSnapshot.getValue(t);
				if (locations != null && locations.size() > 1 && locations.get(0) == null) {
					locations.remove(0);
					if (locations.size() > 0) {
						filterLayout.setVisibility(currentViewMode == ViewMode.LIST ? View.VISIBLE : View.GONE);
					}
					Timber.d("The %s count = %s", dataPath, locations.size());
				}
				LocationsActivity.this.locations = locations;
				filterShopsView.setText("");
				setLocationsToFragments(locations);
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {
				Timber.e("The %s read failed: %s ", dataPath, databaseError.getMessage());
				ShoeBoxAnalytics.sendErrorState(firebaseAnalytics, "Locations read failed: " + databaseError.getMessage());
				setStatusToFragments(databaseError.getMessage());
			}
		};
		locationsRef = firebase.getReference(dataPath);
		locationsRef.addValueEventListener(valueEventListener);

		firebaseAnalytics.logEvent(ShoeBoxAnalytics.State.LOCATIONS, null);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		Timber.d("onPostCreate");
		if (savedInstanceState == null) {
			startReactingOnChanges();
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		bus.register(this);
	}

	@Override
	public void onStop() {
		bus.unregister(this);
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		locationsRef.removeEventListener(valueEventListener);
		compositeSubscription.unsubscribe();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(BUNDLE_CURRENT_VIEW_MODE, currentViewMode.ordinal());
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		startReactingOnChanges();
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
			grantResults) {
		((LocationsMapFragment) mapFragment).onRequestPermissionsEnded(requestCode, permissions, grantResults);
	}

	public List<Location> getLocations() {
		return locations;
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onLocationClicked(LocationClickedEvent event) {
		startActivity(LocationDetailsActivity.getLaunchingIntent(this, event.location));
	}

	@OnClick(R.id.fab)
	public void doFabClicked(FloatingActionButton fab) {
		if (currentViewMode == ViewMode.MAP) {
			firebaseAnalytics.logEvent(ShoeBoxAnalytics.Action.LOCATIONS_VIEW_LIST, null);
			switchFragmentsVisibility(mapFragment, listFragment);
			fab.setImageResource(R.drawable.ic_action_map);
		} else {
			switchFragmentsVisibility(listFragment, mapFragment);
			fab.setImageResource(R.drawable.ic_action_view_as_list);
		}
	}

	private void initFragments(ViewMode viewMode) {
		FragmentManager fm = getSupportFragmentManager();
		mapFragment = fm.findFragmentByTag(FRAGMENT_TAG_MAP);
		listFragment = fm.findFragmentByTag(FRAGMENT_TAG_LIST);

		FragmentTransaction ft = fm.beginTransaction();
		if (mapFragment == null) {
			mapFragment = new LocationsMapFragment();
			ft.add(R.id.fragmentHolder, mapFragment, FRAGMENT_TAG_MAP);
		}
		if (listFragment == null) {
			listFragment = new LocationsListFragment();
			ft.add(R.id.fragmentHolder, listFragment, FRAGMENT_TAG_LIST);
		}
		// fragment transaction don't persist visible state
		ft.show(viewMode == ViewMode.LIST ? listFragment : mapFragment);
		ft.hide(viewMode == ViewMode.MAP ? listFragment : mapFragment);

		ft.commit();
	}

	private void switchFragmentsVisibility(Fragment oldFragment, Fragment newFragment) {
		if (newFragment.isVisible()) {
			return;
		}
		currentViewMode = newFragment == listFragment ? ViewMode.LIST : ViewMode.MAP;
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
		ft.show(newFragment);
		ft.hide(oldFragment);
		ft.commitAllowingStateLoss();

		// set cursor to the new fragment so it can show the data
		((LocationsListener) newFragment).setLocationsResult(locations);

		// after fragment is switch the search view will take focus.
		// execute now and clear focus
		fm.executePendingTransactions();
		filterLayout.setVisibility(currentViewMode == ViewMode.LIST ? View.VISIBLE : View.GONE);
	}

	private void setLocationsToFragments(List<Location> locations) {
		Timber.d("setLocationsToFragments currentViewMode = %s", currentViewMode);
		((LocationsListener) listFragment).setLocationsResult(locations);
		((LocationsListener) mapFragment).setLocationsResult(locations);
	}

	private void setStatusToFragments(String status) {
		Timber.d("setStatusToFragments currentViewMode = %s  &  status = %s", currentViewMode, status);
		((LocationsListener) listFragment).setStatus(status);
		((LocationsListener) mapFragment).setStatus(status);
	}

	private void startReactingOnChanges() {
		compositeSubscription = new CompositeSubscription();

		compositeSubscription.add(RxTextView.textChangeEvents(filterShopsView)
				.debounce(150, TimeUnit.MILLISECONDS)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Action1<TextViewTextChangeEvent>() {
					@Override
					public void call(TextViewTextChangeEvent event) {
						if (event.text().length() != 0) {
							List<Location> filteredLocations = new ArrayList<>();
							for (Location location : locations) {
								if (location.containsFilter(event.text().toString().trim())) {
									filteredLocations.add(location);
								}
							}
							setLocationsToFragments(filteredLocations);
						} else if ((event.text().length() == 0 && event.before() > 0)) {
							setLocationsToFragments(locations);
						}
					}
				}));
	}

	public enum ViewMode {
		LIST, MAP;

		public static ViewMode valueOf(int ordinal) {
			return ViewMode.class.getEnumConstants()[ordinal];
		}
	}


	/**
	 * Interface for notifying when the data finished loading or when we need to set a status message
	 */
	public interface LocationsListener {
		void setStatus(String status);

		void setLocationsResult(List<Location> locations);
	}

}

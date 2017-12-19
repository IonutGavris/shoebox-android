package com.shoebox.android.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shoebox.android.LocationsActivity;
import com.shoebox.android.R;
import com.shoebox.android.adapter.LocationsAdapter;
import com.shoebox.android.bean.Location;
import com.shoebox.android.util.DividerItemDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * A fragment representing a list of {@link Location} items.
 */
public class LocationsListFragment extends BaseFragment implements LocationsActivity.LocationsListener {

	@BindView(R.id.locationsRecycler)
	RecyclerView locationsRecycler;

	LocationsAdapter recyclerViewAdapter;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public LocationsListFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View fragmentView = inflater.inflate(R.layout.fragment_location_list, container, false);
		unbinder = ButterKnife.bind(this, fragmentView);

		locationsRecycler.setLayoutManager(new LinearLayoutManager(fragmentView.getContext()));
		locationsRecycler.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
		recyclerViewAdapter = new LocationsAdapter();
		locationsRecycler.setAdapter(recyclerViewAdapter);

		return fragmentView;
	}

	@Override
	public void setStatus(String status) {
		// TODO
	}

	@Override
	public void setLocationsResult(List<Location> locations) {
		Timber.d("setLocationsResult: size=%d", locations == null ? -1 : locations.size());
		recyclerViewAdapter.setLocations(locations);
	}

}

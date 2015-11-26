package com.shoebox.android.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shoebox.android.LocationsActivity;
import com.shoebox.android.R;
import com.shoebox.android.adapter.LocationsRecyclerViewAdapter;
import com.shoebox.android.beans.Location;
import com.shoebox.android.util.DividerItemDecoration;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A fragment representing a list of {@link Location} items.
 */
public class LocationsListFragment extends BaseFragment implements LocationsActivity.LocationsListener {

	@InjectView(R.id.locationsRecycler)
	RecyclerView locationsRecycler;

	LocationsRecyclerViewAdapter recyclerViewAdapter;

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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View fragmentView = inflater.inflate(R.layout.fragment_location_list, container, false);
		ButterKnife.inject(this, fragmentView);

		locationsRecycler.setLayoutManager(new LinearLayoutManager(fragmentView.getContext()));
		locationsRecycler.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
		recyclerViewAdapter = new LocationsRecyclerViewAdapter();
		locationsRecycler.setAdapter(recyclerViewAdapter);

		return fragmentView;
	}

	@Override
	public void setStatus(String status) {
		// TODO
	}

	@Override
	public void setLocationsResult(List<Location> locations) {
		recyclerViewAdapter.setLocations(locations);
	}

}

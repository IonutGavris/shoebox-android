package com.shoebox.android.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shoebox.android.R;
import com.shoebox.android.beans.Location;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * {@link RecyclerView.Adapter} that can display a list of {@link Location} items
 */
public class LocationsAdapter extends RecyclerView.Adapter<LocationsAdapter.LocationViewHolder> {

	private List<Location> locations = new ArrayList<>();

	public LocationsAdapter() {
	}

	@Override
	public LocationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new LocationViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_location_item,
				parent, false));
	}

	@Override
	public void onBindViewHolder(final LocationViewHolder holder, int position) {
		holder.setData(locations.get(position));
	}

	@Override
	public int getItemCount() {
		return locations == null ? 0 : locations.size();
	}

	public void setLocations(List<Location> locations) {
		this.locations = locations;
		notifyDataSetChanged();
	}

	public class LocationViewHolder extends RecyclerView.ViewHolder {
		@InjectView(R.id.locationTitleView)
		TextView locationTitleView;
		@InjectView(R.id.cityCountryView)
		TextView cityCountryView;

		public LocationViewHolder(View itemView) {
			super(itemView);
			ButterKnife.inject(this, itemView);
		}

		public void setData(final Location location) {
			locationTitleView.setText(location.title);
			cityCountryView.setText(cityCountryView.getResources().getString(R.string.label_city_country, location
					.city, location.country));
		}
	}
}

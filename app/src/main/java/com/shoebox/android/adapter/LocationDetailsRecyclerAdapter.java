package com.shoebox.android.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shoebox.android.R;
import com.shoebox.android.beans.Location;
import com.shoebox.android.beans.LocationContact;
import com.shoebox.android.event.ContactCallClickedEvent;
import com.shoebox.android.util.BusProvider;
import com.squareup.otto.Bus;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LocationDetailsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
	protected static final int TYPE_TITLE = 0;
	protected static final int TYPE_DISTANCE = 1;
	protected static final int TYPE_FULL_ADDRESS = 2;
	protected static final int TYPE_HOURS = 3;
	protected static final int TYPE_MESSAGE = 4;
	protected static final int TYPE_CONTACT = 5;

	private ArrayList<Integer> nonContactItems = new ArrayList<>();

	private Location location;
	private double distance;

	public LocationDetailsRecyclerAdapter(Location location) {
		setLocation(location);
	}

	public boolean manageDetailItem(int item, boolean shouldAdd) {
		if (shouldAdd) {
			if (!nonContactItems.contains(item)) {
				nonContactItems.add(item);
				return true;
			}
		} else {
			return nonContactItems.remove((Integer) item);
		}
		return false;
	}

	/**
	 * Sets the location for which we have to display the details
	 *
	 * @param location The location for which we have to display the details
	 */
	public void setLocation(Location location) {
		this.location = location;
		boolean shouldSort = manageDetailItem(TYPE_TITLE, true);
		shouldSort |= manageDetailItem(TYPE_DISTANCE, true);
		shouldSort |= manageDetailItem(TYPE_FULL_ADDRESS, true);
		shouldSort |= manageDetailItem(TYPE_HOURS, location.hasHours());
		shouldSort |= manageDetailItem(TYPE_MESSAGE, location.hasMessage());
		if (shouldSort) {
			Collections.sort(nonContactItems);
		}
		notifyDataSetChanged();
	}

	public void setDistance(double distance) {
		this.distance = distance;
		notifyDataSetChanged();
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		switch (viewType) {
			case TYPE_TITLE:
			case TYPE_DISTANCE:
			case TYPE_FULL_ADDRESS:
			case TYPE_HOURS:
			case TYPE_MESSAGE:
				return new LocationDetailHolder(inflater.inflate(R.layout.holder_item_location_detail, parent, false));
			case TYPE_CONTACT:
				return new LocationContactHolder(inflater.inflate(R.layout.holder_item_location_contact, parent, false));
		}
		return null;
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		if (position < nonContactItems.size()) {
			switch (getItemViewType(position)) {
				case TYPE_DISTANCE:
					((LocationDetailHolder) holder).setData(distance);
					break;
				case TYPE_TITLE:
				case TYPE_FULL_ADDRESS:
				case TYPE_HOURS:
				case TYPE_MESSAGE:
					((LocationDetailHolder) holder).setData(location, nonContactItems.get(position));
					break;
			}
		} else {
			((LocationContactHolder) holder).setData(location.contacts.get(position - nonContactItems.size()));
		}
	}

	@Override
	public int getItemCount() {
		return nonContactItems.size() + (location.hasContacts() ? location.contacts.size() : 0);
	}

	@Override
	public int getItemViewType(int position) {
		if (position < nonContactItems.size()) {
			return nonContactItems.get(position);
		}
		return TYPE_CONTACT;
	}
}

class LocationDetailHolder extends RecyclerView.ViewHolder {
	@InjectView(R.id.detailIcon)
	ImageView detailIcon;
	@InjectView(R.id.detailDescription)
	TextView detailDescription;

	public LocationDetailHolder(View itemView) {
		super(itemView);
		ButterKnife.inject(this, itemView);
	}

	public void setData(final Location location, final int detailType) {
		String detailDescr = null;
		int actionIcon = 0;
		switch (detailType) {
			case LocationDetailsRecyclerAdapter.TYPE_TITLE:
				detailDescr = location.title;
				actionIcon = R.drawable.ic_location_on_black_24dp;
				break;
			case LocationDetailsRecyclerAdapter.TYPE_FULL_ADDRESS:
				detailDescr = location.addressFull;
				actionIcon = R.drawable.ic_location_city_black_24dp;
				break;
			case LocationDetailsRecyclerAdapter.TYPE_HOURS:
				detailDescr = location.hours;
				actionIcon = R.drawable.ic_schedule_black_24dp;
				break;
			case LocationDetailsRecyclerAdapter.TYPE_MESSAGE:
				detailDescr = location.messages;
				actionIcon = R.drawable.ic_comment_black_24dp;
				break;
		}
		detailIcon.setImageResource(actionIcon);
		detailDescription.setText(detailDescr);
	}

	public void setData(final double distance) {
		detailIcon.setImageResource(R.drawable.ic_ruler_black_24dp);
		detailDescription.setText(
				distance == -1 ? detailDescription.getResources().getString(R.string.label_distance_na) :
						(distance > 0 ? detailDescription.getResources().getString(R.string.label_distance, String.format("%.1f", distance))
								: detailDescription.getResources().getString(R.string.label_distance_calculating)));
	}
}

class LocationContactHolder extends RecyclerView.ViewHolder {
	private final Bus bus = BusProvider.get();

	@InjectView(R.id.contactIcon)
	ImageView contactIcon;
	@InjectView(R.id.contactName)
	TextView contactName;
	@InjectView(R.id.contactDetail)
	TextView contactDetail;

	private View itemView;

	public LocationContactHolder(View itemView) {
		super(itemView);
		ButterKnife.inject(this, itemView);
		this.itemView = itemView;
	}

	public void setData(final LocationContact locationContact) {
		contactName.setText(locationContact.hasName() ? locationContact.name : null);
		contactName.setVisibility(locationContact.hasName() ? View.VISIBLE : View.GONE);

		contactDetail.setText(locationContact.hasPhoneNumber() ? locationContact.phoneNumber : null);
		contactDetail.setVisibility(locationContact.hasPhoneNumber() ? View.VISIBLE : View.GONE);

		if (locationContact.hasPhoneNumber()) {
			contactIcon.setImageResource(R.drawable.ic_contact_phone_black_24dp);
			itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					bus.post(new ContactCallClickedEvent(locationContact.phoneNumber));
				}
			});
		} else {
			contactIcon.setImageResource(R.drawable.ic_face_black_24dp);
		}
	}
}
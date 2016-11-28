package com.shoebox.android.event;

import com.shoebox.android.bean.Location;

/**
 * The event to fire when a location is clicked.
 */
public class LocationClickedEvent {
	public Location location;

	public LocationClickedEvent(Location location) {
		this.location = location;
	}
}

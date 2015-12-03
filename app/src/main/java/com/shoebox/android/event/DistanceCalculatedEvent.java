package com.shoebox.android.event;

/**
 * The event to fire when the distance from my location to selected location could be calculated
 */
public class DistanceCalculatedEvent {
	public double distance;

	public DistanceCalculatedEvent(double distance) {
		this.distance = distance;
	}
}

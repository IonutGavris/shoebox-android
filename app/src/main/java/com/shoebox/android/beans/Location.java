package com.shoebox.android.beans;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * The bean used for box delivery locations.
 * Created by diana.sabau on 25-Nov-15.
 */
public class Location implements ClusterItem {

	public String title;
	public String address;
	public String city;
	public String state;
	public String country;
	public String postalCode;
	public String messages;
	public double latitude;
	public double longitude;

	@Override
	public String toString() {
		return "Location{" +
				"messages='" + messages + '\'' +
				", title='" + title + '\'' +
				", address='" + address + '\'' +
				", city='" + city + '\'' +
				", state='" + state + '\'' +
				", country='" + country + '\'' +
				", postalCode='" + postalCode + '\'' +
				", latitude=" + latitude +
				", longitude=" + longitude +
				'}';
	}

	@Override
	public LatLng getPosition() {
		return new LatLng(latitude, longitude);
	}

	public boolean hasGeoLocation() {
		return latitude != 0 && longitude != 0;
	}
}

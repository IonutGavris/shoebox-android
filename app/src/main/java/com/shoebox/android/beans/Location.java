package com.shoebox.android.beans;

/**
 * The bean used for box delivery locations.
 * Created by diana.sabau on 25-Nov-15.
 */
public class Location {

	public String title;
	public String address;
	public String city;
	public String state;
	public String country;
	public String postalCode;
	public String messages;
	public String latitude;
	public String longitude;

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
}

package com.shoebox.android.beans;

/**
 * The bean used for box delivery locations.
 * Created by diana.sabau on 25-Nov-15.
 */
public class Location {

	public String locationContact1;
	public String locationContact2;
	public String locationDetails;
	public String locationHours;
	public String locationPhoneNo1;
	public String locationPhoneNo2;
	public String locationCity;
	public String locationStreetAddress;
	public float locationLatitude;
	public float locationLongitude;

	@Override
	public String toString() {
		return "Location{" +
				"locationCity='" + locationCity + '\'' +
				", locationContact1='" + locationContact1 + '\'' +
				", locationContact2='" + locationContact2 + '\'' +
				", locationDetails='" + locationDetails + '\'' +
				", locationHours='" + locationHours + '\'' +
				", locationPhoneNo1='" + locationPhoneNo1 + '\'' +
				", locationPhoneNo2='" + locationPhoneNo2 + '\'' +
				", locationStreetAddress='" + locationStreetAddress + '\'' +
				", locationLatitude=" + locationLatitude +
				", locationLongitude=" + locationLongitude +
				'}';
	}
}

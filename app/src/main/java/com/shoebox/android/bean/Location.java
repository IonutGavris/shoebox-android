package com.shoebox.android.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import java.util.List;

/**
 * The bean used for box delivery locations.
 * Created by diana.sabau on 25-Nov-15.
 */
public class Location implements ClusterItem, Parcelable {

	public static final Parcelable.Creator<Location> CREATOR = new Parcelable.Creator<Location>() {
		@Override
		public Location createFromParcel(Parcel source) {
			return new Location(source);
		}

		@Override
		public Location[] newArray(int size) {
			return new Location[size];
		}
	};

	public String title;
	public String address;
	public String addressFull;
	public String city;
	public String state;
	public String country;
	public String postalCode;
	public String messages;
	public String hours;
	public List<LocationContact> contacts;
	public double latitude;
	public double longitude;

	public Location() {
	}

	public Location(Parcel source) {
		readFromParcel(source);
	}

	@Override
	public String toString() {
		return "Location{" +
				"address='" + address + '\'' +
				", title='" + title + '\'' +
				", addressFull='" + addressFull + '\'' +
				", city='" + city + '\'' +
				", state='" + state + '\'' +
				", country='" + country + '\'' +
				", postalCode='" + postalCode + '\'' +
				", messages='" + messages + '\'' +
				", hours='" + hours + '\'' +
				", contacts=" + contacts +
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

	public boolean hasHours() {
		return !TextUtils.isEmpty(hours);
	}

	public boolean hasContacts() {
		return contacts != null && contacts.size() > 0;
	}

	public boolean hasMessage() {
		return !TextUtils.isEmpty(messages);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(title);
		dest.writeString(address);
		dest.writeString(addressFull);
		dest.writeString(city);
		dest.writeString(state);
		dest.writeString(country);
		dest.writeString(postalCode);
		dest.writeString(messages);
		dest.writeString(hours);
		dest.writeList(contacts);
		dest.writeDouble(latitude);
		dest.writeDouble(longitude);
	}

	private void readFromParcel(Parcel parcelSource) {
		title = parcelSource.readString();
		address = parcelSource.readString();
		addressFull = parcelSource.readString();
		city = parcelSource.readString();
		state = parcelSource.readString();
		country = parcelSource.readString();
		postalCode = parcelSource.readString();
		messages = parcelSource.readString();
		hours = parcelSource.readString();
		contacts = parcelSource.readArrayList(LocationContact.class.getClassLoader());
		latitude = parcelSource.readDouble();
		longitude = parcelSource.readDouble();
	}
}

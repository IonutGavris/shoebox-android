package com.shoebox.android.beans;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/**
 * The bean used for a location's contact.
 * Created by diana.sabau on 03-Dec-15.
 */
public class LocationContact implements Parcelable {

	public static final Parcelable.Creator<LocationContact> CREATOR = new Parcelable.Creator<LocationContact>() {
		@Override
		public LocationContact createFromParcel(Parcel source) {
			return new LocationContact(source);
		}

		@Override
		public LocationContact[] newArray(int size) {
			return new LocationContact[size];
		}
	};

	public String name;
	public String phoneNumber;

	public LocationContact() {
	}

	public LocationContact(Parcel source) {
		readFromParcel(source);
	}

	@Override
	public String toString() {
		return "LocationContact{" +
				"name='" + name + '\'' +
				", phoneNumber='" + phoneNumber + '\'' +
				'}';
	}

	public boolean hasName() {
		return !TextUtils.isEmpty(name);
	}

	public boolean hasPhoneNumber() {
		return !TextUtils.isEmpty(phoneNumber);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	private void readFromParcel(Parcel parcelSource) {
		name = parcelSource.readString();
		phoneNumber = parcelSource.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeString(phoneNumber);
	}
}

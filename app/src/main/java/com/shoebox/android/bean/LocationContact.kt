package com.shoebox.android.bean

import android.os.Parcel
import android.os.Parcelable
import android.text.TextUtils

/**
 * The bean used for a location's contact.
 * Created by diana.sabau on 03-Dec-15.
 */
class LocationContact : Parcelable {

    var name: String? = null
    var phoneNumber: String? = null

    constructor() {}

    constructor(source: Parcel) {
        readFromParcel(source)
    }

    fun hasName(): Boolean {
        return !TextUtils.isEmpty(name)
    }

    fun hasPhoneNumber(): Boolean {
        return !TextUtils.isEmpty(phoneNumber)
    }

    override fun describeContents(): Int {
        return 0
    }

    private fun readFromParcel(parcelSource: Parcel) {
        name = parcelSource.readString()
        phoneNumber = parcelSource.readString()
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(name)
        dest.writeString(phoneNumber)
    }

    override fun toString(): String {
        return "LocationContact{" +
                "name='" + name + '\''.toString() +
                ", phoneNumber='" + phoneNumber + '\''.toString() +
                '}'.toString()
    }

    companion object {

        @JvmField val CREATOR: Parcelable.Creator<LocationContact> = object : Parcelable.Creator<LocationContact> {
            override fun createFromParcel(source: Parcel): LocationContact {
                return LocationContact(source)
            }

            override fun newArray(size: Int): Array<LocationContact?> {
                return arrayOfNulls(size)
            }
        }
    }
}

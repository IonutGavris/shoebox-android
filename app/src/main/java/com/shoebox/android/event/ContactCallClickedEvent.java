package com.shoebox.android.event;

/**
 * The event to fire when a contact phone number is clicked.
 */
public class ContactCallClickedEvent {
	public String phoneNumber;

	public ContactCallClickedEvent(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
}

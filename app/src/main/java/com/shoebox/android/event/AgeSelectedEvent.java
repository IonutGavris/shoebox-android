package com.shoebox.android.event;

import com.shoebox.android.bean.AgeInterval;

/**
 * Created by vasile.mihalca on 02/12/15.
 */
public class AgeSelectedEvent {
	public AgeInterval ageInterval;

	public AgeSelectedEvent(AgeInterval ageInterval) {
		this.ageInterval = ageInterval;
	}
}

package com.shoebox.android.beans;

/**
 * Created by vasile.mihalca on 02/12/15.
 */
public class AgeInterval {
	public int minAge;
	public int maxAge;
	public boolean custom;

	public AgeInterval(int minAge, int maxAge) {
		this.minAge = minAge;
		this.maxAge = maxAge;
	}

	public AgeInterval(int maxAge, boolean custom) {
		this.minAge = maxAge;
		this.maxAge = maxAge;
		this.custom = custom;
	}

	public AgeInterval(boolean custom) {
		this.custom = custom;
	}
}

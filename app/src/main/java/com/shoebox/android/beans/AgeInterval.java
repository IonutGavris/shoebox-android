package com.shoebox.android.beans;

import java.io.Serializable;

/**
 * Created by vasile.mihalca on 02/12/15.
 */
public class AgeInterval implements Serializable {
	public int minAge;
	public int maxAge;
	public boolean custom;

	public AgeInterval(int minAge, int maxAge) {
		this.minAge = minAge;
		this.maxAge = maxAge;
	}

	public AgeInterval(int customAge) {
		this.minAge = customAge;
		this.maxAge = customAge;
		this.custom = true;
	}
}

package com.shoebox.android.beans;

/**
 * The bean used for box content suggestions.
 * Created by vasile.mihalca on 24/11/15.
 */
public class Suggestion {
	public String name;
	public String description;
	public String sex;
	public int minAge;
	public int maxAge;

	@Override
	public String toString() {
		return "Suggestion{" +
				"description='" + description + '\'' +
				", name='" + name + '\'' +
				", sex='" + sex + '\'' +
				", minAge=" + minAge +
				", maxAge=" + maxAge +
				'}';
	}
}

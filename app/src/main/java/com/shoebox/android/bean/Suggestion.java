package com.shoebox.android.bean;

import com.google.firebase.database.DataSnapshot;

/**
 * The bean used for box content suggestions.
 * Created by vasile.mihalca on 24/11/15.
 */
public class Suggestion {
	public static final String ORDER_BY = "category";

	public String key;
	public String name;
	public String description;
	public int category;
	public String sex;
	public int minAge;
	public int maxAge;

	public static Suggestion create(DataSnapshot dataSnapshot) {
		Suggestion suggestion = dataSnapshot.getValue(Suggestion.class);
		suggestion.key = dataSnapshot.getKey();
		return suggestion;
	}

	@Override
	public String toString() {
		return "Suggestion{" +
				"key='" + key + '\'' +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", category=" + category +
				", sex='" + sex + '\'' +
				", minAge=" + minAge +
				", maxAge=" + maxAge +
				'}';
	}
}

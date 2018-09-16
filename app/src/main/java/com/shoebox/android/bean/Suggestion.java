package com.shoebox.android.bean;

import java.util.Objects;

/**
 * The bean used for box content suggestions.
 * Created by vasile.mihalca on 24/11/15.
 */
public class Suggestion {
	public static final String ORDER_BY = "category";

	private static final String MALE = "male";
	private static final String FEMALE = "female";

	public String key;
	public String name;
	public String description;
	public int category;
	public String sex;
	public int minAge;
	public int maxAge;

	public boolean isValid(boolean givenIsMale, int givenMinAge, int givenMaxAge) {
		if (givenIsMale && FEMALE.equals(sex)) return false;
		if (!givenIsMale && MALE.equals(sex)) return false;

		if (givenMaxAge < minAge) return false;
		if (givenMinAge > maxAge) return false;

		return true;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Suggestion that = (Suggestion) o;
		return category == that.category &&
				minAge == that.minAge &&
				maxAge == that.maxAge &&
				Objects.equals(key, that.key) &&
				Objects.equals(name, that.name) &&
				Objects.equals(description, that.description) &&
				Objects.equals(sex, that.sex);
	}

	@Override
	public int hashCode() {
		return Objects.hash(key, name, description, category, sex, minAge, maxAge);
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

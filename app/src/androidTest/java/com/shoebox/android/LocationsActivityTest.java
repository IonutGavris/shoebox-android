package com.shoebox.android;

import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class LocationsActivityTest {

	@Rule
	public GrantPermissionRule rule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

	@Rule
	public ActivityTestRule<LocationsActivity> activityTestRule = new ActivityTestRule<>(LocationsActivity.class);

	@Test
	public void takeScreenshots() {
		onView(withId(R.id.fab)).check(matches(isDisplayed())).perform(click());
		onView(withId(R.id.filterShopsView)).perform(typeText("ecco s"), closeSoftKeyboard());
		onView(allOf(withText("ECCO Sibiu"), isDisplayed()));
	}
}
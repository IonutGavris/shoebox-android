package com.shoebox.android;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import tools.fastlane.screengrab.Screengrab;
import tools.fastlane.screengrab.locale.LocaleTestRule;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class ScreengrabTest {

	@ClassRule
	public static final LocaleTestRule localeTestRule = new LocaleTestRule();

	@Rule
	public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

	@Test
	public void takeScreenshots() throws InterruptedException {
		Screengrab.screenshot("main_screen");
		onView(withId(R.id.boxContentBtn)).perform(click()).check(doesNotExist());
		Screengrab.screenshot("box_content_screen");
	}

}

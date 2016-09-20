package com.shoebox.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.IdlingPolicies;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.core.deps.guava.collect.Iterables;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.test.runner.lifecycle.Stage;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.TextView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;

import java.util.concurrent.TimeUnit;

import timber.log.Timber;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static org.hamcrest.Matchers.is;

//import com.google.android.apps.common.testing.deps.guava.collect.Iterables;


/**
 * <p>
 * Turn off animations on your test device. Leaving system animations turned on in the test device might cause
 * unexpected results or may lead your test to fail.
 * <p>
 * Turn off animations from Settings by opening Developing Options and turning all the following options off:
 * Window animation scale
 * Transition animation scale
 * Animator duration scale
 */

public class ShoeBoxBase<T extends AppCompatActivity> extends ActivityInstrumentationTestCase2<T> {

	Context targetContext;
	ShoeBoxApplication application;

	public ShoeBoxBase(Class<T> classT) {
		super(classT);
	}

	/**
	 * Returns a matcher that matches {@link android.support.design.widget.TextInputLayout}s based on hint property value. Note: View's
	 * hint property can be null.
	 *
	 * @param stringMatcher <a href="http://hamcrest.org/JavaHamcrest/javadoc/1.3/org/hamcrest/Matcher.html">
	 *                      <code>Matcher</code></a> of {@link String} with text to match
	 */
	public static Matcher<View> withInputHint(final Matcher<String> stringMatcher) {
		checkNotNull(stringMatcher);
		return new BoundedMatcher<View, TextInputLayout>(TextInputLayout.class) {
			@Override
			public void describeTo(Description description) {
				description.appendText("with hint: ");
				stringMatcher.describeTo(description);
			}

			@Override
			public boolean matchesSafely(TextInputLayout inputLayout) {
				return stringMatcher.matches(inputLayout.getHint());
			}
		};
	}

	/**
	 * Returns a matcher that matches {@link android.support.design.widget.TextInputLayout} based on it's hint property value. Note: View's
	 * Sugar for withHint(is("string")).
	 *
	 * @param hintText {@link String} with the hint text to match
	 */
	public static Matcher<View> withInputHint(String hintText) {
		checkNotNull(hintText);
		return withInputHint(is(hintText));
	}

	/**
	 * Ensures that an object reference passed as a parameter to the calling method is not null.
	 *
	 * @param reference an object reference
	 * @return the non-null reference that was validated
	 * @throws NullPointerException if {@code reference} is null
	 */
	public static <T> T checkNotNull(T reference) {
		if (reference == null) {
			throw new NullPointerException();
		}
		return reference;
	}

	@Before
	public void setUp() throws Exception {
		super.setUp();
		injectInstrumentation(InstrumentationRegistry.getInstrumentation());

		targetContext = getInstrumentation().getTargetContext();
		application = (ShoeBoxApplication) targetContext.getApplicationContext();

		// Make sure Espresso does not time out
		IdlingPolicies.setMasterPolicyTimeout(2, TimeUnit.MINUTES);
		IdlingPolicies.setIdlingResourceTimeout(2, TimeUnit.MINUTES);
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

	public void sleep() {
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void slowDown() {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void keepCalmAndSleep() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public String getText(final Matcher<View> matcher) {
		final String[] stringHolder = {null};
		onView(matcher).perform(new ViewAction() {
			public Matcher<View> getConstraints() {
				return isAssignableFrom(TextView.class);
			}

			@Override
			public String getDescription() {
				return "getting text from a TextView";
			}

			@Override
			public void perform(UiController uiController, View view) {
				TextView tv = (TextView) view; //Save, because of check in getConstraints()
				stringHolder[0] = tv.getText().toString();
			}
		});
		return stringHolder[0];
	}

	public void goToMain(Activity currentActivity) {
		Timber.e("goToMain: activity = %s", currentActivity);
		// go to garage if not there
		if (!(currentActivity instanceof MainActivity)) {
			Intent intent = new Intent(currentActivity, MainActivity.class);
			//avoid recreation of main activity
			//if this is called several times the main activity will not be recreated
			intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			NavUtils.navigateUpTo(currentActivity, intent);
		}
	}

	Activity getCurrentActivity() {
		getInstrumentation().waitForIdleSync();
		final Activity[] activity = new Activity[1];
		try {
			runTestOnUiThread(new Runnable() {
				@Override
				public void run() {
					java.util.Collection<Activity> activites = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED);
					activity[0] = Iterables.getOnlyElement(activites);
				}
			});
		} catch (Throwable throwable) {
			Timber.e(throwable, "getCurrentActivity: error");
		}
		return activity[0];
	}

	public String getPopupComponent() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			return "android.widget.ListPopupWindow$DropDownListView";
		} else {
			return "android.support.v7.widget.ListPopupWindow$DropDownListView";
		}
	}
}










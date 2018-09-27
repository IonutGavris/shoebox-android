package com.shoebox.android

import android.support.test.rule.ActivityTestRule
import android.support.test.rule.GrantPermissionRule
import android.support.test.runner.AndroidJUnit4
import com.agoda.kakao.KTextView
import com.shoebox.android.screens.LocationsActivityScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LocationsActivityTest {

    @Rule
    @JvmField
    var rule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION)

    @Rule
    @JvmField
    var activityTestRule = ActivityTestRule(LocationsActivity::class.java)

    val screen = LocationsActivityScreen()

    @Test
    fun checkFilter() {
        screen {

            fab {
                isDisplayed()
                click()
            }

            editTextFilter {
                isDisplayed()
                typeText("ecco s")
                closeSoftKeyboard()
            }

            KTextView {
                withText("ECCO Sibiu")
                isDisplayed()
            }
        }
    }
}
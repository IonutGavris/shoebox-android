package com.shoebox.android.screens

import com.agoda.kakao.KEditText
import com.agoda.kakao.KTextView
import com.agoda.kakao.Screen
import com.shoebox.android.R

open class LocationsActivityScreen: Screen<LocationsActivityScreen>() {

    val fab = KTextView {
        withId(R.id.fab)
    }
    val editTextFilter = KEditText {
        withId(R.id.filterShopsView)
    }
}
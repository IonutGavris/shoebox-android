package com.shoebox.android.util;

import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

public class ShoeBoxAnalytics {

	public static void sendErrorState(FirebaseAnalytics firebaseAnalytics, String errorMsg) {
		Bundle bundle = new Bundle(1);
		bundle.putString(ShoeBoxAnalytics.Param.ERROR_MSG, errorMsg);
		firebaseAnalytics.logEvent(State.ERROR, bundle);
	}

	public static class Param {
		public static final String ERROR_MSG = "error_msg";
		public static final String SLIDE_LAST_SEEN = "slide_last_seen";
		public static final String IS_MALE = "is_male";
		public static final String AGE_INTERVAL = "age_interval";
		public static final String LOCATION_TITLE = "location_title";
		public static final String LOCATION_CITY = "location_city";
		public static final String LOCATION_COUNTRY = "location_country";

		protected Param() {
		}
	}

	public static class State {
		public static final String ERROR = "error";
		public static final String GENDER_AGE_PICKER = "gender_age_picker";
		public static final String CONTENT_SUGGESTIONS = "content_suggestions";
		public static final String LOCATIONS = "locations";
		public static final String LOCATION_DETAILS = "location_details";

		protected State() {
		}
	}

	public static class Action {
		public static final String CONTACT_US = "contact_us";
		public static final String ABOUT = "about";
		public static final String DO_BOX_CONTENT = "do_box_content";
		public static final String DO_DROP_LOCATIONS = "do_drop_locations";
		public static final String CHOOSE_GIRL = "choose_girl";
		public static final String CHOOSE_BOY = "choose_boy";
		public static final String CHOOSE_AGE_INTERVAL = "choose_age_interval";
		public static final String GOTO_CONTENT_SUGGESTIONS = "goto_content_suggestions";
		public static final String GOTO_LOCATIONS = "goto_locations";
		public static final String LOCATIONS_VIEW_LIST = "locations_view_list";
		public static final String SHOW_DIRECTIONS = "show_directions";
		public static final String CALL_CONTACT = "call_contact";
		public static final String INVITE_FRIENDS = "invite_friends";
		public static final String CHANGE_LANGUAGE = "change_language";
		public static final String SET_LANGUAGE_RO = "set_language_ro";
		public static final String SET_LANGUAGE_EN = "set_language_en";
		public static final String SOCIAL_SHARE = "social_share";

		protected Action() {
		}
	}
}

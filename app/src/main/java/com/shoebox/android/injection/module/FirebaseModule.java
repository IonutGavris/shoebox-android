package com.shoebox.android.injection.module;

import android.content.Context;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.FirebaseDatabase;
import com.shoebox.android.injection.scope.ApplicationScope;

import dagger.Module;
import dagger.Provides;

@Module
public class FirebaseModule {

	@Provides
	@ApplicationScope
	FirebaseDatabase provideFirebaseDatabase() {
		return FirebaseDatabase.getInstance();
	}

	@Provides
	@ApplicationScope
	FirebaseAnalytics provideFirebaseAnalytics(Context context) {
		return FirebaseAnalytics.getInstance(context);
	}

}

package com.shoebox.android.repository;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jakewharton.rxrelay2.BehaviorRelay;
import com.shoebox.android.ShoeBoxApplication;
import com.shoebox.android.util.ShoeBoxAnalytics;

import javax.inject.Inject;

import dagger.Lazy;
import io.reactivex.Observable;
import timber.log.Timber;

public abstract class BaseRepository {

	@Inject
	protected FirebaseDatabase firebaseDatabase;
	@Inject
	protected Lazy<FirebaseAnalytics> firebaseAnalytics;

	protected Context context;
	private ValueEventListener dataListener;
	private BehaviorRelay<RepositoryData> dataSubject = BehaviorRelay.createDefault(new RepositoryData());

	public BaseRepository(Context context) {
		this.context = context;
		ShoeBoxApplication.getComponent(context).inject(this);
	}

	@NonNull
	public abstract Query getDataQuery();

	@NonNull
	public abstract Object getUIData(@NonNull DataSnapshot dataSnapshot);

	public Observable<RepositoryData> getDataStream() {
		startListeningForData();
		return dataSubject;
	}

	public void stopDataStream() {
		getDataQuery().removeEventListener(dataListener);
	}

	private void startListeningForData() {
		if (dataListener != null)
			return;

		dataListener = new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				Timber.d("onDataChange: DB read was successful :)");
				dataSubject.accept(new RepositoryData(getUIData(dataSnapshot)));
			}

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {
				Timber.e("onCancelled: DB read failed: %s ", databaseError.getMessage());
				ShoeBoxAnalytics.sendErrorState(firebaseAnalytics.get(), "Content suggestions read failed: " + databaseError.getMessage());
				dataSubject.accept(new RepositoryData(databaseError.getMessage()));
			}
		};
		getDataQuery().addValueEventListener(dataListener);
	}

}

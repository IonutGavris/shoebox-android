package com.shoebox.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.GenericTypeIndicator;
import com.firebase.client.ValueEventListener;
import com.shoebox.android.adapter.SuggestionsAdapter;
import com.shoebox.android.beans.AgeInterval;
import com.shoebox.android.beans.Suggestion;
import com.shoebox.android.util.DividerItemDecoration;

import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import timber.log.Timber;

public class ContentSuggestionActivity extends BaseActivity {
	private static final String IS_MALE = "isMale";
	private static final String AGE = "age";

	private static final String MALE = "male";
	private static final String FEMALE = "female";
	private static final String BOTH = "both";

	private static final String dataPath = "/suggestions/";
	@InjectView(R.id.recyclerView)
	RecyclerView recyclerView;
	private AgeInterval ageInterval;
	private boolean isMale;
	private SuggestionsAdapter adapter;

	public static Intent getLaunchingIntent(Context context, boolean isMale, AgeInterval interval) {
		Intent intent = new Intent(context, ContentSuggestionActivity.class);
		intent.putExtra(IS_MALE, isMale);
		intent.putExtra(AGE, interval);
		return intent;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ageInterval = (AgeInterval) getIntent().getSerializableExtra(AGE);
		isMale = getIntent().getBooleanExtra(IS_MALE, false);

		setContentView(R.layout.activity_content_suggestion);
		setTitle(R.string.title_activity_suggestions);

		recyclerView.setHasFixedSize(true);
		LinearLayoutManager layoutManager = new LinearLayoutManager(this);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		recyclerView.setLayoutManager(layoutManager);
		recyclerView.setItemAnimator(new DefaultItemAnimator());
		recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
		adapter = new SuggestionsAdapter();
		adapter.setSuggestionsTarget(isMale, ageInterval.minAge, ageInterval.maxAge);
		recyclerView.setAdapter(adapter);


		firebase.child(dataPath).addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				Timber.d("The %s read was successful :)", dataPath);

				GenericTypeIndicator<List<Suggestion>> t = new GenericTypeIndicator<List<Suggestion>>() {
				};
				List<Suggestion> suggestions = dataSnapshot.getValue(t);

				for (int i = 0; i < suggestions.size(); i++) {
					if (suggestions.get(i) == null) {
						suggestions.remove(i);
						continue;
					}

					// picked up 8-10 and interval is 11-100
					if (ageInterval.maxAge < suggestions.get(i).minAge) {
						suggestions.remove(i);
						continue;
					}

					//TODO
					// picked up 1, and interval is 0-1 (de ex suzeta, care-i pana pe la 1 an)

					if (isMale && suggestions.get(i).sex.equals(FEMALE)) {
						suggestions.remove(i);
					} else if (!isMale && suggestions.get(i).sex.equals(MALE)) {
						suggestions.remove(i);
					}
				}

				Timber.d("The %s count = %s", dataPath, suggestions.size());
				adapter.setSuggestions(suggestions);
			}

			@Override
			public void onCancelled(FirebaseError firebaseError) {
				Timber.e("The %s read failed: %s ", dataPath, firebaseError.getMessage());
			}
		});
	}

	@OnClick(R.id.nextStep)
	public void nextStepClick(View view) {
		startActivity(LocationsActivity.getLaunchingIntent(this));
	}
}

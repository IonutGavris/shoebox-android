package com.shoebox.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.shoebox.android.adapter.SuggestionsAdapter;
import com.shoebox.android.bean.AgeInterval;
import com.shoebox.android.bean.Suggestion;
import com.shoebox.android.util.DividerItemDecoration;
import com.shoebox.android.util.ShoeBoxAnalytics;
import com.shoebox.android.util.UIUtils;

import butterknife.BindView;
import butterknife.OnClick;
import timber.log.Timber;

public class ContentSuggestionActivity extends BaseActivity {
	private static final String IS_MALE = "isMale";
	private static final String AGE = "age";

	private static final String dataPath = "suggestions";
	private static final String dataPath_en = "suggestions_en";

	@BindView(R.id.recyclerView)
	RecyclerView recyclerView;

	@BindView(R.id.listStatusView)
	View listStatusView;

	private AgeInterval ageInterval;
	private boolean isMale;
	private SuggestionsAdapter adapter;

	private Query suggestionsQuery;
	private ChildEventListener childEventListener;

	public static Intent getLaunchingIntent(Context context, boolean isMale, AgeInterval interval) {
		Intent intent = new Intent(context, ContentSuggestionActivity.class);
		intent.putExtra(IS_MALE, isMale);
		intent.putExtra(AGE, interval);
		return intent;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_content_suggestion);

		ageInterval = (AgeInterval) getIntent().getSerializableExtra(AGE);
		isMale = getIntent().getBooleanExtra(IS_MALE, false);

		recyclerView.setHasFixedSize(true);
		LinearLayoutManager layoutManager = new LinearLayoutManager(this);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		recyclerView.setLayoutManager(layoutManager);
		recyclerView.setItemAnimator(new DefaultItemAnimator());
		recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

		adapter = new SuggestionsAdapter(isMale, ageInterval.minAge, ageInterval.maxAge);
		recyclerView.setAdapter(adapter);
		if (adapter.hasData()) {
			listStatusView.setVisibility(View.GONE);
		} else {
			listStatusView.setVisibility(View.VISIBLE);
			UIUtils.setListStatus(listStatusView, getString(R.string.msg_loading), false);
		}

		childEventListener = new ChildEventListener() {
			@Override
			public void onChildAdded(DataSnapshot dataSnapshot, String s) {
				adapter.addSuggestion(Suggestion.create(dataSnapshot));
				listStatusView.setVisibility(adapter.hasData() ? View.GONE : View.VISIBLE);
			}

			@Override
			public void onChildChanged(DataSnapshot dataSnapshot, String s) {
				adapter.changeSuggestion(Suggestion.create(dataSnapshot));
			}

			@Override
			public void onChildRemoved(DataSnapshot dataSnapshot) {
				adapter.removeSuggestion(Suggestion.create(dataSnapshot));
			}

			@Override
			public void onChildMoved(DataSnapshot dataSnapshot, String s) {
				// nothing to do
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {
				Timber.e("The %s read failed: %s ", dataPath, databaseError.getMessage());
				if (!adapter.hasData()) {
					listStatusView.setVisibility(View.VISIBLE);
					UIUtils.setListStatus(listStatusView, getString(R.string.msg_fetch_suggestions), true);
				}
				ShoeBoxAnalytics.sendErrorState(firebaseAnalytics, "Content suggestions read failed: " + databaseError.getMessage());
			}
		};

		String path = useRomanianLanguage() ? dataPath : dataPath_en;
		// TODO define the .indexOn rule to index those keys on the server and improve query performance
		suggestionsQuery = firebase.getReference().child(path).orderByChild(Suggestion.ORDER_BY);
		suggestionsQuery.addChildEventListener(childEventListener);

		Bundle bundle = new Bundle(2);
		bundle.putSerializable(ShoeBoxAnalytics.Param.AGE_INTERVAL, ageInterval);
		bundle.putBoolean(ShoeBoxAnalytics.Param.IS_MALE, isMale);
		firebaseAnalytics.logEvent(ShoeBoxAnalytics.State.CONTENT_SUGGESTIONS, bundle);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		suggestionsQuery.removeEventListener(childEventListener);
	}

	@OnClick(R.id.nextStep)
	public void nextStepClick(View view) {
		firebaseAnalytics.logEvent(ShoeBoxAnalytics.Action.GOTO_LOCATIONS, null);
		startActivity(LocationsActivity.getLaunchingIntent(this));
	}
}

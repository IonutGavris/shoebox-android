package com.shoebox.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.GenericTypeIndicator;
import com.firebase.client.ValueEventListener;
import com.shoebox.android.adapter.SuggestionsAdapter;
import com.shoebox.android.beans.Suggestion;
import com.shoebox.android.util.DividerItemDecoration;

import java.util.List;

import butterknife.InjectView;
import de.greenrobot.event.EventBus;
import timber.log.Timber;

public class ContentSuggestionActivity extends BaseActivity {
	private static final String IS_MALE = "isMale";
	private static final String AGE = "age";

	private static final String dataPath = "/suggestions/";
	@InjectView(R.id.recyclerView)
	RecyclerView recyclerView;
	private int age;
	private boolean isMale;
	private EventBus bus = EventBus.getDefault();
	private SuggestionsAdapter adapter;

	public static Intent getLaunchingIntent(Context context, boolean isMale, int age) {
		Intent intent = new Intent(context, ContentSuggestionActivity.class);
		intent.putExtra(IS_MALE, isMale);
		intent.putExtra(AGE, age);
		return intent;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		age = getIntent().getIntExtra(AGE, -1);
		isMale = getIntent().getBooleanExtra(IS_MALE, false);
		Timber.d("onCreate age=%s  &  isMale=%b", age, isMale);

		setContentView(R.layout.activity_content_suggestion);
//		bus.register(this);
		setTitle(R.string.title_activity_suggestions);

		recyclerView.setHasFixedSize(true);
		LinearLayoutManager layoutManager = new LinearLayoutManager(this);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		recyclerView.setLayoutManager(layoutManager);
		recyclerView.setItemAnimator(new DefaultItemAnimator());
		recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
		adapter = new SuggestionsAdapter();
		adapter.setSuggestionsTarget(isMale, age);
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

	@Override
	protected void onDestroy() {
		super.onDestroy();
		//	bus.unregister(this);
	}
}

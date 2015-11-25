package com.shoebox.android;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.GenericTypeIndicator;
import com.firebase.client.ValueEventListener;
import com.shoebox.android.beans.Suggestion;

import java.util.List;

import butterknife.InjectView;
import de.greenrobot.event.EventBus;

public class ContentSuggestionActivity extends BaseActivity {
	private static final String IS_MALE = "male";
	private static final String AGE = "age";

	private static final String dataPath = "/suggestions/";

	private int age;
	private boolean male;
	private EventBus bus = EventBus.getDefault();

	@InjectView(R.id.recyclerView)
	RecyclerView recyclerView;
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
		male = getIntent().getBooleanExtra(IS_MALE, false);

		setContentView(R.layout.activity_content_suggestion);
//		bus.register(this);
		setTitle("Sugestii continut");

		recyclerView.setHasFixedSize(true);
		LinearLayoutManager layoutManager = new LinearLayoutManager(this);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		recyclerView.setLayoutManager(layoutManager);
		recyclerView.setItemAnimator(new DefaultItemAnimator());
		adapter = new SuggestionsAdapter();
		recyclerView.setAdapter(adapter);

		firebase.child(dataPath).addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				System.out.println("got the data !!");

				GenericTypeIndicator<List<Suggestion>> t = new GenericTypeIndicator<List<Suggestion>>() {
				};
				List<Suggestion> suggestions = dataSnapshot.getValue(t);
				if (suggestions != null && suggestions.size() > 1 && suggestions.get(0) == null) {
					suggestions.remove(0);
				}

				adapter.setSuggestions(suggestions);
			}

			@Override
			public void onCancelled(FirebaseError firebaseError) {

			}
		});

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	//	bus.unregister(this);
	}
}

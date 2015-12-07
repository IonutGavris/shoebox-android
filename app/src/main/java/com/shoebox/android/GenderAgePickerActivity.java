package com.shoebox.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.shoebox.android.adapter.AgePickerAdapter;
import com.shoebox.android.beans.AgeInterval;
import com.shoebox.android.event.CustomAgePickedEvent;
import com.shoebox.android.events.AgeSelectedEvent;
import com.shoebox.android.ui.CustomAgeDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class GenderAgePickerActivity extends BaseActivity {
	private EventBus bus = EventBus.getDefault();
	private static final int INITIAL_CUSTOM_AGE = 0;
	private static final int DEFAULT_CUSTOM_AGE = 15;

	@InjectView(R.id.boyCheck)
	View boyCheck;

	@InjectView(R.id.girlCheck)
	View girlCheck;

	@InjectView(R.id.recyclerView)
	RecyclerView recyclerView;

	@InjectView(R.id.disableView)
	View disableView;

	@InjectView(R.id.nextStep)
	Button nextStep;

	private AgeInterval selectedAgeInterval;

	private AgePickerAdapter adapter;

	public static Intent getLaunchingIntent(Context context) {
		return new Intent(context, GenderAgePickerActivity.class);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gender_age_picker);
		setTitle(R.string.title_activity_picker);
		bus.register(this);

		recyclerView.setHasFixedSize(true);
		LinearLayoutManager layoutManager = new LinearLayoutManager(this);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		recyclerView.setLayoutManager(layoutManager);
		recyclerView.setItemAnimator(new DefaultItemAnimator());
		adapter = new AgePickerAdapter();
		recyclerView.setAdapter(adapter);
		nextStep.setEnabled(false);

		// no custom age set by default
		fillAdapter(INITIAL_CUSTOM_AGE);
	}

	private void fillAdapter(int customValue) {
		List<AgeInterval> values = new ArrayList<>();
		values.add(new AgeInterval(4, 6));
		values.add(new AgeInterval(6, 8));
		values.add(new AgeInterval(8, 10));
		values.add(new AgeInterval(10, 12));
		values.add(new AgeInterval(customValue));
		adapter.setAgeIntervals(values);
	}

	@OnClick(R.id.nextStep)
	public void onNextStep() {
		startActivity(ContentSuggestionActivity.getLaunchingIntent(this, boyCheck.getVisibility() == View.VISIBLE, selectedAgeInterval));
	}

	@OnClick(R.id.girl)
	public void onGirlClicked() {
		girlCheck.setVisibility(View.VISIBLE);
		boyCheck.setVisibility(View.INVISIBLE);
		disableView.setVisibility(View.GONE);
	}

	@OnClick(R.id.boy)
	public void onBoyClicked() {
		boyCheck.setVisibility(View.VISIBLE);
		girlCheck.setVisibility(View.INVISIBLE);
		disableView.setVisibility(View.GONE);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		bus.unregister(this);
	}

	public void onEvent(AgeSelectedEvent event) {
		if (event.ageInterval.custom) {
			CustomAgeDialog dialog = new CustomAgeDialog();
			Bundle bundle = new Bundle();
			bundle.putInt(CustomAgeDialog.DEFAULT_AGE, event.ageInterval.maxAge == INITIAL_CUSTOM_AGE ? DEFAULT_CUSTOM_AGE : event.ageInterval.maxAge);
			dialog.setArguments(bundle);
			dialog.show(getSupportFragmentManager(), "");
		}
		for (int i = 0; i < recyclerView.getAdapter().getItemCount(); i++) {
			RecyclerView.ViewHolder holder = recyclerView.findViewHolderForAdapterPosition(i);
			if (holder instanceof AgePickerAdapter.AgeHolder) {
				if (((AgePickerAdapter.AgeHolder) holder).setChecked(event.ageInterval)) {
					selectedAgeInterval = event.ageInterval;
				}
			}
		}
		nextStep.setEnabled(true);
	}

	public void onEvent(CustomAgePickedEvent event) {
		fillAdapter(event.age);
		selectedAgeInterval = new AgeInterval(event.age);
	}

}

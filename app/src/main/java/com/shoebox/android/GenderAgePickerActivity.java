package com.shoebox.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shoebox.android.adapter.AgePickerAdapter;
import com.shoebox.android.beans.AgeInterval;
import com.shoebox.android.events.AgeSelectedEvent;
import com.shoebox.android.ui.CustomAgeDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class GenderAgePickerActivity extends BaseActivity {
	private EventBus bus = EventBus.getDefault();

	@InjectView(R.id.boyCheck)
	View boyCheck;

	@InjectView(R.id.girlCheck)
	View girlCheck;

	@InjectView(R.id.recyclerView)
	RecyclerView recyclerView;

	private AgePickerAdapter adapter;

	public static Intent getLaunchingIntent(Context context) {
		return new Intent(context, GenderAgePickerActivity.class);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gender_age_picker);
		setTitle("Cui daruiesti cadoul");
		bus.register(this);

		recyclerView.setHasFixedSize(true);
		LinearLayoutManager layoutManager = new LinearLayoutManager(this);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		recyclerView.setLayoutManager(layoutManager);
		recyclerView.setItemAnimator(new DefaultItemAnimator());
		adapter = new AgePickerAdapter();
		adapter.setAgeIntervals(getAgeIntevals());
		recyclerView.setAdapter(adapter);
	}

	private List<AgeInterval> getAgeIntevals() {
		List<AgeInterval> results = new ArrayList<>();
		results.add(new AgeInterval(4, 6));
		results.add(new AgeInterval(6, 8));
		results.add(new AgeInterval(8, 10));
		results.add(new AgeInterval(10, 12));
		results.add(new AgeInterval(true));
		return results;
	}

	@OnClick(R.id.nextStep)
	public void onNextStep() {
		startActivity(ContentSuggestionActivity.getLaunchingIntent(this, true, 4));
	}

	@OnClick(R.id.girl)
	public void onGirlClicked() {
		girlCheck.setVisibility(View.VISIBLE);
		boyCheck.setVisibility(View.INVISIBLE);
	}

	@OnClick(R.id.boy)
	public void onBoyClicked() {
		boyCheck.setVisibility(View.VISIBLE);
		girlCheck.setVisibility(View.INVISIBLE);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		bus.unregister(this);
	}

	public void onEvent(AgeSelectedEvent event) {
		if (event.ageInterval.custom) {
			CustomAgeDialog dialog = new CustomAgeDialog();
			dialog.show(getSupportFragmentManager(), "");
		}
		for (int i = 0; i < recyclerView.getAdapter().getItemCount(); i++) {
			RecyclerView.ViewHolder holder = recyclerView.findViewHolderForAdapterPosition(i);
			if (holder instanceof AgePickerAdapter.AgeHolder) {
				((AgePickerAdapter.AgeHolder) holder).setChecked(event.ageInterval);
			}
		}
	}

}

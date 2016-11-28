package com.shoebox.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.shoebox.android.adapter.AgePickerAdapter;
import com.shoebox.android.bean.AgeInterval;
import com.shoebox.android.event.AgeSelectedEvent;
import com.shoebox.android.event.CustomAgePickedEvent;
import com.shoebox.android.ui.CustomAgeDialog;
import com.shoebox.android.util.ShoeBoxAnalytics;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class GenderAgePickerActivity extends BaseActivity {
	private static final int INITIAL_CUSTOM_AGE = 0;
	private static final int DEFAULT_CUSTOM_AGE = 15;
	@BindView(R.id.boyCheck)
	View boyCheck;
	@BindView(R.id.girlCheck)
	View girlCheck;
	@BindView(R.id.recyclerView)
	RecyclerView recyclerView;
	@BindView(R.id.disableView)
	View disableView;
	@BindView(R.id.nextStep)
	Button nextStep;
	@BindView(R.id.frameBackground)
	FrameLayout frameBackground;
	private EventBus bus = EventBus.getDefault();
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

		recyclerView.setHasFixedSize(true);
		LinearLayoutManager layoutManager = new LinearLayoutManager(this);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		recyclerView.setLayoutManager(layoutManager);
		recyclerView.setItemAnimator(new DefaultItemAnimator());
		adapter = new AgePickerAdapter();
		recyclerView.setAdapter(adapter);
		nextStep.setEnabled(false);
		frameBackground.setEnabled(false);

		// no custom age set by default
		fillAdapter(INITIAL_CUSTOM_AGE);

		firebaseAnalytics.logEvent(ShoeBoxAnalytics.State.GENDER_AGE_PICKER, null);
	}

	@Override
	public void onStart() {
		super.onStart();
		bus.register(this);
	}

	@Override
	public void onStop() {
		bus.unregister(this);
		super.onStop();
	}

	@OnClick(R.id.nextStep)
	public void onNextStep() {
		firebaseAnalytics.logEvent(ShoeBoxAnalytics.Action.GOTO_CONTENT_SUGGESTIONS, null);
		startActivity(ContentSuggestionActivity.getLaunchingIntent(this, boyCheck.getVisibility() == View.VISIBLE, selectedAgeInterval));
	}

	@OnClick(R.id.girl)
	public void onGirlClicked() {
		girlCheck.setVisibility(View.VISIBLE);
		boyCheck.setVisibility(View.INVISIBLE);
		disableView.setVisibility(View.GONE);
		firebaseAnalytics.logEvent(ShoeBoxAnalytics.Action.CHOOSE_GIRL, null);
	}

	@OnClick(R.id.boy)
	public void onBoyClicked() {
		boyCheck.setVisibility(View.VISIBLE);
		girlCheck.setVisibility(View.INVISIBLE);
		disableView.setVisibility(View.GONE);
		firebaseAnalytics.logEvent(ShoeBoxAnalytics.Action.CHOOSE_BOY, null);
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onAgeSelectedEvent(AgeSelectedEvent event) {
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
		frameBackground.setEnabled(true);

		Bundle bundle = new Bundle(1);
		bundle.putSerializable(ShoeBoxAnalytics.Param.AGE_INTERVAL, selectedAgeInterval);
		firebaseAnalytics.logEvent(ShoeBoxAnalytics.Action.CHOOSE_AGE_INTERVAL, bundle);
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onCustomAgePickedEvent(CustomAgePickedEvent event) {
		fillAdapter(event.age);
		selectedAgeInterval = new AgeInterval(event.age);
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
}

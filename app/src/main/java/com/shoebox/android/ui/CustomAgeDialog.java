package com.shoebox.android.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.shoebox.android.R;
import com.shoebox.android.event.CustomAgePickedEvent;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by vasile.mihalca on 02/12/15.
 * The dialog is used to allow the user to pick a custom age for the next step.
 */
public class CustomAgeDialog extends BaseDialogFragment {
	public static final String DEFAULT_AGE = "default_age";
	public static final int MIN_AGE = 1;
	public static final int MAX_AGE = 18;

	@BindView(R.id.dialogTitle)
	TextView dialogTitle;
	@BindView(R.id.ageSelection)
	SeekBar ageSelection;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.custom_age_dialog, container);
		unbinder = ButterKnife.bind(this, view);

		int defaultAge = getArguments().getInt(DEFAULT_AGE, 0);
		ageSelection.setMax(MAX_AGE - MIN_AGE);
		ageSelection.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				int age = progress + MIN_AGE;
				dialogTitle.setText(String.format(getResources().getString(R.string.age_dialog_title), age));
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}
		});
		ageSelection.setProgress(defaultAge - MIN_AGE);
		dialogTitle.setText(String.format(getResources().getString(R.string.age_dialog_title), defaultAge));

		return view;
	}

	@OnClick(R.id.okBtn)
	public void onOkBtnClicked() {
		EventBus.getDefault().post(new CustomAgePickedEvent(ageSelection.getProgress() + MIN_AGE));
		dismiss();
	}
}

package com.shoebox.android.ui;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.shoebox.android.R;
import com.shoebox.android.event.CustomAgePickedEvent;

import de.greenrobot.event.EventBus;

/**
 * Created by vasile.mihalca on 02/12/15.
 */
public class CustomAgeDialog extends DialogFragment {
	public static final String DEFAULT_AGE = "default_age";
	public static final int MIN_AGE = 1;
	public static final int MAX_AGE = 18;
	private EventBus bus = EventBus.getDefault();
	private TextView dialogTitle;
	private SeekBar ageSelection;
	private Button okBtn;
	private int defaultAge;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.custom_age_dialog, container);
		defaultAge = getArguments().getInt(DEFAULT_AGE, 0);
		dialogTitle = (TextView) view.findViewById(R.id.dialogTitle);
		ageSelection = (SeekBar) view.findViewById(R.id.ageSelection);
		ageSelection.setMax(MAX_AGE - MIN_AGE);
		okBtn = (Button) view.findViewById(R.id.okBtn);
		okBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				bus.post(new CustomAgePickedEvent(ageSelection.getProgress() + MIN_AGE));
				dismiss();
			}
		});
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
}

package com.shoebox.android.ui;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.shoebox.android.R;

/**
 * Created by vasile.mihalca on 02/12/15.
 */
public class CustomAgeDialog extends DialogFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.custom_age_dialog, container);
		final TextView dialogTitle = (TextView) view.findViewById(R.id.dialogTitle);
		SeekBar ageSelection = (SeekBar) view.findViewById(R.id.ageSelection);
		ageSelection.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				int age = progress + 1;
				dialogTitle.setText(String.format(getResources().getString(R.string.age_dialog_title), age));
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}
		});
		ageSelection.setProgress(14);
		dialogTitle.setText(String.format(getResources().getString(R.string.age_dialog_title), 15));

		return view;
	}
}

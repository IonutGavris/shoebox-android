package com.shoebox.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import butterknife.OnClick;

public class GenderAgePickerActivity extends BaseActivity {

	public static Intent getLaunchingIntent(Context context) {
		return new Intent(context, GenderAgePickerActivity.class);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gender_age_picker);
		setTitle("Cui daruiesti cadoul");
	}

	@OnClick(R.id.nextStep)
	public void onNextStep() {
		Toast.makeText(this, "ahaaa", Toast.LENGTH_LONG).show();
		startActivity(ContentSuggestionActivity.getLaunchingIntent(this, true, 4));
	}

}

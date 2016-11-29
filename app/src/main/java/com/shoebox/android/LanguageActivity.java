package com.shoebox.android;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

import com.jakewharton.processphoenix.ProcessPhoenix;
import com.shoebox.android.util.HelperClass;
import com.shoebox.android.util.UIUtils;

import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class LanguageActivity extends AppCompatActivity {
	private Unbinder unbinder;

	public static Intent getLaunchingIntent(Activity activity) {
		return new Intent(activity, LanguageActivity.class);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_language);
		unbinder = ButterKnife.bind(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbinder.unbind();
	}

	@OnClick(R.id.radioRo)
	void onRoSelected() {
		HelperClass.addStringValueInSharedPreference(getApplicationContext(), HelperClass.keyAppLanguage, UIUtils.LANG_RO);
		finish();
	}

	@OnClick(R.id.radioEn)
	void onEnSelected() {
		HelperClass.addStringValueInSharedPreference(getApplicationContext(), HelperClass.keyAppLanguage, UIUtils.LANG_EN);
		// by default will be RO, we'll trigger restart only if language is changed to EN to avoid flicker
		setResult(RESULT_OK);
		finish();
	}
}

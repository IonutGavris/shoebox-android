package com.shoebox.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.shoebox.android.util.HelperClass;
import com.shoebox.android.util.UIUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class LanguageActivity extends AppCompatActivity {

	private Unbinder unbinder;
	private String languageCodeFromPrefs;

	public static Intent getLaunchingIntent(Activity activity) {
		return new Intent(activity, LanguageActivity.class);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_language);
		unbinder = ButterKnife.bind(this);

		languageCodeFromPrefs = HelperClass.getStringValueInSharedPreference(getApplicationContext(), HelperClass.keyAppLanguage, UIUtils.LANG_RO);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbinder.unbind();
	}

	@OnClick(R.id.radioRo)
	void onRoSelected() {
		selectLanguage(UIUtils.LANG_RO);
	}

	@OnClick(R.id.radioEn)
	void onEnSelected() {
		selectLanguage(UIUtils.LANG_EN);
	}

	private void selectLanguage(String language) {
		if (!languageCodeFromPrefs.equals(language)) {
			// by default will be RO, we'll trigger restart only if language is changed to EN to avoid flicker
			HelperClass.addStringValueInSharedPreference(getApplicationContext(), HelperClass.keyAppLanguage, language);
			setResult(RESULT_OK);
		}
		finish();
	}

}

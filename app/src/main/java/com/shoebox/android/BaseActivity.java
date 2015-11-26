package com.shoebox.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.client.Firebase;
import com.shoebox.android.util.UIUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.Optional;

/**
 * Created by vasile.mihalca on 24/11/15.
 */
public class BaseActivity extends AppCompatActivity {

	private final String firebaseUrl = "https://shoebox.firebaseio.com/";
	@Optional
	@InjectView(R.id.coordinatorLayout)
	public CoordinatorLayout coordinatorLayout;
	protected Firebase firebase;
	@InjectView(R.id.toolbar)
	protected Toolbar toolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		firebase = new Firebase(firebaseUrl);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ButterKnife.reset(this);
	}

	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		ButterKnife.inject(this);

		setSupportActionBar(toolbar);
		if (getSupportActionBar() != null) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public void setContentView(View view) {
		super.setContentView(view);
		ButterKnife.inject(this);
	}

	@Override
	public void setContentView(View view, ViewGroup.LayoutParams params) {
		super.setContentView(view, params);
		ButterKnife.inject(this);
	}

	@Override
	public void addContentView(View view, ViewGroup.LayoutParams params) {
		super.addContentView(view, params);
		ButterKnife.inject(this);
	}

	public void setTitle(String title) {
		if (getSupportActionBar() != null) {
			getSupportActionBar().setTitle(title);
		}
	}

	public void setTitle(int resourceId) {
		if (getSupportActionBar() != null) {
			getSupportActionBar().setTitle(resourceId);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				// This is called when the Home (Up) button is pressed in the Action Bar.
				// hide keyboard when going to the previous screen
				UIUtils.hideCurrentFocusKeyboard(this);
				Intent upIntent = NavUtils.getParentActivityIntent(this);
				NavUtils.navigateUpTo(this, upIntent);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Gets the base view of this activity.
	 *
	 * @return Returns a CoordinatorLayout or the base content view.
	 */
	protected View getBaseView() {
		return coordinatorLayout != null ? coordinatorLayout : getWindow().getDecorView().findViewById(android.R.id.content);
	}
}

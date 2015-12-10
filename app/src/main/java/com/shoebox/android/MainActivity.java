package com.shoebox.android;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;

import com.shoebox.android.util.HelperClass;

import butterknife.InjectView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity
		implements NavigationView.OnNavigationItemSelectedListener {

	@InjectView(R.id.drawer_layout)
	DrawerLayout drawer;

	@InjectView(R.id.nav_view)
	NavigationView navigationView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
				this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.setDrawerListener(toggle);
		toggle.syncState();

		navigationView.setNavigationItemSelectedListener(this);

		if (savedInstanceState == null) {
			boolean isFirstTime = HelperClass.getBooleanValueInSharedPreference(getApplicationContext(), HelperClass.keyIsFirstTime, true);
			if (isFirstTime) {
				startActivity(GettingStartedActivity.getLaunchingIntent(this));
			}
		}
	}

	@Override
	public void onBackPressed() {
		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START);
		} else {
			super.onBackPressed();
		}
	}

	@SuppressWarnings("StatementWithEmptyBody")
	@Override
	public boolean onNavigationItemSelected(MenuItem item) {
		// Handle navigation view item clicks here.
		drawer.closeDrawer(GravityCompat.START);

		switch (item.getItemId()) {
			case R.id.nav_contact:
				Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "contact@shoebox.ro", null));
				intent.putExtra(Intent.EXTRA_EMAIL, "contact@shoebox.ro");
				intent.putExtra(Intent.EXTRA_SUBJECT, "ShoeBox Android");
				startActivity(Intent.createChooser(intent, getString(R.string.send_email)));
				break;
			case R.id.nav_about:
				startActivity(GettingStartedActivity.getLaunchingIntent(this));
				break;
		}
		return true;
	}

	@OnClick(R.id.boxContentBtn)
	public void doBoxContent() {
		startActivity(GenderAgePickerActivity.getLaunchingIntent(this));
	}

	@OnClick(R.id.dropLocationsBtn)
	public void doDropLocations() {
		startActivity(LocationsActivity.getLaunchingIntent(this));
	}


}

package com.shoebox.android;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;

import com.google.android.gms.appinvite.AppInviteInvitation;
import com.jakewharton.processphoenix.ProcessPhoenix;
import com.shoebox.android.util.HelperClass;
import com.shoebox.android.util.ShoeBoxAnalytics;
import com.shoebox.android.util.UIUtils;

import butterknife.BindView;
import butterknife.OnClick;
import timber.log.Timber;

public class MainActivity extends BaseActivity
		implements NavigationView.OnNavigationItemSelectedListener {

	private static final int REQUEST_LANGUAGE = 1;
	private static final int REQUEST_INVITE = 2;

	@BindView(R.id.drawer_layout)
	DrawerLayout drawer;

	@BindView(R.id.nav_view)
	NavigationView navigationView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
				this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.addDrawerListener(toggle);
		toggle.syncState();

		navigationView.setNavigationItemSelectedListener(this);

		if (savedInstanceState == null) {
			if (!UIUtils.LANG_RO.equals(((ShoeBoxApplication) getApplication()).getPhoneLocale().getLanguage()) &&
					!HelperClass.keyExistsInSharedPreference(getApplicationContext(), HelperClass.keyAppLanguage)) {
				// request language selection only if the phone locale is in english
				startActivityForResult(LanguageActivity.getLaunchingIntent(this), REQUEST_LANGUAGE);
			} else {
				boolean isFirstTime = HelperClass.getBooleanValueInSharedPreference(getApplicationContext(), HelperClass.keyIsFirstTime, true);
				if (isFirstTime) {
					startActivity(GettingStartedActivity.getLaunchingIntent(this));
				}
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

	@Override
	public boolean onNavigationItemSelected(MenuItem item) {
		// Handle navigation view item clicks here.
		drawer.closeDrawer(GravityCompat.START);

		switch (item.getItemId()) {
			case R.id.nav_contact:
				firebaseAnalytics.logEvent(ShoeBoxAnalytics.Action.CONTACT_US, null);
				Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "contact@shoebox.ro", null));
				intent.putExtra(Intent.EXTRA_EMAIL, "contact@shoebox.ro");
				intent.putExtra(Intent.EXTRA_SUBJECT, "ShoeBox Android");
				startActivity(Intent.createChooser(intent, getString(R.string.send_email)));
				break;
			case R.id.nav_about:
				firebaseAnalytics.logEvent(ShoeBoxAnalytics.Action.ABOUT, null);
				startActivity(GettingStartedActivity.getLaunchingIntent(this));
				break;
			case R.id.nav_invite:
				firebaseAnalytics.logEvent(ShoeBoxAnalytics.Action.INVITE_FRIENDS, null);
				Intent inviteIntent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
						.setMessage(getString(R.string.invitation_message))
						.setDeepLink(Uri.parse(getString(R.string.invitation_deep_link)))
						.setCallToActionText(getString(R.string.invitation_cta))
						.build();
				startActivityForResult(inviteIntent, REQUEST_INVITE);
				break;
			case R.id.nav_language:
				firebaseAnalytics.logEvent(ShoeBoxAnalytics.Action.CHANGE_LANGUAGE, null);
				startActivityForResult(LanguageActivity.getLaunchingIntent(this), REQUEST_LANGUAGE);
				break;
			case R.id.nav_share:
				firebaseAnalytics.logEvent(ShoeBoxAnalytics.Action.SOCIAL_SHARE, null);
				Intent shareIntent = new Intent(Intent.ACTION_SEND);
				shareIntent.setType("text/plain");
				shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text));
				startActivity(Intent.createChooser(shareIntent, getString(R.string.nav_share)));
				break;
		}
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Timber.d("onActivityResult: requestCode=%s , resultCode=%s", requestCode, resultCode);

		if (requestCode == REQUEST_INVITE) {
			if (resultCode == RESULT_OK) {
				// Get the invitation IDs of all sent messages
				String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
				for (String id : ids) {
					Timber.d("onActivityResult: sent invitation %s", id);
				}
			} else {
				Timber.e("Failed to send invitation");
			}
		} else if (requestCode == REQUEST_LANGUAGE) {
			if (resultCode == RESULT_OK) {
				ProcessPhoenix.triggerRebirth(MainActivity.this, getPackageManager()
						.getLaunchIntentForPackage(getApplicationContext().getPackageName()));
			}
		}
	}

	@OnClick(R.id.boxContentBtn)
	public void doBoxContent() {
		firebaseAnalytics.logEvent(ShoeBoxAnalytics.Action.DO_BOX_CONTENT, null);
		startActivity(GenderAgePickerActivity.getLaunchingIntent(this));
	}

	@OnClick(R.id.dropLocationsBtn)
	public void doDropLocations() {
		firebaseAnalytics.logEvent(ShoeBoxAnalytics.Action.DO_DROP_LOCATIONS, null);
		startActivity(LocationsActivity.getLaunchingIntent(this));
	}
}

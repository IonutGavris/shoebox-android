package com.shoebox.android

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import butterknife.BindView
import butterknife.OnClick
import com.google.android.gms.appinvite.AppInviteInvitation
import com.shoebox.android.util.SettingsPrefs
import com.shoebox.android.util.ShoeBoxAnalytics
import com.shoebox.android.util.UIUtils
import com.shoebox.android.util.getDeviceData
import timber.log.Timber

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.drawer_layout)
    lateinit var drawer: DrawerLayout

    @BindView(R.id.nav_view)
    lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        navigationView.setNavigationItemSelectedListener(this)

        if (savedInstanceState == null) {
            val isFirstTime = SettingsPrefs.isFirstTime(applicationContext)
            if (isFirstTime) {
                Timber.d("onCreate: isFirstTime -> start GettingStartedActivity")
                startActivity(GettingStartedActivity.getLaunchingIntent(this))
            }
        }
    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        drawer.closeDrawer(GravityCompat.START)

        when (item.itemId) {
            R.id.nav_contact -> {
                firebaseAnalytics.logEvent(ShoeBoxAnalytics.Action.CONTACT_US, null)
                UIUtils.launchEmail(this, UIUtils.CONTACT_EMAIL, UIUtils.CONTACT_EMAIL_SUBJECT, getDeviceData())
            }
            R.id.nav_about -> {
                firebaseAnalytics.logEvent(ShoeBoxAnalytics.Action.ABOUT, null)
                startActivity(GettingStartedActivity.getLaunchingIntent(this))
            }
            R.id.nav_invite -> {
                firebaseAnalytics.logEvent(ShoeBoxAnalytics.Action.INVITE_FRIENDS, null)
                val inviteIntent = AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                        .setMessage(getString(R.string.invitation_message))
                        .setDeepLink(Uri.parse(getString(R.string.invitation_deep_link)))
                        .setCallToActionText(getString(R.string.invitation_cta))
                        .build()
                startActivityForResult(inviteIntent, REQUEST_INVITE)
            }
            R.id.nav_share -> {
                firebaseAnalytics.logEvent(ShoeBoxAnalytics.Action.SOCIAL_SHARE, null)
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text))
                startActivity(Intent.createChooser(shareIntent, getString(R.string.nav_share)))
            }
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Timber.d("onActivityResult: requestCode=%s , resultCode=%s", requestCode, resultCode)

        when (requestCode) {
            REQUEST_INVITE -> if (resultCode == Activity.RESULT_OK) {
                // Get the invitation IDs of all sent messages
                val ids = AppInviteInvitation.getInvitationIds(resultCode, data!!)
                for (id in ids) {
                    Timber.d("onActivityResult: sent invitation %s", id)
                }
            } else {
                Timber.e("onActivityResult: Failed to send invitation")
            }
        }
    }

    @OnClick(R.id.boxContentBtn)
    fun doBoxContent() {
        firebaseAnalytics.logEvent(ShoeBoxAnalytics.Action.DO_BOX_CONTENT, null)
        startActivity(GenderAgePickerActivity.getLaunchingIntent(this))
    }

    @OnClick(R.id.dropLocationsBtn)
    fun doDropLocations() {
        firebaseAnalytics.logEvent(ShoeBoxAnalytics.Action.DO_DROP_LOCATIONS, null)
        startActivity(LocationsActivity.getLaunchingIntent(this))
    }

    companion object {

        private const val REQUEST_INVITE = 2
    }
}

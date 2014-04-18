package com.shoebox.android.home;

import com.shoebox.android.MenuActivity;
import com.shoebox.android.util.AlertDialogs;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.shoebox.android.R;

public class HomeActivity extends MenuActivity {
    String urlVideo = "http://embed.animoto.com/play.html?w=swf/vp1&e=1321925553&f=v0F0AqygXKswKHIfJ6wCqg&d=103&m=p&r=240p+480p&volume=100&start_res=480p&i=m&ct=Afla%20despre%20ShoeBox.ro!&cu=http://www.shoebox.ro&options=allowfullscreen";
    String urlShoeBox = "http://shoebox.ro/";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
    }

    public void btnMaImplic_onClick(View v) {
        Intent o = new Intent(HomeActivity.this, ImplicareActivity.class);
        HomeGroup parentActivity = (HomeGroup) getParent();
        parentActivity.replaceContentView("", o);
    }

    public void btnImagini_onClick(View v) {
        Intent o = new Intent(HomeActivity.this, ImageGallery.class);
        HomeGroup parentActivity = (HomeGroup) getParent();
        parentActivity.replaceContentView("", o);

//        String facebookUrl = "http://www.facebook.com/ShoeBox.ro/photos_albums";
//        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl));
//        startActivity(myIntent);

//        String uri = "facebook://facebook.com/inbox";
//        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));

//        try {
//            getPackageManager().getPackageInfo("com.facebook.katana", 0); //Checks if FB is even installed.
//
//            //148779248555772
//
//            Intent fbIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://albums")); //Trys to make intent with FB's URI
//            startActivity(fbIntent);
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
    }

    public void btnVideo_onClick(View v) {
        if (AlertDialogs.checkNetworkStatus(HomeActivity.this)) {
            try {
                String videoId = "3YH3m3o5AkM";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videoId));
                intent.putExtra("VIDEO_ID", videoId);
                startActivity(intent);
            } catch (Exception ex) {
                //Uri CONTENT_URI = Uri.parse(urlVideo);
                //Intent myIntent = new Intent(Intent.ACTION_VIEW, CONTENT_URI);
                Intent intent = new Intent(this, VideoPlayerController.class);
                startActivity(intent);
            }

        } else {
            AlertDialogs.createAlertDialogNoInternetConn(getParent());
        }
    }

    public void btnShoeBox_onClick(View v) {
        if (AlertDialogs.checkNetworkStatus(HomeActivity.this)) {
            Uri CONTENT_URI = Uri.parse(urlShoeBox);
            Intent myIntent = new Intent(Intent.ACTION_VIEW, CONTENT_URI);
            startActivity(myIntent);
        } else {
            AlertDialogs.createAlertDialogNoInternetConn(getParent());
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
package com.shoebox.android;

import android.app.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.shoebox.android.view.SplashVideoView;

public class ShoeBoxAppActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        /*
        SplashVideoView videoHolder = (SplashVideoView) findViewById(R.id.videoView);
        String path = "android.resource://" + getPackageName() + "/" + R.raw.shoeboxintro;
        videoHolder.setVideoURI(Uri.parse(path));
        videoHolder.requestFocus();
        videoHolder.setScreenMode(SplashVideoView.DisplayMode.ZOOM);
        videoHolder.changeVideoSize(1080, 1080);
        videoHolder.start();
        */

		Thread splashThread = new Thread()
		{
			@Override
			public void run()
			{
				try
				{
					int waited = 0;
					while (waited < 2500)
					{
						sleep(100);
						waited += 100;
					}
				}
				catch (InterruptedException e)
				{
				}
				finally
				{
					finish();
					Intent intent=new Intent(ShoeBoxAppActivity.this, TabsActivity.class);
					startActivity(intent);
				}
			}
		};
		splashThread.start();
    }

}

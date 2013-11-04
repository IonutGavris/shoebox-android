package com.shoebox.android;

import com.shoebox.android.util.AlertDialogs;

import com.shoebox.android.R;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class SocialMediaActivity extends MenuActivity
{
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.social_media); 
	}
	
	public void btnFacebook_onClick(View v)
	{
		if(AlertDialogs.checkNetworkStatus(SocialMediaActivity.this))
		{
			Uri CONTENT_URI = Uri.parse("http://www.facebook.com/ShoeBox.ro");
			Intent myIntent = new Intent(Intent.ACTION_VIEW, CONTENT_URI);
			startActivity(myIntent);
		}
		else
		{
			AlertDialogs.createAlertDialogNoInternetConn(getParent());
		}
	}
	
	public void btnTwitter_onClick(View v)
	{
		if(AlertDialogs.checkNetworkStatus(SocialMediaActivity.this))
		{
			Uri CONTENT_URI = Uri.parse("https://twitter.com/ShoeBoxRomania");
			Intent myIntent = new Intent(Intent.ACTION_VIEW, CONTENT_URI);
			startActivity(myIntent);
		}
		else
		{
			AlertDialogs.createAlertDialogNoInternetConn(getParent());
		}
	}

}

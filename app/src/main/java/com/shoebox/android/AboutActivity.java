package com.shoebox.android;

import com.shoebox.android.home.HomeActivity;
import com.shoebox.android.util.AlertDialogs;

import com.shoebox.android.R;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class AboutActivity  extends MenuActivity
{
	String urlShoeBox = "http://shoebox.ro/";
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about); 
	}
	
	
	public void btnShoeBox_onClick(View v)
	{
		if(AlertDialogs.checkNetworkStatus(AboutActivity.this))
		{
			Uri CONTENT_URI = Uri.parse(urlShoeBox);
			Intent myIntent = new Intent(Intent.ACTION_VIEW, CONTENT_URI);
			startActivity(myIntent);
		}
		else
		{
			AlertDialogs.createAlertDialogNoInternetConn(getParent());
		}
	}
}

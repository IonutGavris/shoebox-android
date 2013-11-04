package com.shoebox.android.home;

import com.shoebox.android.MenuActivity;
import com.shoebox.android.util.AlertDialogs;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import com.shoebox.android.R;

public class HomeActivity  extends MenuActivity
{
	String urlVideo = "http://embed.animoto.com/play.html?w=swf/vp1&e=1321925553&f=v0F0AqygXKswKHIfJ6wCqg&d=103&m=p&r=240p+480p&volume=100&start_res=480p&i=m&ct=Afla%20despre%20ShoeBox.ro!&cu=http://www.shoebox.ro&options=allowfullscreen";
	String urlShoeBox = "http://shoebox.ro/";
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home); 
	}
 
	public void btnMaImplic_onClick(View v)
	{
		Intent o = new Intent(HomeActivity.this, ImplicareActivity.class);
		HomeGroup parentActivity = (HomeGroup)getParent();
		parentActivity.replaceContentView("", o);
	}
	
	public void btnImagini_onClick(View v)
	{
		Intent o = new Intent(HomeActivity.this, ImageGallery.class);
		HomeGroup parentActivity = (HomeGroup)getParent();
		parentActivity.replaceContentView("", o);
	}

	public void btnVideo_onClick(View v)
	{
		if(AlertDialogs.checkNetworkStatus(HomeActivity.this))
		{
			Uri CONTENT_URI = Uri.parse(urlVideo);
			Intent myIntent = new Intent(Intent.ACTION_VIEW, CONTENT_URI);
			startActivity(myIntent);
		}
		else
		{
			AlertDialogs.createAlertDialogNoInternetConn(getParent());
		}
	}
	
	public void btnShoeBox_onClick(View v)
	{
		if(AlertDialogs.checkNetworkStatus(HomeActivity.this))
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
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK) 
		{
			return true;
		}  
		return super.onKeyDown(keyCode, event);
	}
}

package com.shoebox.android;

import com.shoebox.android.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem; 

public class MenuActivity extends Activity 
{
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
	} 

	public boolean onCreateOptionsMenu(Menu menu)
	{
		menu.add(0, 0, 0, "Despre").setIcon(R.drawable.about_info);
		menu.add(0, 1, 0, "Social").setIcon(R.drawable.favorites);
		return true;
	} 

	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		return true;
	} 

	public boolean onOptionsItemSelected(MenuItem item) 
	{
		switch(item.getItemId())
		{
			case 0:
				Intent about = new Intent(getApplicationContext(), AboutActivity.class);
				startActivity(about);
			return true;
			case 1:
				Intent login = new Intent(getApplicationContext(), SocialMediaActivity.class);
				startActivity(login);
					return true;
		default:
			return false;
		} 
	}
}
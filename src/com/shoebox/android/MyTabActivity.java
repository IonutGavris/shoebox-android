package com.shoebox.android;
import android.app.ActivityGroup;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MyTabActivity extends ActivityGroup
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		return getLocalActivityManager().getCurrentActivity().onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) 
	{
		return getLocalActivityManager().getCurrentActivity().onMenuItemSelected(featureId, item);
	}
}
 
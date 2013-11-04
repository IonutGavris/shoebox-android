package com.shoebox.android;

import com.shoebox.android.contact.ContactsGroup;
import com.shoebox.android.home.HomeGroup;
import com.shoebox.android.locatii.LocatiiGroup;
import com.shoebox.android.news.NewsActivity;
import com.shoebox.android.news.NewsGroup;

import com.shoebox.android.R;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class TabsActivity extends TabActivity 
{
	TabHost tabHost ;
	public static String path;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		TabHost.TabSpec spec;  
		Intent intent; 

		path=this.getPackageResourcePath();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabs_layout);

		Resources res = getResources();  
		tabHost = getTabHost(); 

		//Home
		intent = new Intent().setClass(this, HomeGroup.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		spec = tabHost.newTabSpec("ShoeBox").setIndicator("ShoeBox",
				res.getDrawable(R.layout.tab_home))
				.setContent(intent); 

		tabHost.addTab(spec);

		//News
		intent = new Intent().setClass(this, NewsActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		spec = tabHost.newTabSpec("Noutati").setIndicator("Noutati",
				res.getDrawable(R.layout.tab_news))
				.setContent(intent);

		tabHost.addTab(spec); 
		
		//Locatii
		intent = new Intent().setClass(this, LocatiiGroup.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		spec = tabHost.newTabSpec("Locatii").setIndicator("Locatii",
				res.getDrawable(R.layout.tab_location))
				.setContent(intent);

		tabHost.addTab(spec);
  
		//Contacts
		intent = new Intent().setClass(this, ContactsGroup.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		spec = tabHost.newTabSpec("Contact").setIndicator("Contact",
				res.getDrawable(R.layout.tab_contact))
				.setContent(intent);

		tabHost.addTab(spec);

		tabHost.setCurrentTab(0); 
	}

	public boolean switchTab(int tab)
	{
		tabHost.setCurrentTab(tab);
		return true;
	}
}

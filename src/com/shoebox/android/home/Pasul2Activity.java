package com.shoebox.android.home;

import com.shoebox.android.MenuActivity;

import com.shoebox.android.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

public class Pasul2Activity extends MenuActivity
{
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pasul2); 
	}

	public void btnPasul3_onClick(View v)
	{
		Intent o = new Intent(Pasul2Activity.this, Pasul3Activity.class);
		HomeGroup parentActivity = (HomeGroup)getParent();
		parentActivity.replaceContentView("", o);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
	    if (keyCode == KeyEvent.KEYCODE_BACK) 
	    {
	    	HomeGroup.self.back();
	        return true;
	    }  
	    return super.onKeyDown(keyCode, event);
	}
}

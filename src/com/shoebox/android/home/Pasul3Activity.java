package com.shoebox.android.home;

import com.shoebox.android.MenuActivity;

import com.shoebox.android.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

public class Pasul3Activity  extends MenuActivity
{
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pasul3); 
	}

	public void btnPasul4_onClick(View v)
	{
		Intent o = new Intent(Pasul3Activity.this, Pasul4Activity.class);
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

package com.shoebox.android.home;

import com.shoebox.android.MenuActivity;

import com.shoebox.android.R;
import android.os.Bundle;
import android.view.KeyEvent;

public class Pasul4Activity  extends MenuActivity
{
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pasul4); 
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
 
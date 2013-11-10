package com.shoebox.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class ShoeBoxAppActivity extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
    	super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
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

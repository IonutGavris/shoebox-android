package com.shoebox.android.locatii;

import java.util.ArrayList;

import com.shoebox.android.MyTabActivity;
import com.shoebox.android.contact.ContactsGroup;
import com.shoebox.android.contact.ContactsListActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

public class LocatiiGroup extends MyTabActivity 
{
	public static LocatiiGroup self;
	
	private ArrayList<View> history;
	
	@Override 
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState); 
		
		new ContactsTask().execute();
		
		this.history = new ArrayList<View>();	
		self = this;
		
		Intent sprint = new Intent(LocatiiGroup.this, LocatiiActivity.class);
		replaceContentView("sprint", sprint); 
	}
	
	public void replaceContentView(String id, Intent newIntent) 
	{
		View view = getLocalActivityManager().startActivity(id, newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView();
		history.add(view);  
		this.setContentView(view);
	}
	
	private class ContactsTask extends AsyncTask<Void, Void, Void>
	{
		private ProgressDialog progress = null;

		@Override
		protected Void doInBackground(Void... params)
		{
			Runnable runnable = new Runnable() 
			{
				@Override
				public void run() 
				{
					for (int i = 0; i <= 10; i++)
					{
						try 
						{
							Thread.sleep(10000);
						} catch (InterruptedException e) 
						{
							e.printStackTrace();
						}
					}
				}
			};
			new Thread(runnable).start();
			return null;
		}

		@Override
		protected void onCancelled() 
		{
			super.onCancelled();
		}

		@Override
		protected void onPreExecute() 
		{
			progress = ProgressDialog.show(LocatiiGroup.this, null, "Loading data...");
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Void result) 
		{
			progress.dismiss();
			super.onPostExecute(result);
		}

		@Override 
		protected void onProgressUpdate(Void... values) 
		{ 
			super.onProgressUpdate(values);
		}
	}
	
    public void back()
    {  
    	if(history.size() > 1) 
	    {  
	        history.remove(history.size()-1);  
	        setContentView(history.get(history.size()-1));  
	    }
        else 
        {  
            finish();  
        }  
    }  
	    
    @Override  
    public void onBackPressed() 
    {  
    	LocatiiGroup.self.back();  
        return;  
    } 
   
   @Override
   public void onResume()
   {
	   super.onResume();
   }
}

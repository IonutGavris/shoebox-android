package com.shoebox.android.util;




import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.shoebox.android.R;

public class AlertDialogs 
{
	public static void createAlertDialogNoInternetConn(Context ctx) 
	{
		AlertDialog alertDialog = new AlertDialog.Builder(ctx).create();
		alertDialog.setTitle("Eroare conexiune!");
		alertDialog.setMessage("Upsss! Verificati conexiunea la internet!");
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int which){}
		}); 
		alertDialog.setIcon(R.drawable.alarm_warning_icon);
		alertDialog.show();
	}


	public static boolean checkNetworkStatus(Context ctx) {
		ConnectivityManager con = (ConnectivityManager) ctx.getSystemService(Activity.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = con.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		boolean wifi = networkInfo != null ? networkInfo.isConnectedOrConnecting() : false;
		networkInfo = con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		boolean internet = networkInfo != null ? networkInfo.isConnectedOrConnecting() : false;
		if (wifi || internet) {
			return true;
		} else {
			return false;
		}
	}

	public static void createAlertDialogCompleteAllFields(Context ctx)
	{
		AlertDialog alertDialog = new AlertDialog.Builder(ctx).create();
		alertDialog.setTitle("Alerta");
		alertDialog.setMessage("Completati toate campurile!");
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int which) {}
		});
		alertDialog.setIcon(R.drawable.alarm_warning_icon);
		alertDialog.show();
	}
	
	public static void createAlertWithSingleButton(Activity a, String title, String message, String btnText)
	{
		AlertDialog alertDialog = new AlertDialog.Builder(a).create();
  	   	alertDialog.setTitle(title);
  	   	alertDialog.setMessage(message);
  	   	alertDialog.setButton(btnText, new DialogInterface.OnClickListener() 
  	   	{
  	   	    public void onClick(DialogInterface dialog, int which) 
  	   	    {
  	   	    } 
  	   	}); 
  	   	alertDialog.show();
	}
}

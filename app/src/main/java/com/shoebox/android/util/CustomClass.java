package com.shoebox.android.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


import android.content.Context;
import android.content.SharedPreferences;

public class CustomClass 
{
	public static String urlRSSFeed = "http://shoebox.ro/feed/";//"http://feeds.feedburner.com/Shoeboxro?format=xml";
	
	public static void saveTheSelectedValue(Context ctx, String key, String value)
	{
		SharedPreferences sp = ctx.getSharedPreferences("PlataRCA", 0);
		SharedPreferences.Editor Ed = sp.edit();
		Ed.putString(key, value);               
		Ed.commit();
	}

	public static String getTheSelectedValue(Context ctx, String key)
	{
		SharedPreferences sp1 = ctx.getSharedPreferences("PlataRCA", 0);
		String selected = sp1.getString(key, "");   
		//Log.e("key = ", selected);
		return selected;
	}

	public static void removeSelectedValue(Context ctx, String key)
	{
		SharedPreferences preferences = ctx.getSharedPreferences("PlataRCA", 0);
		preferences.edit().remove(key).commit();
	}
	
	public static Boolean checkString(String value)
	{
		if(value != null && value.length() > 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public static boolean checkEmail(String email)
	{
		Pattern pattern = Pattern.compile(".+@.+\\.[a-z]+");
		Matcher matcher = pattern.matcher(email);

		boolean matchFound = matcher.matches();
		return matchFound;
	}
	
}

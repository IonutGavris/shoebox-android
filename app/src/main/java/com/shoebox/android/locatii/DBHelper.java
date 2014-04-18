package com.shoebox.android.locatii;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import de.greenrobot.daoexample.DaoMaster;
import de.greenrobot.daoexample.DaoSession;
import de.greenrobot.daoexample.DaoMaster.DevOpenHelper;

public class DBHelper {

	private static SQLiteDatabase db;
	private static DaoMaster daoMaster;
	private static DaoSession daoSessionInstance;
	
	protected DBHelper() {
		//singleton
	}
	  
	public static DaoSession getSessionInstance(Context context) {
		if(daoSessionInstance == null) {
			DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "lcr-db", null);
	        db = helper.getWritableDatabase();
	        daoMaster = new DaoMaster(db);
	        daoSessionInstance = daoMaster.newSession(); 
		}
		return daoSessionInstance;
	}
}

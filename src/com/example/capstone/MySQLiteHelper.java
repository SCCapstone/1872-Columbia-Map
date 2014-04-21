package com.example.capstone;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteHelper extends SQLiteOpenHelper {

	  public static final String TABLE_LOCATIONS = "locations";
	  public static final String COLUMN_ID = "_id";
	  
	  public static final String KEY_NAME = "name";  
	  public static final String KEY_XCOORD = "xcoord";
	  public static final String KEY_YCOORD = "ycoord";
	  public static final String KEY_DESCR = "description";
	  public static final String KEY_IMAGESTRING = "imagestring";
	   
	  private static final String DATABASE_NAME = "locations.db";
	  private static final int DATABASE_VERSION = 1;

	  //Database creation sql statement
	  private static final String DATABASE_CREATE = 
			  "CREATE TABLE " + TABLE_LOCATIONS + " (" + COLUMN_ID + " integer primary key autoincrement, "
              + KEY_XCOORD +" TEXT, "
			  + KEY_YCOORD +" TEXT, "
              + KEY_NAME + " TEXT, "
              + KEY_DESCR + " TEXT, " 
              + KEY_IMAGESTRING +" TEXT)";

	  public MySQLiteHelper(Context context) {
	    super(context, DATABASE_NAME, null, DATABASE_VERSION);
	  }

	  @Override
	  public void onCreate(SQLiteDatabase database) {
	    database.execSQL(DATABASE_CREATE);
	  }

	  @Override
	  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	    db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATIONS);
	    onCreate(db);
	  }
	
}
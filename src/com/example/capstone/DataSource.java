package com.example.capstone;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataSource {

	  //Database fields
	  private SQLiteDatabase database;
	  private MySQLiteHelper dbHelper;
	  private Cursor cursorOne;
	  private Cursor cursorTwo;
	  
	  private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
	      MySQLiteHelper.KEY_XCOORD, MySQLiteHelper.KEY_YCOORD, MySQLiteHelper.KEY_NAME,
	      MySQLiteHelper.KEY_DESCR, MySQLiteHelper.KEY_IMAGESTRING};

	  public DataSource(Context context) {
	    dbHelper = new MySQLiteHelper(context);
	  }

	  public void open() throws SQLException {
	    database = dbHelper.getWritableDatabase();
	  }

	  public void close() {
	    dbHelper.close();
	  }

	  public LocationObject addLocation(String xcoord, String ycoord, String
			  title, String description, String imageString) {
	    
		Log.v("ADD", "Beginning of add location in DataSource");
		
		ContentValues values = new ContentValues();
	    values.put(MySQLiteHelper.KEY_XCOORD, xcoord);
	    values.put(MySQLiteHelper.KEY_YCOORD, ycoord);
	    values.put(MySQLiteHelper.KEY_NAME, title);
	    values.put(MySQLiteHelper.KEY_DESCR, description);
	    values.put(MySQLiteHelper.KEY_IMAGESTRING, imageString);
	    
	    Log.v("ADD values: ", values.toString());
	    
	    long insertId = database.insert(MySQLiteHelper.TABLE_LOCATIONS, null,
	        values);
	    
	    cursorOne = database.query(MySQLiteHelper.TABLE_LOCATIONS,
	        allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
	        null, null, null);
	    cursorOne.moveToFirst();
	    
	    LocationObject newLocations = cursorToLocation(cursorOne);
	    cursorOne.close();
	    return newLocations;
	  }

	  public void deleteLocation(LocationObject object) {
	    long id = object.getId();
	    database.delete(MySQLiteHelper.TABLE_LOCATIONS, MySQLiteHelper.COLUMN_ID
	        + " = " + id, null);
	    Log.v("DELETE id:", String.valueOf(id));
	    Log.v("DELETE", "inside of deleteLocation");
	  }

	  public List<LocationObject> getAllLocations() {
	    List<LocationObject> locationsList = new ArrayList<LocationObject>();

	    cursorTwo = database.query(MySQLiteHelper.TABLE_LOCATIONS,
	        allColumns, null, null, null, null, null);
	    
	    cursorTwo.moveToFirst();
	    
	    while(cursorTwo.isAfterLast() == false) 
	    {
	      LocationObject location = cursorToLocation(cursorTwo);
	      locationsList.add(location);
	      cursorTwo.moveToNext();
	      
	      Log.v("RETRIEVE location object in getAllLocations: ", location.toString());
	      Log.v("RETRIEVE list size in getAllLocations:", String.valueOf(locationsList.size()));
	    }
	    
	    //Make sure to close the cursor
	    cursorTwo.close();
	    
	    //Make sure that there is actually something inside the locationsList
	    for (int i=0; i<locationsList.size(); i++)
	    {
	    	Log.v("RETRIEVE locationList xCoord:", locationsList.get(i).getxCoord());
	      	Log.v("RETRIEVE locationList yCoord:", locationsList.get(i).getyCoord());
	    	Log.v("RETRIEVE locationList titles:", locationsList.get(i).getLocTitle());
	    	Log.v("RETRIEVE locationList description:", locationsList.get(i).getLocDescription());
	    	Log.v("RETRIEVE locationList image string:", locationsList.get(i).getImageString());
	    }
	   
	    return locationsList;
	  }

	  private LocationObject cursorToLocation(Cursor cursor) {
		  
	    LocationObject location = new LocationObject();
	    location.setId(cursor.getInt(cursor.getColumnIndex(MySQLiteHelper.COLUMN_ID)));
	    location.setxCoord(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.KEY_XCOORD)));
	    location.setyCoord(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.KEY_YCOORD)));
	    location.setLocTitle(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.KEY_NAME)));
	    location.setLocDescription(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.KEY_DESCR)));
	    location.setImageString(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.KEY_IMAGESTRING)));
	    
	    Log.v("RETRIEVE title in cursorToLocation: ", location.getLocTitle());
	    
	    return location;
	  }

}
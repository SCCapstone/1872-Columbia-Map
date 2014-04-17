package com.example.capstone;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;
import java.util.Vector;
import org.json.JSONArray;
import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("UseValueOf")
public class EditLocation extends ListActivity implements OnClickListener {
	
	private static String FILENAME = "DataFile.txt";
	private Context context;
	private String finalimage,finaloutput,locationTitle,description,xLocation,yLocation;
	private Bitmap loadedImage;
	private static int RESULT_LOAD_IMAGE = 1;
	private ListView titlesList;
	private Vector<String> xCoordVector;
	private Vector<String> yCoordVector;
	private Vector<String> titleVector;
	private Vector<String> specialTitleVector;
	private Vector<String> descriptionVector;
	private Vector<String> imageStringVector;
	private Vector<LocationObject> locationVector;
	private ArrayAdapter<String> adapter;
	private Button SelectLocationButton;
	private SharedPreferences prefs;
	private boolean DELETE_FLAG=false;
	private int indexOfLocationToBeDeleted;

	public EditLocation()
	{
		
	}
	
	public EditLocation(Context context) 
	{
        this.context = context;
    }

	public String getLocationTitle() {
		return locationTitle;
	}

	public void setLocationTitle(String locationTitle) {
		this.locationTitle = locationTitle;
	}
	
	/**
	 * @return the fILENAME
	 */
	public static String getFILENAME() {
		return FILENAME;
	}

	/**
	 * @param fILENAME the fILENAME to set
	 */
	public static void setFILENAME(String fILENAME) {
		FILENAME = fILENAME;
	}

	public String switchFilename(String oldFileName)
	{
		String changeName = oldFileName;
		changeName = "change.txt";
		return changeName;
	}
	
	@SuppressLint("UseValueOf")
	@Override	
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);	
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setContentView(R.layout.location_edit);
		context = getApplicationContext();
		
		final Button LoadImage = (Button) findViewById(R.id.LoadImage);
		final Button DoneEditing = (Button) findViewById(R.id.DoneEditing);
		//final Button Delete = (Button)findViewById(R.id.deleteLocationButton);
		SelectLocationButton = (Button)findViewById(R.id.loadLocationButton);
		
		TextView EditScreenTitle = (TextView) findViewById(R.id.editscreentitle);
		TextView locationsList = (TextView) findViewById(R.id.locationsList);
		Typeface TradeGothic = Typeface.createFromAsset(getAssets(),"TradeGothic.ttf");
		Typeface TradeGothic18 = Typeface.createFromAsset(getAssets(),"TradeG18.ttf");
		
		EditScreenTitle.setTypeface(TradeGothic18);
		DoneEditing.setTypeface(TradeGothic);
		LoadImage.setTypeface(TradeGothic);
		locationsList.setTypeface(TradeGothic);
		//Delete.setTypeface(TradeGothic);
		SelectLocationButton.setTypeface(TradeGothic);
		
		initViews();
		  
		Intent i1 = getIntent();
		Double xLoc = i1.getDoubleExtra("xLocation", 0.0);
		Double yLoc = i1.getDoubleExtra("yLocation", 0.0);
		xLocation = new Double(xLoc).toString();
		yLocation = new Double(yLoc).toString();
		//FILENAME = xLocation+"."+yLocation+"."+"DATA.txt";
		
		//Begin another syncTask to browse and attach an image file
		LoadImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LoadImageAsync loadImageTask = new LoadImageAsync();
				loadImageTask.execute();
			}
		});
		     
	    //Initialize the vector and the adapter
		specialTitleVector = new Vector<String>();
		titleVector = new Vector<String>();
		xCoordVector = new Vector<String>();
		yCoordVector = new Vector<String>();
		descriptionVector = new Vector<String>();
		imageStringVector = new Vector<String>();
		
		//Get the data stored in SharedPreferences
		prefs = PreferenceManager.getDefaultSharedPreferences(this);

		Map<String,?> keys = prefs.getAll();
		
		//Put everything from shared prefs into vectors
	    for(Map.Entry<String,?> entry : keys.entrySet())
	    {
		    Log.v("PREFS",entry.getKey() + ": " + entry.getValue().toString());
		    
		    titleVector.add(entry.getKey().toString());
//		    if(!entry.getKey().contains("locationTitle") && !entry.getKey().contains("xLocation") &&
//		    		!entry.getKey().contains("yLocation") && !entry.getKey().contains("description")
//		    		&& !entry.getKey().contains("imageString"))
//		    {
//		    	specialTitleVector.add(entry.getKey());
//		    }
//		    
//		    if(entry.getKey().equals("locationTitle"))
//		    {
//		    	titleVector.add(entry.getValue().toString());
//		    	Log.v("TITLE_VECTOR", entry.getValue().toString());
//		    }
//		    else if(entry.getKey().equals("xLocation"))
//		    {
//		    	xCoordVector.add(entry.getValue().toString());
//		    	Log.v("XCOORD", entry.getValue().toString());
//		    }
//		    else if(entry.getKey().equals("yLocation"))
//		    {
//		    	yCoordVector.add(entry.getValue().toString());
//		    	Log.v("YCOORD", entry.getValue().toString());
//		    }
//		    
//		    else if(entry.getKey().equals("description"))
//		    {
//		    	descriptionVector.add(entry.getValue().toString());
//		    	Log.v("DESCRIPTION", entry.getValue().toString());
//		    }
//		    else if(entry.getKey().equals("imageString"))
//		    {
//		    	imageStringVector.add(entry.getValue().toString());
//		    	Log.v("IMAGE", entry.getValue().toString());
//		    }
//		    
		}
		
	    adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,titleVector);
	    titlesList.setAdapter(adapter);
	    
	    if(adapter != null) 
	    {
			//check to see if the item was selected
			titlesList.setOnItemClickListener(new OnItemClickListener() 
			{

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {

					indexOfLocationToBeDeleted = position;
					DELETE_FLAG = true;

					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Toast.makeText(
									EditLocation.this,
									"This location has been marked for deletion",
									Toast.LENGTH_SHORT).show();
						}
					});
				}
			});
			
			vectorSizeCheck();
//			Delete.setOnClickListener(new View.OnClickListener() 
//			{
//				@Override
//				public void onClick(View v) {
//					
//					//The location is in fact deleted from the list in OnClick(View v) below because the adapter needs to be updated there
//					vectorSizeCheck();
//				}
//			});
		}
	}
    
	private void readJSON()
	{
		try {
		    JSONArray jsonArray2 = new JSONArray(prefs.getString("key", "[]"));
		    for (int i = 0; i < jsonArray2.length(); i++) {
		         Log.v("RETRIEVE", jsonArray2.getInt(i)+"");
		    }
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}
	
	private void vectorSizeCheck()
	{	
		//check vector sizes 
	    Log.v("SAVE", String.valueOf(specialTitleVector.size()));
	    Log.v("TITLE_VECTOR", String.valueOf(titleVector.size()));
	    Log.v("XCOORD", String.valueOf(xCoordVector.size()));
	    Log.v("YCOORD", String.valueOf(yCoordVector.size()));
	    Log.v("DESCRIPTION", String.valueOf(descriptionVector.size()));
	    Log.v("IMAGE", String.valueOf(imageStringVector.size()));
			
	}
	
	private void initViews()
	{
		titlesList = (ListView) findViewById(android.R.id.list);
	    titlesList.setScrollbarFadingEnabled(false);
	    SelectLocationButton.setOnClickListener(this);
	}
	
	private void addTitlesToVector()
	{
	
		titleVector.add(getLocationTitle());
	}
	
	@Override
	public void onClick(View v) 
	{
		if (adapter != null) 
		{
			//add location
			switch (v.getId()) 
			{
			case R.id.loadLocationButton:
				addTitlesToVector();
				break;
			}
			adapter.notifyDataSetChanged();
			
			//if we are not deleting anything, make sure the default filename is set
			setFILENAME(FILENAME);
			
			//delete location if one is selected
			if (DELETE_FLAG == true) 
			{
				switch (v.getId()) 
				{
				case R.id.loadLocationButton:
					RemoveFromSharedPrefs(indexOfLocationToBeDeleted);
					//specialTitleVector.remove(indexOfLocationToBeDeleted);
     				titleVector.remove(indexOfLocationToBeDeleted);
//					xCoordVector.remove(indexOfLocationToBeDeleted);
//					yCoordVector.remove(indexOfLocationToBeDeleted);
//					descriptionVector.remove(indexOfLocationToBeDeleted);
//					imageStringVector.remove(indexOfLocationToBeDeleted);
//					adapter.remove(adapter.getItem(indexOfLocationToBeDeleted));
					break;
				}
				adapter.notifyDataSetChanged();
				
				String overWriteData ="";
				for (int i=0; i<titleVector.size(); i++)
				{       
					    overWriteData = xLocation+"\n"+yLocation+"\n"+locationTitle+"\n"+description+"\n"+finalimage+"\n";
					    
					    /*produces index out of bounds error even though it should not*/
				    	//overWriteData=xCoordVector.get(i)+"\n"+yCoordVector.get(i)+"\n"+titleVector.get(i)+"\n"+descriptionVector.get(i)+"\n"+imageStringVector.get(i)+"\n";
				    	OverWriteFile(overWriteData); 
				}
				    
			}
		}
	}
	
	public void RemoveFromSharedPrefs(int indexOfLocationToBeDeleted)
	{
//		String specialTitleToBeRemoved = specialTitleVector.get(indexOfLocationToBeDeleted);
//		prefs.edit().remove(specialTitleToBeRemoved).commit();
		
		String titleToBeRemoved = titleVector.get(indexOfLocationToBeDeleted);
		prefs.edit().remove(titleToBeRemoved).commit();
		
//		String xCoordToBeRemoved = xCoordVector.get(indexOfLocationToBeDeleted);
//		prefs.edit().remove(xCoordToBeRemoved).commit();
//		
//		String yCoordToBeRemoved = yCoordVector.get(indexOfLocationToBeDeleted);
//		prefs.edit().remove(yCoordToBeRemoved).commit();
//		
//		String descrToBeRemoved = descriptionVector.get(indexOfLocationToBeDeleted);
//		prefs.edit().remove(descrToBeRemoved).commit();
//		
//		String imageToBeRemoved = imageStringVector.get(indexOfLocationToBeDeleted);
//		prefs.edit().remove(imageToBeRemoved).commit();
	}
	
	//called when user clicks on Done Editing button 
	public void DoneEditing(View view) 
	{
		EditText titleedit = (EditText) findViewById(R.id.titleedit);
		locationTitle=titleedit.getText().toString();
		
		EditText descriptionedit = (EditText) findViewById(R.id.descriptionedit);
		description=descriptionedit.getText().toString();
			
		if(loadedImage!=null)
		{
			String image = BitMapToString(loadedImage);
			StringBuffer stringBuffer = new StringBuffer();
			
			for (int i = 0; i < image.length(); i++) 
			{
				if (image.charAt(i) != '\n' && image.charAt(i) != '\r') 
				{
					stringBuffer.append(image.charAt(i));
				}
			}
			finalimage = stringBuffer.toString();
			
			finaloutput=xLocation+"\n"+yLocation+"\n"+locationTitle+"\n"+description+"\n"+finalimage+"\n";
			
			setLocationTitle(locationTitle);	
			storeLocationDataInSharedPrefs();
			//Write location data to an instance of the location object class
			createLocationObject();
		}
		else
		{
			finalimage = "dummyString";
			finaloutput=xLocation+"\n"+yLocation+"\n"+locationTitle+"\n"+description+finalimage+"\n";
			setLocationTitle(locationTitle);
			storeLocationDataInSharedPrefs();
			createLocationObject();
		}
		
		WritetoFile(finaloutput);
		Toast.makeText(context.getApplicationContext(), "Location Saved", Toast.LENGTH_SHORT).show();
		
	}
	private void storeLocationDataInSharedPrefs() 
	{
		//for specialTitleVector to be displayed as the location list
		PreferenceManager.getDefaultSharedPreferences(this)
		.edit().putString(getLocationTitle(), getLocationTitle()).commit();	
		
//		PreferenceManager.getDefaultSharedPreferences(this)
//		.edit().putString("xLocation", xLocation).commit();	
//		
//		PreferenceManager.getDefaultSharedPreferences(this)
//		.edit().putString("yLocation", yLocation).commit();	
//		
//		PreferenceManager.getDefaultSharedPreferences(this)
//		.edit().putString("locationTitle", locationTitle).commit();
//		
//		PreferenceManager.getDefaultSharedPreferences(this)
//		.edit().putString("description", description).commit();
//		
//		PreferenceManager.getDefaultSharedPreferences(this)
//		.edit().putString("imageString", finalimage).commit();	
	}

	/**
	 * Writes location data to a location object 
	 */
	private void createLocationObject() 
	{
	    locationVector = new Vector();
		LocationObject saveLocation = new LocationObject(xLocation, yLocation, locationTitle, description, finalimage);
		
		locationVector.add(saveLocation);
		
//		JSONArray jsonArray = new JSONArray();
//		jsonArray.put(locationVector);
//		Editor editor = prefs.edit();
//		editor.putString(locationTitle, jsonArray.toString());
//		editor.commit();
	
	}
	
	private class LoadImageAsync extends AsyncTask<Void, Void, Void> 
	 {
		    @Override
		    protected Void doInBackground(Void...params) 
		    {
		    	 Intent load = new Intent(
		                    Intent.ACTION_PICK,
		                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

		            startActivityForResult(load, RESULT_LOAD_IMAGE);
		    	
		    
		        try 
		        {			  
					  EditLocation.this.runOnUiThread(new Runnable()
						{
							  @Override
							  public void run() 
							  {
							      Toast.makeText(EditLocation.this, "Loading image", Toast.LENGTH_SHORT).show();
							  }
					     });
		
				} 
		        catch (Exception e) 
				{
					e.printStackTrace();
				}       
		    	
		    	return null;
		    }
	 }
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	 {
	     super.onActivityResult(requestCode, resultCode, data);

	     if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) 
	     {
	         Uri selectedImage = data.getData();
	         String[] filePathColumn = {MediaStore.Images.Media.DATA};

	         Cursor cursor = getContentResolver().query(selectedImage,
	                 filePathColumn, null, null, null);
	         
	         cursor.moveToFirst();

	         int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
	         String picturePath = cursor.getString(columnIndex);
	         cursor.close();
	         
	         //imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
	         loadedImage = BitmapFactory.decodeFile(picturePath);
	         ImageView imageView = (ImageView) findViewById(R.id.imageView1);
	         
	         if(loadedImage != null)
	         {
	        	 imageView.setImageBitmap(loadedImage);	 
	        	 Toast.makeText(EditLocation.this, "Image loaded", Toast.LENGTH_SHORT).show();
	         }
	         else if (loadedImage == null)
	         {
	        	 Toast.makeText(EditLocation.this, "Image not loaded", Toast.LENGTH_SHORT).show();
	         }
	     }
	     
	 }
	
	//convert bitmap to string
	public String BitMapToString(Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		byte[] b = baos.toByteArray();
		String temp = Base64.encodeToString(b, Base64.DEFAULT);
		return temp;
	}
	
	//convert string to bitmap
	public Bitmap StringToBitMap(String encodedString) {
		try {
			byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
			Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
					encodeByte.length);
			return bitmap;
		} catch (Exception e) {
			e.getMessage();
			return null;
		}
	}
	
	public static String getFilename() 
	{
		return FILENAME;
	}
	
	private void OverWriteFile(String output)
	{

		 try 
		 {
			 //this new file name will be used by the main map 
			 String switchedName = switchFilename(FILENAME);
			 Log.v("HELP", switchedName);
			 setFILENAME(switchedName);
			 OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(switchedName, Context.MODE_PRIVATE));
			
			 outputStreamWriter.write(output);
			 outputStreamWriter.close();
			 Toast.makeText(context.getApplicationContext(), "Location Deleted", Toast.LENGTH_LONG).show();
		 }
		 catch (IOException e) {
			 Log.e("HELP", "File overwrite failed: " + e.toString());
		 }
		
	}
	
	private void WritetoFile(String output) 
	 {
		
		 try {
			 OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(FILENAME, Context.MODE_APPEND));
			 //outputStreamWriter.write("Location Test");
			 //while (st.hasMoreElements()) {
				 //outputStreamWriter.write(st.nextElement().toString()+ "\n");
			 //}
			 outputStreamWriter.write(output);
			 outputStreamWriter.close();
			 Toast.makeText(context.getApplicationContext(), "Location Saved", Toast.LENGTH_LONG).show();
		 }
		 catch (IOException e) {
			 Log.e("HELP", "File write failed: " + e.toString());
		 }

		 //WRITE SPLIT WITH COMMA
		 //System.out.println("---- Split by comma ',' ------");
		 //StringTokenizer st2 = new StringTokenizer(str, ",");

		 //while (st2.hasMoreElements()) {
		 //	System.out.println(st2.nextElement());
		 //}
	 }


} 
	 


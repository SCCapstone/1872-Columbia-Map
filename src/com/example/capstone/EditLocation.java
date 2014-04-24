package com.example.capstone;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("UseValueOf")
public class EditLocation extends ListActivity implements OnClickListener {
	
	private static final String FILENAME = "DataFile.txt";
	private static String TAG = "Location DATA";
	private Context context;
	private String finalimage,finaloutput,locationTitle,description,xLocation,yLocation;
	private Bitmap loadedImage;
	private static int RESULT_LOAD_IMAGE = 1;
	private ListView titlesList;
	private Vector<String> titleVector;
	private List<LocationObject> locationArray;
	private ArrayAdapter<String> adapter;
	private Button DeleteLocationButton;
	private boolean DELETE_FLAG=false;
	private int indexOfLocationToBeDeleted;
	private DataSource datasource;
	private EditText editTitle;
	private EditText editDescription;

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
	
	public String getxLocation() {
		return xLocation;
	}

	public String getyLocation() {
		return yLocation;
	}

	/**
	 * @param xLocation the xLocation to set
	 */
	public void setxLocation(String xLocation) {
		this.xLocation = xLocation;
	}

	/**
	 * @param yLocation the yLocation to set
	 */
	public void setyLocation(String yLocation) {
		this.yLocation = yLocation;
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
		DeleteLocationButton = (Button)findViewById(R.id.deleteLocationButton);
		
		TextView ScreenTitle = (TextView) findViewById(R.id.editscreentitle);
		TextView locationsList = (TextView) findViewById(R.id.locationsList);
		
		editTitle = (EditText) findViewById(R.id.titleedit);
		editDescription = (EditText) findViewById(R.id.descriptionedit);
		TextView EditScreenTitle = (TextView) findViewById(R.id.editscreentitle);
		Typeface TradeGothic = Typeface.createFromAsset(getAssets(),"TradeGothic.ttf");
		Typeface TradeGothic18 = Typeface.createFromAsset(getAssets(),"TradeG18.ttf");
		
		ScreenTitle.setTypeface(TradeGothic18);
		EditScreenTitle.setTypeface(TradeGothic18);
		DoneEditing.setTypeface(TradeGothic);
		LoadImage.setTypeface(TradeGothic);
		locationsList.setTypeface(TradeGothic);
		DeleteLocationButton.setTypeface(TradeGothic);
		
		initViews();
		  
		Intent i1 = getIntent();
		Double xLoc = i1.getDoubleExtra("xLocation", 0.0);
		Double yLoc = i1.getDoubleExtra("yLocation", 0.0);
		xLocation = Double.toString(xLoc);
		yLocation = Double.toString(yLoc);
		setxLocation(xLocation);
		setyLocation(yLocation);
	
		//Begin another syncTask to browse and attach an image file
		LoadImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LoadImageAsync loadImageTask = new LoadImageAsync();
				loadImageTask.execute();
			}
		});
		     
	    //Initialize the vector and the adapter
		titleVector = new Vector<String>();
			    
	    /***Retrieve location data from the database***/
		datasource = new DataSource(this);
		datasource.open();
		
	    final List<LocationObject> listFromDataBase = getDataFromDataBase();
	    Log.v("RETRIEVE arrayFromDataBase size before deletion:", String.valueOf(listFromDataBase.size()));
	    
	    /***Put the titles into the adapter to be displayed as a list***/
	    if (listFromDataBase.size()>0)
	    {
		    for(int k=0; k<listFromDataBase.size(); k++)
		    {
		    	String title = listFromDataBase.get(k).getLocTitle();
		    	titleVector.add(title);
		    	Log.v("VECTOR title:", title);
		    	Log.v("VECTOR size", String.valueOf(titleVector.size()));
		    }
	    }
	    adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,titleVector);
	    titlesList.setAdapter(adapter);
	
	    /***Record index of the location title to be deleted***/
	    recordLocationTitleToBeDeleted();
	    
	    /***Delete the location from the database and overwrite layout file***/
	    DeleteLocationButton.setOnClickListener(new View.OnClickListener() 
		{	
			@Override
			public void onClick(View view) 
			{
				if (DELETE_FLAG == true) 
				{
					switch (view.getId()) 
					{
					case R.id.deleteLocationButton:
										
						//Remove the title from shared preferences
						titleVector.remove(indexOfLocationToBeDeleted);
						
						LocationObject locObj = listFromDataBase.remove(indexOfLocationToBeDeleted);
						Log.v("RETRIEVE arrayFromDataBase after deletion: ", String.valueOf(listFromDataBase.size()));
						
						if(locObj != null)
						{
							datasource.deleteLocation(locObj);
							Log.v("RETRIEVE obj being deleted", locObj.toString());
						}
						
						try 
						{
							overWriteFile(listFromDataBase);
						} catch (IOException e) 
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
							Log.v("RETRIEVE", "inside the catch statement after call to deleteLocationsFromMap");
							Log.v("RETRIEVE e:", e.toString());
						}
						break;
					}
					adapter.notifyDataSetChanged();
				  }
					Log.v("LOCATION", "end of deleting a location");
				
			}
		});
	}


	private void recordLocationTitleToBeDeleted() {
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
		}
	}

	private List<LocationObject> getDataFromDataBase() {

		locationArray = datasource.getAllLocations();
				
		//Retrieve the location data from the database
		if(locationArray != null)
		{
			for(int k=0; k<locationArray.size(); k++) 
			{
	            String x = locationArray.get(k).getxCoord();
	            String y = locationArray.get(k).getyCoord();
	            String locTitle = locationArray.get(k).getLocTitle();
	            String descr = locationArray.get(k).getLocDescription();
	            String image = locationArray.get(k).getImageString();
	            
	            Log.v("LOCATION_ARRAY getDataFromDataBase:", x+y+locTitle+descr+image+"\n");
			}          
		}
		
		return locationArray;
	}
	
	private void initViews()
	{
		titlesList = (ListView) findViewById(android.R.id.list);
	    titlesList.setScrollbarFadingEnabled(false);
	    DeleteLocationButton.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) 
	{
		//All the good stuff happens in DoneEditing(View view) 
	}
	

	//called when user clicks on Done Editing button 
	public void DoneEditing(View view) 
	{
		locationTitle=editTitle.getText().toString();
		setLocationTitle(locationTitle);
		
		description=editDescription.getText().toString();
			
		recordLocationData();
		
		Toast.makeText(context.getApplicationContext(), "Location Saved", Toast.LENGTH_SHORT).show();
		
		if (adapter != null) 
		{
			//add location
			switch (view.getId()) 
			{
			case R.id.DoneEditing:				
		        titleVector.add(locationTitle);		
				Log.v("TITLE", locationTitle);
				break;
			}
			adapter.notifyDataSetChanged();
		}
		
		Log.v("ADD: ", "end of DoneEditing");
	}

	/**
	 * Record location data, including the image string
	 */
	private void recordLocationData() 
	{
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
				
			createLocationObject();
			writetoFile(finaloutput);
			
			datasource.addLocation(getxLocation(), getyLocation(), locationTitle, description, finalimage);  
			Log.v("CHECK again: ", getxLocation()+"\n"+getyLocation());
		}
		else
		{
			finalimage = "dummystring";
			finaloutput=xLocation+"\n"+yLocation+"\n"+locationTitle+"\n"+description+"\n"+finalimage+"\n";
			
			createLocationObject();
			writetoFile(finaloutput);
			
			datasource.addLocation(getxLocation(), getyLocation(), locationTitle, description, finalimage);  
			Log.v("CHECK again: ", getxLocation()+"\n"+getyLocation());
		}
		
		Log.v("ADD finaloutput: ", finaloutput);
	}

	/**
	 * Writes location data to a location object 
	 */
	private void createLocationObject() 
	{
	    locationArray = new Vector();
		LocationObject saveLocation = new LocationObject(0, xLocation, yLocation, locationTitle, description, finalimage);
		
		//save the object to a vector
		if (saveLocation != null)
		{
			locationArray.add(saveLocation);
		}
					
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
	         else 
	         {
	        	 Toast.makeText(EditLocation.this, "Image not loaded", Toast.LENGTH_SHORT).show();
	         }
	     }
	     
	 }
	
	//Convert bitmap to string
	public String BitMapToString(Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		byte[] b = baos.toByteArray();
		String temp = Base64.encodeToString(b, Base64.DEFAULT);
		return temp;
	}
	
	//Convert string to bitmap
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
	
	private void writetoFile(String output) 
	 {
		
		 try {
			 OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(FILENAME, Context.MODE_APPEND));
			
			 outputStreamWriter.write(output);
			 outputStreamWriter.close();
			 Toast.makeText(context.getApplicationContext(), "Location Saved", Toast.LENGTH_LONG).show();
		 }
		 catch (IOException e) {
			 Log.e(TAG, "File write failed: " + e.toString());
		 }
	 }

	private void overWriteFile(List<LocationObject> list) throws IOException
	{
		String newInput = "";
		StringBuilder stringBuilder = new StringBuilder();
		
		for (int i = 0; i < list.size(); i++)
		{
			        //Use this format: xLocation+"\n"+yLocation+"\n"+locationTitle+"\n"+description+"\n"+finalimage+"\n";
			        stringBuilder.append(list.get(i)
					.getxCoord()
					+ "\n"
					+ list.get(i).getyCoord()
					+ "\n"
					+ list.get(i).getLocTitle()
					+ "\n"
					+ list.get(i).getLocDescription()
					+ "\n"
					+ list.get(i).getImageString()
					+ "\n");
		}
		 newInput = stringBuilder.toString();
		 Log.v("DELETE newInput: ", newInput);
		 
         OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(FILENAME, Context.MODE_PRIVATE));
         outputStreamWriter.write(newInput);
         outputStreamWriter.close();
		 
	    Toast.makeText(context.getApplicationContext(), "Location Deleted", Toast.LENGTH_SHORT).show();
	}
	
	  @Override
	  protected void onResume() {
	    datasource.open();
	    super.onResume();
	  }

	  @Override
	  protected void onPause() {
	    datasource.close();
	    super.onPause();
	  }

} 
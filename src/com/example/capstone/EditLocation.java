package com.example.capstone;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.StringTokenizer;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

public class EditLocation extends Activity {
	
	private static final String FILENAME = "DataFile.txt";
	private static String TAG = "Location DATA";
	private Context context;
	private String finaloutput,title,description,xLocation,yLocation;
	private Bitmap loadedImage;
	private static int RESULT_LOAD_IMAGE = 1;
	
	public EditLocation()
	{
		
	}
	
	public EditLocation(Context context) 
	{
        this.context = context;
    }
    
	
	@Override	
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);	
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setContentView(R.layout.location_edit);
	
		final Button LoadImage = (Button) findViewById(R.id.LoadImage);
		final Button DoneEditing = (Button) findViewById(R.id.DoneEditing);
		TextView EditScreenTitle = (TextView) findViewById(R.id.editscreentitle);
		Typeface TradeGothic = Typeface.createFromAsset(getAssets(),"TradeGothic.ttf");
		Typeface TradeGothic18 = Typeface.createFromAsset(getAssets(),"TradeG18.ttf");
		EditScreenTitle.setTypeface(TradeGothic18);
		DoneEditing.setTypeface(TradeGothic);
		LoadImage.setTypeface(TradeGothic);
											
		context = getApplicationContext();
		
		Intent i1 = getIntent();
		Double xLoc = i1.getDoubleExtra("xLocation", 0.0);
		Double yLoc = i1.getDoubleExtra("yLocation", 0.0);
		xLocation = new Double(xLoc).toString();
		yLocation = new Double(yLoc).toString();
		//FILENAME = xLocation+"."+yLocation+"."+"DATA.txt";
		
		// Begin another syncTask to browse and attach an image file
		LoadImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LoadImageAsync loadImageTask = new LoadImageAsync();
				loadImageTask.execute();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.main, menu);
			return true;
	}
	
	//called when user clicks on Done Editing button i
	public void DoneEditing(View view) 
	{
		EditText titleedit = (EditText) findViewById(R.id.titleedit);
		title=titleedit.getText().toString();
		EditText descriptionedit = (EditText) findViewById(R.id.descriptionedit);
		description=descriptionedit.getText().toString();
		
	    StringBuilder builder = new StringBuilder("");
	    
		if(loadedImage!=null)
		{
			String image = BitMapToString(loadedImage);
			//finaloutput=xLocation+"\n"+yLocation+"\n"+title+"\n"+description+"\n"+image;
			builder.append(xLocation);
			builder.append("\n");
			builder.append(yLocation);
			builder.append("\n");
			builder.append(title);
			builder.append("\n");
			builder.append(description);
			builder.append("\n");
			builder.append(image);
			
			//WritetoFile(xLocation);
			//WritetoFile(yLocation);
			//WritetoFile(title);
			//WritetoFile(description);
			//WritetoFile(image);
		}
		else
		{
			//WritetoFile(xLocation);
			//WritetoFile(yLocation);
			//WritetoFile(title);
			//WritetoFile(description);
			//finaloutput=xLocation+"\n"+yLocation+"\n"+title+"\n"+description;
			builder.append(xLocation);
			builder.append("\n");
			builder.append(yLocation);
			builder.append("\n");
			builder.append(title);
			builder.append("\n");
			builder.append(description);
			
			
		}
		WritetoFile(builder.toString());
		Toast.makeText(context.getApplicationContext(), "Location Saved", Toast.LENGTH_LONG).show();
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
					// TODO Auto-generated catch block
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
	/*public Bitmap StringToBitMap(String encodedString) {
		try {
			byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
			Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
					encodeByte.length);
			return bitmap;
		} catch (Exception e) {
			e.getMessage();
			return null;
		}
	}*/
	
	public static String getFilename() 
	{
		return FILENAME;
	}
	
	private void WritetoFile(String output) 
	 {
		//String textToSaveString = "Testing Token Method";
		 //StringTokenizer st = new StringTokenizer(output);

		 try {
			 OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(FILENAME, Context.MODE_APPEND));
			 //outputStreamWriter.write("Location Test");
			 //while (st.hasMoreElements()) {
				 //outputStreamWriter.write(st.nextElement().toString()+ "\n");
			 //}
			 outputStreamWriter.write(output);
			 //Toast.makeText(context.getApplicationContext(), "writing: " + output, Toast.LENGTH_LONG).show();
			 outputStreamWriter.close();
			 Toast.makeText(context.getApplicationContext(), "Location Saved", Toast.LENGTH_LONG).show();
		 }
		 catch (IOException e) {
			 Log.e(TAG, "File write failed: " + e.toString());
		 }

		 //WRITE SPLIT WITH COMMA
		 //System.out.println("---- Split by comma ',' ------");
		 //StringTokenizer st2 = new StringTokenizer(str, ",");

		 //while (st2.hasMoreElements()) {
		 //	System.out.println(st2.nextElement());
		 //}
	 }
	 
}

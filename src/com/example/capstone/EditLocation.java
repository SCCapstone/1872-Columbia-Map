package com.example.capstone;

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
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

public class EditLocation extends Activity {
	
	//test
	private static final String FILENAME = "DataFile.txt";
	private static String TAG = "Location DATA";
	private Context context;
	private String finaloutput,title,description;
	
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
	
		final Button DoneEditing = (Button) findViewById(R.id.DoneEditing);
		TextView EditScreenTitle = (TextView) findViewById(R.id.editscreentitle);
		Typeface TradeGothic = Typeface.createFromAsset(getAssets(),"TradeGothic.ttf");
		Typeface TradeGothic18 = Typeface.createFromAsset(getAssets(),"TradeG18.ttf");
		EditScreenTitle.setTypeface(TradeGothic18);
		DoneEditing.setTypeface(TradeGothic);
											
		context = getApplicationContext();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.main, menu);
			return true;
	}
	
	//called when user clicks on Done Editing button
	public void DoneEditing(View view) 
	{
		EditText titleedit = (EditText) findViewById(R.id.titleedit);
		title=titleedit.getText().toString();
		EditText descriptionedit = (EditText) findViewById(R.id.descriptionedit);
		description=descriptionedit.getText().toString();
		finaloutput=title+description;
		WritetoFile(finaloutput);
		Toast.makeText(context.getApplicationContext(), "Location Saved", Toast.LENGTH_LONG).show();
	}
	
	
	public static String getFilename() 
	{
		return FILENAME;
	}
	
	private void WritetoFile(String output) 
	 {
		//String textToSaveString = "Testing Token Method";
		 StringTokenizer st = new StringTokenizer(output);

		 try {
			 OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(FILENAME, Context.MODE_APPEND));
			 //outputStreamWriter.write("Location Test");
			 while (st.hasMoreElements()) {
				 outputStreamWriter.write(st.nextElement().toString()+ "\n");
			 }
			
			 Toast.makeText(context.getApplicationContext(), "writing: " + output, Toast.LENGTH_LONG).show();
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

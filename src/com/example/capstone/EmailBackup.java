package com.example.capstone;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.Scanner;
import java.util.StringTokenizer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * @author Olga Agafonova
 * March 2, 2014
 * 
 * This class enables the user to send a message with or without an attachment. 
 * Right now, the attached file is written and read in this class. In the future,
 * it should be able to retrieve the file using the read and write methods in SubsamplingScaleImageView
 * (even if SubsamplingScaleImageView was not accessed first).
 */

public class EmailBackup extends Activity 
{

	private String email = "";
	private String subject = "";
	private String body = "";
	private static String fileName = "";
	
	private EditText emailEdit;
	private EditText textSubject;
	private EditText textMessage;
	private static Context context;
	private CheckBox checkAttachment;
	private CheckBox deleteLayoutFile;
	private FileInputStream inputStream; 
	private static String SEND_EMAIL = "SEND EMAIL";
	
	Mail m = new Mail("capstone1872@gmail.com", "woodrowwilson");
	private static int RESULT_LOAD_IMAGE = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_email_backup);
		
		context = getApplicationContext();		
		fileName = SubsamplingScaleImageView.getFilename();
		
		if (fileName == null)
		{
			//if no file has been written in SubsamplingScaleImageView
			//then create a dummy file
		    fileName = "DataFile.txt";
		}

		ConnectionDetector cd = new ConnectionDetector(context);
		boolean isInternetPresent = cd.isConnectingToInternet(); 
		
		if (isInternetPresent == true)
		{
			Toast.makeText(EmailBackup.this, "The Internet is avaiable", Toast.LENGTH_LONG).show();
		}
		else
		{
			Toast.makeText(EmailBackup.this, "The Internet is not available", Toast.LENGTH_LONG).show();
		}
		
	    final Button sendEmail = (Button) findViewById(R.id.Send);
	    Typeface TradeGothic = Typeface.createFromAsset(getAssets(),"TradeGothic.ttf");
		sendEmail.setTypeface(TradeGothic);
		
		final Button loadImage = (Button) findViewById(R.id.LoadImage);
		loadImage.setTypeface(TradeGothic);
		
		emailEdit = (EditText) findViewById(R.id.emailedit);	
		textSubject = (EditText) findViewById(R.id.editTextSubject);
		textMessage = (EditText) findViewById(R.id.editTextMessage);
		
		//Checkbox to delete the default layout file
		deleteLayoutFile = (CheckBox) findViewById(R.id.deleteLayoutFile);
		
		deleteLayoutFile.setOnClickListener(new OnClickListener() 
		{
	 
		  @Override
		  public void onClick(View v) 
		  {
	        //is the box checked?
			if (((CheckBox) v).isChecked())
			{
				//Delete the previous version of the file if it exists by overwriting
				//it with a blank file
		        File file = new File("/data/data/com.example.capstone/files/DataFile.txt.");
		        
				if(file.exists() == true)
				{
				   file.delete();
				   Toast.makeText(EmailBackup.this, "File deleted", Toast.LENGTH_SHORT).show();
				}
				else
				{
					Toast.makeText(EmailBackup.this, "File does not exist", Toast.LENGTH_SHORT).show();
				}
			}
	 
		  }
		  
		});
				
		//Checkbox asking user if he/she wants to attach the default layout file (which
		//is just DataFile.txt for now)
		checkAttachment = (CheckBox) findViewById(R.id.checkAttachment);
		 
		checkAttachment.setOnClickListener(new OnClickListener() 
		{
	 
		  @Override
		  public void onClick(View v) 
		  {
	        //is the box checked?
			if (((CheckBox) v).isChecked())
			{
				attachFile();
			}
	 
		  }
		});
			
		//Begin asyncTask to send email
		sendEmail.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
			    EmailBackupAsync emailTask = new EmailBackupAsync();
			    emailTask.execute();
			}
		});
		
		//Begin another syncTask to browse and attach an image file
		loadImage.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
			    LoadImageAsync loadImageTask = new LoadImageAsync();
			    loadImageTask.execute();
			}
		});
		
	}

	 /**
	  * This method overwrites the existing file and attaches a new one 
	  */
	 private void attachFile() 
	{
		
		//Write a test file
		//The default file name is DataFile.txt
//		String dummyInput = "1234 \n 5678 \n 8910";
//		WriteFile(dummyInput);

		readFile();
    	
		//Attach a file
		try 
		{
			//path:/data/data/com.example.capstone/files/somefile.txt.
			m.addAttachment(context.getFilesDir().getPath()+"/"+SubsamplingScaleImageView.getFilename());
			Toast.makeText(EmailBackup.this, "File attached successfully", Toast.LENGTH_SHORT).show();
			
		} 
		catch (Exception e) 
		{
			Toast.makeText(EmailBackup.this, "Failed to attach file", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	 private void readFile() {
		//read from file on the path /data/data/com.example.capstone/files/somefile.txt
		try 
		{
			inputStream = context.openFileInput(fileName);
			Toast.makeText(EmailBackup.this, "File read successfully", Toast.LENGTH_SHORT).show();
			
		} catch (FileNotFoundException e1) 
		{
			Toast.makeText(EmailBackup.this, "File was not read", Toast.LENGTH_SHORT).show();
			e1.printStackTrace();
		}
	}
	
	 private class EmailBackupAsync extends AsyncTask<Void, Void, Void> 

	 {
		    @Override
		    protected Void doInBackground(Void...params) 
		    {
		    	email = emailEdit.getText().toString();	
		    	
				subject = textSubject.getText().toString();
				body = textMessage.getText().toString();

				String[] toArr = {email}; 
				Log.v(SEND_EMAIL, email);
				
				m.set_to(toArr); 
				m.set_from("capstone1872@gmail.com"); 
				m.set_subject(subject); 
				m.setBody(body); 
							 
		        try 
		        {
					if(m.send())
					{ 
				     
					  
					  EmailBackup.this.runOnUiThread(new Runnable()
						{
							  @Override
							  public void run() 
							  {
							      Toast.makeText(EmailBackup.this, "Sending email", Toast.LENGTH_SHORT).show();
							  }
					     });
					 
					} 
					else 
					{ 

					  Log.v(SEND_EMAIL, "email not sent");
					  
					  EmailBackup.this.runOnUiThread(new Runnable()
						{
							  @Override
							  public void run() 
							  {

							      Toast.makeText(EmailBackup.this, "Unable to send email", Toast.LENGTH_SHORT).show();
							  }
					     });
					}
				} 
		        catch (Exception e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}       
		    	
		    	return null;
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
					  EmailBackup.this.runOnUiThread(new Runnable()
						{
							  @Override
							  public void run() 
							  {
							      Toast.makeText(EmailBackup.this, "Loading image", Toast.LENGTH_SHORT).show();
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
	         Bitmap loadedImage = BitmapFactory.decodeFile(picturePath);
	         ImageView imageView = (ImageView) findViewById(R.id.imageView1);
	         
	         if(loadedImage != null)
	         {
	        	 imageView.setImageBitmap(loadedImage);	 
	        	 Toast.makeText(EmailBackup.this, "Image loaded", Toast.LENGTH_SHORT).show();
	         }
	         
	         Toast.makeText(EmailBackup.this, "Image not loaded", Toast.LENGTH_SHORT).show();
	     }
	     
}
	 	 
     public static void writeFile(String output) 
	 {
		 //String textToSaveString = "Testing Token Method";
		 StringTokenizer st = new StringTokenizer(output);

		 try {
			 
			 //Default location is /data/data/com.example.capstone/files/somefile.txt			 
			 OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(fileName, 0));
			 
			 while (st.hasMoreElements()) 
			 {
				 outputStreamWriter.write(st.nextElement().toString()+ "\n");
			 }
			 
			 //Toast.makeText(context.getApplicationContext(), "Writing: " + output, Toast.LENGTH_LONG).show();
			 outputStreamWriter.flush();
			 outputStreamWriter.close();
		 }
		 catch (IOException e) 
		 {
			 Log.e(SEND_EMAIL, "Failed to write file: " + e.toString());
		 }
	 }
	 	
}






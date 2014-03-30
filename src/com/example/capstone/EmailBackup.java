package com.example.capstone;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
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
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
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
 * Last edited on March 29, 2014
 * 
 * This class enables the user to send a message with or without an attachment. 
 */

public class EmailBackup extends Activity 
{
	private String email = "";
	private String subject = "";
	private String body = "";
	private static File layoutFile;
	private static String fileName = "";
	private static String imageFileDir = "";
	private EditText emailEdit;
	private EditText textSubject;
	private EditText textMessage;
	private static Context context;
	private CheckBox checkAttachment;
	private CheckBox attachImage;
	private static String SEND_EMAIL = "SEND EMAIL";
	private Bitmap loadImage;
		
	Mail m = new Mail("capstone1872@gmail.com", "woodrowwilson");
	private static int RESULT_LOAD_IMAGE = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_email_backup);
		
		context = getApplicationContext();
		
		fileName = context.getFilesDir().getPath()+"/"+EditLocation.getFilename();//default image path: /mnt/sdcard/Pictures/
		
		imageFileDir = Environment.getExternalStoragePublicDirectory(
	            Environment.DIRECTORY_PICTURES).toString()+"/";
				
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
				attachFile(fileName);
			}
	 
		  }
		});
		
		attachImage = (CheckBox) findViewById(R.id.attachImage);
		attachImage.setOnClickListener(new OnClickListener()
		{
			  @Override
			  public void onClick(View v) 
			  {
		        //is the box checked?
				if (((CheckBox) v).isChecked())
				{
					fileName = imageFileDir;
					attachFile(imageFileDir);
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
	 private void attachFile(String fileToAttach) 
	{	    	
		try 
		{
			//default text file path:/data/data/com.example.capstone/files/somefile.txt.
			m.addAttachment(fileToAttach);
			Toast.makeText(EmailBackup.this, fileToAttach, Toast.LENGTH_SHORT).show();
			
		} 
		catch (Exception e) 
		{
			Toast.makeText(EmailBackup.this, "Failed to attach file", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
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
					//TODO Auto-generated catch block
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
	         imageFileDir = cursor.getString(columnIndex);
	         cursor.close();
	         
             //These lines load the image into an image view window
	         loadImage = BitmapFactory.decodeFile(imageFileDir);
	         ImageView imageView = (ImageView) findViewById(R.id.imageView1);
	         
	         if(loadImage != null)
	         {
	        	 imageView.setImageBitmap(loadImage);	 
	         }
	         else if(loadImage == null)
	         {
	        	 Toast.makeText(EmailBackup.this, "Image not loaded", Toast.LENGTH_SHORT).show();
	         }
	     }     
}
	 	 
     /**
	 * @return the loadImage
	 */
	public Bitmap getLoadImage() 
	{
		return loadImage;
	}

	/**
	 * @param loadImage the loadImage to set
	 */
	public void setLoadImage(Bitmap loadImage) 
	{
		this.loadImage = loadImage;
	}

	public static void writeFile() 
	 {
 		 String dummyInput = "0000 \n 1111 \n 2222";
		 
 		 try 
		 {	 
			 
				FileWriter fw = new FileWriter(layoutFile.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(dummyInput);
				bw.close();
			 
			    Toast.makeText(context.getApplicationContext(), "Writing: " + dummyInput, Toast.LENGTH_SHORT).show();
			
		 }
		 catch (IOException e) 
		 {
			 Log.e(SEND_EMAIL, "Failed to write file: " + e.toString());
		 }
	 }
}






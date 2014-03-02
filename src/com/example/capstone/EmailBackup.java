package com.example.capstone;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.Scanner;
import java.util.StringTokenizer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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
	private String path = "";
  	private InputStream inputStream;
	
	private EditText emailEdit;
	private EditText textSubject;
	private EditText textMessage;
	private static Context context;
	private CheckBox checkAttachment;
	private static String SEND_EMAIL = "SEND EMAIL";
	
	Mail m = new Mail("capstone1872@gmail.com", "woodrowwilson"); 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_email_backup);
		
		context = getApplicationContext();
		path = context.getFilesDir().getPath()+"/";
		
		//This causes a null pointer now and we need to think about SubsamplingScaleImageView
		//and EmailBackup talk to each other
		//fileName = path+SubsamplingScaleImageView.getFilename();
	    fileName = "DataFile.txt";
		
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
	}

	 /**
	  * This method overwrites the existing file and attaches a new one 
	  */
	 private void attachFile() 
	{
		//Delete the previous version of the file if it exists
        File file = new File("/data/data/com.example.capstone/files/DataFile.txt.");
		if(file.exists())
		{
		   file.delete();
		}
		
		//Write a test file
		//The default file name is DataFile.txt
		String dummyInput = "Sample input";
		WriteFile(dummyInput);
		
		//read from file on the path /data/data/com.example.capstone/files/somefile.txt
		try 
		{
			inputStream = context.openFileInput(fileName);
			Toast.makeText(EmailBackup.this, "File read successfully", Toast.LENGTH_LONG).show();
			
		} catch (FileNotFoundException e1) 
		{
			Toast.makeText(EmailBackup.this, "File was not read", Toast.LENGTH_LONG).show();
			e1.printStackTrace();
		}
		
     	Scanner scanner = new Scanner(inputStream);
		//ReadFile(scanner);
     	
		//Attach a file
		try 
		{
			//path:/data/data/com.example.capstone/files/somefile.txt.
			m.addAttachment(context.getFilesDir().getPath()+"/"+SubsamplingScaleImageView.getFilename());
			Toast.makeText(EmailBackup.this, "File attached successfully", Toast.LENGTH_LONG).show();
			
		} 
		catch (Exception e) 
		{
			Toast.makeText(EmailBackup.this, "Failed to attach file", Toast.LENGTH_LONG).show();
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
					// TODO Auto-generated catch block
					e.printStackTrace();
				}       
		    	
		    	return null;
		    }
	 }
	 
     public static void WriteFile(String output) 
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
	 
	 //Leave this here for now: not yet sure how we will read files
     private String ReadFile(Scanner scanner) 
     {
    	 String input = "";
    	 
             	if(scanner.hasNext()) 
             	{
             		input = scanner.next();
             		scanner.next();
             		
                 	//Toast.makeText(context.getApplicationContext(), "Reading: " + input, Toast.LENGTH_LONG).show();
             	}
             	//scanner.close();
     	
		return input;
     }
	
}

package com.example.capstone;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EmailBackup extends Activity 
{

	private String email = "";
	private String subject = "";
	private String body = "";
	
	private EditText emailEdit;
	private EditText textSubject;
	private EditText textMessage;
	private String SEND_EMAIL = "SEND EMAIL";
	
	Mail m = new Mail("capstone1872@gmail.com", "woodrowwilson"); 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_email_backup);
					
	    final Button sendEmail = (Button) findViewById(R.id.Send);
	    Typeface TradeGothic = Typeface.createFromAsset(getAssets(),"TradeGothic.ttf");
		sendEmail.setTypeface(TradeGothic);
		
		emailEdit = (EditText) findViewById(R.id.emailedit);
		textSubject = (EditText) findViewById(R.id.editTextSubject);
		textMessage = (EditText) findViewById(R.id.editTextMessage);
		
		email = emailEdit.getText().toString();
		Log.v(SEND_EMAIL, email);
		
		subject = textSubject.getText().toString();
		Log.v(SEND_EMAIL, subject);
		
		body = textMessage.getText().toString();
		Log.v(SEND_EMAIL, body);
		
		//this need to be moved to onClick once I verify emails are being sent
		EmailBackupAsync emailTask = new EmailBackupAsync();
		emailTask.execute();
		
		sendEmail.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
			    //EmailBackupAsync emailTask = new EmailBackupAsync();
			    //emailTask.execute();
			}
		});
	}
	
	 private class EmailBackupAsync extends AsyncTask<Void, Void, Void> 
	 {
		    @Override
		    protected Void doInBackground(Void...params) 
		    {
		    	
				String[] toArr = {"capstone1872@gmail.com", "miohoya@gmail.com"}; 
				Log.v(SEND_EMAIL, email);
				
				m.set_to(toArr); 
				m.set_from("capstone1872@gmail.com"); 
				m.set_subject("test"); 
				m.setBody("test message"); 
				
				//m.addAttachment("/sdcard/filelocation"); 			 
		        try 
		        {
					if(m.send())
					{ 
				  
					  Log.v(SEND_EMAIL, "email sent");
					} 
					else 
					{ 

					  Log.v(SEND_EMAIL, "email not sent");
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
}

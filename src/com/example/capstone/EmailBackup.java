package com.example.capstone;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EmailBackup extends Activity 
{

	private String email = "";
	private String subject = "";
	private String body = "";
	private String testEmail = "capstone1872@gmail.com";
	
	private EditText emailEdit;
	private EditText textSubject;
	private EditText textMessage;
	private static Context context;
	private String SEND_EMAIL = "SEND EMAIL";
	
	Mail m = new Mail("capstone1872@gmail.com", "woodrowwilson"); 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_email_backup);
		
		context = getApplicationContext();
					
		ConnectionDetector cd = new ConnectionDetector(context);
		boolean isInternetPresent = cd.isConnectingToInternet(); 
		
		Toast.makeText(EmailBackup.this, "Internet available?  " + isInternetPresent, Toast.LENGTH_LONG).show();
		
	    final Button sendEmail = (Button) findViewById(R.id.Send);
	    Typeface TradeGothic = Typeface.createFromAsset(getAssets(),"TradeGothic.ttf");
		sendEmail.setTypeface(TradeGothic);
		
		emailEdit = (EditText) findViewById(R.id.emailedit);	
		textSubject = (EditText) findViewById(R.id.editTextSubject);
		textMessage = (EditText) findViewById(R.id.editTextMessage);
				
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
				
				//m.addAttachment("/sdcard/filelocation"); 			 
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
}

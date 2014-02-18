package com.example.capstone;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

public class AdminLogin extends Activity {

	//public final int popupWidth = 700;    
	//public final int popupHeight = 700;
	//public boolean popupon = false;	
	public String admin_username = "admin";
	public String username = null;
	public String admin_password = "0000";
	public String password = null;
	
	@Override	
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);	
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setContentView(R.layout.login_main);
	
		//final Button AdminPanel = (Button) findViewById(R.id.AdminPanel);
		TextView LoginTitle = (TextView) findViewById(R.id.logintitle);
		Typeface TradeGothic = Typeface.createFromAsset(getAssets(),"TradeGothic.ttf");
		Typeface TradeGothic18 = Typeface.createFromAsset(getAssets(),"TradeG18.ttf");
		LoginTitle.setTypeface(TradeGothic18);
		//AdminPanel.setTypeface(TradeGothic);
											
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.main, menu);
			return true;
	}
	
	//called when user clicks on Login
		public void AdminPanel(View view) {
			EditText usernameedit = (EditText)findViewById(R.id.usernameedit);
			username = usernameedit.getText().toString(); 
			EditText passwordedit = (EditText)findViewById(R.id.passwordedit);
			password = passwordedit.getText().toString(); 
			if(admin_username.equals(username) && admin_password.equals(password)){
				Intent intent = new Intent(this, LocationListActivity.class);
		    	startActivity(intent);
        	} else {
        		Context context = getApplicationContext();
        		CharSequence text = "Incorrect Credentials";
        		int duration = Toast.LENGTH_SHORT;
        		Toast toast = Toast.makeText(context, text, duration);
        		toast.show();
        	}
		}
}

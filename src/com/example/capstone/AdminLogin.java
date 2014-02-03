package com.example.capstone;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AdminLogin extends Activity {

	public final int popupWidth = 700;    
	public final int popupHeight = 700;
	public boolean popupon = false;	
	
	@Override	
	protected void onCreate(Bundle savedInstanceState) 
	{
	super.onCreate(savedInstanceState);	
	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	setContentView(R.layout.login_main);

	TextView LoginTitle = (TextView) findViewById(R.id.logintitle);
	TextView UserName = (TextView) findViewById(R.id.username);
	TextView Password = (TextView) findViewById(R.id.password);
	EditText UserNameEdit = (EditText) findViewById(R.id.usernameedit);
	EditText PasswordEdit = (EditText) findViewById(R.id.passwordedit);
	Button login = (Button) findViewById(R.id.login);
	Button createlogin = (Button) findViewById(R.id.createlogin);
	Typeface TradeGothic18 = Typeface.createFromAsset(getAssets(),"TradeG18.ttf");
	UserName.setTypeface(TradeGothic18);
	Password.setTypeface(TradeGothic18);
	UserNameEdit.setTypeface(TradeGothic18);
	PasswordEdit.setTypeface(TradeGothic18);
	login.setTypeface(TradeGothic18);
	LoginTitle.setTypeface(TradeGothic18);
											
	createlogin.setOnClickListener(new OnClickListener() 
	{
		@Override     
		public void onClick(View view) 
		{						
			//start the CreateAdminLogin activity
			Intent adminIntent = new Intent(AdminLogin.this, CreateAdminLogin.class);
			startActivity(adminIntent);
			
			Log.d("AdminLogin", "After intent");
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
}

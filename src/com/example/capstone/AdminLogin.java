package com.example.capstone;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AdminLogin extends Activity {

	
	@Override	
	protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	setContentView(R.layout.login_main);

	TextView LoginTitle = (TextView) findViewById(R.id.logintitle);
	TextView UserName = (TextView) findViewById(R.id.username);
	TextView Password = (TextView) findViewById(R.id.password);
	EditText UserNameEdit = (EditText) findViewById(R.id.usernameedit);
	EditText PasswordEdit = (EditText) findViewById(R.id.passwordedit);
	Button login = (Button) findViewById(R.id.login);
	Typeface TradeGothic18 = Typeface.createFromAsset(getAssets(),"TradeG18.ttf");
	UserName.setTypeface(TradeGothic18);
	Password.setTypeface(TradeGothic18);
	UserNameEdit.setTypeface(TradeGothic18);
	PasswordEdit.setTypeface(TradeGothic18);
	login.setTypeface(TradeGothic18);
	LoginTitle.setTypeface(TradeGothic18);
	
	
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}

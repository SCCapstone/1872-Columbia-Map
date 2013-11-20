package com.example.capstone;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

public class AdminLogin extends Activity {

	
	@Override	
	protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	setContentView(R.layout.login_main);

	TextView UserName = (TextView) findViewById(R.id.username);
	TextView Password = (TextView) findViewById(R.id.password);
	Typeface TradeGothicReg = Typeface.createFromAsset(getAssets(),"TradeGothic.ttf");
	UserName.setTypeface(TradeGothicReg);
	Password.setTypeface(TradeGothicReg);
	
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}

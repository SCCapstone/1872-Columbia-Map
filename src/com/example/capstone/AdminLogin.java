package com.example.capstone;

import com.example.capstone.GoogleLoginActivity;

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
		Typeface TradeGothic18 = Typeface.createFromAsset(getAssets(),"TradeG18.ttf");
		LoginTitle.setTypeface(TradeGothic18);
											
	}
	public void launchGoogleLogin(View v)
	{
	        AdminLogin.this.startActivity(new Intent(AdminLogin.this, GoogleLoginActivity.class));
	        AdminLogin.this.finish();
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.main, menu);
			return true;
	}
}

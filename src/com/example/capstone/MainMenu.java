package com.example.capstone;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.LightingColorFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainMenu extends Activity {

	@Override	
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	setContentView(R.layout.menu_main);
	
	Button AdminLogin = (Button) findViewById(R.id.AdminLogin);
	Button ViewMap = (Button) findViewById(R.id.ViewMap);
	TextView MainTitle = (TextView) findViewById(R.id.maintitle);
	TextView MapTitle = (TextView) findViewById(R.id.maptitle);
	Typeface TradeGothic18 = Typeface.createFromAsset(getAssets(),"TradeG18.ttf");
	Typeface TradeGothicBold2 = Typeface.createFromAsset(getAssets(),"Trade Gothic Bold No. 2.ttf");
	MainTitle.setTypeface(TradeGothicBold2);
	MapTitle.setTypeface(TradeGothic18);
	ViewMap.setTypeface(TradeGothic18);
	AdminLogin.setTypeface(TradeGothic18);
	AdminLogin.getBackground().setColorFilter(new LightingColorFilter(0x000000, 0xFFC72C));
	ViewMap.getBackground().setColorFilter(new LightingColorFilter(0x000000, 0xD22630));
	
	
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	//called when user clicks on ViewMap
	public void ViewMap(View view) {
    	Intent intent = new Intent(this, MainActivity.class);
    	startActivity(intent);
	}
	
	//called when user clicks on Admin Login
	public void AdminLogin(View view) {
    	Intent intent = new Intent(this, AdminLogin.class);
    	startActivity(intent);
	}
}

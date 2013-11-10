package com.example.capstone;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class MainMenu extends Activity {

	@Override	
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.menu_main);
	
	
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
	
//	public void AdminLogin(View view) {
//    	Intent intent = new Intent(this, MainActivity.class);
//    	startActivity(intent);
//	}
}

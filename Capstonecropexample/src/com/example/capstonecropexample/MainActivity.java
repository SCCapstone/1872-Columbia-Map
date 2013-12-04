package com.example.capstonecropexample;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

public class MainActivity extends Activity 
{   
	//Create the "x" and "y" positions of the Buttons.
	Point button1p;
	Point button2p;
	Point button3p;
	Point button4p;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);    
		setContentView(R.layout.activity_main);      
		
		ImageButton btn1_show = (ImageButton) findViewById(R.id.show_popup1); 
		btn1_show.setOnClickListener(new OnClickListener() 
		{      
			@Override     
			public void onClick(View arg0) 
			{	
				//Open popup window        
				if (button1p != null)        
					showPopup(MainActivity.this, button1p);				
			}			
		}); 
		
		ImageButton btn2_show = (ImageButton) findViewById(R.id.show_popup2);
		btn2_show.setOnClickListener(new OnClickListener() 
		{      
			@Override     
			public void onClick(View arg0) 
			{	
				//Open popup window        
				if (button2p != null)        
					showPopup(MainActivity.this, button2p);				
			}			
		});
		
		/* Button 3 created and on click listener added */
		
		ImageButton btn3_show = (ImageButton) findViewById(R.id.show_popup3);
		btn3_show.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				if(button3p != null)
					showPopup(MainActivity.this, button3p);
			}
		});
		
		/* Button 4 creates and on click listener added */
		
		ImageButton btn4_show = (ImageButton) findViewById(R.id.show_popup4);
		btn4_show.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				if(button4p != null)
					showPopup(MainActivity.this, button4p);
			}
		});
		
			
	}
	
	// Get the x and y position after the button is draw on screen 
	// (It's important to note that we can't get the position in the onCreate(), 
	// because at that stage most probably the view isn't drawn yet, so it will return (0, 0)) 
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) 
	{      
		int[] location = new int[2];    
		ImageButton button1 = (ImageButton) findViewById(R.id.show_popup1);      
		
		// Get the x, y location and store it in the location[] array    
		// location[0] = x, location[1] = y.    
		button1.getLocationOnScreen(location);      
		
		//Initialize the Point with x, and y positions    
		button1p = new Point();    
		button1p.x = location[0];    
		button1p.y = location[1]; 
		
		ImageButton button2 = (ImageButton) findViewById(R.id.show_popup2);
		button2.getLocationOnScreen(location);    
		button2p = new Point();    
		button2p.x = location[0];    
		button2p.y = location[1];	
		
		ImageButton button3 = (ImageButton) findViewById(R.id.show_popup3);
		button3.getLocationOnScreen(location);
		button3p = new Point();
		button3p.x = location[0];
		button3p.y = location[1];
		
		ImageButton button4 = (ImageButton) findViewById(R.id.show_popup4);
		button4.getLocationOnScreen(location);
		button4p = new Point();
		button4p.x = location[0];
		button4p.y = location[1];
		
	}   
	
	// The method that displays the popup. 
	private void showPopup(final Activity context, Point p) 
	{    
		int popupWidth = 600;    
		int popupHeight = 500;
		
		// Determine the correct pop-up (button) that needs inflating		
		int temp = 0;		
		
		if (p==button1p)
			temp = R.id.popup1;
		else if (p==button2p)
			temp = R.id.popup2;
		else if (p==button3p)
			temp = R.id.popup3;
		else if (p==button4p)
			temp = R.id.popup4;
		
		
		// Inflate part 1
		LinearLayout viewGroup = (LinearLayout)context.findViewById(temp);
		
		LayoutInflater layoutInflater = (LayoutInflater) context      
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// Inflate part 2
		temp = 0;
		if (p==button1p)
			temp = R.layout.popup_layout1;
		else if (p==button2p)
			temp = R.layout.popup_layout2;
		else if (p==button3p)
			temp = R.layout.popup_layout3;
		else if (p==button4p)
			temp = R.layout.popup_layout4;
		
		
		//View layout = layoutInflater.inflate(R.layout.popup_layoutx, viewGroup);		
		View layout = layoutInflater.inflate(temp, viewGroup);
				
		// Creating the PopupWindow    
		final PopupWindow popup = new PopupWindow(context);    
		popup.setContentView(layout);    
		popup.setWidth(popupWidth);    
		popup.setHeight(popupHeight);    
		popup.setFocusable(true);      
		
		// Offset: x aligns the popup to the right, y down, relative to button's position.
		int OFFSET_X = -160;    
		int	OFFSET_Y = 35;      
		
		// Clear the default translucent background    
		popup.setBackgroundDrawable(new BitmapDrawable());      
		
		// Displaying the popup at the specified location, + offsets.    
		popup.showAtLocation(layout, Gravity.NO_GRAVITY, p.x + OFFSET_X, p.y + OFFSET_Y);      
		
		// Getting a reference to Close button, and close the popup when clicked.    
		Button close = (Button) layout.findViewById(R.id.close);    
		close.setOnClickListener(new OnClickListener() 
		{   	
			@Override     
			public void onClick(View v) 
			{				  
				popup.dismiss();      
			}    
		}); 
	} 
}
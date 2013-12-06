package com.example.capstone;

import android.annotation.SuppressLint;
import android.app.Activity;
//import android.app.AlertDialog;
import android.content.pm.ActivityInfo;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
//import android.os.Handler;
import android.util.FloatMath;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;
//import android.widget.Toast;

@SuppressWarnings("deprecation")
public class MainActivity extends Activity 
{	
	ImageView imageDetail;
    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;

    private static final float MIN_ZOOM = 0.5f;
    private static final float MAX_ZOOM = 4f;

    private Matrix matrix = new Matrix();
    private Matrix savedMatrix = new Matrix();

    private PointF start = new PointF();
    private PointF mid = new PointF();

    private int mode = NONE;
    private float oldDistance = 1f;
    private float dx; // postTranslate X distance
	private float dy; // postTranslate Y distance
	private float[] matrixValues = new float[9];
	float matrixX = 0; // X coordinate of matrix inside the ImageView
	float matrixY = 0; // Y coordinate of matrix inside the ImageView
	float width = 0; // width of draw-able
	float height = 0; // height of draw-able
	public int[] USC_loc = {1567, 1047}; //locations for this map resolution
	public int[] Statehouse_loc = {1257, 966};
	public int[] Church_loc = {1371, 805};
	public int[] WWFH_loc = {1556, 650};
	public int lowprecision = 50; //for big buildings
	public int highprecision = 30; //for small buildings
	public final int popupWidth = 700;    
	public final int popupHeight = 700;
	public boolean popupon = false;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		//this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setContentView(R.layout.activity_main);		
		
		imageDetail = (ImageView) findViewById(R.id.Map);
		
		
		/** * set on touch listener on image */
		imageDetail.setOnTouchListener(new View.OnTouchListener() 
		{
			@Override
			public boolean onTouch(View view, MotionEvent event)
			{
				
				ImageView imageView = (ImageView)view;				
				Matrix inverse = new Matrix();
				imageView.getImageMatrix().invert(inverse);
				
				//getX and getY will return the touch location in the ImageView's
				//coordinate system. The inverse matrix of the coordinate system
				//is the point within the image.
				
				if (!popupon) //prevents multi-popup error
				{					
					float[] touchPoint = new float[] {event.getX(), event.getY()};
					inverse.mapPoints(touchPoint);
					//touchPoint now contains x and y in image's coordinate system.

        		
					//pointCheck(int X_touched, int Y_touched, int locs[], int precision)
					if (pointCheck(touchPoint[0], touchPoint[1], USC_loc, lowprecision))
					{
						popupon=true;
						//Toast.makeText(getApplicationContext(), "USC", Toast.LENGTH_SHORT).show();
        			
						LayoutInflater layoutInflator = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
						View layout = layoutInflator.inflate(R.layout.popup_layout2, null);

						// Creating the PopupWindow 
						final PopupWindow popup = new PopupWindow(layout, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);        			
						popup.setContentView(layout);    
						popup.setWidth(popupWidth);    
						popup.setHeight(popupHeight);    
						popup.setFocusable(true);        			
						
						// Displaying the pop-up at a specified location
						popup.showAtLocation(layout, Gravity.CENTER, 0,0);
        			
						// Getting a reference to Close button, and close the popup when clicked.    
						Button close = (Button) layout.findViewById(R.id.close);   
						
																
						close.setOnClickListener(new OnClickListener() 
						{
							@Override     
									
							public void onClick(View v) 
							{				  
								popup.dismiss();	
								popupon=false;
							}								
						});						
						
						
													
					}
					else if (pointCheck(touchPoint[0], touchPoint[1], Church_loc, highprecision))
					{
						popupon=true;
						//Toast.makeText(getApplicationContext(), "Church", Toast.LENGTH_SHORT).show();

						LayoutInflater layoutInflator = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
						View layout = layoutInflator.inflate(R.layout.popup_layout4, null);

						// Creating the PopupWindow 
						final PopupWindow popup = new PopupWindow(layout, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);        			
						popup.setContentView(layout);    
						popup.setWidth(popupWidth);    
						popup.setHeight(popupHeight);    
						popup.setFocusable(true);      
        			
						// Clear the default translucent background    
						//popup.setBackgroundDrawable(new BitmapDrawable());
						// Displaying the popup at a specified location
						popup.showAtLocation(layout, Gravity.CENTER, 0,0); 
        			
						// Getting a reference to Close button, and close the popup when clicked.    
						Button close = (Button) layout.findViewById(R.id.close);    
						close.setOnClickListener(new OnClickListener() 
						{   	
							@Override     
							public void onClick(View v) 
							{				  
								popup.dismiss();  
								popupon=false;
							}    
						}); 
					}
					else if (pointCheck(touchPoint[0], touchPoint[1], Statehouse_loc, highprecision))
					{
						popupon=true;
						//testing with toast
						//Toast.makeText(getApplicationContext(), "StateHouse Area", Toast.LENGTH_SHORT).show();

						LayoutInflater layoutInflator = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
						View layout = layoutInflator.inflate(R.layout.popup_layout1, null);

						// Creating the pop-upWindow 
						final PopupWindow popup = new PopupWindow(layout, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);        			
						popup.setContentView(layout);    
						popup.setWidth(popupWidth);    
						popup.setHeight(popupHeight);    
						popup.setFocusable(true);      
						
						// Displaying the pop-up at a specified location
						popup.showAtLocation(layout, Gravity.CENTER, 0, 0);
        			
						// Getting a reference to Close button, and close the popup when clicked.    
						Button close = (Button) layout.findViewById(R.id.close);    
						close.setOnClickListener(new OnClickListener() 
						{   	
							@Override     
							public void onClick(View v) 
							{				  
								popup.dismiss();    
								popupon=false;
							}    
						}); 
        			
					}
					else if (pointCheck(touchPoint[0], touchPoint[1], WWFH_loc, highprecision))
					{
						popupon=true;
						//Toast.makeText(getApplicationContext(), "WWFH", Toast.LENGTH_SHORT).show();
        			
						LayoutInflater layoutInflator = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
						View layout = layoutInflator.inflate(R.layout.popup_layout3, null);

						// Creating the PopupWindow 
						final PopupWindow popup = new PopupWindow(layout, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);        			
						popup.setContentView(layout);    
						popup.setWidth(popupWidth);    
						popup.setHeight(popupHeight);    
						popup.setFocusable(true);      
        									
						// Displaying the pop-up at a specified location
						popup.showAtLocation(layout, Gravity.CENTER, 0, 0);
        			
						// Getting a reference to Close button, and close the popup when clicked.    
						Button close = (Button) layout.findViewById(R.id.close);    
						close.setOnClickListener(new OnClickListener() 
						{   	
							@Override     
							public void onClick(View v) 
							{				  
								popup.dismiss();   
								popupon=false;								
							}    
						});		
					} 
					/*Coordinate Display Test
					else
					{
						String x = String.valueOf(touchPoint[0]);
						String y = String.valueOf(touchPoint[1]);
						Toast.makeText(getApplicationContext(), x+"-"+y+" Nothing is Here ", Toast.LENGTH_SHORT).show();
					}
					*/
				}//end of (!popupon)	
				
				switch(event.getAction() & MotionEvent.ACTION_MASK)
				{				
				case MotionEvent.ACTION_DOWN:	
					if (!popupon)
					{
					savedMatrix.set(matrix);
					start.set(event.getX(), event.getY());
					mode = DRAG;
					}
					break;

				case MotionEvent.ACTION_POINTER_DOWN:
					if (!popupon)
					{
						oldDistance = spacing(event);					
						if(oldDistance > 10f) 
						{
							savedMatrix.set(matrix);
							midPoint(mid, event);
							mode = ZOOM;
						}
					}
					break;

				case MotionEvent.ACTION_UP:
				case MotionEvent.ACTION_POINTER_UP:	
					if (!popupon)
						mode = NONE;					
					break;
					
				case MotionEvent.ACTION_MOVE:	
					if (!popupon)
					{
						if(mode == DRAG) 
						{
							matrix.set(savedMatrix);
							matrix.getValues(matrixValues);
							matrixX = matrixValues[2];
							matrixY = matrixValues[5];
							width = matrixValues[0] * (((ImageView) view).getDrawable().getIntrinsicWidth());
							height = matrixValues[4] * (((ImageView) view).getDrawable().getIntrinsicHeight());
							dx = event.getX() - start.x;
							dy = event.getY() - start.y;
								
							// (7*width/8) and (7*height/8) are modified values
							// image will not translate past 7/8 width or length of image
							//if image will go outside left bound
							if (matrixX + dx < 0 -7*width/8)
								dx = -matrixX -7*width/8;
       
							//if image will go outside right bound
							if(matrixX + dx + width > view.getWidth() + 7*width/8)
								dx = view.getWidth() - matrixX - width + 7*width/8;

							//if image will go outside top bound
							if (matrixY + dy < 0 - 7*height/8)            
								dy = -matrixY - 7*height/8;
							
							//if image will go outside bottom bound
							if(matrixY + dy + height > view.getHeight() + 7*height/8)
								dy = view.getHeight() - matrixY - height + 7*height/8;
        
							matrix.postTranslate(dx, dy);
						}
						else
							if(mode == ZOOM) 
							{
								float newDistance = spacing(event);
								if(newDistance > 10f) 
								{
									matrix.set(savedMatrix);
									float scale = newDistance / oldDistance;
									float[] values = new float[9];
									matrix.getValues(values);
									float currentScale = values[Matrix.MSCALE_X];
									if(scale * currentScale > MAX_ZOOM)
										scale = MAX_ZOOM / currentScale;
									else 
										if (scale * currentScale < MIN_ZOOM)
											scale = MIN_ZOOM / currentScale;
									matrix.postScale(scale, scale, mid.x, mid.y);
								}
							}
					}
					break;
				
				}//end switch
				if (!popupon)
				{
					imageView.setImageMatrix(matrix);
				}
				return true;				
				
				}//end of public boolean onTouch(View view, MotionEvent event)

			
			@SuppressLint("FloatMath")
			private float spacing(MotionEvent event) 
			{
				float x = event.getX(0) - event.getX(1);
				float y = event.getY(0) - event.getY(1);
				return FloatMath.sqrt(x * x + y * y);
			}

			private void midPoint(PointF point, MotionEvent event) 
			{
				point.set((event.getX(0) + event.getX(1)) / 2, (event.getY(0) + event.getY(1)) / 2);
			}

		}); //end of imageDetail.setOnTouchListener(new View.OnTouchListener()
		
	} //end of protected void onCreate(Bundle savedInstanceState) 


	/* Yeah, this code didn't work.  It will be commented out for now. -Charles
	// The method that displays the popup. 
		@SuppressWarnings({ "deprecation"})
		private void showPopup(final Activity context, float x, float y, int popnum) 
		{    
			int popupWidth = 500;    
			int popupHeight = 400;
			
			// Determine the correct pop-up (button) that needs inflating		
			int temp = 0;		
			
			if (popnum == 1)
				temp = R.id.popup1;
			else if (popnum == 2)
				temp = R.id.popup2;
			else if (popnum ==3)
				temp = R.id.popup3;
			else if (popnum ==4)
				temp = R.id.popup4;
			    	
			
			// Inflate part 1
			LinearLayout viewGroup = (LinearLayout)context.findViewById(temp);
			
			LayoutInflater layoutInflater = (LayoutInflater) context      
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			// Inflate part 2
			temp = 0;
			
			if (popnum == 1)
				temp = R.id.popup1;
			else if (popnum == 1)
				temp = R.id.popup2;
			else if (popnum ==3)
				temp = R.id.popup3;
			else if (popnum ==4)
				temp = R.id.popup4;
			
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
			//popup.setBackgroundDrawable(new BitmapDrawable());      
			
			// Displaying the popup at the specified location, + offsets.    
			popup.showAtLocation(layout, Gravity.NO_GRAVITY, (int) x + OFFSET_X, (int) y  + OFFSET_Y);      
			
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
	*/
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	//Input: event.get(X), event.get(Y), int locs[], precision
	//Output: boolean: true- within bounds
	//                 false- out of bounds
	public boolean pointCheck(double X_touched, double Y_touched, int loc[], int precision)
	{
		//former code: 
		//if(event.getX() > 200 && event.getY() > 200 && event.getX() < 250 && event.getY() < 250)
		if ( X_touched > loc[0]-precision && 
				Y_touched > loc[1]-precision && 
				X_touched < loc[0]+precision && 
				Y_touched < loc[1]+precision)
			return true;
		else
			return false;
	}
}//end class MainActivity

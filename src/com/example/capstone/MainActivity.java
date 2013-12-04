package com.example.capstone;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.FloatMath;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

public class MainActivity extends Activity 
{	
	ImageView imageDetail;
    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;

    private static final float MIN_ZOOM = 1f;
    private static final float MAX_ZOOM = 5f;

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
	float width = 0; // width of drawable
	float height = 0; // height of drawable
	
	Point button1p;
	
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
				float[] touchPoint = new float[] {event.getX(), event.getY()};
				inverse.mapPoints(touchPoint);
				//touchPoint now contains x and y in image's coordinate system.
				String x = String.valueOf(touchPoint[0]);
				String y = String.valueOf(touchPoint[1]);
				
				
        		final Handler handler = new Handler();
        		final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        		if(event.getX() > 200 && event.getY() > 200)
        		{
        			showPopup(MainActivity.this, event.getX(), event.getY());
        		}
        		else
        		{
        			builder.setMessage(x+"-"+y);
        		}
        		final AlertDialog dialog = builder.create();
        		dialog.show();
        		handler.postDelayed(new Runnable() {
          		public void run() {
            		dialog.dismiss();    
        		}
        		}, 3000);				

				
				switch(event.getAction() & MotionEvent.ACTION_MASK)
				{				
				case MotionEvent.ACTION_DOWN:
					savedMatrix.set(matrix);
					start.set(event.getX(), event.getY());
					mode = DRAG;
					break;

				case MotionEvent.ACTION_POINTER_DOWN:
					oldDistance = spacing(event);					
					if(oldDistance > 10f) 
					{
						savedMatrix.set(matrix);
						midPoint(mid, event);
						mode = ZOOM;
					}
					break;

				case MotionEvent.ACTION_UP:
				case MotionEvent.ACTION_POINTER_UP:
					mode = NONE;
					break;
				case MotionEvent.ACTION_MOVE:
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
					break;
				
				}//end switch
				
				imageView.setImageMatrix(matrix);
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


	// The method that displays the popup. 
		@SuppressWarnings({ "deprecation"})
		private void showPopup(final Activity context, float x, float y) 
		{    
			int popupWidth = 600;    
			int popupHeight = 500;
			
			// Determine the correct pop-up (button) that needs inflating		
			int temp = 0;		
			
			//if (p==button1p)
				temp = R.id.popup1;
			/*else
				if (p==button2p)
					temp = R.id.popup2;
				
				else
					if (p==button3p)
						temp = R.id.popup3;
			    */
			
			// Inflate part 1
			LinearLayout viewGroup = (LinearLayout)context.findViewById(temp);
			
			LayoutInflater layoutInflater = (LayoutInflater) context      
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			// Inflate part 2
			temp = 0;
			//if (p==button1p)
				temp = R.layout.popup_layout1;
			/*else
				if (p==button2p)
					temp = R.layout.popup_layout2;
			
				else
					if (p==button3p)
						temp = R.layout.popup_layout3;
			*/
			
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
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}//end class MainActivity

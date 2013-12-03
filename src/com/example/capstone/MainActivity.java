package com.example.capstone;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.pm.ActivityInfo;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.util.FloatMath;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends Activity {
	
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setContentView(R.layout.activity_main);
		
		
		imageDetail = (ImageView) findViewById(R.id.Map);
		
		/** * set on touch listener on image */
		imageDetail.setOnTouchListener(new View.OnTouchListener() {
			@Override
			  public boolean onTouch(View view, MotionEvent event) {
        ImageView imageView = (ImageView)view;
        
        String x = String.valueOf(event.getX());
        String y = String.valueOf(event.getY());
        final Handler handler = new Handler();
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(x+"-"+y);
        final AlertDialog dialog = builder.create();
        dialog.show();
        handler.postDelayed(new Runnable() {
          public void run() {
            dialog.dismiss();    
          }
        }, 3000);

        switch(event.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:
                savedMatrix.set(matrix);
                start.set(event.getX(), event.getY());
                mode = DRAG;
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                oldDistance = spacing(event);
                if(oldDistance > 10f) {
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
                if(mode == DRAG) {
                    matrix.set(savedMatrix);

        matrix.getValues(matrixValues);
        matrixX = matrixValues[2];
        matrixY = matrixValues[5];
        width = matrixValues[0] * (((ImageView) view).getDrawable()
                                .getIntrinsicWidth());
        height = matrixValues[4] * (((ImageView) view).getDrawable()
                                .getIntrinsicHeight());

        dx = event.getX() - start.x;
        dy = event.getY() - start.y;

        // width/2 and height/2 are modified values
        // image will not translate past 1/2 width or length of image
        //if image will go outside left bound        
        if (matrixX + dx < 0 -width/2){
            dx = -matrixX -width/2;        
        }
        //if image will go outside right bound
        if(matrixX + dx + width > view.getWidth() + width/2){
            dx = view.getWidth() - matrixX - width + width/2;        
        }
        
        //if image will go outside top bound
        if (matrixY + dy < 0 - height/2){
            dy = -matrixY - height/2;
        }
        //if image will go outside bottom bound
        if(matrixY + dy + height > view.getHeight() + height/2){
            dy = view.getHeight() - matrixY - height + height/2;
        }
        matrix.postTranslate(dx, dy);   
                }
                else if(mode == ZOOM) {
                    float newDistance = spacing(event);
                    if(newDistance > 10f) {
                        matrix.set(savedMatrix);
                        float scale = newDistance / oldDistance;
                        float[] values = new float[9];
                        matrix.getValues(values);
                        float currentScale = values[Matrix.MSCALE_X];
                        if(scale * currentScale > MAX_ZOOM) 
                            scale = MAX_ZOOM / currentScale;
                        else if (scale * currentScale < MIN_ZOOM)
                            scale = MIN_ZOOM / currentScale;
                        matrix.postScale(scale, scale, mid.x, mid.y);
                    }
                }
                break;
        }
        imageView.setImageMatrix(matrix);
        return true;
    }

    @SuppressLint("FloatMath")
	private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return FloatMath.sqrt(x * x + y * y);
    }

    private void midPoint(PointF point, MotionEvent event) {
        point.set((event.getX(0) + event.getX(1)) / 2, (event.getY(0) + event.getY(1)) / 2);
    }

		});
	}

	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	

}

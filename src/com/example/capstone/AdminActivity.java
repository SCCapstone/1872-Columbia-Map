package com.example.capstone;

import java.io.IOException;

import com.example.capstone.R.id;

import android.app.Activity;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

public class AdminActivity extends Activity {
    
	private static final String STATE_SCALE = "state-scale";
    private static final String STATE_CENTER_X = "state-center-x";
    private static final String STATE_CENTER_Y = "state-center-y";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        try {
            TestAdminSubsampling imageView = (TestAdminSubsampling)findViewById(id.imageView);
            imageView.setImageAsset("MAPHIGHJ.JPG");

            if (savedInstanceState != null &&
                    savedInstanceState.containsKey(STATE_SCALE) &&
                    savedInstanceState.containsKey(STATE_CENTER_X) &&
                    savedInstanceState.containsKey(STATE_CENTER_Y)) {
                imageView.setScaleAndCenter(savedInstanceState.getFloat(STATE_SCALE), new PointF(savedInstanceState.getFloat(STATE_CENTER_X), savedInstanceState.getFloat(STATE_CENTER_Y)));
            }
        } catch (IOException e) {
            Log.e(MainActivity.class.getSimpleName(), "Could not load asset", e);
        }
        
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        TestAdminSubsampling imageView = (TestAdminSubsampling)findViewById(id.imageView);
        outState.putFloat(STATE_SCALE, imageView.getScale());
        PointF center = imageView.getCenter();
        if (center != null) {
            outState.putFloat(STATE_CENTER_X, center.x);
            outState.putFloat(STATE_CENTER_Y, center.y);
        }
    }
    
}

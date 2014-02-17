package com.example.capstone;

import java.io.IOException;

import android.app.Activity;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.example.capstone.R.id;

public abstract class SingleFragmentActivity extends FragmentActivity {

	protected abstract Fragment createFragment();
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adminview_fragment);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
        if (fragment == null) {
        	fragment = createFragment();
        	fm.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
        }
    }

}

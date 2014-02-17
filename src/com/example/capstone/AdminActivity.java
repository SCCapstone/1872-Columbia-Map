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

public class AdminActivity extends SingleFragmentActivity {
    
    @Override
    protected Fragment createFragment() {
    	return new LocationFragment();
    }

}

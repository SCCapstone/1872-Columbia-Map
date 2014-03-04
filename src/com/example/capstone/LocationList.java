package com.example.capstone;

import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;

public class LocationList {
	private ArrayList<Location> mLocations;
	private static LocationList sLocationList;
	private Context mAppContext;
	private LocationList(Context appContext) {
		mAppContext = appContext;
		mLocations = new ArrayList<Location>();
		for (int i=0; i<100; i++) {
			Location c = new Location();
			c.setTitle("Location #" + i);
			c.setDescription("Enter location description here...");
			mLocations.add(c);
		}
	}
	public static LocationList get(Context c) {
		if (sLocationList == null) {
			sLocationList = new LocationList(c.getApplicationContext());
		}
		return sLocationList;
	}
	public ArrayList<Location> getLocations() {
		return mLocations;
	}
	public Location getLocation(UUID id) {
		for (Location c : mLocations) {
			if (c.getId().equals(id)) return c;
		}
		return null;
	}
}

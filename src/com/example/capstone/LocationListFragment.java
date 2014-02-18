package com.example.capstone;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.widget.ArrayAdapter;

public class LocationListFragment extends ListFragment {
	private ArrayList<Location> mLocations;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//getActivity().setTitle(R.string.locations_title);
		mLocations = LocationList.get(getActivity()).getLocations();
		ArrayAdapter<Location> adapter = new ArrayAdapter<Location>(getActivity(),android.R.layout.simple_list_item_1,mLocations);
		setListAdapter(adapter);
	}
}

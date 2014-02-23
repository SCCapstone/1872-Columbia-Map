package com.example.capstone;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

public class LocationListFragment extends ListFragment {
	private static final String TAG = "LocationListFragment";
	private ArrayList<Location> mLocations;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActivity().setTitle(R.string.locations_title);
		mLocations = LocationList.get(getActivity()).getLocations();
		LocationAdapter adapter = new LocationAdapter(mLocations);
		setListAdapter(adapter);
	}
	
	//push test
	@Override
	public void onListItemClick(ListView listview, View v, int position, long id) {
		Location c = ((LocationAdapter)getListAdapter()).getItem(position);
		Log.d(TAG, c.getTitle() + " was clicked");
	}
	
	private class LocationAdapter extends ArrayAdapter<Location> {
		public LocationAdapter(ArrayList<Location> locations) {
			super(getActivity(), 0, locations);
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			//if not given a view, inflate one
			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_location, null);
			}
			//configure view for this location
			Location c = getItem(position);
			TextView titleTextView = (TextView)convertView.findViewById(R.id.location_list_item_titleTextView);
			titleTextView.setText(c.getTitle());
			TextView dateTextView = (TextView)convertView.findViewById(R.id.location_list_item_dateTextView);
			dateTextView.setText(c.getDate().toString());
			CheckBox solvedCheckBox = (CheckBox)convertView.findViewById(R.id.location_list_item_solvedCheckBox);
			solvedCheckBox.setChecked(c.isSolved());
			return convertView;
		}
	}
	
}

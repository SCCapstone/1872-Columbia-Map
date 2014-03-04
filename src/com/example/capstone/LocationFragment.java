package com.example.capstone;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

public class LocationFragment extends Fragment {
	private Location mLocation;
	private EditText mTitleField;
	private Button mDateButton;
	private CheckBox mSolvedCheckBox;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mLocation = new Location();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflator, ViewGroup parent, Bundle savedInstanceState) {
		View v = inflator.inflate(R.layout.location_fragment, parent, false);
		mTitleField = (EditText)v.findViewById(R.id.location_title);
		mTitleField.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence c, int start, int before, int count) {
				mLocation.setTitle(c.toString());
			}
			public void beforeTextChanged(CharSequence c, int start, int count, int after) {
				//this space left intentionally blank
			}
			public void afterTextChanged(Editable c) {
				//this too
			}
		});
		mDateButton = (Button)v.findViewById(R.id.location_date);
		mDateButton.setText(mLocation.getDescription());
		mDateButton.setEnabled(false);
		mSolvedCheckBox = (CheckBox)v.findViewById(R.id.location_solved);
		mSolvedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				//set the location's solved property
				mLocation.setChecked(isChecked);
			}
		});
		return v;
	}
}

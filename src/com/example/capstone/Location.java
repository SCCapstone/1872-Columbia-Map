package com.example.capstone;

import java.util.Date;
import java.util.UUID;

public class Location {
	private UUID mId;
	private String mTitle;
	private String mDescription;
	private boolean mChecked;
	public Location() {
		//Generate unique identifier
		mId = UUID.randomUUID();
	}
	@Override
	public String toString() {
		return mTitle;
	}
	public String getDescription() {
		return mDescription;
	}
	public void setDescription(String description) {
		description = mDescription;
	}
	public boolean isSolved() {
		return mChecked;
	}
	public void setChecked(boolean checked) {
		checked = mChecked;
	}
	public String getTitle() {
		return mTitle;
	}
	public void setTitle(String title) {
		mTitle = title;
	}
	public UUID getId() {
		return mId;
	}
}

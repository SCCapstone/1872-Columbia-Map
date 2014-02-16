package com.example.capstone;

import java.util.Date;
import java.util.UUID;

public class Location {
	private UUID mId;
	private String mTitle;
	private Date mDate;
	private boolean mSolved;
	public Location() {
		//Generate unique identifier
		mId = UUID.randomUUID();
		mDate = new Date();
	}
	public Date getDate() {
		return mDate;
	}
	public void setDate(Date date) {
		date = mDate;
	}
	public boolean isSolved() {
		return mSolved;
	}
	public void setSolved(boolean solved) {
		solved = mSolved;
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

package com.example.capstone;

import org.json.JSONException;
import org.json.JSONObject;

public class LocationObject 
{
	private long _id;
	private String xCoord;
	private String yCoord;
	private String locTitle;
	private String locDescription;
	private String imageString;
	private long id;
	
	public LocationObject()
	{
		id = 0;
		xCoord = "";
		yCoord = "";
		locTitle = "";
		locDescription = "";
		imageString = "";
	}
	
	public long getId() 
	{
		return id;
	}

	public void setId(long id) 
	{
		this.id = id;
	}

	
	public LocationObject(int id, String x, String y, String title, String description, String image)
	{
		this._id = id;
		xCoord = x;
		yCoord = y;
		locTitle = title;
		locDescription = description;
		imageString = image;
	}
	
	/**
	 * @return the xCoord
	 */
	public String getxCoord() {
		return xCoord;
	}

	/**
	 * @return the yCoord
	 */
	public String getyCoord() {
		return yCoord;
	}

	/**
	 * @param string 
	 * @return the locTitle
	 */
	public String getLocTitle(String string) {
		return locTitle;
	}
	
	public String getLocTitle() {
		return locTitle;
	}

	/**
	 * @param string 
	 * @return the locDescription
	 */
	public String getLocDescription(String string) {
		return locDescription;
	}

	public String getLocDescription() {
		return locDescription;
	}
	/**
	 * @param string 
	 * @return the imageString
	 */
	public String getImageString(String string) {
		return imageString;
	}
	
	public String getImageString() {
		return imageString;
	}

	/**
	 * @param xCoord the xCoord to set
	 */
	public void setxCoord(String xCoord) {
		this.xCoord = xCoord;
	}

	/**
	 * @param yCoord the yCoord to set
	 */
	public void setyCoord(String yCoord) {
		this.yCoord = yCoord;
	}

	/**
	 * @param locTitle the locTitle to set
	 */
	public void setLocTitle(String locTitle) {
		this.locTitle = locTitle;
	}

	/**
	 * @param locDescription the locDescription to set
	 */
	public void setLocDescription(String locDescription) {
		this.locDescription = locDescription;
	}

	/**
	 * @param imageString the imageString to set
	 */
	public void setImageString(String imageString) {
		this.imageString = imageString;
	}
	
	public JSONObject toJSON() throws JSONException
	{
		JSONObject json = new JSONObject();
		json.put("xCoord", xCoord);
		json.put("yCoord", yCoord);
		json.put("locTitle", locTitle);
		json.put("locDescription", locDescription);
		json.put("imageString", imageString);
		return json;
	}
	
	public LocationObject(JSONObject json) throws JSONException
	{
		xCoord = json.getString("xCoord");
		yCoord = json.getString("yCoord");
		locTitle = json.getString("locTitle");
		locDescription = json.getString("locDescription");
		imageString = json.getString("imageString");
		
	}
	
}

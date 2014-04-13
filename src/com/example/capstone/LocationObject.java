package com.example.capstone;

public class LocationObject 
{
	private String xCoord;
	private String yCoord;
	private String locTitle;
	private String locDescription;
	private String imageString;
	
	public LocationObject()
	{
		xCoord = "";
		yCoord = "";
		locTitle = "";
		locDescription = "";
		imageString = "";
	}
	
	public LocationObject(String x, String y, String title, String description, String image)
	{
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
	 * @return the locTitle
	 */
	public String getLocTitle() {
		return locTitle;
	}

	/**
	 * @return the locDescription
	 */
	public String getLocDescription() {
		return locDescription;
	}

	/**
	 * @return the imageString
	 */
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
	
}

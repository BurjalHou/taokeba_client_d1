package com.taokeba.bean;

public class Course {
	
	private int courseID;
	private int courseNum;
	private String courseTitle;
	private double units;
	private String type;
	
	public int getCourseID() {
		return this.courseID;
	}
	
	public int getCourseNum() {
		return this.courseNum;
	}
	
	public String getTitle() {
		return this.courseTitle;
	}
	
	public double getUnits() {
		return this.units;
	}
	
	public String getTyep() {
		return this.type;
	}
	
	public void setCourseID(int id) {
		this.courseID = id;
	}
	
	public void setCourseNum(int num) {
		this.courseNum = num;
	}
	
	public void setTitle(String title) {
		this.courseTitle = title;
	}
	
	public void setUnits(double units) {
		this.units = units;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
}

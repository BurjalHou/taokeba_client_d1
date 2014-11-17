package com.taokeba.bean;

public class Answer {
	
	private int id;
	private String user;
	private String text;
	private String date;
	
	public int getID() {
		return this.id;
	}
	
	public String getUser() {
		return this.user;
	}
	
	public String getText() {
		return this.text;
	}
	
	public String getDate() {
		return this.date;
	}
	
	public void setID(int id) {
		this.id = id;
	}
	
	public void setUser(String user) {
		this.user = user;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
}

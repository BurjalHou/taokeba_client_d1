package com.taokeba.bean;

public class Comment {
	
	private long commentID;
	private String text;
	private String date;
	private String user;
	
	public long getCommentID() {
		return this.commentID;
	}
	
	public String getText() {
		return this.text;
	}
	
	public String getDate() {
		return this.date;
	}
	
	public String getUser() {
		return this.user;
	}
	
	public void setCommentID(Long id) {
		this.commentID = id;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
	public void setUser(String user) {
		this.user = user;
	}
}

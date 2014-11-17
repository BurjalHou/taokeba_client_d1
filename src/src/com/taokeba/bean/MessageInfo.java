package com.taokeba.bean;


public class MessageInfo {

	private int messageID;
	private String time;
	private String text;
	private int fromID;
	private int getToType;
	
	public int getMessageID() {
		return messageID;
	}
	public void setMessageID(int messageID) {
		this.messageID = messageID;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getFromID() {
		return fromID;
	}
	public void setFromID(int fromID) {
		this.fromID = fromID;
	}
	public int getGetToType() {
		return getToType;
	}
	public void setGetToType(int getToType) {
		this.getToType = getToType;
	}	
	
	
}

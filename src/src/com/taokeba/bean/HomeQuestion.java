package com.taokeba.bean;

import java.io.IOException;
import java.io.StringReader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.graphics.Paint.Join;

public class HomeQuestion {

	private int id;
	private int sectionID;
	private long courseNum;
	private int courseID;
	private String title;
	private String text;
	private String date;
	private String author;
	
	public void setID(int id) {
		this.id = id;
	}
	
	public void setSectionID(int id) {
		this.sectionID = id;
	}
	
	public void setCourseNum(long num) {
		this.courseNum = num;
	}
	
	public void setCourseID(int id) {
		this.courseID = id;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
	public void setAuthor(String author) {
		this.author = author;
	}
	
	public int getID() {
		return this.id;
	}
	
	public int getSectionID() {
		return this.sectionID;
	}
	
	public long getCourseNum() {
		return this.courseNum;
	}
	
	public int getCourseID() {
		return this.courseID;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public String getText() {
		return this.text;
	}
	
	public String getDate() {
		return this.date;
	}
	
	public String getAuthor() {
		return this.author;
	}
	
	public static HomeQuestion parse(String s) throws XmlPullParserException {
		HomeQuestion question = new HomeQuestion();
		
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		factory.setNamespaceAware(false);
		XmlPullParser xmlParser = factory.newPullParser();
		
		try{
			xmlParser.setInput(new StringReader(s));
			int eventType = xmlParser.getEventType();
			while(eventType != XmlPullParser.END_DOCUMENT) {
				String tag = xmlParser.getName();
				switch(eventType) {
				case XmlPullParser.START_TAG:
					if(tag.equalsIgnoreCase("Title")) {
						question.setTitle(xmlParser.nextText());
					} else if(tag.equalsIgnoreCase("CourseNum")) {
						question.setCourseNum(Long.parseLong(xmlParser.nextText()));
					} else if(tag.equalsIgnoreCase("Text")) {
						question.setText(xmlParser.nextText());
					} else if(tag.equalsIgnoreCase("Date")) {
						question.setDate(xmlParser.nextText());
					} 
					break;
				default:
					break;
				}
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		return question;
	}
	
}

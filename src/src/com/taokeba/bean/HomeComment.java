package com.taokeba.bean;

import java.io.IOException;
import java.io.StringReader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class HomeComment {
	
	private int sectionID;
	private String courseNum;
	private String title;
	private String text;
	private String date;
	
	public void setSectionID(int sectionID) {
		this.sectionID = sectionID;
	}
	
	public void setCourseNum(String num) {
		this.courseNum = num;
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
	
	public int getSectionID() {
		return this.sectionID;
	}
	
	public String getCourseNum() {
		return this.courseNum;
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
	
	public static HomeComment parse(String s) throws XmlPullParserException {
		HomeComment comment = null;
		
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		factory.setNamespaceAware(false);
		XmlPullParser xmlParser = factory.newPullParser();
		
		try {
			xmlParser.setInput(new StringReader(s));
			int eventType = xmlParser.getEventType();
			
			while (eventType != XmlPullParser.END_DOCUMENT) {
				String tag = xmlParser.getName();
				switch (eventType) {
				case XmlPullParser.START_TAG:
					if(tag.equalsIgnoreCase("Comments")) {
						comment = new HomeComment();
					} else {
						if(tag.equalsIgnoreCase("SectionID")) {
							comment.setSectionID(Integer.parseInt(xmlParser.nextText()));
						} else if(tag.equalsIgnoreCase("CourseNum")) {
							comment.setCourseNum(xmlParser.nextText());
						} else if(tag.equalsIgnoreCase("Title")) {
							comment.setTitle(xmlParser.nextText());
						} else if(tag.equalsIgnoreCase("Text")) {
							comment.setText(xmlParser.nextText());
						} else if(tag.equalsIgnoreCase("Date")) {
							comment.setDate(xmlParser.nextText());
						}
					}
					break;

				default:
					break;
				}
				eventType = xmlParser.next();
			}
			
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		return comment;
	}
}

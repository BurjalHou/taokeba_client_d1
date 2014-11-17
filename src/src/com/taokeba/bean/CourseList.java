package com.taokeba.bean;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class CourseList {
	
	private List<Course> courseList = new ArrayList<Course>();
	
	public List<Course> getCourseList() {
		return this.courseList;
	}
	
	public static CourseList parse(String s) throws XmlPullParserException {
		CourseList courses = new CourseList();
		Course course = null;
		
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		factory.setNamespaceAware(true);
		XmlPullParser xmlParser = factory.newPullParser();
		
		try {
			xmlParser.setInput(new StringReader(s));
			int eventType = xmlParser.getEventType();
			while(eventType != XmlPullParser.END_DOCUMENT) {
				String tag = xmlParser.getName();
				switch (eventType) {
				case XmlPullParser.START_TAG:
					if(tag.equalsIgnoreCase("Course")) {
						course = new Course();
					} else {
						if(tag.equalsIgnoreCase("CourseID")) {
							course.setCourseID(Integer.parseInt(xmlParser.nextText()));
						} else if(tag.equalsIgnoreCase("CourseNum")) {
							course.setCourseNum(Integer.parseInt(xmlParser.nextText()));
						} else if(tag.equalsIgnoreCase("CourseTitle")) {
							course.setTitle(xmlParser.nextText());
						} else if(tag.equalsIgnoreCase("Unit")){
							course.setUnits(Double.parseDouble(xmlParser.nextText()));
						} else if(tag.equalsIgnoreCase("Type")) {
							course.setType(xmlParser.nextText());
						}
					}
					break;
					
				case XmlPullParser.END_TAG:
					if(tag.equalsIgnoreCase("Course") && course != null) {
						courses.getCourseList().add(course);
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
		
		return courses;
	}
	
}

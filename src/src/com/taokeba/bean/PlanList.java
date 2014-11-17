package com.taokeba.bean;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.taokeba.AppException;
import com.taokeba.common.StringUtils;

public class PlanList {

	private List<Section> courseList = new ArrayList<Section>();
	
	public List<Section> getCourseList() {
		return this.courseList;
	}
	
	public static PlanList parse(String inputStream) throws IOException, AppException, XmlPullParserException {
		PlanList planList = new PlanList();
		Section course = null;
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		factory.setNamespaceAware(true);
		XmlPullParser xmlParser = factory.newPullParser();
	
		try {
			xmlParser.setInput(new StringReader(inputStream));
			int evtType = xmlParser.getEventType();
			while(evtType != XmlPullParser.END_DOCUMENT) {
				String tag = xmlParser.getName();
				switch (evtType) {
				case XmlPullParser.START_TAG:
					if(tag.equalsIgnoreCase("Section")) {
						course = new Section();
					} else if(course != null) {
						if(tag.equalsIgnoreCase("SectionID")) {
							course.setSectionID(StringUtils.toInt(xmlParser.nextText(), 0));
						} else if(tag.equalsIgnoreCase("Term")) {
							course.setTerm(xmlParser.nextText());
						} else if(tag.equalsIgnoreCase("CourseID")) {
							course.setCourseID(StringUtils.toInt(xmlParser.nextText(), 0));
						} else if(tag.equalsIgnoreCase("CourseNum")) {
							course.setCourseNum(xmlParser.nextText());
						} else if(tag.equalsIgnoreCase("Title")) {
							course.setTitle(xmlParser.nextText());
						} else if(tag.equalsIgnoreCase("Department")) {
							course.setDepartment(xmlParser.nextText());
						} else if(tag.equalsIgnoreCase("Unit")) {
							course.setUnit(StringUtils.toDouble(xmlParser.nextText()));
						} else if(tag.equalsIgnoreCase("type")) {
							course.setType(xmlParser.nextText());
						} else if(tag.equalsIgnoreCase("Instructor")) {
							course.setInstructor(xmlParser.nextText());
						} else if(tag.equalsIgnoreCase("Grade")) {
							course.setGrade(StringUtils.toInt(xmlParser.nextText(), 0));
						} else if(tag.equalsIgnoreCase("Exempt")) {
							course.setExempt(xmlParser.nextText());
						}
					}
					break;
				case XmlPullParser.END_TAG:
					if(tag.equalsIgnoreCase("Section") && course != null) {
						planList.getCourseList().add(course);
					}
				default:
					break;
				}
				evtType = xmlParser.next();
			}
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw AppException.xml(e);
		} 
		return planList;
	}
}

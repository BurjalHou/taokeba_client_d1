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

public class InterestList {

	private List<Section> interestList = new ArrayList<Section>();
	
	public List<Section> getCourseList() {
		return this.interestList;
	}
	
	public static InterestList parse(String inputStream) throws IOException, AppException, XmlPullParserException {
		InterestList interestList = new InterestList();
		Section section = null;
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
						section = new Section();
					} else if(section != null) {
						if(tag.equalsIgnoreCase("SectionID")) {
							section.setSectionID(StringUtils.toInt(xmlParser.nextText(), 0));
						} else if(tag.equalsIgnoreCase("Term")) {
							section.setTerm(xmlParser.nextText());
						} else if(tag.equalsIgnoreCase("CourseID")) {
							section.setCourseID(StringUtils.toInt(xmlParser.nextText(), 0));
						} else if(tag.equalsIgnoreCase("CourseNum")) {
							section.setCourseNum(xmlParser.nextText());
						} else if(tag.equalsIgnoreCase("Title")) {
							section.setTitle(xmlParser.nextText());
						} else if(tag.equalsIgnoreCase("Department")) {
							section.setDepartment(xmlParser.nextText());
						} else if(tag.equalsIgnoreCase("Unit")) {
							section.setUnit(StringUtils.toDouble(xmlParser.nextText()));
						} else if(tag.equalsIgnoreCase("type")) {
							section.setType(xmlParser.nextText());
						} else if(tag.equalsIgnoreCase("Instructor")) {
							section.setInstructor(xmlParser.nextText());
						} else if(tag.equalsIgnoreCase("Exempt")) {
							section.setExempt(xmlParser.nextText());
						}
					}
					break;
				case XmlPullParser.END_TAG:
					if(tag.equalsIgnoreCase("Section") && section != null) {
						interestList.getCourseList().add(section);
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
		return interestList;
	}
}

package com.taokeba.bean;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.taokeba.common.StringUtils;

public class SectionRank {

	private List<Section> sectionList = new ArrayList<Section>();
	
	public List<Section> getSectionList() {
		return this.sectionList;
	}
	
	public static SectionRank parse(String s) throws XmlPullParserException {
		SectionRank rank = new SectionRank();
		Section section = null;
		
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
					if(tag.equalsIgnoreCase("Section")) {
						section = new Section();
					} else {
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
						} else if(tag.equalsIgnoreCase("Grade")) {
							section.setGrade(StringUtils.toInt(xmlParser.nextText(), 0));
						} else if(tag.equalsIgnoreCase("Exempt")) {
							section.setExempt(xmlParser.nextText());
						} else if(tag.equalsIgnoreCase("Rate")) {
							section.setRate(xmlParser.nextText());
						} else if(tag.equalsIgnoreCase("CommentNum")) {
							section.setCommentNum(StringUtils.toInt(xmlParser.nextText(), 0));
						}
					}
					break;
				case XmlPullParser.END_TAG: 
					if(tag.equalsIgnoreCase("Section") && section != null) {
						rank.getSectionList().add(section);
					}
				
				default:
					break;
				}
				eventType = xmlParser.next();
			}
			
		} catch(IOException e) {
			e.printStackTrace();
		}
		return rank;
	}
	
}

package com.taokeba.bean;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class HomeCommentList {

	private List<HomeComment> commentList = new ArrayList<HomeComment>();
	
	public List<HomeComment> getCommentList() {
		return this.commentList;
	}
	
	public static HomeCommentList parse(String s) throws XmlPullParserException {
		HomeCommentList commentList = new HomeCommentList();
		HomeComment comment = null;
		
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		factory.setNamespaceAware(false);
		XmlPullParser xmlParser = factory.newPullParser();
		
		try {
			xmlParser.setInput(new StringReader(s));
			int eventType = xmlParser.getEventType();
			while(eventType != XmlPullParser.END_DOCUMENT) {
				String tag = xmlParser.getName();
				switch (eventType) {
				case XmlPullParser.START_TAG:
					if(tag.equalsIgnoreCase("Comment")) {
						comment = new HomeComment();
					} else {
						if(tag.equalsIgnoreCase("SectionID")) {
							comment.setSectionID(Integer.parseInt(xmlParser.nextText()));
						} else if(tag.equals("CourseNum")) {
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
				case XmlPullParser.END_TAG:
					if(tag.equalsIgnoreCase("Comment") && comment != null) {
						commentList.getCommentList().add(comment);
					}
					break;
				default:
					break;
				}
				eventType = xmlParser.next();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return commentList;
	}
	
}

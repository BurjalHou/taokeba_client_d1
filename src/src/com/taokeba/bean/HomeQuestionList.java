package com.taokeba.bean;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;

import com.taokeba.common.StringUtils;

public class HomeQuestionList {
	
	private List<HomeQuestion> question = new ArrayList<HomeQuestion>();
	
	public List<HomeQuestion> getHomeQuestionList() {
		return this.question;
	}
	
	public static HomeQuestionList parse(String s) throws XmlPullParserException,NullPointerException {
		HomeQuestionList questionList = new HomeQuestionList();
		HomeQuestion question = null;
		
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		factory.setNamespaceAware(false);
		XmlPullParser xmlParser = factory.newPullParser();
		try {
			xmlParser.setInput(new StringReader(s));
			int eventType = xmlParser.getEventType();
			while(eventType != XmlPullParser.END_DOCUMENT) {
				String tag = xmlParser.getName();
				switch(eventType) {
				case XmlPullParser.START_TAG:
					if(tag.equalsIgnoreCase("Question")) {
						question = new HomeQuestion();
					} else {
						if(tag.equalsIgnoreCase("QuestionID")) {
							question.setID(Integer.parseInt(xmlParser.nextText()));
						} else if(tag.equalsIgnoreCase("CourseID")) {
							question.setCourseID(Integer.parseInt(xmlParser.nextText()));
						} else if(tag.equalsIgnoreCase("CourseNum")) {
							question.setCourseNum(Long.parseLong(xmlParser.nextText()));
						} else if(tag.equalsIgnoreCase("Title")) {
							question.setTitle(xmlParser.nextText()); 
						} else if(tag.equalsIgnoreCase("Text")) {
							question.setText(xmlParser.nextText());
						} else if(tag.equalsIgnoreCase("Date")) {
							question.setDate(xmlParser.nextText());
						} else if(tag.equalsIgnoreCase("Author")) {
							question.setAuthor(xmlParser.nextText());
						}
					}
					break;
					
				case XmlPullParser.END_TAG:
					if(tag.equalsIgnoreCase("Question") && question != null ) {
						questionList.getHomeQuestionList().add(question);
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
		return questionList;
	}
	
}

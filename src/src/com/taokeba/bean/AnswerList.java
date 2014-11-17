package com.taokeba.bean;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.taokeba.common.StringUtils;
import com.taokeba.ui.QuestionActivity;

public class AnswerList {
	
	private List<Answer> answerList = new ArrayList<Answer>();
	private HomeQuestion question = null;
	
	public List<Answer> getAnswerList() {
		return this.answerList;
	}
	
	public HomeQuestion getQuestion() {
		return this.question;
	}
	
	public static AnswerList parse(String s) throws XmlPullParserException {
		AnswerList answerList = new AnswerList();
		Answer answer = null;
		
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		factory.setNamespaceAware(false);
		XmlPullParser xmlParser = factory.newPullParser();
		
		try {
			xmlParser.setInput(new StringReader(s));
			int eventType = xmlParser.getEventType();
			while(eventType != XmlPullParser.END_DOCUMENT) {
				String tag = xmlParser.getName();
				switch (eventType) {
				case  XmlPullParser.START_TAG:
					if(tag.equalsIgnoreCase("Answer")) {
						answer = new Answer();
					} else {
						if(tag.equalsIgnoreCase("AnswerID")) {
							answer.setID(StringUtils.toInt(xmlParser.nextText(), 0));
						} else if(tag.equalsIgnoreCase("UserAlias")) {
							answer.setUser(xmlParser.nextText());
						} else if(tag.equalsIgnoreCase("Text")) {
							answer.setText(xmlParser.nextText());
						} else if(tag.equalsIgnoreCase("Date")) {
							answer.setDate(xmlParser.nextText());
						}
					} 
					break;
				case XmlPullParser.END_TAG:
					if(tag.equalsIgnoreCase("Answer") && answer != null) {
						answerList.getAnswerList().add(answer);
					}
				default:
					break;
				}
				eventType = xmlParser.next();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return answerList;
	}
}

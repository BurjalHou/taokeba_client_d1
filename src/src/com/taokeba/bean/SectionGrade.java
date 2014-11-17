package com.taokeba.bean;

import java.io.IOException;
import java.io.StringReader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.R.xml;

public class SectionGrade {
	
	private int gradeA;
	private int gradeB;
	private int gradeC;
	private int gradeD;
	private int gradeE;
	
	private void setGradeANum(int c) {
		this.gradeA = c;
	}
	
	private void setGradeBNum(int c) {
		this.gradeB = c;
	}
	
	private void setGradeCNum(int c) {
		this.gradeC = c;
	}
	
	private void setGradeDNum(int c) {
		this.gradeD = c;
	}
	
	private void setGradeENum(int c) {
		this.gradeE = c;
	}
	
	public int getGradeANum() {
		return this.gradeA;
	}
	
	public int getGradeBNum() {
		return this.gradeB;
	}
	
	public int getGradeCNum() {
		return this.gradeC;
	}
	
	public int getGradeDNum() {
		return this.gradeD;
	}
	
	public int getGradeENum() {
		return this.gradeE;
	}
	
	public static SectionGrade parse(String s) throws XmlPullParserException {
		
		SectionGrade grade = null;
		
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
					if(tag.equalsIgnoreCase("Grade")) {
						grade = new SectionGrade();
					} else {
						String text = xmlParser.nextText();
						if(tag.equalsIgnoreCase("A")) {
							grade.setGradeANum(Integer.valueOf(text));
						} else if(tag.equalsIgnoreCase("B")) {
							grade.setGradeBNum(Integer.valueOf(text));
						} else if(tag.equalsIgnoreCase("C")) {
							grade.setGradeCNum(Integer.valueOf(text));
						} else if(tag.equalsIgnoreCase("D")) {
							grade.setGradeDNum(Integer.valueOf(text));
						} else if(tag.equalsIgnoreCase("E")) {
							grade.setGradeENum(Integer.valueOf(text));
						}
					}
					break;
				}
				eventType = xmlParser.next();
			}
			
		} catch(IOException e) {
			e.printStackTrace();
		}
		return grade;
		
	}
	
}

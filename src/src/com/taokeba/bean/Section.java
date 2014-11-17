package com.taokeba.bean;

import java.io.IOException;
import java.io.StringReader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.taokeba.common.StringUtils;

public class Section {
	
	private int sectionID;
	private String term;
	private int termID;
	private int courseID;
	private String courseNum;
	private String classNum;
	private String department;
	private String title;
	private double unit;
	private String type;
	private String instructorID;
	private String instructor;
	private String description;
	private double grade;
	private String exempt;
	private String rate;
	private String time;
	private String location;
	private boolean select;
	private boolean interest;
	private int commentNum;
	
	public int getSectionID() {
		return this.sectionID;
	}
	
	public String getTerm() {
		return this.term;
	}
	
	public int getTermID() {
		return this.termID;
	}
	
	public int getCourseID() {
		return this.courseID;
	}
	
	public String getCourseNum() {
		return this.courseNum;
	}
	
	public String getClassNum() {
		return this.classNum;
	}
	
	public double getUnit() {
		return this.unit;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public String getDepartment() {
		return this.department;
	}
	
	public String getType() {
		return this.type;
	}
	
	public String getInstructorID() {
		return this.instructorID;
	}
	
	public String getInstructor() {
		return this.instructor;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public double getGrade() {
		return this.grade;
	}
	
	public String getExempt() {
		return this.exempt;
	}
	
	public String getRate() {
		return this.rate;
	}
	
	public String getTime() {
		return this.time;
	}
	
	public String getLocation() {
		return this.location;
	}
	
	public boolean getSelect() {
		return this.select;
	}
	
	public boolean getInterest() {
		return this.interest;
	}
	
	public int getCommentNum() {
		return this.commentNum;
	}
	
	public void setSectionID(int id) {
		this.sectionID = id;
	}
	
	public void setTerm(String term) {
		this.term = term;
	}
	
	public void setTermID(int id) {
		this.termID = id;
	}
	
	public void setCourseID(int id) {
		this.courseID = id;
	}
	
	public void setCourseNum(String num) {
		this.courseNum = num;
	}
	
	public void setClassNum(String num) {
		this.classNum = num;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setDepartment(String department) {
		this.department = department;
	}
	
	public void setUnit(double unit) {
		this.unit = unit;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public void setInstructorID(String id) {
		this.instructorID = id;
	}
	
	public void setInstructor(String inst) {
		this.instructor = inst;
	}
	
	public void setDescription(String des) {
		this.description = des;
	}
	
	public void setGrade(double grade) {
		this.grade = grade;
	}
	
	public void setExempt(String exempt) {
		this.exempt = exempt;
	}
	
	public void setRate(String rate) {
		this.rate = rate;
	}
	
	public void setTime(String time) {
		this.time = time;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
	
	public void setSelect(boolean b) {
		this.select = b;
	}
	
	public void setInterest(boolean b) {
		this.interest = b;
	}
	
	public void setCommentNum(int n) {
		this.commentNum = n;
	}
	
	public static Section parse(String data) throws XmlPullParserException {
		Section section = null;
		
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		factory.setNamespaceAware(true);
		XmlPullParser xmlParser = factory.newPullParser();
		try {
			xmlParser.setInput(new StringReader(data));
			int eventType = xmlParser.getEventType();
			while(eventType != XmlPullParser.END_DOCUMENT) {
				String tag = xmlParser.getName();
				switch (eventType) {
				case XmlPullParser.START_TAG:
					if(tag.equalsIgnoreCase("Section")) {
						section = new Section();
					} else {
						if(tag.equalsIgnoreCase("SectionID")) {
							section.setSectionID(StringUtils.toInt(xmlParser.nextText(), 0));
						} else if(tag.equalsIgnoreCase("CourseNumber")) {
							section.setCourseNum(xmlParser.nextText());
						} else if(tag.equalsIgnoreCase("Term")) {
							section.setTerm(xmlParser.nextText());
						} else if(tag.equalsIgnoreCase("TermID")) {
							section.setTermID(StringUtils.toInt(xmlParser.nextText(), 0));
						} else if(tag.equalsIgnoreCase("CourseID")) {
							section.setCourseID(StringUtils.toInt(xmlParser.nextText(), 0));
						} else if(tag.equalsIgnoreCase("ClassNum")) {
							section.setClassNum(xmlParser.nextText());
						} else if (tag.equalsIgnoreCase("title")) {
							section.setTitle(xmlParser.nextText());
						} else if(tag.equalsIgnoreCase("Department")) {
							section.setDepartment(xmlParser.nextText());
						} else if(tag.equalsIgnoreCase("InstructorID")) {
							section.setInstructorID(xmlParser.nextText());
						} else if(tag.equalsIgnoreCase("Instructor")) {
							section.setInstructor(xmlParser.nextText());
						} else if(tag.equalsIgnoreCase("Description")) {
							section.setDescription(xmlParser.nextText());
						} else if(tag.equalsIgnoreCase("Rate")) {
							section.setRate(xmlParser.nextText());
						} else if(tag.equalsIgnoreCase("Grade")) {
							section.setGrade(StringUtils.toDouble(xmlParser.nextText()));
						} else if(tag.equalsIgnoreCase("Units")) {
							section.setUnit(StringUtils.toDouble(xmlParser.nextText()));
						} else if(tag.equalsIgnoreCase("Type")) {
							section.setType(xmlParser.nextText());
						} else if(tag.equalsIgnoreCase("Time")) {
							section.setTime(xmlParser.nextText());
						} else if(tag.equalsIgnoreCase("Location")) {
							section.setLocation(xmlParser.nextText());
						} else if(tag.equalsIgnoreCase("Select")) {
							section.setSelect(Boolean.parseBoolean(xmlParser.nextText()));
						} else if(tag.equalsIgnoreCase("Interest")) {
							section.setInterest(Boolean.parseBoolean(xmlParser.nextText()));
						}
					}
					break;
	
				default:
					break;
				}
				eventType = xmlParser.next();
			}
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return section;
	}

}

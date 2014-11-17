package com.taokeba.bean;

import java.io.IOException;
import java.io.StringReader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.taokeba.common.StringUtils;

public class UserInfo {
	private int id;
	private int sunetID;
    private String userName;
    private String aliasName;
    private String studentNumber;
    private String department;
    private String major;
    private String QQ;
    private String graduteDate;
    
    public static UserInfo userInfo;
    
    private UserInfo() {
    	
    }
    
    private UserInfo(int id, int sunetID, String name, String alias, String department, String qq, String major) {
    	this.id = id;
    	this.sunetID = sunetID;
    	this.userName = name;
    	this.aliasName = alias;
    	this.department = department;
    	this.QQ = qq;
    	this.major = major;
    }
    
    public static UserInfo getUserInfo() {
    	if(userInfo == null) {
    		userInfo = new UserInfo();
    	}
    	return userInfo;
    }
    
    public static UserInfo getUserInfo(int id, int userID, String name, String alias, String department, String qq, String major) {
		if(userInfo == null) {
			userInfo = new UserInfo(id, userID, name, alias, department, qq, major);
		}
		return userInfo;
	}
    
    public void setID(int id) {
		this.id = id;
	}
    
    public void setSunetID(int id) {
		this.sunetID = id;
	}
    
    public void setUserName(String name) {
		this.userName = name;
	}
    
    public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}
    
    public void setStudentNumber(String number) {
    	this.studentNumber = number;
    }
    
    public void setDepartment(String department) {
		this.department = department;
	}
    
    public void setMajor(String major) {
    	this.major = major;
    }
    
    public void setQQ(String qq) {
    	this.QQ = qq;
    }
    
    public int getID() {
        return this.id;
    }
    
    public int getSunetID() {
    	return this.sunetID;
    }
    
    public String getUserName() {
        return this.userName;
    }
    
    public String getAliasName() {
    	return this.aliasName;
    }

    public String getStudentnumber() {
        return this.studentNumber;
    }

    public String getDepartment() {
        return this.department;
    }

    public String getMajor() {
		return this.major;
	}
    
    public String getQQ() {
		return this.QQ;
	}
    
    public static UserInfo parse(String input) throws XmlPullParserException {
    	UserInfo userInfo = null;
    	
    	XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
    	factory.setNamespaceAware(true);
    	XmlPullParser xmlParser = factory.newPullParser();
    	
    	try {
    		xmlParser.setInput(new StringReader(input));
        	int eventType = xmlParser.getEventType();
        	
	    	while(eventType != XmlPullParser.END_DOCUMENT) {
	    		String tag = xmlParser.getName();
	    		switch (eventType) {
				case XmlPullParser.START_TAG:
					if(tag.equalsIgnoreCase("UserInfo")) {
						userInfo = getUserInfo();
					} else if(userInfo != null){
						String text = xmlParser.nextText();
						if(tag.equalsIgnoreCase("ID")) {
							userInfo.setID(StringUtils.toInt(text, 0));
						} else if(tag.equalsIgnoreCase("SunnetID")) {
							userInfo.setSunetID(StringUtils.toInt(text, 0));
						} else if(tag.equalsIgnoreCase("UserName")) {
							userInfo.setUserName(text);
						} else if(tag.equalsIgnoreCase("AliasName")) {
							userInfo.setAliasName(text);
						} else if(tag.equalsIgnoreCase("Department")) {
							userInfo.setDepartment(text);
						} else if(tag.equalsIgnoreCase("Major")) {
							userInfo.setMajor(text);
						} else if(tag.equalsIgnoreCase("QQ")) {
							userInfo.setQQ(text);
						} 
					}
					break;
	
				default:
					break;
				}
	    		eventType = xmlParser.next();
	    	}
    	}
    	catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return userInfo;
    }
}

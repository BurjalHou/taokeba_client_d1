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

public class MessageInfoList {

	private List<MessageInfo> messageInfoList = new ArrayList<MessageInfo>();
	
	public List<MessageInfo> getMessageInfoList() {
		return this.messageInfoList;
	}
	
	public static MessageInfoList parse(String data) throws XmlPullParserException {
		MessageInfoList messageList  = new MessageInfoList();
		MessageInfo message = null;
		
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		factory.setNamespaceAware(true);
		XmlPullParser xmlParser = factory.newPullParser();
		
		try {
			xmlParser.setInput(new StringReader(data));
			int eventType = xmlParser.getEventType();
			while(eventType != XmlPullParser.END_DOCUMENT) {
				String tag = xmlParser.getName();
				switch(eventType) {
				case XmlPullParser.START_TAG:
					if(tag.equalsIgnoreCase("Message")) {
						message = new MessageInfo();
					} else {
						if(tag.equalsIgnoreCase("MessageID")) {
							message.setMessageID(StringUtils.toInt(xmlParser.nextText(),0));							
						} else if(tag.equalsIgnoreCase("CreateTime")) {
							message.setTime(xmlParser.nextText());
						} else if(tag.equalsIgnoreCase("Text")) {
							message.setText(xmlParser.nextText());
						} else if(tag.equalsIgnoreCase("From")) {
							message.setFromID(StringUtils.toInt(xmlParser.nextText(),0));
						} else if(tag.equalsIgnoreCase("Type")) {
							message.setGetToType(StringUtils.toInt(xmlParser.nextText(),0));
						}
					}
					break;
					
				case XmlPullParser.END_TAG:
					if(tag.equalsIgnoreCase("Message")&&message != null) {
						messageList.getMessageInfoList().add(message);
					}
					break;
					
				default:
					break;
				}
				eventType = xmlParser.next();
			}
		} catch (IOException e) {
			Log.e("error....","parser Message in left side menu........>>>>>MessageInfoList.class...");
			e.printStackTrace();
		}
		
		return messageList;
	}
}

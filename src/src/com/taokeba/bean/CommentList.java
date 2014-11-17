package com.taokeba.bean;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;

import com.taokeba.AppException;

public class CommentList {
	
	private List<Comment> commentList = new ArrayList<Comment>();
	
	public List<Comment> getCommentList() {
		return this.commentList;
	}
	
	public static CommentList parse(String in) throws XmlPullParserException, AppException {
		CommentList commentList = new CommentList();
		Comment comment = null;
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		factory.setNamespaceAware(false);
		XmlPullParser xmlParser = factory.newPullParser();
		
		try {
			xmlParser.setInput(new StringReader(in));
			int eventType = xmlParser.getEventType();
			while(eventType != XmlPullParser.END_DOCUMENT) {
				String tag = xmlParser.getName();
				switch (eventType) {
				case XmlPullParser.START_TAG:
					if(tag.equalsIgnoreCase("Comment")) {
						comment = new Comment();
					} else {
						if(tag.equalsIgnoreCase("CommentID")) {
							comment.setCommentID(Long.parseLong(xmlParser.nextText()));
						} else if(tag.equalsIgnoreCase("Content")) {
							comment.setText(xmlParser.nextText());
						} else if(tag.equalsIgnoreCase("Author")) {
							comment.setUser(xmlParser.nextText());
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
		} catch(XmlPullParserException e) {
			e.printStackTrace();
			throw AppException.xml(e);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return commentList;
	}
	
}

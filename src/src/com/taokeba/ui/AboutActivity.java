package com.taokeba.ui;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.taokeba.R;
import com.taokeba.common.NetworkUtils;

/**
 * About
 * @Created by zhaolin on 14-2-18.
 * @Updated by Burjal Hou 14-11-3
 * 
 */
public class AboutActivity extends Activity {

	private TextView tvAbout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        
        tvAbout = (TextView) findViewById(R.id.tv_about);
        new AsyncAboutInfo().execute();
        
    }
    
    public class AsyncAboutInfo extends AsyncTask<Void, Void, Boolean> {
    	
    	String data = "";
    	
		@Override
		protected Boolean doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			final String url = NetworkUtils.WEB_SERVER_ADDRESS + "About";
			HttpPost httpRequest = new HttpPost(url);
	        List<NameValuePair> params = new ArrayList<NameValuePair>();
	        try {
	            httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
	            HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
	            if(httpResponse.getStatusLine().getStatusCode() == 200) {
	            	data = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
Log.i("AboutActivity_getInfo", "===about==="+data.toString());	            	
	            } else {
	                Log.e("error...AboutActivity_getInfo", "AboutActivity_getInfo====="+String.valueOf(httpResponse.getStatusLine().getStatusCode()));
	            }
	            parse(data);
	        } catch (UnsupportedEncodingException e) {
	            e.printStackTrace();
	        } catch (ClientProtocolException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        } catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			tvAbout.setText(registedStudents+"\t个注册学生\n"+
					registedTeachers+"个注册教师\n"+
					courses+"\t个课程\n"+
					evaluations+"\tevaluations\n\n"+
					retes+"\t个评分\n"+
					comments+"\t个评论\n"+
					questions+"\t个提问\n"+
					answers+"\t个回答\n\n"+
					active+"\t个在线用户\n"+
					developer+"\t个开发者\n"+
					servers+"\tInfoLab funded servers\n"+
					assu+"\tASSU comment reviewers"+
					"\n\n\n© 数据工程与信息系统实验室"
					//+"\nAndroid developed by: Burjal Hou "+
					//"\n新浪微博：@Burjal"
					);
			
		}
    	
		
    }
    
    public static void parse(String input) throws XmlPullParserException {
    	
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
						if(tag.equalsIgnoreCase("Students")) {
							registedStudents = xmlParser.nextText();
						} else if(tag.equalsIgnoreCase("Teacher")) {
							registedTeachers = xmlParser.nextText();
						} else if(tag.equalsIgnoreCase("Course")) {
							courses = xmlParser.nextText();
						} else if(tag.equalsIgnoreCase("Evaluations")) {
							evaluations = xmlParser.nextText();
						} else if(tag.equalsIgnoreCase("Rate")) {
							retes = xmlParser.nextText();
						} else if(tag.equalsIgnoreCase("Comment")) {
							comments = xmlParser.nextText();
						} else if(tag.equalsIgnoreCase("Questions")) {
							questions = xmlParser.nextText();
						} else if(tag.equalsIgnoreCase("Answers")) {
							answers = xmlParser.nextText();
						} else if(tag.equalsIgnoreCase("Active")) {
							active = xmlParser.nextText();
						} else if(tag.equalsIgnoreCase("Developer")) {
							developer = xmlParser.nextText();
						} else if(tag.equalsIgnoreCase("Server")) {
							servers = xmlParser.nextText();
						} else if(tag.equalsIgnoreCase("Assu")) {
							assu = xmlParser.nextText();
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
    	
	}
    
    private static String registedStudents = "0" ;
    private static String registedTeachers = "0";
    private static  String courses = "0";
    private static  String evaluations = "0";//评估
	
    private static  String retes = "0";
    private static  String comments = "0";
    private static  String questions = "0";
    private static  String answers = "0";
	
    private static  String active = "0";
    private static  String developer = "2";
    private static  String servers = "2";
    private static  String assu = "3";
}

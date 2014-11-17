package com.taokeba.bean;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.taokeba.common.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaolin on 14-2-17.
 */
public class User {

    private int userID;
    private String userName;
    private String studentNumber;
    private String department;
    private String major;
    private String QQ;

    public User(int id) {
        this.userID = id;
    }
    
    public void setUserID(int userID) {
		this.userID = userID;
	}
    
    public void setUserName(String name) {
		this.userName = name;
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
    
    public int getUserID() {
        return this.userID;
    }

    public String getUserName() {
        return this.userName;
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
    
    public void getUserInfo() {
        UserInfo userInfo = new UserInfo(this.userID);
        userInfo.execute((Void)null);
    }
    
    public class UserInfo extends AsyncTask<Void, Void, Boolean> {

        private int id;
        private InputStream result;
        
        private UserInfo(int id) {
            this.id = id;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            String url = "http://10.0.2.2:8080/taokeba/UserInfo";
            HttpPost httpRequest = new HttpPost(url);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("user_id", String.valueOf(id)));
            try {
                httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
                if(httpResponse.getStatusLine().getStatusCode() == 200) {
                    //result = EntityUtils.toString(httpResponse.getEntity());
                	result = httpResponse.getEntity().getContent();
                    Log.v("result", result.toString());
                } else {
                    Log.v("error", String.valueOf(httpResponse.getStatusLine().getStatusCode()));
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
        	User user = null;
        	XmlPullParser xmlParser = Xml.newPullParser();
        	try {
				xmlParser.setInput(result, "UTF-8");
				int evtType = xmlParser.getEventType();
				while(evtType != XmlPullParser.END_DOCUMENT) {
					String tag = xmlParser.getName();
					switch (evtType) {
					case XmlPullParser.START_TAG:
						if(tag.equalsIgnoreCase("userinfo")) {
							user = new User(id);
						} else if(user != null ) {
							if(tag.equalsIgnoreCase("UserID")) {
								user.userID = StringUtils.toInt(xmlParser.nextText(), 0);
							} else if(tag.equalsIgnoreCase("UserName")) {
								user.setUserName(xmlParser.nextText());
							} else if(tag.equalsIgnoreCase("studentNumber")) {
								user.setStudentNumber(xmlParser.nextText());
							} else if(tag.equalsIgnoreCase("department")) {
								user.setDepartment(xmlParser.nextText());
							} else if(tag.equalsIgnoreCase("major")) {
								user.setMajor(xmlParser.nextText());
							} else if(tag.equalsIgnoreCase("QQ")) {
								user.setQQ(xmlParser.nextText());
							}
						}
						break;
					case XmlPullParser.END_DOCUMENT:
						break;
					default:
						break;
					}
					evtType = xmlParser.nextTag();
				}
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					result.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
        }
    }
}

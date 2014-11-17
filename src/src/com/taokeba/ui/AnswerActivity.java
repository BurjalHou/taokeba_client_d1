package com.taokeba.ui;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.xmlpull.v1.XmlPullParserException;

import com.taokeba.R;
import com.taokeba.bean.Answer;
import com.taokeba.bean.AnswerList;
import com.taokeba.bean.HomeQuestion;
import com.taokeba.common.NetworkUtils;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class AnswerActivity extends Activity {

	private int questionID;
	
	private TextView questionText;
	private TextView questionAuthor;
	private TextView questionPostDate;
	
	private ListView answerListView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_answer);
		
		questionID = getIntent().getExtras().getInt("question_id");
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		
		questionText = (TextView) findViewById(R.id.question_text);
		questionAuthor = (TextView) findViewById(R.id.question_author);
		questionPostDate = (TextView) findViewById(R.id.question_post_date);
		
		answerListView = (ListView) findViewById(R.id.question_answer_list_view);
		
		new Answers(questionID).execute();
		//new Question(questionID).execute();
	}
	
	private class Answers extends AsyncTask<Void, Void, Void> {

		private int id;
		private String xml;
		private List<Answer> data = new ArrayList<Answer>();
		
		public Answers(int id) {
			this.id = id;
		}
		
		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			
			String url = NetworkUtils.WEB_SERVER_ADDRESS + "Answer";
			HttpPost httpRequest = new HttpPost(url);
			List<NameValuePair> param = new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("id", String.valueOf(this.id)));
			
			try {
				httpRequest.setEntity(new UrlEncodedFormEntity(param, HTTP.UTF_8));
				HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
				if(httpResponse.getStatusLine().getStatusCode() == 200) {
					xml = EntityUtils.toString(httpResponse.getEntity(), HTTP.UTF_8);
					data = AnswerList.parse(xml).getAnswerList();
				} else {
					Log.v("error", httpResponse.getStatusLine().getStatusCode() + "");
				}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			
			List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
			Map<String, Object> map = null;
			
			for(Answer a : data) {
				map = new HashMap<String, Object>();
				map.put("text", a.getText());
				map.put("author", a.getUser());
				map.put("date", a.getDate());
				list.add(map);
			}
			
			SimpleAdapter adapter = new SimpleAdapter(getApplication(), list, R.layout.question_answer_list_item, 
							new String[] {"text", "author", "date"}, 
							new int[] {R.id.question_answer_text, R.id.question_author,
										R.id.question_post_date});
			
			answerListView.setAdapter(adapter);
			
		}
		
	}
	
	private class Question extends AsyncTask<Void, Void, Void> {

		private int id;
		private String xml;
		private HomeQuestion question;
		
		public Question(int id) {
			this.id = id;
		}
		
		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			String url = NetworkUtils.WEB_SERVER_ADDRESS + "Question";
			HttpPost httpRequest = new HttpPost(url);
			List<NameValuePair> param = new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("id", String.valueOf(this.id)));
			try {
				httpRequest.setEntity(new UrlEncodedFormEntity(param, HTTP.UTF_8));
				HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
				if(httpResponse.getStatusLine().getStatusCode() == 200) {
					xml = EntityUtils.toString(httpResponse.getEntity(), HTTP.UTF_8);
					System.out.println(xml);
					question = HomeQuestion.parse(xml);
				} else {
					Log.v("error", httpResponse.getStatusLine().getStatusCode() + "");
				}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			getActionBar().setTitle(question.getCourseNum() + " " + question.getTitle());
			questionText.setText(question.getText());
			questionAuthor.setText(question.getAuthor());
			questionPostDate.setText(question.getDate());
		}
		
	}
	
}

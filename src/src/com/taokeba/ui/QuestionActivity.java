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

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.taokeba.R;
import com.taokeba.bean.Answer;
import com.taokeba.bean.AnswerList;
import com.taokeba.bean.UserConfig;
import com.taokeba.bean.UserInfo;
import com.taokeba.common.NetworkUtils;

public class QuestionActivity extends Activity {

	private int questionID = 0;
	//bundle 传值，content，date，author
	private String strContent=""; //提问内容
	private String strDate="";//提问日期
	private String strAuthor="";//提问作者
	
	private EditText etAnswer;
	private Button btnAddAnswer;
	
	
	private TextView questionText;
	private TextView questionAuthor;
	private TextView questionPostDate;
	
	private ListView answerListView;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question);
		
		Bundle bundle = getIntent().getExtras();
		questionID = bundle.getInt("question_id");
		strContent = bundle.getString("text");
		strDate = bundle.getString("post_date");
		strAuthor = bundle.getString("author");
		
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		questionText = (TextView) findViewById(R.id.tv_question);
		questionAuthor = (TextView) findViewById(R.id.tv_question_author);
		questionPostDate = (TextView) findViewById(R.id.tv_question_post_date);		
		questionText.setText(strContent);
		questionAuthor.setText("["+strAuthor+"]\t");
		questionPostDate.setText(strDate);		
		
		answerListView = (ListView) findViewById(R.id.lv_question_answer);
		
		new Answers(questionID).execute();
		
		etAnswer = (EditText) findViewById(R.id.et_add_answer);
		btnAddAnswer = (Button) findViewById(R.id.btn_add_answer);
		
		
		btnAddAnswer.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String myAnswer = etAnswer.getText().toString().trim();
				if(myAnswer.length()>0) {
					new AddAnswers(questionID,UserConfig.STUDENT_ID, myAnswer).execute();
				} else {
					Toast.makeText(QuestionActivity.this, R.string.complete_answer_content, Toast.LENGTH_SHORT).show();
				}
			}
		});		
		
		
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
			adapter.notifyDataSetChanged();
		}
		
	}
	
	/**
	 * 添加回复
	 * @author Burjal Hou
	 *
	 */
	private class AddAnswers extends AsyncTask<Void, Void, String> {

		private int question_id;
		private String stu_id;
		private String answer;
		private String xml = "";
		private List<Answer> data = new ArrayList<Answer>();
		
		public AddAnswers(int question_id,String stu_id,String answer) {
			this.question_id = question_id;
			this.stu_id = stu_id;
			this.answer = answer;
		}
		
		@Override
		protected String doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			
			String url = NetworkUtils.WEB_SERVER_ADDRESS + "AddAnswer";
			HttpPost httpRequest = new HttpPost(url);
			List<NameValuePair> param = new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("question_id", String.valueOf(question_id)));
			param.add(new BasicNameValuePair("stu_id", this.stu_id));
			param.add(new BasicNameValuePair("my_answer", this.answer));
Log.i("QuestionAct.....AddAnswer", param.toString());	
			try {
				httpRequest.setEntity(new UrlEncodedFormEntity(param, HTTP.UTF_8));
				HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
				if(httpResponse.getStatusLine().getStatusCode() == 200) {
					xml = EntityUtils.toString(httpResponse.getEntity(), HTTP.UTF_8);
					
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
			}
			
			return xml;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			
			if(result.equals("1")) {
				etAnswer.setText("");
				Toast.makeText(QuestionActivity.this, R.string.add_answer_success, Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(QuestionActivity.this, R.string.add_answer_false, Toast.LENGTH_SHORT).show();
			}
			
			//刷新回复界面
			new Answers(questionID).execute();
			return;
		}
		
	}
}

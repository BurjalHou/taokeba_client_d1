package com.taokeba.fragment;

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

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.taokeba.R;
import com.taokeba.adapter.ViewPagerAdapter;
import com.taokeba.bean.Answer;
import com.taokeba.bean.HomeQuestion;
import com.taokeba.bean.HomeQuestionList;
import com.taokeba.bean.UserConfig;
import com.taokeba.bean.UserInfo;
import com.taokeba.common.NetworkUtils;
import com.taokeba.ui.AnswerActivity;
import com.taokeba.ui.QuestionActivity;

/**
 * 社区
 * 获取消息成功
 * @Created by zhaolin on 14-2-20.
 * 
 * @Updated by Burjal Hou on 14-10-28
 * 
 */

public class CommunityFragment extends Fragment {

	private ViewPager viewPager;
	private PagerTabStrip pagerTabStrip;
	
	private final List<String> titles = new ArrayList<String>();
	private final List<View> views = new ArrayList<View>();
	
	//最新提问
	private SwipeRefreshLayout answerRefreshLayout;
	private ListView questionListView;
	//我的提问
	private SwipeRefreshLayout askRefreshLayout;
	private ListView myQuestionListView;
	
	//打开问答详细界面
	List<Map<String, Object>> allQuestionList = new ArrayList<Map<String, Object>>();
	List<Map<String, Object>> myQuestionList = new ArrayList<Map<String, Object>>();
	
	//我要提问
	private EditText etMyQuestion;
	private Button btnMyQuestion;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_community, container, false);
        
        viewPager = (ViewPager)rootView.findViewById(R.id.community_view_pager);
        pagerTabStrip = (PagerTabStrip)rootView.findViewById(R.id.community_pager_tab_strip);
        
        pagerTabStrip.setTabIndicatorColorResource(R.color.view_pager_indicator);
        
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        
        answerRefreshLayout = (SwipeRefreshLayout) layoutInflater.inflate(R.layout.community_list_answer, null);
        askRefreshLayout = (SwipeRefreshLayout) layoutInflater.inflate(R.layout.community_list_ask, null);
        
        views.add(answerRefreshLayout);
        views.add(askRefreshLayout);
        
        titles.add(getResources().getString(R.string.community_view_pager_title_answer));
        titles.add(getResources().getString(R.string.community_view_pager_title_ask));
        
        viewPager.setAdapter(new ViewPagerAdapter(titles, views));
        
        questionListView = (ListView) answerRefreshLayout.findViewById(R.id.community_anwser_list_view);
        myQuestionListView = (ListView)askRefreshLayout.findViewById(R.id.community_ask_list_view);
        
        //我要提问
        etMyQuestion = (EditText) askRefreshLayout.findViewById(R.id.et_add_question);
        btnMyQuestion = (Button) askRefreshLayout.findViewById(R.id.btn_add_question);
        btnMyQuestion.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String myQues = etMyQuestion.getText().toString().trim();
				if(myQues.length()>0) {					
					new AddMyQuestion(myQues).execute();
				} else {
					Toast.makeText(getActivity(), R.string.question_can_not_null, Toast.LENGTH_SHORT).show();
				}
			}
		});
        
        viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int pos) {
				// TODO Auto-generated method stub
				if(pos == 0) {
					new Questions().execute();
				} else if (pos == 1) {
					new MyQuestions().execute();
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
        
        initAnswer();
        initAsk();
//        setData();
        new Questions().execute();
        
        return rootView;
    }
    
    private void initAnswer() {
    	
    	questionListView.setDividerHeight(0);
    	
    	answerRefreshLayout.setColorScheme(android.R.color.holo_blue_light, android.R.color.holo_red_light,
                android.R.color.holo_green_light, android.R.color.holo_purple);
    	answerRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				new Questions().execute();
			}
		});
    	
    	questionListView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				if(firstVisibleItem == 0) {
					answerRefreshLayout.setEnabled(true);
				} else {
					answerRefreshLayout.setEnabled(false);
				}
			}
		});
    	
    	questionListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int pos,
					long id) {
				// TODO Auto-generated method stub
//				goToAnswer(questionListView, pos);
				goToQuestionDetail(questionListView, pos, 
						Integer.parseInt(allQuestionList.get(pos).get("question_id").toString()), 
						allQuestionList.get(pos).get("text").toString(),
						allQuestionList.get(pos).get("date").toString(),
						allQuestionList.get(pos).get("author").toString());
			}
		});
    }
    
    private void initAsk() {
    	
    	myQuestionListView.setDividerHeight(0);
    	
    	askRefreshLayout.setColorScheme(android.R.color.holo_green_light, android.R.color.holo_red_light,
                android.R.color.holo_green_light, android.R.color.holo_purple);
        
    	askRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				new MyQuestions().execute();
			}
		});
    	myQuestionListView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				if(firstVisibleItem == 0) {
					askRefreshLayout.setEnabled(true);
				} else {
					askRefreshLayout.setEnabled(false);
				}
			}
		});
    	
    	myQuestionListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int pos,
					long id) {
				// TODO Auto-generated method stub
//				goToAnswer(myQuestionListView, pos);
				goToQuestionDetail(myQuestionListView, pos, 
						Integer.parseInt(myQuestionList.get(pos).get("question_id").toString()), 
						myQuestionList.get(pos).get("text").toString(),
						myQuestionList.get(pos).get("date").toString(),
						myQuestionList.get(pos).get("author").toString());
			}
		});
    }
    
    private class Questions extends AsyncTask<Void, Void, Void> {

    	private String data = null;
    	private List<HomeQuestion> questionList = new ArrayList<HomeQuestion>();
    	
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
			String url = NetworkUtils.WEB_SERVER_ADDRESS + "HomeQuestion";
			HttpPost httpRequest = new HttpPost(url);
			
			try {
				HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
				if(httpResponse.getStatusLine().getStatusCode() == 200) {
					data = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
					questionList = HomeQuestionList.parse(data).getHomeQuestionList();
Log.i("CommunityFragment...", "getResponse--->\n"+data.toString());
					
				} else {
					Log.e("CommunityFragment...error", ""+httpResponse.getStatusLine().getStatusCode() );
				}
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
//			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			allQuestionList.clear();
			Map<String, Object> map = null;
			if(!questionList.isEmpty()) {
				for(HomeQuestion q : questionList) {
					map = new HashMap<String, Object>();
					map.put("id", q.getID());
					map.put("course_id", q.getCourseID());
					map.put("num", q.getCourseNum());
					map.put("title", q.getTitle());
					map.put("text", q.getText());
					map.put("date", q.getDate());
					map.put("author",q.getAuthor());
					map.put("question_id", q.getID());
					allQuestionList.add(map);
				}
			}
			SimpleAdapter adapter = new SimpleAdapter(getActivity(), allQuestionList, R.layout.home_question_list_item, 
					new String[] {"id", "course_id", "num", "title", "text", "date"}, 
					new int[] {R.id.home_question_item_id, R.id.home_question_course_id, 
								R.id.home_question_course_num, R.id.home_question_course_name, 
								R.id.home_question_text, R.id.home_question_date});
			questionListView.setAdapter(adapter);
			
			answerRefreshLayout.setRefreshing(false);
			Toast.makeText(getActivity(), R.string.refresh_complete, Toast.LENGTH_SHORT).show();
			adapter.notifyDataSetChanged();
			
		}
    	
    }
    
    private class MyQuestions extends AsyncTask<Void, Void, String> {

    	private String data = "";
    	private List<HomeQuestion> questionList = new ArrayList<HomeQuestion>();
    	
		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
			String url = NetworkUtils.WEB_SERVER_ADDRESS + "Questions";
			HttpPost httpRequest = new HttpPost(url);
			List<NameValuePair> param = new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("id", String.valueOf(UserInfo.getUserInfo().getID())));
			try {
				httpRequest.setEntity(new UrlEncodedFormEntity(param, HTTP.UTF_8));
				HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
				if(httpResponse.getStatusLine().getStatusCode() == 200) {
Log.i("===============", "start===get === my == question===");
					data = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
					questionList = HomeQuestionList.parse(data).getHomeQuestionList();
Log.i("CommunityFragment...", "getResponse--->\n"+data.toString());
					
				} else {
					Log.e("CommunityFragment...error..myquestion", ""+httpResponse.getStatusLine().getStatusCode() );
				}
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
			
			return data;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
//			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			myQuestionList.clear();
			Map<String, Object> map = null;
			if(!questionList.isEmpty()) {
				for(HomeQuestion q : questionList) {
					map = new HashMap<String, Object>();
					map.put("id", q.getID());
					map.put("course_id", q.getCourseID());
					map.put("num", q.getCourseNum());
					map.put("title", q.getTitle());
					map.put("text", q.getText());
					map.put("date", q.getDate());
					map.put("author",q.getAuthor());
					map.put("question_id", q.getID());
					myQuestionList.add(map);
				}
			}
			SimpleAdapter adapter = new SimpleAdapter(getActivity(), myQuestionList, R.layout.home_question_list_item, 
					new String[] {"id", "course_id", "num", "title", "text", "date"}, 
					new int[] {R.id.home_question_item_id, R.id.home_question_course_id, 
								R.id.home_question_course_num, R.id.home_question_course_name, 
								R.id.home_question_text, R.id.home_question_date});
			myQuestionListView.setAdapter(adapter);
			
			askRefreshLayout.setRefreshing(false);
Toast.makeText(getActivity(), "gerResponse:"+result, Toast.LENGTH_SHORT).show();
			Toast.makeText(getActivity(), R.string.refresh_complete, Toast.LENGTH_SHORT).show();
			adapter.notifyDataSetChanged();
			
		}
    	
    }
    
    /**
     * @deprecated
     * @param listView
     * @param pos
     */
    private void goToAnswer(ListView listView, int pos) {
    	Map<String, Object> item = (Map<String, Object>) listView.getItemAtPosition(pos);
    	int id = (Integer) item.get("id");
    	Intent intent = new Intent(getActivity(), AnswerActivity.class);
    	Bundle bundle = new Bundle();
    	bundle.putInt("question_id", id);
    	intent.putExtras(bundle);
    	startActivity(intent);
    }
    
    //打开问答详情界面
    //添加回复
    private void goToQuestionDetail(ListView listView,int pos,
    		int questionID,String content,String date,String author) {
    	Map<String,Object> item = (Map<String,Object>)listView.getItemAtPosition(pos);
    	Intent intent = new Intent(getActivity(),QuestionActivity.class);
    	Bundle bundle = new Bundle();
    	bundle.putInt("question_id", questionID);
    	bundle.putString("text", content);
    	bundle.putString("post_date", date);
    	bundle.putString("author", author);
    	intent.putExtras(bundle);
    	startActivity(intent);
    }
    
    private void setData() {
    	List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;
		
//		map = new HashMap<String, Object>();
//		map.put("num", 321101);
//		map.put("course_id", 321101);
//		map.put("title", "体育课");
//		map.put("text", "test question");
//		map.put("date", "2014-6-21 15:23:52");
//		list.add(map);
	
		SimpleAdapter adapter = new SimpleAdapter(getActivity(), list, R.layout.home_question_list_item, 
				new String[] {"id", "course_id", "num", "title", "text", "date"}, 
				new int[] {R.id.home_question_item_id, R.id.home_question_course_id, 
							R.id.home_question_course_num, R.id.home_question_course_name, 
							R.id.home_question_text, R.id.home_question_date});
		questionListView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
    }
    
    /**
	 * 添加提问
	 * @add  1：添加成功  2：添加失败
	 * @author Burjal Hou
	 * @date 14-11-3
	 */
	private class AddMyQuestion extends AsyncTask<Void, Void, String> {

		private String stu_id;
		private String content;
		private String xml = "";
		private List<Answer> data = new ArrayList<Answer>();
		
		public AddMyQuestion(String content) {
			this.content = content;
			stu_id = String.valueOf(UserConfig.STUDENT_ID);
		}
		
		@Override
		protected String doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			
			String url = NetworkUtils.WEB_SERVER_ADDRESS + "AddQuestion";
			HttpPost httpRequest = new HttpPost(url);
			List<NameValuePair> param = new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("stu_id", this.stu_id));
			param.add(new BasicNameValuePair("question", this.content));
Log.i("CommunityFragment.....AddQuestion", param.toString());	
			try {
				httpRequest.setEntity(new UrlEncodedFormEntity(param, HTTP.UTF_8));
				HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
				if(httpResponse.getStatusLine().getStatusCode() == 200) {
					xml = EntityUtils.toString(httpResponse.getEntity(), HTTP.UTF_8);
Log.i("getResponse---->", xml);					
				} else {
					Log.e("CommunityFragment...error", "Can't add my Quseriong-----"+httpResponse.getStatusLine().getStatusCode() + "");
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
				etMyQuestion.setText("");
				Toast.makeText(getActivity(), R.string.add_question_success, Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getActivity(), R.string.add_question_false, Toast.LENGTH_SHORT).show();
			}
			
			//刷新我的提问界面
			new MyQuestions().execute();
			return;
		}
		
	}
    
}

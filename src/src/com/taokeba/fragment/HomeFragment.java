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

import android.annotation.SuppressLint;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.taokeba.R;
import com.taokeba.adapter.ViewPagerAdapter;
import com.taokeba.bean.HomeComment;
import com.taokeba.bean.HomeCommentList;
import com.taokeba.bean.HomeQuestion;
import com.taokeba.bean.HomeQuestionList;
import com.taokeba.bean.Section;
import com.taokeba.bean.SectionRank;
import com.taokeba.bean.UserInfo;
import com.taokeba.common.Config;
import com.taokeba.common.NetworkUtils;
import com.taokeba.ui.AnswerActivity;
import com.taokeba.ui.QuestionActivity;
import com.taokeba.ui.SectionActivity;

/**
 * Created by zhaolin on 14-2-18.
 * 
 * HomeQuestions 暂未修改   14-10-27
 * 
 */
@SuppressLint("ValidFragment")
public class HomeFragment extends Fragment {

    private ViewPager viewPager;
    private PagerTabStrip pagerTabStrip;

    private ListView homeListView;
    private ListView answerListView;
    private ListView commentListView;
    
    private SwipeRefreshLayout homeRefreshLayout;
    private SwipeRefreshLayout anwserRefreshLayout;
    private SwipeRefreshLayout commentRefreshLayout;
    
    private final List<View> views = new ArrayList<View>();
    private final List<String> titles = new ArrayList<String>();
    
    private List<Map<String, Object>> questionItemList = new ArrayList<Map<String, Object>>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        viewPager = (ViewPager)(rootView.findViewById(R.id.home_view_pager));
        pagerTabStrip = (PagerTabStrip)(rootView.findViewById(R.id.home_pager_tab_strip));
        pagerTabStrip.setTabIndicatorColorResource(R.color.view_pager_indicator);
        
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

        homeRefreshLayout = (SwipeRefreshLayout) layoutInflater.inflate(R.layout.home_course_list_view, null);
        anwserRefreshLayout = (SwipeRefreshLayout) layoutInflater.inflate(R.layout.home_question_list_view, null);
        commentRefreshLayout = (SwipeRefreshLayout) layoutInflater.inflate(R.layout.home_comment_list_view, null);
        
        homeListView = (ListView) homeRefreshLayout.findViewById(R.id.home_course_list);
        answerListView = (ListView) anwserRefreshLayout.findViewById(R.id.home_anwser_list);
        commentListView = (ListView) commentRefreshLayout.findViewById(R.id.home_comment_list);
        
        homeInit();
        anwserInit();
        commentInit();
        
        new HomeCourses().execute();
        
        views.add(homeRefreshLayout);
        views.add(commentRefreshLayout);
        views.add(anwserRefreshLayout);
        
        titles.add(getResources().getString(R.string.home_view_pager_title_main));
        titles.add(getResources().getString(R.string.home_view_pager_title_comment));
        titles.add(getResources().getString(R.string.home_view_pager_title_answer));
        
        viewPager.setAdapter(new ViewPagerAdapter(titles, views));
        
        viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int pos) {
				// TODO Auto-generated method stub
				if(pos == 0) {
					if(homeListView.getChildCount() == 0) {
						new HomeCourses().execute();
					}
		        } else if(pos == 1) {
		        	if(commentListView.getChildCount() == 0) {
		        		new HomeComments(UserInfo.getUserInfo().getID()).execute();
		        	}
		        } else if(pos == 2) {
		        	if(answerListView.getChildCount() == 0) {
		        		new HomeQuestions().execute();
		        	}
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
        
        return rootView;
    }

    private void homeInit() {
    	homeListView.setDividerHeight(0);
    	
    	homeListView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				if(firstVisibleItem == 0) {
					homeRefreshLayout.setEnabled(true);
				} else {
					homeRefreshLayout.setEnabled(false);
				}
			}
		});
    	
    	homeListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int pos,
					long id) {
				// TODO Auto-generated method stub
Log.i("HomeFragment", "click item position-->"+pos+"...");
				goToSection(homeListView, pos);
			}
		});
    	
    	homeRefreshLayout.setEnabled(false);
         homeRefreshLayout.setColorScheme(android.R.color.holo_green_light, android.R.color.holo_red_light,
                android.R.color.holo_green_light, android.R.color.holo_purple);
        
        homeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				new HomeCourses().execute();
			}
		});
        
    }
    
    private void anwserInit() {
    	answerListView.setDividerHeight(0);
    	
    	answerListView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				if(firstVisibleItem == 0) {
					anwserRefreshLayout.setEnabled(true);
				} else {
					anwserRefreshLayout.setEnabled(false);
				}
			}
		});
    	
		anwserRefreshLayout.setColorScheme(android.R.color.holo_green_light, android.R.color.holo_red_light,
                android.R.color.holo_green_light, android.R.color.holo_purple);
		anwserRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				new HomeQuestions().execute();
			}
		});
		
		answerListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int pos,
					long id) {
				// TODO Auto-generated method stub
				goToQuestionDetail(answerListView, pos);
			}
		});
		
	}
    
    private void commentInit() {
    	
    	commentListView.setDividerHeight(0);
    	
    	commentListView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				if(firstVisibleItem == 0) {
					commentRefreshLayout.setEnabled(true);
				} else {
					commentRefreshLayout.setEnabled(false);
				}
			}
		});
    	
		commentRefreshLayout.setColorScheme(android.R.color.holo_green_light, android.R.color.holo_red_light,
                android.R.color.holo_green_light, android.R.color.holo_purple);
		commentRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				new HomeComments(UserInfo.getUserInfo().getID());
			}
		});
		
		commentListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int pos,
					long id) {
				// TODO Auto-generated method stub
				goToSection(commentListView, pos);
			}
		});
		
    }
    
    private class HomeCourses extends AsyncTask<Void, Void, Boolean> {

    	private String data;
    	private List<Section> list;
    	
		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub
			String url = NetworkUtils.WEB_SERVER_ADDRESS + "Rank";
			HttpPost httpRequest = new HttpPost(url);
			List<NameValuePair> param = new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("type", "rate"));
			
			try {
				httpRequest.setEntity(new UrlEncodedFormEntity(param, HTTP.UTF_8));
				HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
				if(httpResponse.getStatusLine().getStatusCode() == 200) {
					data = EntityUtils.toString(httpResponse.getEntity(), HTTP.UTF_8);
					list = SectionRank.parse(data).getSectionList();
Log.i("HomeFragment", "getResponse:\n"+data.toString());
				} else {
					Log.v("HomeFrag...error", String.valueOf(httpResponse.getStatusLine().getStatusCode()));
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
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
			Map<String, Object> map = null;
			
			for (Section s : list) {
				map = new HashMap<String, Object>();
				map.put("id", s.getSectionID());
				map.put("num", s.getCourseNum());
				map.put("title", s.getTitle());
				map.put("term", s.getTerm());
				map.put("department", s.getDepartment());
				map.put("instructor", s.getInstructor());
				map.put("exempt", s.getExempt());
				map.put("rate", s.getRate());
				data.add(map);
			}
		
			SimpleAdapter adapter = new SimpleAdapter(getActivity(), data, R.layout.rank_list_rate_item,
					new String[] {"sectionId", "num", "title", "term", "department", "instructor", "exempt", "rate"},
					new int[] {R.id.rank_rate_section_id, R.id.rank_rate_course_num,
								R.id.rank_rate_course_title, R.id.rank_rate_term,
								R.id.rank_rate_department, R.id.rank_rate_instructor,
								R.id.rank_rate_exempt, R.id.rank_rate});
			homeListView.setAdapter(adapter);
			homeRefreshLayout.setRefreshing(false);
			Toast.makeText(getActivity(), R.string.refresh_complete, Toast.LENGTH_SHORT).show();
			adapter.notifyDataSetChanged();
			
		}
    	
    }
    
    private class HomeComments extends AsyncTask<Void, Void, Void> {
    	
    	private String data = null;
    	private List<HomeComment> list = null;
    	
    	private int id;
    	
    	private HomeComments(int id) {
    		this.id = id;
    	}
    	
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			String url = NetworkUtils.WEB_SERVER_ADDRESS + "HomeComment";
			HttpPost httpRequest = new HttpPost(url);
			List<NameValuePair> param = new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("id", String.valueOf(this.id)));
System.out.println("id->" + UserInfo.getUserInfo().getID());
			try {
				httpRequest.setEntity(new UrlEncodedFormEntity(param));
				HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
				if(httpResponse.getStatusLine().getStatusCode() == 200) {
					data = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
					list = HomeCommentList.parse(data).getCommentList();
Log.i("HomeFragment....", "get comment content--------->\n"+data.toString()+"\nlist.size():"+list.size());
				} else {
					Log.e("error...", "in HOMEFRAGMENT to get COMMENT CONTENT**********\n"+httpResponse.getStatusLine().getStatusCode() + "");
				}
			} catch(IOException e) {
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
			List<Map<String, Object>> comments = new ArrayList<Map<String,Object>>();
			Map<String, Object> map = null;
			
			for(HomeComment c : list) {
				map = new HashMap<String, Object>();
				map.put("id", c.getSectionID());
				map.put("num", c.getCourseNum());
				map.put("title", c.getTitle());
				map.put("text", c.getText());
				map.put("date", c.getDate());
				comments.add(map);
Log.i("map.....", map.get("id")+";\t"+map.get("num")+";\t"+map.get("title")+";\t"+map.get("text")+";\t"+map.get("date"));
			}
			
			SimpleAdapter adapter = new SimpleAdapter(getActivity(), comments, R.layout.home_comment_list_item,
					new String[]{"id", "num", "title", "text", "date"},
					new int[] {R.id.home_comment_section_id, R.id.home_comment_course_num,
								R.id.home_comment_course_name, R.id.home_comment_text, R.id.home_comment_date});
			commentListView.setAdapter(adapter);
			commentRefreshLayout.setRefreshing(false);
			Toast.makeText(getActivity(), R.string.refresh_complete, Toast.LENGTH_SHORT).show();
			adapter.notifyDataSetChanged();
		}
    	
    }
    
    private class HomeQuestions extends AsyncTask<Void, Void, Void> {

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
Log.i("HomeFragment", "Question.Response-->\n"+data.toString()+"\nQuestionList-->"+questionList.toString());
					questionList.clear();
					questionList = HomeQuestionList.parse(data).getHomeQuestionList();
Log.i("HomeFragment", "size:"+questionList.size());
				} else {
					Log.e("HomeFra.->Question<-error..", httpResponse.getStatusLine().getStatusCode() + "");
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
			questionItemList.clear();
			Map<String, Object> map = null;
			
			for(HomeQuestion q : questionList) {
Log.i("question--list", q.toString());
				map = new HashMap<String, Object>();
				map.put("id", q.getCourseID());
				map.put("num", q.getCourseNum());
				map.put("title", q.getTitle());
				map.put("text", q.getText());
				map.put("date", q.getDate());
				map.put("author", q.getAuthor());
				map.put("question_id", q.getID());
				questionItemList.add(map);
			}
			
			SimpleAdapter adapter = new SimpleAdapter(getActivity(), questionItemList, R.layout.home_question_list_item, 
					new String[] {"id", "num", "title", "text", "date"}, 
					new int[] {R.id.home_question_course_id, R.id.home_question_course_num,
								R.id.home_question_course_name, R.id.home_question_text,
								R.id.home_question_date});
			answerListView.setAdapter(adapter);
			anwserRefreshLayout.setRefreshing(false);
			Toast.makeText(getActivity(), R.string.refresh_complete, Toast.LENGTH_SHORT).show();
			adapter.notifyDataSetChanged();
			
		}
    	
    }
    
    private void goToSection(ListView listView, int pos) {
    	Map<String, Object> item = (Map<String, Object>) listView.getItemAtPosition(pos);
    	int id = (Integer) item.get("id");
    	Config.setSECTION_ID(id);
Log.i("HomeFragment", "*****section id-->"+id+"*********");
    	Intent intent = new Intent(getActivity(), SectionActivity.class);
    	startActivity(intent);
    }
    
    //打开问答详情界面
    private void goToQuestionDetail(ListView listView, int pos) {
    	Map<String, Object> item = (Map<String, Object>) listView.getItemAtPosition(pos);
    	int course_id = (Integer) item.get("id");
    	String text = (String)item.get("text");
    	String date = (String)item.get("date");
    	String author = (String)questionItemList.get(pos).get("author");
    	int question_id = Integer.parseInt(questionItemList.get(pos).get("question_id").toString());
    	Intent intent = new Intent(getActivity(), QuestionActivity.class);
    	Bundle bundle = new Bundle();
    	bundle.putInt("question_id", question_id);
    	bundle.putString("text", text);
    	bundle.putString("post_date", date);
    	bundle.putString("author", author);
Log.i("----start activity:", "QuestionActivity.class"+"--->id="+question_id+"\t________from HomeFragment.java in MainActivity.class");
    	intent.putExtras(bundle);
    	startActivity(intent);
	}
    
}

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
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.taokeba.R;
import com.taokeba.adapter.ViewPagerAdapter;
import com.taokeba.bean.Section;
import com.taokeba.bean.SectionRank;
import com.taokeba.common.Config;
import com.taokeba.common.NetworkUtils;
import com.taokeba.ui.SectionActivity;

/**
 * 课程排行
 * 
 *  @Created by zhaolin on 14-2-18.
 *    
 *  @Updated by Burjal Hou on 14-10-27.
 * 
 */
public class RankFragment extends Fragment {

    private ViewPager viewPager;
    private PagerTabStrip pagerTabStrip;

    private SwipeRefreshLayout rateRefreshLayout;
    private SwipeRefreshLayout commentRefreshLayout;
    private SwipeRefreshLayout popularRefreshLayout;
    
    private ListView rateListView;
    private ListView commentListView;
    private ListView popularListView;
    
    private final List<View> views = new ArrayList<View>();
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_rank, container, false);

        viewPager = (ViewPager)(rootView.findViewById(R.id.rank_view_pager));
        pagerTabStrip = (PagerTabStrip)(rootView.findViewById(R.id.rank_pager_tab_strip));
        pagerTabStrip.setTabIndicatorColorResource(R.color.view_pager_indicator);

        //Google下拉刷新
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        rateRefreshLayout = (SwipeRefreshLayout)layoutInflater.inflate(R.layout.fragment_rank_list_rate, null);
        commentRefreshLayout = (SwipeRefreshLayout)layoutInflater.inflate(R.layout.fragment_rank_list_comment, null);
        popularRefreshLayout = (SwipeRefreshLayout)layoutInflater.inflate(R.layout.fragment_rank_list_popular, null);

        rateListView = (ListView) rateRefreshLayout.findViewById(R.id.section_rate_list);
        commentListView = (ListView) commentRefreshLayout.findViewById(R.id.section_comment_list);
        popularListView = (ListView) popularRefreshLayout.findViewById(R.id.section_popular_list);
        
        //调用notifyDataSetChanged()方法会重新调用BaseAdapter的getView()方法
        rateListView.setDividerHeight(0);
        commentListView.setDividerHeight(0);
        popularListView.setDividerHeight(0);
        
        views.add(rateRefreshLayout);
        views.add(commentRefreshLayout);
        views.add(popularRefreshLayout);

        final ArrayList<String> titles = new ArrayList<String>();
        titles.add(getResources().getString(R.string.rank_view_pager_title_rate));
        titles.add(getResources().getString(R.string.rank_view_pager_title_comment));
        titles.add(getResources().getString(R.string.rank_view_pager_title_mark));
        
        new SectionRankByRate().execute();
        
        viewPager.setAdapter(new ViewPagerAdapter(titles, views));
        
        viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int pos) {
				// TODO Auto-generated method stub
				if(pos == 0) {
		        	new SectionRankByRate().execute();
		        } else if(pos == 1) {
		        	new SectionRankByComment().execute();
		        } else if(pos == 2) {
		        	new SectionRankByPopular().execute();
		        }
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
		});
        
        initRateList();
        initCommentList();
        initPopularList();
        
        return rootView;
    }
    
    private void initRateList() {
    	
    	rateListView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				if(firstVisibleItem == 0) {
					rateRefreshLayout.setEnabled(true);
				} else {
					rateRefreshLayout.setEnabled(false);
				}
			}
		});
    	
    	rateRefreshLayout.setColorScheme(android.R.color.holo_green_light, android.R.color.holo_red_light,
                android.R.color.holo_green_light, android.R.color.holo_purple);
        
    	rateRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				new SectionRankByRate().execute();
			}
		});
    	
    	
    	rateListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int pos,
					long id) {
				goToSection(rateListView, pos);
			}
		});
    }
    
    private void initCommentList() {
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
					rateRefreshLayout.setEnabled(true);
				} else {
					rateRefreshLayout.setEnabled(false);
				}
			}
		});
    	
    	commentRefreshLayout.setColorScheme(android.R.color.holo_green_light, android.R.color.holo_red_light,
                android.R.color.holo_green_light, android.R.color.holo_purple);
        
    	commentRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				
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
    
    private void initPopularList() {
    	popularListView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				if(firstVisibleItem == 0) {
					rateRefreshLayout.setEnabled(true);
				} else {
					rateRefreshLayout.setEnabled(false);
				}
			}
		});
    	
    	popularRefreshLayout.setColorScheme(android.R.color.holo_green_light, android.R.color.holo_red_light,
                android.R.color.holo_green_light, android.R.color.holo_purple);
        
    	popularRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				
			}
		});
    	
    	popularListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int pos,
					long id) {
				// TODO Auto-generated method stub
				goToSection(popularListView, pos);
			}
		});
    }
    
    //按评分
    private class SectionRankByRate extends AsyncTask<Void, Void, Boolean> {
    	private String data = "";
    	private List<Section> list = new ArrayList<Section>();
    	
		@Override
		protected Boolean doInBackground(Void... params) {
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
Log.i("RankFragment...", "SectionRankByRate-按评分->\n"+data.toString());
				} else {
					Log.v("error", String.valueOf(httpResponse.getStatusLine().getStatusCode()));
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (XmlPullParserException e) {
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
					new String[] {"id", "num", "title", "term", "department", "instructor", "exempt", "rate"},
					new int[] {R.id.rank_rate_section_id, R.id.rank_rate_course_num,
								R.id.rank_rate_course_title, R.id.rank_rate_term,
								R.id.rank_rate_department, R.id.rank_rate_instructor,
								R.id.rank_rate_exempt, R.id.rank_rate});
			rateListView.setAdapter(adapter);
			rateRefreshLayout.setRefreshing(false);
			adapter.notifyDataSetChanged();
		}
    	
    }
    
    //按评论
    private class SectionRankByComment extends AsyncTask<Void, Void, Boolean> {
    	private String data = "";
    	private List<Section> list = new ArrayList<Section>();
    	
		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub
			String url = NetworkUtils.WEB_SERVER_ADDRESS + "Rank";
			HttpPost httpRequest = new HttpPost(url);
			List<NameValuePair> param = new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("type", "com"));
			
			try {
				httpRequest.setEntity(new UrlEncodedFormEntity(param, HTTP.UTF_8));
				HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
				if(httpResponse.getStatusLine().getStatusCode() == 200) {
					data = EntityUtils.toString(httpResponse.getEntity(), HTTP.UTF_8);
					list = SectionRank.parse(data).getSectionList();
Log.i("RankFragment...", "SectionRankByComment-按评论->\n"+data.toString());
				} else {
					Log.v("error", String.valueOf(httpResponse.getStatusLine().getStatusCode()));
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (XmlPullParserException e) {
				e.printStackTrace();
			}
			
			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
			Map<String, Object> map = null;
			
			for(Section s : list) {
				map = new HashMap<String, Object>();
				map.put("id", s.getSectionID());
				map.put("num", s.getCourseNum());
				map.put("title", s.getTitle());
				map.put("term", s.getTerm());
				map.put("department", s.getDepartment());
				map.put("instructor", s.getInstructor());
				map.put("exempt", s.getExempt());
				map.put("commentNum", s.getCommentNum());
				data.add(map);
			}
			SimpleAdapter adapter = new SimpleAdapter(getActivity(), data, R.layout.rank_list_comment_item,
					new String[] {"id", "num", "title", "term", "department", "instructor", "exempt", "commentNum"},
					new int[] {R.id.rank_comment_section_id, R.id.rank_comment_course_num,
								R.id.rank_comment_course_title, R.id.rank_comment_term,
								R.id.rank_comment_department, R.id.rank_comment_instructor,
								R.id.rank_comment_exempt, R.id.rank_comment});
			commentListView.setAdapter(adapter);
			commentRefreshLayout.setRefreshing(false);
			adapter.notifyDataSetChanged();
		}
    	
    }
    
    //按平均分
    private class SectionRankByPopular extends AsyncTask<Void, Void, Boolean> {
    	private String data = "";
    	private List<Section> list = new ArrayList<Section>();
    	
		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub
			String url = NetworkUtils.WEB_SERVER_ADDRESS + "Rank";
			HttpPost httpRequest = new HttpPost(url);
			List<NameValuePair> param = new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("type", "pop"));
			
			try {
				httpRequest.setEntity(new UrlEncodedFormEntity(param, HTTP.UTF_8));
				HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
				if(httpResponse.getStatusLine().getStatusCode() == 200) {
					data = EntityUtils.toString(httpResponse.getEntity(), HTTP.UTF_8);
//String temp = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Sections><Section><SectionID>6</SectionID>" +
//		"<Term>2014秋</Term><CourseID>671</CourseID><CourseNum>SE3005</CourseNum><Title>离散数学</Title>" +
//		"<Unit>4.5</Unit><Type>学位课</Type><Department>软件学院 </Department><InstructorID>335</InstructorID>" +
//		"<Instructor>黄健斌</Instructor><AvgGrade>-1</AvgGrade><Exempt>学位课</Exempt><Rate>9.872</Rate>" +
//		"<CommentNum>76</CommentNum></Section><Section><SectionID>5</SectionID><Term>2014秋</Term>" +
//		"<CourseID>671</CourseID><CourseNum>SE3005</CourseNum><Title>离散数学</Title><Unit>4.5</Unit>" +
//		"<Type>学位课</Type><Department>软件学院 </Department><InstructorID>335</InstructorID>" +
//		"<Instructor>黄健斌</Instructor><AvgGrade>-1</AvgGrade><Exempt>学位课</Exempt><Rate>9.80983606557377</Rate>" +
//		"<CommentNum>63</CommentNum></Section><Section><SectionID>1</SectionID><Term>2014秋</Term>" +
//		"<CourseID>704</CourseID><CourseNum>SE5036</CourseNum><Title>数据分析与挖掘</Title><Unit>3.0</Unit>" +
//		"<Type>任选课</Type><Department>软件学院 </Department><InstructorID>335</InstructorID><Instructor>黄健斌</Instructor>" +
//		"<AvgGrade>-1</AvgGrade><Exempt>任选课</Exempt><Rate>9.2</Rate><CommentNum>1</CommentNum></Section></Sections>";
					
					list = SectionRank.parse(data).getSectionList();
Log.i("RankFragment...", "SectionRankByPopular-按平均分->\n"+data.toString());
				} else {
					Log.v("error", String.valueOf(httpResponse.getStatusLine().getStatusCode()));
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
			List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
			Map<String, Object> map = null;
			
			for(Section s : list) {
				map = new HashMap<String, Object>();
				map.put("id", s.getSectionID());
				map.put("num", s.getCourseNum());
				map.put("title", s.getTitle());
				map.put("term", s.getTerm());
				map.put("department", s.getDepartment());
				map.put("instructor", s.getInstructor());
				map.put("exempt", s.getExempt());
				map.put("grade", s.getGrade());
				data.add(map);
			}
			SimpleAdapter adapter = new SimpleAdapter(getActivity(), data, R.layout.rank_list_popular_item,
					new String[] {"id", "num", "title", "term", "department", "instructor", "exempt", "grade"},
					new int[] {R.id.rank_popular_section_id, R.id.rank_popular_course_num,
								R.id.rank_popular_course_title, R.id.rank_popular_term,
								R.id.rank_popular_department, R.id.rank_popular_instructor,
								R.id.rank_popular_exempt, R.id.rank_popular});
			popularListView.setAdapter(adapter);
			popularRefreshLayout.setRefreshing(false);
			adapter.notifyDataSetChanged();
		}
    	
    }
    
    private void goToSection(ListView listView, int pos) {
Log.i("RankFragment...", "item click-->\nlistview=="+listView.toString()+"\nposition=="+pos);
    	Map<String, Object> item = (Map<String, Object>) listView.getItemAtPosition(pos);
    	int id = (Integer) item.get("id");
    	Intent intent = new Intent(getActivity(), SectionActivity.class);
    	Config.setSECTION_ID(id);
    	startActivity(intent);
    }

}

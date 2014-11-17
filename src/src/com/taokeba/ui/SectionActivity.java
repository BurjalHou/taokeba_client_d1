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
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.taokeba.AppException;
import com.taokeba.R;
import com.taokeba.adapter.ViewPagerAdapter;
import com.taokeba.bean.Comment;
import com.taokeba.bean.CommentList;
import com.taokeba.bean.HomeQuestion;
import com.taokeba.bean.HomeQuestionList;
import com.taokeba.bean.Section;
import com.taokeba.bean.UserConfig;
import com.taokeba.bean.UserInfo;
import com.taokeba.common.Config;
import com.taokeba.common.NetworkUtils;

/**
 * 选中课程
 * @add 添加提问；item->回答提问(QuestionActivity.class)
 * @Created by zhaolin on 14-3-4.
 *    
 * @Updated by Burjal Hou on 14-10-27.
 * 
 * 
 */

public class SectionActivity extends Activity {

    private int sectionID = 0;//课程编号
    
    private ViewPager viewPager;
    private PagerTabStrip pagerTabStrip;
    
    private SwipeRefreshLayout coursewareRefresh;//课件
    private SwipeRefreshLayout commentRefresh;//评论
    private SwipeRefreshLayout questionRefresh;//问答
    
    private TextView courseNum;//课程编号
    private TextView sectionTitle;//课程名称
    private TextView sectionTerm;//开课学期
    private TextView sectionDepartment;//开科学院
    private TextView instructor;//主讲教师
    private TextView description;//
    private TextView sectionRate;//课程评分
    private TextView sectionGrade;//平均成绩
    private TextView sectionUnits;//
    private TextView sectionType;//课程类型
    private TextView sectionTime;//上课时间
    private TextView sectionLocation;//上课地点
    private TextView sectionAvgGrade;//详细分布(进入JS交互Activity)
    
    private ListView commentListView;//
    private ListView questionListView;//
    private ListView coursewareListView;//课件listview
    
    private List<View> views = new ArrayList<View>();//
    private List<String> titles = new ArrayList<String>();//
    
    private int sectionTermID;//
    private String sectionClassNum;//
    private int courseID;//
    
    //发表评论
    private EditText etAddComment;//
    private Button btnAddComment;//
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section);

        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        sectionID = Config.getSECTION_ID();
        
        viewPager = (ViewPager) findViewById(R.id.section_view_pager);
        pagerTabStrip = (PagerTabStrip) findViewById(R.id.section_pager_tab_strip);
        pagerTabStrip.setTabIndicatorColorResource(R.color.view_pager_indicator);
        
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        
        View sectionCommonInfo = (GridLayout) inflater.inflate(R.layout.section_common_info, null);
        coursewareRefresh = (SwipeRefreshLayout) inflater.inflate(R.layout.section_info_courseware_list, null);
        commentRefresh = (SwipeRefreshLayout) inflater.inflate(R.layout.section_info_comment_list, null);
        questionRefresh = (SwipeRefreshLayout) inflater.inflate(R.layout.section_info_question_list, null);
        
        coursewareListView = (ListView)coursewareRefresh.findViewById(R.id.section_info_courseware_pdf_list);
        commentListView = (ListView) commentRefresh.findViewById(R.id.section_info_comment_list);
        questionListView = (ListView) questionRefresh.findViewById(R.id.section_info_question_list);
        
        views.add(sectionCommonInfo);
        views.add(coursewareRefresh);
        views.add(commentRefresh);
        views.add(questionRefresh);
        
        titles.add(getResources().getString(R.string.section_info_title));
        titles.add(getResources().getString(R.string.section_info_courseware));
        titles.add(getResources().getString(R.string.section_comment_title));
        titles.add(getResources().getString(R.string.section_question_title));
        
        
        viewPager.setAdapter(new ViewPagerAdapter(titles, views));
        
        viewPager.setOnPageChangeListener(new OnPageChangeListener() {			
			@Override
			public void onPageSelected(int pos) {
				if(pos == 0) {
					new SectionInfo(sectionID).execute();
				} else if(pos == 1) {
//					new SectionCourseware(section).execute();
				} else if(pos == 2) {
					if(commentListView.getChildCount()==0) {
						//获取评论列表
						new SectionComment(sectionID).execute();						
					}
				} else if(pos == 3) {
					if(questionListView.getChildCount()==0) {
						//获取问答列表
						new SectionQuestions().execute();	
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
        
        courseNum = (TextView) sectionCommonInfo.findViewById(R.id.section_course_num);
        sectionTitle = (TextView) sectionCommonInfo.findViewById(R.id.section_title);
        sectionTerm = (TextView) sectionCommonInfo.findViewById(R.id.section_term);
        sectionDepartment = (TextView) sectionCommonInfo.findViewById(R.id.section_department);
        //instructorID = (TextView) findViewById(R.id.section_instructor_id);
        instructor = (TextView) sectionCommonInfo.findViewById(R.id.section_instructor);
        description = (TextView) sectionCommonInfo.findViewById(R.id.section_description);
        sectionRate = (TextView) sectionCommonInfo.findViewById(R.id.section_rate);
        sectionGrade = (TextView) sectionCommonInfo.findViewById(R.id.section_grade);
        sectionUnits = (TextView) sectionCommonInfo.findViewById(R.id.section_unit);
        sectionType = (TextView) sectionCommonInfo.findViewById(R.id.section_type);
        sectionTime = (TextView) sectionCommonInfo.findViewById(R.id.section_time);
        sectionLocation = (TextView) sectionCommonInfo.findViewById(R.id.section_location);
        
        sectionAvgGrade = (TextView) sectionCommonInfo.findViewById(R.id.section_avg_grade);
        
        
        sectionAvgGrade.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), SectionGradeActivity.class);
				Bundle b = new Bundle();
				b.putInt("id", sectionID);
				intent.putExtras(b);
				startActivity(intent);
			}
		});
        
        coursewareInit();//课件界面初始化
        commentInit();//评论界面初始化
        questionInit();//问答界面初始化
        
        new SectionInfo(sectionID).execute();
        
    }

    private void coursewareInit() {
    	coursewareListView.setDividerHeight(0);
    	
    	coursewareListView.setOnScrollListener(new AbsListView.OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				if(firstVisibleItem == 0) {
					coursewareRefresh.setEnabled(true);
				} else {
					coursewareRefresh.setEnabled(false);
				}
			}
		});
    	
    	coursewareListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				//打开一个课件详情
			}
		});
    	
    	coursewareRefresh.setEnabled(false);
    	coursewareRefresh.setColorScheme(android.R.color.holo_green_light, android.R.color.holo_red_light,
                 android.R.color.holo_green_light, android.R.color.holo_purple);
    	
    	//课件--下拉刷新
    	coursewareRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				
			}
		});
              
    	
    }
    
    
    private void commentInit() {
    	commentListView.setDividerHeight(0);
    	
    	commentListView.setOnScrollListener(new AbsListView.OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				if(firstVisibleItem == 0) {
					commentRefresh.setEnabled(true);
				} else {
					commentRefresh.setEnabled(false);
				}
			}
		});
    	
    	commentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				//打开一个评论详情
			}
		});
    	
    	commentRefresh.setEnabled(false);
    	commentRefresh.setColorScheme(android.R.color.holo_green_light, android.R.color.holo_red_light,
                 android.R.color.holo_green_light, android.R.color.holo_purple);
    	
    	//评论--下拉刷新
        commentRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				new SectionComment(sectionID).execute();	
			}
		});
        
        
      //发表评论
        etAddComment = (EditText) commentRefresh.findViewById(R.id.et_add_comment);
        btnAddComment = (Button) commentRefresh.findViewById(R.id.btn_add_comment);
        btnAddComment.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String myComment = etAddComment.getText().toString().trim();
				if(myComment.length()<=0) {
					Toast.makeText(SectionActivity.this, R.string.add_comment_can_not_null, Toast.LENGTH_SHORT).show();
				}else {
					new AddComment(UserConfig.STUDENT_ID, sectionID, myComment).execute();
				}
			}
		});
        
    	
    }
    
    private void questionInit() {
    	questionListView.setDividerHeight(0);
    	
    	questionListView.setOnScrollListener(new AbsListView.OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				if(firstVisibleItem == 0) {
					questionRefresh.setEnabled(true);
				} else {
					questionRefresh.setEnabled(false);
				}
			}
		});
    	
    	questionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				//打开一个问答详情
			}
		});
    	
    	questionRefresh.setEnabled(false);
    	
    	 questionRefresh.setColorScheme(android.R.color.holo_green_light, android.R.color.holo_red_light,
                 android.R.color.holo_green_light, android.R.color.holo_purple);
         
         
         //问答--下拉刷新
         questionRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
 			
 			@Override
 			public void onRefresh() {
 				// TODO Auto-generated method stub
 				new SectionQuestions().execute();
 			}
 		});
         
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.course_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {		
		case R.id.action_add_section://选课
			new AddSection(UserInfo.getUserInfo().getID(), courseID, sectionTermID, sectionClassNum).execute();
			break;
		
		case R.id.action_interest://收藏
			new AddIntrest(UserInfo.getUserInfo().getID(), sectionID).execute();
			break;
			
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * 获取基本信息
	 * @author Burjal Hou
	 *
	 */
	private class SectionInfo extends AsyncTask<Void, Void, Boolean> {
    	
    	private String data = "";
    	private int id;
    	private Section section;
    	
    	public SectionInfo(int id) {
			this.id = id;
		}
    	
		@Override
		protected Boolean doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			String url = NetworkUtils.WEB_SERVER_ADDRESS + "SectionInfo";
			HttpPost httpRequest = new HttpPost(url);
			List<NameValuePair> param = new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("user_id", String.valueOf(UserInfo.getUserInfo().getID())));
			param.add(new BasicNameValuePair("id", Config.getSECTION_ID()+""));
Log.i("SectAct.....","Request command to web------>user_id->" + UserInfo.getUserInfo().getID()+";id->"+Config.getSECTION_ID()+"");			
			try {
				httpRequest.setEntity(new UrlEncodedFormEntity(param, HTTP.UTF_8));
				HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
				if(httpResponse.getStatusLine().getStatusCode() == 200) {
					data = EntityUtils.toString(httpResponse.getEntity(), HTTP.UTF_8);
Log.i("SectionActivity...","-----get all sections----\n"+"data->" + data);
					section = Section.parse(data);
				} else {
					Log.e("SectionActivity...error", String.valueOf(httpResponse.getStatusLine().getStatusCode()));
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
			if(section != null) {
				getActionBar().setTitle(section.getCourseNum() + " " +section.getTitle());
				courseNum.setText(section.getCourseNum());
				sectionTitle.setText(section.getTitle());
				sectionTerm.setText(section.getTerm());
				sectionDepartment.setText(section.getDepartment());
				description.setText(section.getDepartment());
				instructor.setText(section.getInstructor());
				description.setText(section.getDescription());
		        sectionRate.setText(section.getRate());
		        //sectionGrade.setText(String.valueOf(section.getGrade()));
		        sectionGrade.setText(String.valueOf("92"));
		        sectionUnits.setText(String.valueOf(section.getUnit()));
		        sectionType.setText(section.getType());
		        sectionTime.setText(section.getTime());
		        sectionLocation.setText(section.getLocation());
		        //instructorID.setText(section.getInstructorID());
		        
		        courseID = section.getCourseID();
		        sectionTermID = section.getTermID();
		        sectionClassNum = section.getClassNum();
			}
		}
    	
    }
    
	/**
	 * 获取评论列表
	 * @author Administrator
	 *
	 */
    private class SectionComment extends AsyncTask<Void, Void, Boolean> {
    	
    	private int id;
    	private String data;
    	private List<Comment> comments = new ArrayList<Comment>();
    	private SimpleAdapter adapter;
    	
    	public SectionComment(int id) {
    		this.id = id;
    	}
    	
		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub
			String url = NetworkUtils.WEB_SERVER_ADDRESS + "SectionComment";
			HttpPost httpRequest = new HttpPost(url);
			List<NameValuePair> param = new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("id", String.valueOf(id)));
			
Log.i("SectionActivity..", "---to get COMMENTS---\nrequest--->"+param.toString());
			try {
				httpRequest.setEntity(new UrlEncodedFormEntity(param, HTTP.UTF_8));
				HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
				if(httpResponse.getStatusLine().getStatusCode() == 200) {
					data = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
Log.i("SectionActivity...", "........get response:\n"+data.toString());
					comments = CommentList.parse(data).getCommentList();
				} else {
					Log.e("SectionActivity..error", "---can't to get COMMENTS---->"+httpResponse.getStatusLine().getStatusCode() + "");
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
			} catch (AppException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
			Map<String, Object> map = null;
			for(Comment c : comments) {
				map = new HashMap<String, Object>();
				map.put("text", c.getText());
				map.put("date", c.getDate());
				map.put("user", c.getUser());
				dataList.add(map);
			}
			
			
			adapter = new SimpleAdapter(getApplication(), dataList, R.layout.section_comment_list_item,
					new String[] {"text", "date", "user",""},
					new int[] {R.id.section_comment_text, R.id.section_comment_date, R.id.section_comment_user,R.id.vLine});
			
			commentListView.setAdapter(adapter);
			commentRefresh.setRefreshing(false);
			Toast.makeText(SectionActivity.this, R.string.refresh_complete, Toast.LENGTH_SHORT).show();
			adapter.notifyDataSetChanged();
		}
    	
    }
    
	
	/**
	 * 选课
	 * @add 0:已经选该课程  1：选课成功  2：选课失败
	 * @author Burjal Hou
	 *
	 */
    private class AddSection extends AsyncTask<Void, Void, String> {
    	
    	private String data = "";
    	private int userID;
    	private int courseID;
    	private int termID;
    	private String classNum;
    	
    	public AddSection(int userID, int courseID,int termID, String classNum) {
    		this.userID = userID;
    		this.courseID = courseID;
    		this.termID = termID;
    		this.classNum = classNum;
    	}
    	
		@Override
		protected String doInBackground(Void... arg0) {
			
			String url = NetworkUtils.WEB_SERVER_ADDRESS + "AddSectionPlan";
			HttpPost httpRequest = new HttpPost(url);
			List<NameValuePair> param = new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("user_id", String.valueOf(this.userID)));
			param.add(new BasicNameValuePair("course_id", String.valueOf(this.courseID)));
			param.add(new BasicNameValuePair("term_id", String.valueOf(this.termID)));
			param.add(new BasicNameValuePair("class_num", this.classNum));
			
			try {
				httpRequest.setEntity(new UrlEncodedFormEntity(param));
				HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
Log.i("SectionActivity...","request:"+param.toString());
				if(httpResponse.getStatusLine().getStatusCode() == 200) {
					data = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
Log.i("SectionActivity...", "---->add result............"+data.toString()+"\ncourse_id:"+this.courseID);
				} else {
					Log.e("SectionActivity...", "---->add failed............"+httpResponse.getStatusLine().getStatusCode());
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return data;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			
			if(result.equals("1")) {//选课成功
				Toast.makeText(getApplicationContext(), R.string.add_section_success, Toast.LENGTH_LONG).show();
			} else if(result.equals("0")){//已选
				Toast.makeText(getApplicationContext(), R.string.add_section_exist, Toast.LENGTH_LONG).show();
			} else {//失败
				Toast.makeText(getApplicationContext(), R.string.add_section_false, Toast.LENGTH_LONG).show();
			}						
		}
    	
    }
    
    /**
     * 添加至收藏夹
     * 1：成功    2：失败   3：已经收藏
     * @author Administrator
     *
     */
	private class AddIntrest extends AsyncTask<Void, Void, Void> {
    	
    	private String result = "0";
    	private int userID;
    	private int sectionID;    	
    	
    	public AddIntrest(int userID, int sectionID) {
    		this.userID = userID;
    		this.sectionID = sectionID;    		
    	}
    	
		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			
			String url = NetworkUtils.WEB_SERVER_ADDRESS + "AddInterestPlan";
			HttpPost httpRequest = new HttpPost(url);
			List<NameValuePair> param = new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("user_id", String.valueOf(this.userID)));
			param.add(new BasicNameValuePair("section_id", String.valueOf(this.sectionID)));
			
			try {
				httpRequest.setEntity(new UrlEncodedFormEntity(param));
				HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
				if(httpResponse.getStatusLine().getStatusCode() == 200) {
					result = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
				} else {
					Log.e("SectionActivity...", "---->add interest failed............");
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
			
			return null;
		}

		@Override
		protected void onPostExecute(Void param) {
			// TODO Auto-generated method stub
			super.onPostExecute(param);
Log.i("add_interest", result);
			if(result.equals("1")) { //成功
				Toast.makeText(getApplicationContext(), R.string.add_interest_success, Toast.LENGTH_SHORT).show();
			} else if(result.equals("3")) {//已经收藏
				Toast.makeText(getApplicationContext(), R.string.add_interest_exist, Toast.LENGTH_SHORT).show();
			} else {//收藏失败
				Toast.makeText(getApplicationContext(), R.string.add_interest_false, Toast.LENGTH_SHORT).show();
			}
			
			
		}
    	
    }
	
	
	/**
	 *添加评论
	 * @add 1：添加成功  2：添加失败  
	 * @uncomplete 3：关闭评论通道 (后台未完成)
	 * @author Burjal Hou
	 * @date 14-11-4
	 */
	private class AddComment extends AsyncTask<Void, Void, String> {

		private String stu_id;
		private String comment;
		private int section_id;
		private String xml = "";
		
		public AddComment(String stu_id,int section_id,String comment) {
			this.stu_id = stu_id;
			this.comment = comment;
			this.comment = comment;
		}
		
		@Override
		protected String doInBackground(Void... arg0) {
			
			String url = NetworkUtils.WEB_SERVER_ADDRESS + "AddComment";
			HttpPost httpRequest = new HttpPost(url);
			List<NameValuePair> param = new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("section_id", Config.getSECTION_ID()+""));
			param.add(new BasicNameValuePair("stu_id", this.stu_id));
			param.add(new BasicNameValuePair("comment", this.comment));
Log.i("SectionActivity.....AddComment", "----------->"+param.toString());	
			try {
				httpRequest.setEntity(new UrlEncodedFormEntity(param, HTTP.UTF_8));
				HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
				if(httpResponse.getStatusLine().getStatusCode() == 200) {
					xml = EntityUtils.toString(httpResponse.getEntity(), HTTP.UTF_8);
Log.i("AddComment...result...", "======="+xml.toString()+"========");					
				} else {
					Log.e("SectionAct...AddComment...error", httpResponse.getStatusLine().getStatusCode() + "");
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}			
			return xml;
		}

		@Override
		protected void onPostExecute(String result) {
			
			if(result.equals("1")) {//添加评论成功
				//刷新评论界面
				new SectionComment(sectionID).execute();	
				
				etAddComment.setText("");
				Toast.makeText(SectionActivity.this, R.string.add_comment_success, Toast.LENGTH_SHORT).show();
			} else if(result.equals("3")) {//教师关闭评论通道
				Toast.makeText(SectionActivity.this, R.string.add_comment_out_range, Toast.LENGTH_SHORT).show();
			} else {//添加评论失败
				Log.e("add_comment_result.....", result);
				Toast.makeText(SectionActivity.this, R.string.add_comment_false, Toast.LENGTH_SHORT).show();
			}			
			return;
		}
		
	}
	
	/**
	 * 获取课程问答列表
	 * @author Burjal Hou
	 * @date 14-11-3
	 *
	 */
	private class SectionQuestions extends AsyncTask<Void, Void, String> {

    	private String data = "";
    	int section_id;
    	int stu_id;
    	    	
    	public SectionQuestions() {
    		this.section_id = Config.getSECTION_ID();
    		this.stu_id = Integer.parseInt(UserConfig.STUDENT_ID);
    	}
    	
    	private List<HomeQuestion> questionList = new ArrayList<HomeQuestion>();
    	
		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
			String url = NetworkUtils.WEB_SERVER_ADDRESS + "SectionQuestion";
			HttpPost httpRequest = new HttpPost(url);
			
			try {
				HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
				if(httpResponse.getStatusLine().getStatusCode() == 200) {
					data = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
Log.i("SectionActivity", "===get questions to section===\n"+data.toString());					
					questionList = HomeQuestionList.parse(data).getHomeQuestionList();
Log.i("SectionActivity", "size:"+questionList.size());
				} else {
					Log.e("SectionActivity.->Question<-error..", httpResponse.getStatusLine().getStatusCode() + "");
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
			
			
//			questionItemList.clear();
//			Map<String, Object> map = null;
//			
//			for(HomeQuestion q : questionList) {
//Log.i("question--list", q.toString());
//				map = new HashMap<String, Object>();
//				map.put("id", q.getCourseID());
//				map.put("num", q.getCourseNum());
//				map.put("title", q.getTitle());
//				map.put("text", q.getText());
//				map.put("date", q.getDate());
//				map.put("author", q.getAuthor());
//				map.put("question_id", q.getID());
//				questionItemList.add(map);
//			}
//			
//			SimpleAdapter adapter = new SimpleAdapter(getActivity(), questionItemList, R.layout.home_question_list_item, 
//					new String[] {"id", "num", "title", "text", "date"}, 
//					new int[] {R.id.home_question_course_id, R.id.home_question_course_num,
//								R.id.home_question_course_name, R.id.home_question_text,
//								R.id.home_question_date});
//			answerListView.setAdapter(adapter);
//			anwserRefreshLayout.setRefreshing(false);
//			Toast.makeText(SectionActivity.this, R.string.refresh_complete, Toast.LENGTH_SHORT).show();
//			adapter.notifyDataSetChanged();
//			
			Toast.makeText(SectionActivity.this, result, Toast.LENGTH_SHORT).show();
			questionRefresh.setRefreshing(false);
		}
    	
    }
	
	
}

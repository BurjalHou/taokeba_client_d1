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
import com.taokeba.bean.Course;
import com.taokeba.bean.CourseList;
import com.taokeba.common.Config;
import com.taokeba.common.NetworkUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;


/**
 * 课程搜索
 * 
 * @author Burjal Hou
 *
 */

public class SearchActivity extends Activity {
	
	private ListView searchListView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
//		Bundle bundle = getIntent().getExtras();
//		String query = bundle.getString("query");
		String query = Config.getSEARCH_SECTION_QUERY();
Log.i("SearchActivity..", "..query...---->"+query);		
		searchListView = (ListView) findViewById(R.id.search_result_list_view);
		
		searchListView.setDividerHeight(0);
		
		searchListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int pos,
					long id) {
				// TODO Auto-generated method stub
				Map<String, Object> item = (Map<String, Object>) searchListView.getItemAtPosition(pos);
				int courseID = (Integer) item.get("id");
				Intent intent = new Intent(getApplicationContext(), SearchSectionActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("course_id", courseID);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		
		new Courses(query).execute();
	}
	
	private class Courses extends AsyncTask<Void, Void, Void> {
		
		private String data = "";
		private List<Course> courseList = new ArrayList<Course>();
		private String query = "";
		
		public Courses(String query) {
			this.query = query;
		}
		
		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			
			String url = NetworkUtils.WEB_SERVER_ADDRESS + "Search";
			HttpPost httpRequest = new HttpPost(url);
			List<NameValuePair> param = new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("query", this.query));
			try {
				httpRequest.setEntity(new UrlEncodedFormEntity(param, HTTP.UTF_8));
				HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
				if(httpResponse.getStatusLine().getStatusCode() == 200) {
					data = EntityUtils.toString(httpResponse.getEntity(), HTTP.UTF_8);
					
String temp = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Result><Course><CourseID>3</CourseID>" +
		"<CourseNum>5</CourseNum><CourseTitle>title</CourseTitle>" +
		"<Unit>5</Unit><Type>type</Type></Course></Result>";

					courseList = CourseList.parse(temp).getCourseList();
				} else {
					Log.e("SearchActivity...", "WEB REQUEST FALSE!!!----->"+httpResponse.getStatusLine().getStatusCode());
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
			List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
			Map<String, Object> map = null;
			
			for(Course c : courseList) {
				map = new HashMap<String, Object>();
				map.put("id", c.getCourseID());
				map.put("num", c.getCourseNum());
				map.put("title", c.getTitle());
				map.put("units", c.getUnits());
				map.put("type", c.getTyep());
				data.add(map);
			}
			SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(), data, R.layout.search_list_item, 
					new String[] {"id", "num", "title", "units", "type"},
					new int[] {R.id.search_course_id, R.id.search_course_num, R.id.search_course_title,
								R.id.search_course_units, R.id.search_course_type});
			adapter.notifyDataSetChanged();
			searchListView.setAdapter(adapter);
			
			if(data.size()==0) {
				 Toast.makeText(getApplicationContext(), "暂无《"+query+"》课程，请重新搜索！", Toast.LENGTH_SHORT).show();
			}
			
		}
		
	}
	
	
}

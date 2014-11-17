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

import com.taokeba.AppException;
import com.taokeba.R;
import com.taokeba.bean.PlanList;
import com.taokeba.bean.Section;
import com.taokeba.bean.UserInfo;
import com.taokeba.common.NetworkUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class SearchSectionActivity extends Activity {

	private ListView sectionListView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_section);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		
		int id = getIntent().getExtras().getInt("course_id");
		
		sectionListView = (ListView) findViewById(R.id.search_section_list_view);
		sectionListView.setDividerHeight(0);
		
		sectionListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int pos,
					long id) {
				// TODO Auto-generated method stub
				goToSection(sectionListView, pos);
			}
		});
		
		new Sections(id).execute();
	}
	
	private class Sections extends AsyncTask<Void, Void, Void> {

		private String xml;
		
		private int id;
		private List<Section> sectionList;
		
		public Sections(int id) {
			this.id = id;
		}
		
		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			
			String url = NetworkUtils.WEB_SERVER_ADDRESS + "SearchSection";
			HttpPost httpRequest = new HttpPost(url);
			List<NameValuePair> param = new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("id", String.valueOf(this.id)));
			param.add(new BasicNameValuePair("user_id", String.valueOf(UserInfo.getUserInfo().getID())));
			try {
				httpRequest.setEntity(new UrlEncodedFormEntity(param, HTTP.UTF_8));
				HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
				if(httpResponse.getStatusLine().getStatusCode() == 200) {
					xml = EntityUtils.toString(httpResponse.getEntity(), HTTP.UTF_8);
					sectionList = PlanList.parse(xml).getCourseList();
				} else {
					Log.e("SearchSectionActivity...", "WEB REQUEST FALSE!!!--->"+httpResponse.getStatusLine().getStatusCode());
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
			} catch (AppException e) {
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
			System.out.println("data->" + xml);
			List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
			Map<String, Object> map = null;
			
			for(Section c : sectionList) {
				map = new HashMap<String, Object>();
				map.put("id", c.getSectionID());
				map.put("num", c.getCourseNum());
				map.put("title", c.getTitle());
				map.put("term", c.getTerm());
				map.put("department", c.getDepartment());
				map.put("instructor", c.getInstructor());
				map.put("exempt", c.getExempt());
				data.add(map);
			}
			
			SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(), data, R.layout.plan_list_item, 
					new String[] {"id", "num", "title", "term", "department", "instructor", "exempt"}, 
					new int[] {R.id.plan_section_id, R.id.plan_course_num, 
								R.id.plan_course_title, R.id.plan_term, 
								R.id.plan_department, R.id.plan_instructor,
								R.id.plan_exempt});
			adapter.notifyDataSetChanged();
			sectionListView.setAdapter(adapter);
		}
		
	}
	
	private void goToSection(ListView listView, int pos) {
    	Map<String, Object> item = (Map<String, Object>) listView.getItemAtPosition(pos);
    	int id = (Integer) item.get("id");
    	Intent intent = new Intent(getApplicationContext(), SectionActivity.class);
    	Bundle bundle = new Bundle();
    	bundle.putInt("section_id", id);
    	intent.putExtras(bundle);
    	System.out.println("id->" + id);
    	startActivity(intent);
    }

}

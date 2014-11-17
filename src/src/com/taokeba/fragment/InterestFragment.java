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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.taokeba.AppException;
import com.taokeba.R;
import com.taokeba.bean.Section;
import com.taokeba.bean.InterestList;
import com.taokeba.bean.UserInfo;
import com.taokeba.common.Config;
import com.taokeba.common.NetworkUtils;
import com.taokeba.ui.SectionActivity;

public class InterestFragment extends Fragment {

	private ListView interestList;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_interest, null);
		
		interestList = (ListView) rootView.findViewById(R.id.interest_list);
		
		CourseInterestList courseInterestList = new CourseInterestList();
		courseInterestList.execute((Void)null);
		
		interestList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int pos,
					long id) {
				// TODO Auto-generated method stub
				goToSection(interestList, pos);
			}
		});
		
		return rootView;
	}
	
	private class CourseInterestList extends AsyncTask<Void, Void, Boolean> {

		private String inStream;
    	private List<Section> list;
    	private SimpleAdapter adapter;
		
		@Override
		protected Boolean doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			String url = NetworkUtils.WEB_SERVER_ADDRESS + "Interest";
            HttpPost httpRequest = new HttpPost(url);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id", String.valueOf(UserInfo.getUserInfo().getID())));

            try {
                httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
                if(httpResponse.getStatusLine().getStatusCode() == 200) {
                	inStream = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
                } else {
                    Log.v("error", String.valueOf(httpResponse.getStatusLine().getStatusCode()));
                }
                
                list = InterestList.parse(inStream).getCourseList();
                
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
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
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
			Map<String, Object> map = null;
			
			for(Section c : list) {
				map = new HashMap<String, Object>();
				map.put("id", c.getSectionID());
				map.put("num", c.getCourseNum());
				map.put("title", c.getTitle());
				map.put("term", c.getTerm());
				map.put("department", c.getDepartment());
				map.put("instructor", c.getInstructor());
				map.put("exempt", c.getType());
				data.add(map);
			}
			
			adapter = new SimpleAdapter(getActivity(), data, R.layout.interest_list_item, 
					new String[] {"id", "num", "title", "term", "department", "instructor", "exempt"}, 
					new int[] {R.id.interest_course_id, R.id.interest_course_num, 
								R.id.interest_course_title, R.id.interest_term, 
								R.id.interest_department, R.id.interest_instructor,
								R.id.interest_exempt});
			interestList.setDividerHeight(0);
			interestList.setAdapter(adapter);
		}
		
	}
	
	private void goToSection(ListView listView, int pos) {
    	Map<String, Object> item = (Map<String, Object>) listView.getItemAtPosition(pos);
    	int id = (Integer) item.get("id");
    	Intent intent = new Intent(getActivity(), SectionActivity.class);
    	Config.setSECTION_ID(id);
    	startActivity(intent);
    }
	
}

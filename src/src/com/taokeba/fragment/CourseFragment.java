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
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.taokeba.AppException;
import com.taokeba.R;
import com.taokeba.bean.Section;
import com.taokeba.bean.PlanList;
import com.taokeba.bean.UserInfo;
import com.taokeba.common.Config;
import com.taokeba.common.NetworkUtils;
import com.taokeba.ui.SectionActivity;

/**
 * Created by zhaolin on 14-2-20.
 */
@SuppressLint("ValidFragment")
public class CourseFragment extends Fragment {
	
	private ListView planList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_plan, container, false);
        planList = (ListView) rootView.findViewById(R.id.plan_list);
        
        CourseList courseList = new CourseList();
        courseList.execute((Void)null);
        
        planList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int pos,
					long id) {
				// TODO Auto-generated method stub
				Map<String, Object> item = (Map<String, Object>) planList.getItemAtPosition(pos);
				int sectionID = (Integer) item.get("id");
				Intent intent = new Intent(getActivity(), SectionActivity.class);
		    	Config.setSECTION_ID(sectionID);
				startActivity(intent);
			}
		});
        
        planList.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View v,
					int pos, long id) {
				// TODO Auto-generated method stub
				Map<String, Object> item = (Map<String, Object>) planList.getItemAtPosition(pos);
				String title = (String) item.get("title");
				AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
				dialog.setTitle(title);
				dialog.setItems(new String[] {getResources().getString(R.string.delete_course_title)}, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						if(which == 0) {
							Toast.makeText(getActivity(), R.string.delete_course_success, Toast.LENGTH_SHORT).show();
							new CourseList().execute();
						}
					}
					
				});
				dialog.show();
				return true;
			}
		});
        
        return rootView;
    }
    
    private class CourseList extends AsyncTask<Void, Void, Boolean> {
    	
    	private String inStream = "";
    	private List<Section> list;
    	private SimpleAdapter adapter;
    	
		@Override
		protected Boolean doInBackground(Void... voids) {
			// TODO Auto-generated method stub
			String url = NetworkUtils.WEB_SERVER_ADDRESS + "Plan";
            HttpPost httpRequest = new HttpPost(url);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id", String.valueOf(UserInfo.getUserInfo().getID())));
            
            try {
                httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
                if(httpResponse.getStatusLine().getStatusCode() == 200) {
                	inStream = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
Log.i("ActCourseFra...", inStream);
                } else {
                    Log.e("ActCourseFragment...error", String.valueOf(httpResponse.getStatusLine().getStatusCode()));
                }
                
                list = PlanList.parse(inStream).getCourseList();
                
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
				map.put("exempt", c.getExempt());
				map.put("location", c.getLocation());
				data.add(map);
			}
			
			adapter = new SimpleAdapter(getActivity(), data, R.layout.plan_list_item, 
					new String[] {"id", "num", "title", "term", "department", "instructor", "exempt"}, 
					new int[] {R.id.plan_section_id, R.id.plan_course_num, 
								R.id.plan_course_title, R.id.plan_term, 
								R.id.plan_department, R.id.plan_instructor,
								R.id.plan_exempt});
			planList.setDividerHeight(0);
			planList.setAdapter(adapter);
			adapter.notifyDataSetChanged();
		}

    	
    }
    
}

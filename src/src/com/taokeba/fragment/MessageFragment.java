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
import org.apache.http.util.EntityUtils;
import org.xmlpull.v1.XmlPullParserException;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.taokeba.R;
import com.taokeba.bean.MessageInfo;
import com.taokeba.bean.MessageInfoList;
import com.taokeba.bean.SectionRank;
import com.taokeba.bean.UserInfo;
import com.taokeba.common.NetworkUtils;

/**
 * 站内消息
 * @add No item click 
 * @Created by zhaolin on 14-2-20.
 * 
 * @Updated by Burjal Hou on 14-10-28
 */
@SuppressLint("ValidFragment")
public class MessageFragment extends Fragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView messageListView;
    private TextView messageListEmpty;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_message, container, false);

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.message_refresher);
        messageListView = (ListView) rootView.findViewById(R.id.message_list);
        messageListEmpty = (TextView) rootView.findViewById(R.id.message_list_empty);
        
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            	new Messages(UserInfo.getUserInfo().getID()).execute((Void)null);
            }
        });
        swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        
        messageListView.setEmptyView(messageListEmpty);
        messageListView.setDividerHeight(0);
        
        messageListView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				if(firstVisibleItem == 0) {
					swipeRefreshLayout.setEnabled(true);
				} else {
					swipeRefreshLayout.setEnabled(false);
				}
			}
		});
        
        //必须加.execute()，否则异步方法不被执行
        new Messages(UserInfo.getUserInfo().getID()).execute((Void)null);
        return rootView;
    }
    
    private class Messages extends AsyncTask<Void, Void, String> {
    	
    	private int id;
    	private String data = "";
    	private List<MessageInfo> list = new ArrayList<MessageInfo>();
 	
    	public Messages(int id) {
    		this.id = id;
    	}
    	
		@Override
		protected String doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			final String url = NetworkUtils.WEB_SERVER_ADDRESS + "Message";
			HttpPost httpRequest = new HttpPost(url);
			List<NameValuePair> param = new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("id", String.valueOf(this.id)));
			
			try {
				httpRequest.setEntity(new UrlEncodedFormEntity(param));
				HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
				if(httpResponse.getStatusLine().getStatusCode() == 200) {
					data = EntityUtils.toString(httpResponse.getEntity());
					
String temp = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Messages><Message><MessageID>12</MessageID><CreateTime>" +
		"2014-10-29</CreateTime><Text>站内信</Text><From>10</From><Type>2</Type></Message></Messages>";
					
					list = MessageInfoList.parse(temp).getMessageInfoList();
				}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return data;
		}

		@Override
		protected void onPostExecute(String result) {

			List<Map<String,Object>> data = new ArrayList<Map<String,Object>>();
			Map<String,Object> map = null;
			
			for(MessageInfo m : list) {
				map = new HashMap<String, Object>();
//				map.put("message_type", m.getGetToType());
				map.put("message_type", "站内信");
				map.put("message_content", m.getText());
				map.put("message_time", m.getTime());
				
				data.add(map);
			}
			
			SimpleAdapter adapter = new SimpleAdapter(getActivity(), data, R.layout.message_list_item,
	                new String[] {"message_type", "message_content", "message_time"},
	                new int[] {R.id.message_type, R.id.message_content, R.id.message_send_time});

			messageListView.setAdapter(adapter);		
			swipeRefreshLayout.setRefreshing(false);
			adapter.notifyDataSetChanged();
			
Toast.makeText(getActivity(), "gerResponse:"+result, Toast.LENGTH_SHORT).show();
			Toast.makeText(getActivity(), R.string.refresh_complete, Toast.LENGTH_SHORT).show();
			
		}
    	
    }
    
       
    /**
     * @deprecated
     * @add for test demo.
     */
    private void setData() {
    	List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;
Log.i("Message...", "in set data............");		
		map = new HashMap<String, Object>();
		map.put("message_type", "站内信");
		map.put("message_content", "test message");
		map.put("message_time", "2014-6-22 14:37:21");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("message_type", "站内信");
		map.put("message_content", "test message");
		map.put("message_time", "2014-6-22 14:39:29");
		list.add(map);
	
		SimpleAdapter adapter = new SimpleAdapter(getActivity(), list, R.layout.message_list_item,
							        new String[] {"message_type", "message_content", "message_time"},
							        new int[] {R.id.message_type, R.id.message_content, R.id.message_send_time});

		messageListView.setAdapter(adapter);		
		swipeRefreshLayout.setRefreshing(false);
		adapter.notifyDataSetChanged();
    }
    
    
}

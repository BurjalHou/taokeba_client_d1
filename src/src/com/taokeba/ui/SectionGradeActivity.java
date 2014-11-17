package com.taokeba.ui;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ListView;

import com.taokeba.bean.SectionGrade;
import com.taokeba.common.JSInterface;
import com.taokeba.common.NetworkUtils;

/**
 * JSP交互
 * 
 * @author zhaolin
 * 
 * @update Burjal Hou 14-10-27
 * @add Add Intent(section_id) to return to RankFragment.class
 *
 */

public class SectionGradeActivity extends Activity {

	private int sectionID;
	private static Handler handler = new Handler();
	
	private WebView webView;
	
	private SectionGrade grade;
	
	@SuppressLint("JavascriptInterface")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_avg_grade);
		
		Bundle bundle = getIntent().getExtras();
		sectionID = bundle.getInt("id");
		
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		webView = (WebView) findViewById(R.id.section_web_view);
		
		webView.setHorizontalScrollBarEnabled(false);
		webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
		
		WebSettings settings = webView.getSettings();
		
		settings.setDefaultTextEncodingName(HTTP.UTF_8);
		settings.setJavaScriptEnabled(true);
		settings.setSupportZoom(true);
		settings.setUseWideViewPort(true);
		
		GradeDistribute gradeDistribute = new GradeDistribute(sectionID);
		gradeDistribute.execute((Void)null);

		
		webView.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View arg0) {
				// TODO Auto-generated method stub
				return true;
			}
		});
	}
	
	private class GradeDistribute extends AsyncTask<Void, Void, Boolean> {
		
		private int id;
		private String data;
		
		public GradeDistribute(int id) {
			this.id = id;
		}
		
		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub
			String url = NetworkUtils.WEB_SERVER_ADDRESS + "SectionGrade";
			
			HttpPost httpRequest = new HttpPost(url);
			List<NameValuePair> param = new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("id", String.valueOf(id)));
			
			try {
				httpRequest.setEntity(new UrlEncodedFormEntity(param, "UTF-8"));
				HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
				if(httpResponse.getStatusLine().getStatusCode() == 200) {
					data = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
					grade = SectionGrade.parse(data);
				} else {
					Log.e("SectionGradeActivity...error", httpResponse.getStatusLine().getStatusCode() + "");
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
			String url = "file:///android_asset/index.html";
			webView.addJavascriptInterface(new JSInterface(getApplication(), sectionID, grade), "grade");
			webView.loadUrl(url);
		}
		
	}

}

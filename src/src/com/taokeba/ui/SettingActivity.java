package com.taokeba.ui;

import net.sf.andpdf.pdfviewer.PdfViewerActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.taokeba.R;
import com.taokeba.common.PdfDownloadUtils;

/**
 * Created by zhaolin on 14-2-18.
 */
public class SettingActivity extends Activity {
	
	private Button btnTest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        btnTest = (Button) findViewById(R.id.btnTest);
        btnTest.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub				
				new DownPDFTask().execute();
				
			}
		});
        
    }
    
    private class DownPDFTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(result.equals(DOWNLOAD_SUCCESS)) {
				Toast.makeText(SettingActivity.this, "success", Toast.LENGTH_SHORT).show();
				showDownload();
				Intent intent = new Intent(SettingActivity.this, PDFReaderActivity.class);
			     intent.putExtra(PdfViewerActivity.EXTRA_PDFFILENAME, Environment.getExternalStorageDirectory() +"/file/demo1112.pdf");
			     startActivity(intent);
			} else if (result.equals(DOWNLOAD_FAILE)) {
				Toast.makeText(SettingActivity.this, "exist", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(SettingActivity.this, "false", Toast.LENGTH_SHORT).show();
			}
		}

		public void showDownload()
        {
//            File file = new File(Environment.getExternalStorageDirectory()+"/dir/demo1111.pdf");
//            PackageManager packageManager = getPackageManager();
//            Intent testIntent = new Intent(Intent.ACTION_VIEW);
//            testIntent.setType("application/dir");
//            List list = packageManager.queryIntentActivities(testIntent, PackageManager.MATCH_DEFAULT_ONLY);
//            Intent intent = new Intent();
//            intent.setAction(Intent.ACTION_VIEW);
//            Uri uri = Uri.fromFile(file);
//            intent.setDataAndType(uri, "application/dir");
//            startActivity(intent);
        }
		
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			PdfDownloadUtils pdfDownload = new PdfDownloadUtils();
			int result = pdfDownload.downloadFile("http://course.xidian.edu.cn/Lesson/upload/ppt/11072014104031639.pdf", "file/", "demo1112.pdf");
//			int result = pdfDownload.downloadFile("http://course.xidian.edu.cn/Lesson/flash.jsp?url=upload/video/11052014212847774.flv", "dir/", "001.flv");
			
			return ""+result;
		}    	
    }
    private static final String DOWNLOAD_SUCCESS = "0";
    private static final String DOWNLOAD_FAILE = "1";
    private static final String DOWNLOAD_EXIST = "-1";
}

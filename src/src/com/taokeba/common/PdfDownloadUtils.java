package com.taokeba.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.util.Log;

/**
 * PDF Downloader
 * @author Burjal Hou
 * @date 14-11-11
 *
 */
public class PdfDownloadUtils{
	
	private static final int DOWNLOAD_FAIL = -1;
	private static final int DOWNLOAD_SUCCESS = 0;
	private static final int DOWNLOAD_EXIST = 1;
	
	private static final String LOG_TAG = "PDFDownloadUtils===";
	
	public String downloadText(String urlStr) {
		
		URL url = null;
		StringBuffer sb = new StringBuffer();
		String line = null;
		BufferedReader br = null;
		
		try {
			//创建一个URL对象
			url = new URL(urlStr);
			
			//创建一个Http连接
			HttpURLConnection urlCon = (HttpURLConnection)url.openConnection();
			
			//使用IO流读取数据
			br = new BufferedReader(new InputStreamReader(urlCon.getInputStream()));
			
			while ((line = br.readLine())!= null) {
				sb.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(LOG_TAG, urlStr + "<--false");
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
				Log.e(LOG_TAG, "****COLSE FILL FALSE****");
			}
		}
		return sb.toString();		
	}
	
	/**
	 * 返回-1下载文件失败，返回0下载成功，返回1则是文件已存在
	 * @param urlStr
	 * @param path
	 * @param fileName
	 * @return
	 */
	public int downloadFile(String urlStr,String path,String fileName) {
		InputStream inputStream = null;
		
		FileUtils fileUtils = new FileUtils();
		if(FileUtils.checkFileExists(fileName)) {
			return DOWNLOAD_EXIST;
		} else {
			try {
				inputStream = getInputStreamFromUrl(urlStr);
			} catch (IOException e) {
				e.printStackTrace();
				Log.e(LOG_TAG, "++++++...getInputStream...++++++");
				return DOWNLOAD_FAIL;
			}
			File resultFile = fileUtils.write2SDFromInput(path, fileName, inputStream);
			if(resultFile == null) {
				return DOWNLOAD_FAIL;
			}			
		}
		try {
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
			Log.e(LOG_TAG, "++++CLOSE EXCEPTION+++++");
		}
		return DOWNLOAD_SUCCESS;
	}
	
	private InputStream getInputStreamFromUrl(String urlStr) throws MalformedURLException,IOException {
		URL url = null;
		url = new URL(urlStr);
		HttpURLConnection urlCon = (HttpURLConnection)url.openConnection();
		InputStream inputStream = urlCon.getInputStream();
		return inputStream;
	}
}

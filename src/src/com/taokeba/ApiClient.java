package com.taokeba;

import android.os.Build;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;



/**
 * Created by zhaolin on 14-3-5.
 */
public class ApiClient {

    public static final String UTF_8 = "UTF-8";

    public final static int TIMEOUT_CONNECTION = 20000;
    public final static int TIMEOUT_SOCKET = 20000;
    public final static int RETRY_TIME = 3;

    public static String appCookie;
    public static String appUserAgent;

    public static void cleanCookie() {
        appCookie = "";
    }

    private static String getUserAgent(AppContext appContext) {
        if(appUserAgent == null || appUserAgent == "") {
            StringBuilder ua = new StringBuilder("taokeba.com");
            ua.append('/' + appContext.getPackageInfo().versionName + '_' + appContext.getPackageInfo().versionCode);
            ua.append("/Android");
            ua.append("/" + Build.VERSION.RELEASE);
            ua.append("/" + Build.MODEL);
            //ua.append("/" + appContext.getAppId())
            appUserAgent = ua.toString();
        }
        return appUserAgent;
    }

    public static HttpClient getHttpClient() {
        HttpClient client = new DefaultHttpClient();

        return client;

    }

}

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

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.taokeba.R;
import com.taokeba.bean.User;
import com.taokeba.bean.UserConfig;
import com.taokeba.bean.UserInfo;
import com.taokeba.common.CyptoUtils;
import com.taokeba.common.NetworkUtils;

/**
 * Created by zhaolin on 14-2-26.
 */
public class SignInActivity extends Activity {

    private Button signInButton;
    private ImageView portrait;
    private EditText userIDEditText;
    private EditText passwordEditText;

    private String userID;
    private String password;

    private ProgressDialog dialog;
    
    public static User user;
    
    private long exitTime = 0;//双击“返回”键退出程序
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        signInButton = (Button) findViewById(R.id.signin_button);
        portrait = (ImageView) findViewById(R.id.signin_portrait);
        userIDEditText = (EditText) findViewById(R.id.signin_user_id);
        passwordEditText = (EditText) findViewById(R.id.signin_password);

        portrait.setImageResource(R.drawable.ic_launcher);

        signInButton.setOnClickListener(new signInButtonListener());
        
        getLoginInfo();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sigin_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_regist:
                Intent intent = new Intent(this, RegistActivity.class);
                startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private class signInButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            userID = userIDEditText.getText().toString();
            password = passwordEditText.getText().toString();
            
            if(TextUtils.isEmpty(userID) || TextUtils.isEmpty(password)) {
                Toast.makeText(getApplicationContext(), R.string.sign_in_information_complete, Toast.LENGTH_SHORT).show();
            } else {
                showProgress();
                UserLoginTask login = new UserLoginTask(userID, CyptoUtils.encode(password));
                login.execute((Void)null);
//            	Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                startActivity(intent);
            }
                              
            
        }
    }

    public void showProgress() {
        dialog = new ProgressDialog(SignInActivity.this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage(getResources().getString(R.string.sign_in_log_in));
        dialog.setCancelable(true);
        dialog.show();
    }

    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private String userID = "";
        private String password = "";

        private String result = "";

        UserLoginTask(String userID, String password) {
            this.userID = userID;
            this.password = password;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
Log.i("result...login", result + "");
            if(result.equals("-1") || result.equals("-2")) {
                Toast.makeText(getApplicationContext(), getResources().getText(R.string.sign_in_error), Toast.LENGTH_SHORT).show();
                dialog.cancel();
            } else if(result.equals("0")) {
            	Remember();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                dialog.cancel();
                finish();
            } else {
                Toast.makeText(getApplicationContext(), getResources().getText(R.string.sign_in_network), Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            String url = NetworkUtils.WEB_SERVER_ADDRESS + "SignIn";
            HttpPost httpRequest = new HttpPost(url);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("user_id", userID));
            params.add(new BasicNameValuePair("password", password));
            params.add(new BasicNameValuePair("type", "stu"));
            
System.out.println("user_id->" + userID+"\npassword->"+password+"\ntype->");
            
            try {
                httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
                if(httpResponse.getStatusLine().getStatusCode() == 200) {
                    result = EntityUtils.toString(httpResponse.getEntity());
                } else if (httpResponse.getStatusLine().getStatusCode() == 5) {
                	Toast.makeText(getApplicationContext(), getResources().getText(R.string.sign_in_wrong_status), Toast.LENGTH_SHORT).show();
                }else {
                    Log.e("error...ActSingIn", String.valueOf(httpResponse.getStatusLine().getStatusCode()));
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    @Override
    protected void onDestroy() {
        finish();
        super.onDestroy();
    }

    private void Remember() {
        SharedPreferences sp = getSharedPreferences("login_info", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("user_id", userIDEditText.getText().toString());
        editor.putString("password", passwordEditText.getText().toString());
        editor.putBoolean("auto", true);
        editor.commit();
        
        UserConfig.STUDENT_ID = userIDEditText.getText().toString();

    }
    
    private void getLoginInfo() {
		SharedPreferences sp = getSharedPreferences("login_info", MODE_PRIVATE);
		userIDEditText.setText(sp.getString("user_id", ""));
		passwordEditText.setText(sp.getString("password", ""));
	}
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){

            if((System.currentTimeMillis() - exitTime) > 2000){
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.click_twice_to_quit), Toast.LENGTH_SHORT).show();                                
                exitTime = System.currentTimeMillis();
            }
            else{
                onDestroy();
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }   
	
    
}

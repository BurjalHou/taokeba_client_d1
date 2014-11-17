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
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import android.widget.Toast;

import com.taokeba.R;
import com.taokeba.bean.UserInfo;
import com.taokeba.common.Config;
import com.taokeba.common.NetworkUtils;
import com.taokeba.fragment.CommunityFragment;
import com.taokeba.fragment.CourseFragment;
import com.taokeba.fragment.HomeFragment;
import com.taokeba.fragment.InterestFragment;
import com.taokeba.fragment.MessageFragment;
import com.taokeba.fragment.RankFragment;
import com.taokeba.fragment.UserProfileFragment;

public class MainActivity extends Activity {

    private DrawerLayout drawerLayout;
    private ListView drawerList = null;
    private ActionBarDrawerToggle drawerToggle;

    private CharSequence drawerTitle;
    private CharSequence title;
    private String[] itemListTitles;

    private ImageView drawerHeaderImage;
    private TextView drawerHeaderUserName;

    private long exitTime = 0;

    private UserInfo userInfo;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title = drawerTitle = getTitle();
Log.i("ActMain...", "getTitle()-->"+title);

        //右滑菜单
        itemListTitles = getResources().getStringArray(R.array.items_array);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);

        //右滑菜单头部->用户信息
        View drawerHeadView = View.inflate(this, R.layout.drawer_list_header, null);
        drawerList.addHeaderView(drawerHeadView);

        drawerHeaderImage = (ImageView) findViewById(R.id.drawer_list_head_picture);
        drawerHeaderUserName = (TextView) findViewById(R.id.drawer_list_head_user_name);

        drawerHeaderImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher));

        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        drawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, itemListTitles));
        drawerList.setOnItemClickListener(new DrawerItemListener());

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.drawable.ic_drawer,
                R.string.drawer_open,
                R.string.drawer_close
            ) {

            public void onDrawerOpened(View drawerView) {
               getActionBar().setTitle(drawerTitle);
               invalidateOptionsMenu();
            }

            public void onDrawerClosed(View drawerView) {
                getActionBar().setTitle(title);
                invalidateOptionsMenu();
            }
        };

        drawerLayout.setDrawerListener(drawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }
        
Log.e("ActMain", "........before getUserProfile");
        userInfo = getUserProfile();
        drawerHeaderUserName.setText(userInfo.getUserName());        
Log.e("ActMain", "........after getUserProfile");
        
        UserProfile user = new UserProfile(getSharedPreferences("login_info", MODE_PRIVATE).getString("user_id", ""));
        user.execute((Void)null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_actions, menu);

        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        	
        searchView.setOnQueryTextListener(new OnQueryTextListener() {
			
			@Override
			public boolean onQueryTextSubmit(String query) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
//				Bundle bundle = new Bundle();
//				bundle.putString("query", query);
//				intent.putExtras(bundle);
				Config.setSEARCH_SECTION_QUERY(query);
				startActivity(intent);
				return true;
			}
			
			@Override
			public boolean onQueryTextChange(String arg0) {
				// TODO Auto-generated method stub
				return false;
			}
		});
        
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_settings:
                intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.action_about:
                intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = drawerLayout.isDrawerOpen(drawerList);
        menu.findItem(R.id.action_search).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    private class DrawerItemListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            selectItem((int)(adapterView.getAdapter().getItemId(position)));
        }
    }

    private void selectItem(int position) {

        Fragment fragment = null;
        switch (position) {
        	//用户
            case -1:
                fragment = new UserProfileFragment();
                break;
               
            //我的课程    
            case 0:
                fragment = new CourseFragment();
                break;
                
            //首页    
            case 1:
            	fragment = new HomeFragment();
                break;
                
            //课程排行    
            case 2:
                fragment = new RankFragment();
                break;
                
            //关注的课程    
            case 3:
                fragment = new InterestFragment();
                break;
                
            //社区    
            case 4:
                fragment = new CommunityFragment();
                break;
                
            //消息    
            case 5:
                fragment = new MessageFragment();
                break;
                
            default:
                break;
        }
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        
        drawerList.setItemChecked(position + 1, true);
        if(position != -1) {
            setTitle(itemListTitles[position]);
        } else {
            setTitle(userInfo.getUserName());
        }
        drawerLayout.closeDrawer(drawerList);
    }

    @Override
    public void setTitle(CharSequence title) {
        this.title = title;
        getActionBar().setTitle(title);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
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
    
	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.finish();
	}
    
	private UserInfo getUserProfile() {
		SharedPreferences sp = getSharedPreferences("user_info", MODE_PRIVATE);
		return UserInfo.getUserInfo(sp.getInt("id", 0), 
				sp.getInt("sunet_id", 0),
				sp.getString("user_name", ""), 
				sp.getString("user_alias_name", ""), 
				sp.getString("department", ""), 
				sp.getString("qq", ""), 
				sp.getString("major", ""));
	}
	
	public class UserProfile extends AsyncTask<Void, Void, Boolean> {
    	
    	String inStream = "";
    	UserInfo userInfo;
    	String id = null;
    	
    	public UserProfile(String id) {
			// TODO Auto-generated constructor stub
    		this.id = id;
		}
    	
		@Override
		protected Boolean doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			final String url = NetworkUtils.WEB_SERVER_ADDRESS + "UserInfo";
			HttpPost httpRequest = new HttpPost(url);
	        List<NameValuePair> params = new ArrayList<NameValuePair>();
	        params.add(new BasicNameValuePair("user_id", id));
Log.i("ActMain_Info_body", id+""+"\nparams:"+params.toString());
	        try {
	            httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
	            HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
	            if(httpResponse.getStatusLine().getStatusCode() == 200) {
	            	inStream = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
Log.i("ActMain_getInfo", inStream.toString());	            	
	            } else {
	                Log.e("error...ActMain_getInfo", String.valueOf(httpResponse.getStatusLine().getStatusCode()));
	            }
	            userInfo = UserInfo.parse(inStream);
	        } catch (UnsupportedEncodingException e) {
	            e.printStackTrace();
	        } catch (ClientProtocolException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
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
			try {
				SharedPreferences sp = getSharedPreferences("user_info", MODE_PRIVATE);
	            SharedPreferences.Editor editor = sp.edit();
	            editor.putInt("id", userInfo.getID());
	            editor.putInt("sunet_id", userInfo.getSunetID());
	            editor.putString("user_name", userInfo.getUserName());
	            editor.putString("user_alias_name", userInfo.getAliasName());
	            editor.putString("department", userInfo.getDepartment());
	            editor.putString("major", userInfo.getMajor());
	            editor.putString("qq", userInfo.getQQ());
	            editor.commit();
			} catch (NullPointerException e) {
				Log.e("MainAct...getUserInfo", "web request false!!!........");
				e.printStackTrace();
			}
		}
    	
		
    }
    
}

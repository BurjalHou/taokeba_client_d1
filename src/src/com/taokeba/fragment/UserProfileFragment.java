package com.taokeba.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.taokeba.R;
import com.taokeba.bean.UserInfo;
import com.taokeba.ui.SignInActivity;

/**
 * 个人主页:
 * 信息展示，登出 
 * @Created by zhaolin on 14-2-25.
 * 
 * @Updated by Burjal Hou on 14-10-31
 */
@SuppressLint("ValidFragment")
public class UserProfileFragment extends Fragment {

    private ImageView portraitImageView;
    private TextView userNameTextView;
    private TextView nickNameTextView;
    private TextView departTextView;
    
    private ListView infoListView;
    
    private UserInfo userInfo;
    
    private Button btnSignOut;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_profile, container, false);
        
        userInfo = UserInfo.getUserInfo();
        
        portraitImageView = (ImageView) rootView.findViewById(R.id.profile_portrait);
        userNameTextView = (TextView) rootView.findViewById(R.id.profile_user_name);
        nickNameTextView = (TextView) rootView.findViewById(R.id.profile_nick_name);
        departTextView = (TextView) rootView.findViewById(R.id.profile_department);
        infoListView = (ListView) rootView.findViewById(R.id.profile_list);
        
        btnSignOut = (Button)rootView.findViewById(R.id.btnSignOut);
        btnSignOut.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				getActivity().finish();
				startActivity(new Intent(getActivity(),SignInActivity.class));
			}
		});
        
        portraitImageView.setImageResource(R.drawable.ic_launcher);
        userNameTextView.setText(userInfo.getUserName());
        nickNameTextView.setText(userInfo.getAliasName());
        departTextView.setText(userInfo.getDepartment());
        
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        
        Map<String, String> map = new HashMap<String, String>();
        map.put("title", "QQ");
        map.put("text", userInfo.getQQ());
        data.add(map);
        map = new HashMap<String, String>();
        map.put("title", "专业");
        map.put("text", userInfo.getMajor());
        data.add(map);
        
        SimpleAdapter adapter = new SimpleAdapter(getActivity(), data, R.layout.profile_list_item,
        										new String[] {"title", "text"},
        										new int[] {R.id.profile_item_title, R.id.profile_item});
        
        infoListView.setAdapter(adapter);
        infoListView.setDividerHeight(0);
        infoListView.setClickable(false);
        return rootView;
    }
    
    
}

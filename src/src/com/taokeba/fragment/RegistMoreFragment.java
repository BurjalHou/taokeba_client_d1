/*
 * Copyright 2012 Roman Nurik
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taokeba.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.taokeba.R;
import com.taokeba.bean.RegisterConfig;
import com.taokeba.widget.KCalendar;
import com.taokeba.widget.KCalendar.OnCalendarClickListener;
import com.taokeba.widget.KCalendar.OnCalendarDateChangedListener;
import com.taokeba.widget.RegistMorePage;
import com.taokeba.wizard.ui.PageFragmentCallbacks;

public class RegistMoreFragment extends Fragment {
    private static final String ARG_KEY = "key";

    private PageFragmentCallbacks mCallbacks;
    private String mKey;
    private RegistMorePage mPage;
    private Spinner spStudentType;//学生类型
    private Spinner spDepartment;//学院
    private TextView tvMajor;//专业
    private TextView tvStuClass;//班级
    private TextView etStartDate;//入学日期
    private TextView etEndDate;//毕业日期
    
    String date = null;// 设置默认选中的日期  格式为 “2014-04-05” 

    public static RegistMoreFragment create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        RegistMoreFragment fragment = new RegistMoreFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public RegistMoreFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        mKey = args.getString(ARG_KEY);
        mPage = (RegistMorePage) mCallbacks.onGetPage(mKey);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_regist_more, container, false);
        ((TextView) rootView.findViewById(android.R.id.title)).setText(mPage.getTitle());
        
        spStudentType = ((Spinner)rootView.findViewById(R.id.regist_student_type));
        spinnerSetAdapter(RegisterConfig.STUDENT_TYPE,spStudentType);
//        tvStudentType.setText(mPage.getData().getString(RegistMorePage.STUDENT_TYPE_DATA_KEY));
        
        spDepartment = ((Spinner)rootView.findViewById(R.id.regist_school));
        spinnerSetAdapter(RegisterConfig.STUDENT_DEPARTMENT,spDepartment);
//        tvDepartment.setText(mPage.getData().getString(RegistMorePage.SCHOOL_DATA_KEY));
        
        tvMajor = ((TextView)rootView.findViewById(R.id.regist_major));
        tvMajor.setText(mPage.getData().getString(RegistMorePage.MAJOR_DATA_KEY));
        
        tvStuClass = ((TextView)rootView.findViewById(R.id.regist_class));
        tvStuClass.setText(mPage.getData().getString(RegistMorePage.CLASS_DATA_KEY));
        
        etStartDate = ((TextView)rootView.findViewById(R.id.regist_start_date));
        etStartDate.setText(mPage.getData().getString(RegistMorePage.START_DATA_KEY));
        
        etEndDate = ((TextView)rootView.findViewById(R.id.regist_end_date));        
        etEndDate.setText(mPage.getData().getString(RegistMorePage.END_DATA_KEY));
        
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (!(activity instanceof PageFragmentCallbacks)) {
            throw new ClassCastException("Activity must implement PageFragmentCallbacks");
        }

        mCallbacks = (PageFragmentCallbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    private void spinnerSetAdapter(String[] data,Spinner spinner){
    	ArrayAdapter<String> aa = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_dropdown_item_1line,data); //第二个参数表示spinner没有展开前的UI类型 
    	spinner.setAdapter(aa); 
    }
    
    private void initSpinner() {
    	 //学生类型        
    	mPage.getData().putString(RegistMorePage.STUDENT_TYPE_DATA_KEY, 
    			spStudentType.getSelectedItem().toString());
    	 //学院
    	mPage.getData().putString(RegistMorePage.SCHOOL_DATA_KEY, 
    			spDepartment.getSelectedItem().toString());
    	mPage.notifyDataChanged();
    }
    
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initSpinner();
        //学生类型        
//        String typeTemp = spStudentType.getSelectedItem().toString();
//        mPage.getData().putString(RegistMorePage.STUDENT_TYPE_DATA_KEY, 
//        		typeTemp);
//        mPage.notifyDataChanged();
//        
//        //学院
//        String departmentTemp = spDepartment.getSelectedItem().toString();
//        mPage.getData().putString(RegistMorePage.SCHOOL_DATA_KEY, 
//        		departmentTemp);
//        mPage.notifyDataChanged();
        
        //专业
        tvMajor.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                    int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPage.getData().putString(RegistMorePage.MAJOR_DATA_KEY,
                        (editable != null) ? editable.toString() : null);
                mPage.notifyDataChanged();
                
              //刷新spinner内容
				initSpinner();
            }
        });
        
        //班级
        tvStuClass.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                    int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPage.getData().putString(RegistMorePage.CLASS_DATA_KEY,
                        (editable != null) ? editable.toString() : null);
                mPage.notifyDataChanged();
                
              //刷新spinner内容
				initSpinner();
            }
        });
        
        //入学日期        
        etStartDate.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new PopupWindows(getActivity(), etStartDate);
			}
		});

        //毕业日期
        etEndDate.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new PopupWindows(getActivity(), etEndDate);
			}
		});
        

    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);

        // In a future update to the support library, this should override setUserVisibleHint
        // instead of setMenuVisibility.
        if (tvMajor != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            if (!menuVisible) {
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            }
        }
    }
    
    public class PopupWindows extends PopupWindow {

		public PopupWindows(Context mContext, final View parent) {

			View view = View.inflate(mContext, R.layout.popupwindow_calendar,
					null);
			view.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.fade_in));
			LinearLayout ll_popup = (LinearLayout) view
					.findViewById(R.id.ll_popup);
			ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.push_bottom_in_1));

			setWidth(LayoutParams.FILL_PARENT);
			setHeight(LayoutParams.FILL_PARENT);
			setBackgroundDrawable(new BitmapDrawable());
			setFocusable(true);
			setOutsideTouchable(true);
			setContentView(view);
			showAtLocation(parent, Gravity.BOTTOM, 0, 0);
			update();

			final TextView popupwindow_calendar_month = (TextView) view
					.findViewById(R.id.popupwindow_calendar_month);
			final KCalendar calendar = (KCalendar) view
					.findViewById(R.id.popupwindow_calendar);
			Button popupwindow_calendar_bt_enter = (Button) view
					.findViewById(R.id.popupwindow_calendar_bt_enter);

			popupwindow_calendar_month.setText(calendar.getCalendarYear() + "年"
					+ calendar.getCalendarMonth() + "月");

			if (null != date) {
				if(parent == etStartDate) {
					if(mPage.getData().getString(RegistMorePage.START_DATA_KEY) != null)
						date = mPage.getData().getString(RegistMorePage.START_DATA_KEY);
				} else if(parent == etEndDate){
					if(mPage.getData().getString(RegistMorePage.END_DATA_KEY) != null)
						date = mPage.getData().getString(RegistMorePage.END_DATA_KEY);
				}
				int years = Integer.parseInt(date.substring(0,
						date.indexOf("-")));
				int month = Integer.parseInt(date.substring(
						date.indexOf("-") + 1, date.lastIndexOf("-")));
				popupwindow_calendar_month.setText(years + "年" + month + "月");

				calendar.showCalendar(years, month);
				calendar.setCalendarDayBgColor(date,
						R.drawable.calendar_date_focused);				
			}
			
			List<String> list = new ArrayList<String>(); //设置标记列表
			list.add("2014-04-01");
			list.add("2014-04-02");
			calendar.addMarks(list, 0);

			//监听所选中的日期
			calendar.setOnCalendarClickListener(new OnCalendarClickListener() {

				public void onCalendarClick(int row, int col, String dateFormat) {
					int month = Integer.parseInt(dateFormat.substring(
							dateFormat.indexOf("-") + 1,
							dateFormat.lastIndexOf("-")));
					
					if (calendar.getCalendarMonth() - month == 1//跨年跳转
							|| calendar.getCalendarMonth() - month == -11) {
						calendar.lastMonth();
						
					} else if (month - calendar.getCalendarMonth() == 1 //跨年跳转
							|| month - calendar.getCalendarMonth() == -11) {
						calendar.nextMonth();
						
					} else {
						calendar.removeAllBgColor(); 
						calendar.setCalendarDayBgColor(dateFormat,
								R.drawable.calendar_date_focused);
						date = dateFormat;//最后返回给全局 date
					}
				}
			});

			//监听当前月份
			calendar.setOnCalendarDateChangedListener(new OnCalendarDateChangedListener() {
				public void onCalendarDateChanged(int year, int month) {
					popupwindow_calendar_month
							.setText(year + "年" + month + "月");
				}
			});
			
			//上月监听按钮
			RelativeLayout popupwindow_calendar_last_month = (RelativeLayout) view
					.findViewById(R.id.popupwindow_calendar_last_month);
			popupwindow_calendar_last_month
					.setOnClickListener(new OnClickListener() {

						public void onClick(View v) {
							calendar.lastMonth();
						}

					});
			
			//下月监听按钮
			RelativeLayout popupwindow_calendar_next_month = (RelativeLayout) view
					.findViewById(R.id.popupwindow_calendar_next_month);
			popupwindow_calendar_next_month
					.setOnClickListener(new OnClickListener() {

						public void onClick(View v) {
							calendar.nextMonth();
						}
					});
			
			//关闭窗口
			popupwindow_calendar_bt_enter
					.setOnClickListener(new OnClickListener() {

						public void onClick(View v) {
							if(parent == etStartDate) {
								etStartDate.setText(date);
								mPage.getData().putString(RegistMorePage.START_DATA_KEY, 
						        		date);
						        mPage.notifyDataChanged();						        
							} else if(parent == etEndDate) {
								etEndDate.setText(date);
								mPage.getData().putString(RegistMorePage.END_DATA_KEY, 
						        		date);
						        mPage.notifyDataChanged();
							}
							
							//刷新spinner内容
							initSpinner();
							
							dismiss();
						}
					});
		}
	}
}

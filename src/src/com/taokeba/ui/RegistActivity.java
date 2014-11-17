package com.taokeba.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.taokeba.R;
import com.taokeba.bean.RegisterConfig;
import com.taokeba.common.NetworkUtils;
import com.taokeba.wizard.model.AbstractWizardModel;
import com.taokeba.wizard.model.ModelCallbacks;
import com.taokeba.wizard.model.Page;
import com.taokeba.wizard.ui.PageFragmentCallbacks;
import com.taokeba.wizard.ui.ReviewFragment;
import com.taokeba.wizard.ui.StepPagerStrip;

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

/**
 * 注册
 * Created by zhaolin on 14-3-3.
 * 
 * Updated by Burjal Hou on 14-10-30
 */
public class RegistActivity extends FragmentActivity
        implements PageFragmentCallbacks, ReviewFragment.Callbacks, ModelCallbacks {

    private ViewPager viewPager;
    private MyPagerAdapter pagerAdapter;

    private boolean editingAfterReview;

    private AbstractWizardModel wizardModel = new WizardModel(this);

    private boolean consumePageSelectedEvent;

    private Button nextButton;
    private Button prevButton;

    private List<Page> currentPageSequence;
    private StepPagerStrip stepPagerStrip;

    private ProgressDialog dialog;//注册提交
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        getActionBar().setHomeButtonEnabled(true);

        wizardModel.registerListener(this);

        pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(pagerAdapter);

        nextButton = (Button) findViewById(R.id.next_button);
        prevButton = (Button) findViewById(R.id.prev_button);

        stepPagerStrip = (StepPagerStrip) findViewById(R.id.strip);

        stepPagerStrip.setOnPageSelectedListener(new StepPagerStrip.OnPageSelectedListener() {
            @Override
            public void onPageStripSelected(int position) {
                position = Math.min(pagerAdapter.getCount() - 1, position);
                if(viewPager.getCurrentItem() != position) {
                    viewPager.setCurrentItem(position);
                }
            }
        });

        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                stepPagerStrip.setCurrentPage(position);
                if(consumePageSelectedEvent) {
                    consumePageSelectedEvent = false;
                    return;
                }
                editingAfterReview = false;
                updateBottomBar();
            }
        });

        /**
         * 注册提交
         */
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewPager.getCurrentItem() == currentPageSequence.size()) {
                    DialogFragment dg = new DialogFragment() {
                        @Override
                        public Dialog onCreateDialog(Bundle savedInstanceState) {
                            return new AlertDialog.Builder(getActivity())
                                    .setMessage(R.string.submit_confirm_message)
                                    .setPositiveButton(
                                            R.string.submit_confirm_button,
                                            new DialogInterface.OnClickListener() {												
												public void onClick(DialogInterface arg0, int arg1) {	//注册提交
													if(passwordIsIegal() == 0) {
														if(inputIsOk()) {
//															showProgress();
															new UserRegistTask().execute();
														} else {
															//提交信息不完善
															Toast.makeText(getActivity(), getResources().getString(R.string.regist_information_uncomplete), Toast.LENGTH_LONG).show();
														}
													} else if(passwordIsIegal() == 1) {
														//密码长度小于6位
														Toast.makeText(getActivity(), getResources().getString(R.string.regist_password_length_wrong), Toast.LENGTH_LONG).show();
													} else if(passwordIsIegal() == 2){
														//两次密码输入有误
														Toast.makeText(getActivity(), getResources().getString(R.string.regist_password_repeat_wrong), Toast.LENGTH_LONG).show();
													}													
												}
											})
                                    .setNegativeButton(android.R.string.cancel,
                                            null).create();
                        }
                    };
                    dg.show(getSupportFragmentManager(), "place_order_dialog");
                } else {
                    if (editingAfterReview) {
                        viewPager.setCurrentItem(pagerAdapter.getCount() - 1);
                    } else {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    }
                }
            }
        });

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
            }
        });

        onPageTreeChanged();
        updateBottomBar();
    }

    private void showProgress() {
        dialog = new ProgressDialog(this.getApplication().getApplicationContext());
//        		this.getApplicationContext());
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage(getResources().getString(R.string.sign_in_regist_in));
        dialog.setCancelable(true);
        dialog.show();
    }

    /**
     * 注册
     * @author Burjal Hou
     *
     */
    public class UserRegistTask extends AsyncTask<Void, Void, Boolean> {
        
        private int result = 0;
       
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(result==(200)) {//注册成功
                Toast.makeText(getApplicationContext(), getResources().getText(R.string.regist_success), Toast.LENGTH_SHORT).show();
//                dialog.cancel();
                startActivity(new Intent(getApplicationContext(),SignInActivity.class));
            } else {//注册失败
                Toast.makeText(getApplicationContext(), getResources().getText(R.string.regist_network), Toast.LENGTH_SHORT).show();
//                dialog.cancel();
            }
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            String url = NetworkUtils.WEB_SERVER_ADDRESS + "Register";
            HttpPost httpRequest = new HttpPost(url);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            
//------------test regist
//           RegisterConfig.userID = "2013456";
//           RegisterConfig.name = "小王";
//          RegisterConfig.nickName = "fly";
//          RegisterConfig.qqNum = "132345647";
//          RegisterConfig.email = "15456454@qq.com";
//          RegisterConfig.departmentID = "03";
//          RegisterConfig.password = "123456";
//          RegisterConfig.type = "博士";
//          RegisterConfig.major = "01";//departmentID
//          RegisterConfig.stuClass = "2";
//          RegisterConfig.entranceYear = "2012-10-10";
//           RegisterConfig.graduateDate = "2019-6-30";
            
//--------------            
            params.add(new BasicNameValuePair("user_id", RegisterConfig.userID));
            params.add(new BasicNameValuePair("name", RegisterConfig.name));
            params.add(new BasicNameValuePair("username", RegisterConfig.nickName));
            params.add(new BasicNameValuePair("qq_num", RegisterConfig.qqNum));
            params.add(new BasicNameValuePair("email", RegisterConfig.email));
            params.add(new BasicNameValuePair("departmentid", RegisterConfig.departmentID.split("-")[0]));//学院 ,存储格式为：00-研究生院，上传必须为整数
            params.add(new BasicNameValuePair("password", RegisterConfig.password));
            params.add(new BasicNameValuePair("type", RegisterConfig.type));
            params.add(new BasicNameValuePair("major", RegisterConfig.major));
            params.add(new BasicNameValuePair("stu_class", RegisterConfig.stuClass));
            params.add(new BasicNameValuePair("entranceyear", RegisterConfig.entranceYear));
            params.add(new BasicNameValuePair("graduatedate", RegisterConfig.graduateDate));
            
Log.i("Regist...info","regist information--------------------->\n"+"user_id:"+ RegisterConfig.userID+";\tname:"+RegisterConfig.name+";\tusername:"+RegisterConfig.nickName+";\tqq:"+RegisterConfig.qqNum+";\temail:"+RegisterConfig.email+
		";\tdepartmentID:"+RegisterConfig.departmentID.split("-")[0]+";\tpassword:"+RegisterConfig.password+";\ttype:"+RegisterConfig.type+";\tmajor:"+RegisterConfig.major+
		";\tstu_class:"+RegisterConfig.stuClass+";\tentranceYear:"+RegisterConfig.entranceYear+";\tgraduate:"+RegisterConfig.graduateDate);
            try {
                httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
                result = httpResponse.getStatusLine().getStatusCode();
                if(result == 200) {
                    
                } else {
                    Log.v("error...ActRegistIn", ""+result);
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
    
    
    /**
     * 检查两次密码输入是否相同
     * @return 0:合法   1：长度不够   2：两次输入不相同
     */
    private int passwordIsIegal() {
    	int result = 0;    	
    	if(RegisterConfig.password.length()<6) {
    		result = 1;
    	} else if (!(RegisterConfig.password.equals(RegisterConfig.passwordRepat))) {
    		result = 2;
    	}
    	return result;
    }
    
    /**
     * 检查提交信息是否完整
     * @return
     */
    private boolean inputIsOk() {
    	boolean result = true;
    	if(RegisterConfig.userID.equals(RegisterConfig.REGISTER_INPUT_NONE)|RegisterConfig.name.equals(RegisterConfig.REGISTER_INPUT_NONE)|
    			RegisterConfig.nickName.equals(RegisterConfig.REGISTER_INPUT_NONE)|RegisterConfig.qqNum.equals(RegisterConfig.REGISTER_INPUT_NONE)|
    			RegisterConfig.departmentID.equals(RegisterConfig.REGISTER_INPUT_NONE)|RegisterConfig.password.equals(RegisterConfig.REGISTER_INPUT_NONE)|
    			RegisterConfig.stuClass.equals(RegisterConfig.REGISTER_INPUT_NONE)|RegisterConfig.entranceYear.equals(RegisterConfig.REGISTER_INPUT_NONE)|
    			RegisterConfig.graduateDate.equals(RegisterConfig.REGISTER_INPUT_NONE)) {
    		result = false;
    	}
    	return result;
    }
    
    
    private void updateBottomBar() {
        int position = viewPager.getCurrentItem();
        if (position == currentPageSequence.size()) {
            nextButton.setText(R.string.finish);
            //nextButton.setBackgroundResource(R.drawable.finish_background);
            //nextButton.setTextAppearance(this, R.style.TextAppearanceFinish);
        } else {
            nextButton.setText(editingAfterReview ? R.string.regist_prev
                    : R.string.regist_next);
//            nextButton
//                    .setBackgroundResource(R.drawable.selectable_item_background);
            TypedValue v = new TypedValue();
            getTheme().resolveAttribute(android.R.attr.textAppearanceMedium, v,
                    true);
            nextButton.setTextAppearance(this, v.resourceId);
            nextButton.setEnabled(position != pagerAdapter.getCutOffPage());
        }

        prevButton
                .setVisibility(position <= 0 ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    public void onPageTreeChanged() {
        currentPageSequence = wizardModel.getCurrentPageSequence();
        recalculateCutOffPage();
        stepPagerStrip.setPageCount(currentPageSequence.size() + 1); // + 1 =
        // review
        // step
        pagerAdapter.notifyDataSetChanged();
        updateBottomBar();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wizardModel.unregisterListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle("model", wizardModel.save());
    }

    @Override
    public AbstractWizardModel onGetModel() {
        return wizardModel;
    }

    @Override
    public void onEditScreenAfterReview(String key) {
        for (int i = currentPageSequence.size() - 1; i >= 0; i--) {
            if (currentPageSequence.get(i).getKey().equals(key)) {
                consumePageSelectedEvent = true;
                editingAfterReview = true;
                viewPager.setCurrentItem(i);
                updateBottomBar();
                break;
            }
        }
    }

    @Override
    public void onPageDataChanged(Page page) {
        if (page.isRequired()) {
            if (recalculateCutOffPage()) {
                pagerAdapter.notifyDataSetChanged();
                updateBottomBar();
            }
        }
    }

    @Override
    public Page onGetPage(String key) {
        return wizardModel.findByKey(key);
    }

    private boolean recalculateCutOffPage() {
        // Cut off the pager adapter at first required page that isn't completed
        int cutOffPage = currentPageSequence.size() + 1;
        for (int i = 0; i < currentPageSequence.size(); i++) {
            Page page = currentPageSequence.get(i);
            if (page.isRequired() && !page.isCompleted()) {
                cutOffPage = i;
                break;
            }
        }

        if (pagerAdapter.getCutOffPage() != cutOffPage) {
            pagerAdapter.setCutOffPage(cutOffPage);
            return true;
        }

        return false;
    }

    public class MyPagerAdapter extends FragmentStatePagerAdapter {
        private int mCutOffPage;
        private Fragment mPrimaryItem;

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            if (i >= currentPageSequence.size()) {
                return new ReviewFragment();
            }

            return currentPageSequence.get(i).createFragment();
        }

        @Override
        public int getItemPosition(Object object) {
            // TODO: be smarter about this
            if (object == mPrimaryItem) {
                // Re-use the current fragment (its position never changes)
                return POSITION_UNCHANGED;
            }

            return POSITION_NONE;
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position,
                                   Object object) {
            super.setPrimaryItem(container, position, object);
            mPrimaryItem = (Fragment) object;
        }

        @Override
        public int getCount() {
            return Math.min(mCutOffPage + 1, currentPageSequence == null ? 1
                    : currentPageSequence.size() + 1);
        }

        public void setCutOffPage(int cutOffPage) {
            if (cutOffPage < 0) {
                cutOffPage = Integer.MAX_VALUE;
            }
            mCutOffPage = cutOffPage;
        }

        public int getCutOffPage() {
            return mCutOffPage;
        }
    }
}

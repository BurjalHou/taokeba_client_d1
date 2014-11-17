package com.taokeba;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.taokeba.ui.MainActivity;
import com.taokeba.ui.SignInActivity;

/**
 * Created by zhaolin on 14-2-17.
 */
public class AppStart extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View view = View.inflate(this, R.layout.start, null);
        setContentView(view);

        AlphaAnimation aa = new AlphaAnimation(0.7f, 1.0f);
        aa.setDuration(1000);
        view.setAnimation(aa);

        aa.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                redirectTo();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    private void redirectTo() {
    	SharedPreferences sp = getSharedPreferences("login_info", MODE_PRIVATE);
    	boolean auto = sp.getBoolean("auto", false);
    	
    	Intent intent = null;
    	if(auto == true) {
    		intent = new Intent(this, MainActivity.class);
    	} else {
    		intent = new Intent(this, SignInActivity.class);
    	}
        startActivity(intent);
        finish();
    }
}

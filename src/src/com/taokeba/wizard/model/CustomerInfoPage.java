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

package com.taokeba.wizard.model;

import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.taokeba.wizard.ui.CustomerInfoFragment;

import java.util.ArrayList;

public class CustomerInfoPage extends Page {
    public static final String STUDENT_NUM_DATA_KEY = "student_num";
    public static final String NAME_DATA_KEY = "name";
    public static final String NICK_NAME_DATA_KEY = "nick_name";
    public static final String QQ_DATA_KEY = "qq";
    public static final String PASSWORD_DATA_KAY = "password";
    public static final String PASSWORD_AGAIN_DATA_KEY = "password_again";

    public CustomerInfoPage(ModelCallbacks callbacks, String title) {
        super(callbacks, title);
    }

    @Override
    public Fragment createFragment() {
        return CustomerInfoFragment.create(getKey());
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {
        dest.add(new ReviewItem("学号", mData.getString(STUDENT_NUM_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("姓名", mData.getString(NAME_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("昵称", mData.getString(NICK_NAME_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("QQ", mData.getString(QQ_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("密码", mData.getString(PASSWORD_DATA_KAY), getKey(), -1));
        dest.add(new ReviewItem("重复密码", mData.getString(PASSWORD_AGAIN_DATA_KEY), getKey(), -1));
    }

    @Override
    public boolean isCompleted() {
        return !TextUtils.isEmpty(mData.getString(NAME_DATA_KEY));
    }
}

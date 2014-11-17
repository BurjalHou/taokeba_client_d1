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

package com.taokeba.widget;

import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.taokeba.fragment.RegistMoreFragment;
import com.taokeba.wizard.model.ModelCallbacks;
import com.taokeba.wizard.model.Page;
import com.taokeba.wizard.model.ReviewItem;

import java.util.ArrayList;

public class RegistMorePage extends Page {
    public static final String STUDENT_TYPE_DATA_KEY = "student_type";
    public static final String SCHOOL_DATA_KEY = "school";
    public static final String MAJOR_DATA_KEY = "major";
    public static final String CLASS_DATA_KEY = "class";
    public static final String START_DATA_KEY = "start_date";
    public static final String END_DATA_KEY = "end_date";

    public RegistMorePage(ModelCallbacks callbacks, String title) {
        super(callbacks, title);
    }

    @Override
    public Fragment createFragment() {
        return RegistMoreFragment.create(getKey());
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {
        dest.add(new ReviewItem("学生类型", mData.getString(STUDENT_TYPE_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("学院", mData.getString(SCHOOL_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("专业", mData.getString(MAJOR_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("班级", mData.getString(CLASS_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("入学时间", mData.getString(START_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("毕业时间", mData.getString(END_DATA_KEY), getKey(), -1));
    }

    @Override
    public boolean isCompleted() {
        return !TextUtils.isEmpty(mData.getString(STUDENT_TYPE_DATA_KEY));
    }
}

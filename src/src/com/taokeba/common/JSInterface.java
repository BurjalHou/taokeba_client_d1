package com.taokeba.common;

import com.taokeba.bean.SectionGrade;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

public class JSInterface {
	
	private Context context = null;
	private int id;
	private SectionGrade grade;
	
	public JSInterface(Context c, int id, SectionGrade grade) {
		this.context = c;
		this.id = id;
		this.grade = grade;
	}

	@JavascriptInterface
	public int getANum() {
		return grade.getGradeANum();
	}
	
	@JavascriptInterface
	public int getBNum() {
		return grade.getGradeBNum();
	}
	
	@JavascriptInterface
	public int getCNum() {
		return grade.getGradeCNum();
	}
	
	@JavascriptInterface
	public int getDNum() {
		return grade.getGradeDNum();
	}
	
	@JavascriptInterface
	public int getENum() {
		return grade.getGradeENum();
	}
	
	@JavascriptInterface
	public void showToast(String s) {
		Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
	}
	
	@JavascriptInterface
	public int getSectionID() {
		return this.id;
	}
	
}
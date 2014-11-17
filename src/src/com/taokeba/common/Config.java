package com.taokeba.common;

public class Config {

	private static int SECTION_ID = 0; //SectionActivity ,为ActionBar返回无intent数值问题
	private static String SEARCH_SECTION_QUERY = "";//MainActivity,SearchActivity,SearchSectionActivity三个用到搜索成员变量
	
	public static int getSECTION_ID() {
		return SECTION_ID;
	}
	public static void setSECTION_ID(int sECTION_ID) {
		SECTION_ID = sECTION_ID;
	}
	
	public static String getSEARCH_SECTION_QUERY() {
		return SEARCH_SECTION_QUERY;
	}
	public static void setSEARCH_SECTION_QUERY(String sEARCH_SECTION_QUERY) {
		SEARCH_SECTION_QUERY = sEARCH_SECTION_QUERY;
	}
	
	
}

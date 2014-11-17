package com.taokeba.bean;

import android.view.View;

public class RegisterConfig {

	//注册默认填充空白信息
	//ReviewFragment.java # method:View getView (line:174)
	//RegistActivity.java #method: boolean inputIsOk()
	public static final String REGISTER_INPUT_NONE = "(None)";   
	
	//Spinner
	public static final String[] STUDENT_TYPE= {"本科","硕士","博士","毕业生"};
	public static final String[] STUDENT_DEPARTMENT = {//"00-研究生院",
		"01-通信工程学院","02-电子工程学院","03-计算机学院",		"04-机电工程学院","05-物理与光电工程学院",
		"06-经济与管理学院","07-数学与统计学院","08-人文学院","09-外国语学院","10-软件学院",
		"11-微电子学院","12-生命科学技术学院","13-空间科学与技术学院","14-先进材料与纳米科技学院"};
	
	//各专业
//	public static final String[] MAJOR_ALL = {"通信与信息系统","光通信","交通信息工程及控制","军事通信学","密码学","信息安全","电子与通信工程",
//		"电路与系统","电磁场与微波技术","信息对抗技术","信号与信息处理","遥感信息科学与技术","智能信息处理","系统工程","模式识别与智能系统","环境科学",
//		"环境工程","电子与通信工程","教育技术学","计算机系统结构","计算机软件与理论","计算机应用技术","软件工程","计算机技术","工程力学","机械制造及其自动化",
//		"机械电子工程","机械设计及理论","电子机械科学与技术","工业设计","精密仪器及机械","测试计量技术及仪器","电机与电器","控制理论与控制工程","机械工程",
//		"仪器仪表工程","控制工程","等离子体物理","凝聚态物理","光学","无线电物理","光学工程","物理电子学","光学工程","电子与通信工程","国民经济学","金融学",
//		"产业经济学","管理科学与工程","会计学","企业管理","技术经济及管理","行政管理","图书馆学","情报学","金融","工业工程","物流工程","工商管理","计算数学",
//		"","","","",""	
//		概率论与数理统计
//		应用数学
//		运筹学与控制论
//		统计学
//		美学
//		宗教学
//		马克思主义基本原理
//		思想政治教育
//		高等教育学
//		体育教育训练学
//		英语语言文学
//		外国语言学及应用语言
//		英语笔译
//		软件工程
//		电力电子与电力传动
//		微电子学与固体电子学
//		集成电路系统设计
//		集成电路工程
//		软件工程
//		生物信息科学与技术
//		生物医学工程
//		生物材料与细胞工程
//		生物医学工程
//		检测技术与自动化装置
//		导航、制导与控制
//		材料物理与化学
//		材料学
//		应用化学
//		材料工程
//		光通信
//		通信与信息系统
//		信息安全
//		军事通信学
//		密码学
//		电路与系统
//		电磁场与微波技术
//		信号与信息处理
//		信息对抗技术
//		智能信息处理
//		模式识别与智能系统
//		遥感信息科学与技术
//		计算机系统结构
//		计算机软件与理论
//		计算机应用技术
//		软件工程
//		机械制造及其自动化
//		机械电子工程
//		机械设计及理论
//		电子机械科学与技术
//		测试计量技术及仪器
//		控制理论与控制工程
//		机器人技术
//		物理电子学
//		光学工程
//
//		无线电物理
//		管理科学与工程
//		概率论与数理统计
//		应用数学
//		运筹学与控制论
//		微电子学与固体电子学
//		集成电路系统设计
//		生物信息科学与技术
//		导航、制导与控制
//		材料物理与化学
//};
	public static final String[] MAJOR_01 = {};
	public static final String[] MAJOR_02 = {};
	public static final String[] MAJOR_03 = {};
	public static final String[] MAJOR_04 = {};
	public static final String[] MAJOR_05 = {};
	public static final String[] MAJOR_06 = {};
	public static final String[] MAJOR_07 = {};
	public static final String[] MAJOR_08 = {};
	public static final String[] MAJOR_09 = {};
	public static final String[] MAJOR_10 = {};
	public static final String[] MAJOR_11 = {};
	public static final String[] MAJOR_12 = {};
	public static final String[] MAJOR_13 = {};
	public static final String[] MAJOR_14 = {};
	
	
	
	public static String userID = "" ;
	public static String name = "" ;
	public static String nickName = "" ;
	public static String qqNum = "" ;
	public static String email = "" ;
	public static String departmentID = "" ;
	public static String password = "" ;
	public static String type = "" ;
	public static String major = "" ;
	public static String stuClass = "" ;
	public static String entranceYear = "" ;
	public static String graduateDate = "" ;
	public static String passwordRepat = "" ;//重复密码
	
	
	
}

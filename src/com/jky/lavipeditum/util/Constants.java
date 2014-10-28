package com.jky.lavipeditum.util;

/**
 * 
 * @ClassName: Constants 
 * @Description: 常量类
 *
 * @author o0teamo0o
 * @date 2014年10月21日 上午9:37:12 
 *
 */
public class Constants {

	public static final String PREFERENCES_NAME = "LavipeditumPreferences"; //共享首选项文件名字
	
	public static final String MAIN_FRAGMENT_NAME = "MainFragment"; //主界面fragment
	public static final String LEFT_FRAGMENT_NAME = "LeftMenuFragment"; //左边菜单栏fragment
	
	public static final int GO_LOGIN_REQUESTCODE = 00; //主界面跳转到登陆界面的请求code
	public static final int LOGIN_RETURN_RESULTCODE = 11;  //登陆返回信息
	public static final int LOGIN_BACK_RESULTCOCE = -1; //在登陆界面按返回的返回值
	
	public static final int HOME_PAGER = 3;
	public static final int HEREABOUT_PAGER = 2;
	public static final int MY_PAGER = 1;
	public static final int SETTING_PAGER = 0;
}

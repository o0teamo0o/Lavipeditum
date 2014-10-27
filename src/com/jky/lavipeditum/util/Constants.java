package com.jky.lavipeditum.util;

import android.util.SparseArray;

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
	
	/*****************************微博SDK常量开始***********************************/
	
	public static final String APP_KEY = "3723347129";  //APP_KEY
	public static final String REDIRECT_URL ="https://api.weibo.com/oauth2/default.html"; //回调页面
	public static final String SCOPE = ""; //应用对应的权限
	
	public static final String WEIBO_PREFERENCE_NAME = "com_jky_weibo_sdk"; //微博的共享首先项名称
	public static final String KEY_UID = "uid"; //用户id
	public static final String KEY_ACCESS_TOKEN = "access_token"; //授权的access_token令牌
	public static final String KEY_EXPIRES_IN = "expires_in"; //token保存时间
	
	public static final String API_SERVER       = "https://api.weibo.com/2"; //访问微博服务接口的地址
	public static final String HTTPMETHOD_POST  = "POST"; //POST 请求方式
	public static final String HTTPMETHOD_GET   = "GET"; //GET 请求方式
	public static final String API_BASE_URL = API_SERVER + "/users"; //用户数据
	
	public static final int READ_USER           = 0;
	public static final int READ_USER_BY_DOMAIN = 1;
	public static final int READ_USER_COUNT     = 2;
	
	public static final SparseArray<String> SAPILIST = new SparseArray<String>();
    static {
    	SAPILIST.put(READ_USER,           API_BASE_URL + "/show.json");
    	SAPILIST.put(READ_USER_BY_DOMAIN, API_BASE_URL + "/domain_show.json");
    	SAPILIST.put(READ_USER_COUNT,     API_BASE_URL + "/counts.json");
    }
	
	/*****************************微博SDK常量结束***********************************/

	public static final String PREFERENCES_NAME = "LavipeditumPreferences"; //共享首选项文件名字
	
	public static final String MAIN_FRAGMENT_NAME = "MainFragment"; //主界面fragment
	public static final String LEFT_FRAGMENT_NAME = "LeftMenuFragment"; //左边菜单栏fragment
	
	public static final int GO_LOGIN_REQUESTCODE = 00; //主界面跳转到登陆界面的请求code
	public static final int LOGIN_RETURN_RESULTCODE = 11;  //登陆返回信息
	public static final int LOGIN_BACK_RESULTCOCE = -1; //在登陆界面按返回的返回值
	
	//主界面的四个页面
	public static final int HOME_PAGER = 3; //主页
	public static final int HEREABOUT_PAGER = 2; //附近
	public static final int MY_PAGER = 1; //我的
	public static final int SETTING_PAGER = 0; //设置
	
	public static final String SMS_APPKEY = "3f9aec45c4d9"; //短信验证SDK Appkey
	public static final String SMS_APP_SECRET = "7eddda429188e467a7d17b5d85c246a3"; //短信验证SDK App Secret
	
}

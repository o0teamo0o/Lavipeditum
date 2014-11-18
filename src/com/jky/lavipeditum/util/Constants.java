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
	
	public static final String WEIBO_APP_KEY = "3723347129";  //APP_KEY
	public static final String WEIBO_REDIRECT_URL ="http://www.jky.com/lavipeditum/3723347129"; //回调页面
	public static final String WEIBO_SCOPE = ""; //应用对应的权限
	
	public static final String WEIBO_PREFERENCE_NAME = "com_jky_weibo_sdk"; //微博的共享首先项名称
	public static final String WEIBO_KEY_UID = "uid"; //用户id
	public static final String WEIBO_KEY_ACCESS_TOKEN = "access_token"; //授权的access_token令牌
	public static final String WEIBO_KEY_EXPIRES_IN = "expires_in"; //token保存时间
	
	public static final String WEIBO_API_SERVER       = "https://api.weibo.com/2"; //访问微博服务接口的地址
	public static final String WEIBO_HTTPMETHOD_POST  = "POST"; //POST 请求方式
	public static final String WEIBO_HTTPMETHOD_GET   = "GET"; //GET 请求方式
	public static final String WEIBO_API_BASE_URL = WEIBO_API_SERVER + "/users"; //用户数据
	
	public static final int WEIBO_READ_USER           = 0;
	public static final int WEIBO_READ_USER_BY_DOMAIN = 1;
	public static final int WEIBO_READ_USER_COUNT     = 2;
	
	public static final SparseArray<String> SAPILIST = new SparseArray<String>();
    static {
    	SAPILIST.put(WEIBO_READ_USER,           WEIBO_API_BASE_URL + "/show.json");
    	SAPILIST.put(WEIBO_READ_USER_BY_DOMAIN, WEIBO_API_BASE_URL + "/domain_show.json");
    	SAPILIST.put(WEIBO_READ_USER_COUNT,     WEIBO_API_BASE_URL + "/counts.json");
    }
	
	/*****************************微博SDK常量结束***********************************/
    
    
    /*****************************腾讯SDK常量开始***********************************/
     
    public static final String TENCENT_APP_ID = "1103408492"; //APP_ID
    public static final String TENCENT_APP_KEY = "dNVEMvDnFfcnZ36X"; //APP_KEY
    
    public static final String TENCENT_PARAM_ACCESS_TOKEN = "access_token"; //腾讯token令牌
    public static final String TENCENT_PARAM_EXPIRES_IN = "expires_in"; //token保存时间
    public static final String TENCENT_PARAM_OPEN_ID = "openid"; //用户id
    
    public static final int TENCENT_REQUEST_API = 10100; //通过QQ本身软件登陆
    public static final int TENCENT_RESULT_LOGIN = 10101; //登陆返回码
    public static final int TENCENT_REQUEST_APPBAR = 10102; //本app内登陆
    
    /*****************************腾讯SDK常量结束***********************************/
    
    /*****************************Share_SDK短信常量开始*****************************/
	
	public static final String SMS_APPKEY = "3f9aec45c4d9"; //短信验证SDK Appkey
	public static final String SMS_APP_SECRET = "7eddda429188e467a7d17b5d85c246a3"; //短信验证SDK App Secret
	public static final String SMS_COUNTRY_ID = "86"; //中国国家代号
	
	// 短信注册，随机产生头像
	public static final String[] AVATARS = {
		"http://tupian.qqjay.com/u/2011/0729/e755c434c91fed9f6f73152731788cb3.jpg",
		"http://99touxiang.com/public/upload/nvsheng/125/27-011820_433.jpg",
		"http://img1.touxiang.cn/uploads/allimg/111029/2330264224-36.png",
		"http://img1.2345.com/duoteimg/qqTxImg/2012/04/09/13339485237265.jpg",
		"http://diy.qqjay.com/u/files/2012/0523/f466c38e1c6c99ee2d6cd7746207a97a.jpg",
		"http://img1.touxiang.cn/uploads/20121224/24-054837_708.jpg",
		"http://img1.touxiang.cn/uploads/20121212/12-060125_658.jpg",
		"http://img1.touxiang.cn/uploads/20130608/08-054059_703.jpg",
		"http://diy.qqjay.com/u2/2013/0422/fadc08459b1ef5fc1ea6b5b8d22e44b4.jpg",
		"http://img1.2345.com/duoteimg/qqTxImg/2012/04/09/13339510584349.jpg",
		"http://img1.touxiang.cn/uploads/20130515/15-080722_514.jpg",
		"http://diy.qqjay.com/u2/2013/0401/4355c29b30d295b26da6f242a65bcaad.jpg"
	};
	
	/*****************************Share_SDK短信常量结束*****************************/

	public static final String PREFERENCES_NAME = "LavipeditumPreferences"; //共享首选项文件名字
	
	public static final String MAIN_FRAGMENT_NAME = "MainFragment"; //主界面fragment
	public static final String LEFT_FRAGMENT_NAME = "LeftMenuFragment"; //左边菜单栏fragment

	public static final String SELLER = "seller"; //商人
	public static final String COOPERATION = "cooperation"; //是否合租
	
	public static final int GO_LOGIN_REQUESTCODE = 00; //主界面跳转到登陆界面的请求code
	public static final int LOGIN_RETURN_RESULTCODE = 10;  //登陆返回信息
	public static final int LOGIN_BACK_RESULTCOCE = -1; //在登陆界面按返回的返回值
	public static final int LOGIN_SUCCESS = 100; //登陆成功之后的返回值
	public static final int GO_CONSTACT_PAGER_REQUESTCODE = 01; //注册页面跳转到添加联系人号码界面的请求code
	public static final int CONSTACT_PAGER_RESULTCODE = 11; //联系人返回的界面
	public static final int LOCATION_CITY_BACK_REQUESTCODE = 02; //主界面弹出的popupwindow跳转到定位界面
	public static final int LOCATION_CITY_RESULTCOCE = 12; //定位城市返回值
	public static final int GO_SELLER_LOGIN_REQUESTCODE = 13; //跳转到商家登陆界面
	public static final int SELLER_LOGIN_BACK_REQUESTCODE = 03; //商家登陆界面返回值
	public static final int SELLER_LOGIN_SUCCESS = 103; //商家登陆成功的返回值
	
	//主界面的四个页面
	public static final int HOME_PAGER = 3; //主页
	public static final int HEREABOUT_PAGER = 2; //附近
	public static final int MY_PAGER = 1; //我的
	public static final int SETTING_PAGER = 0; //设置
	
	//数据库的保存路径
	public static final String FILEPATH = "data/data/com.jky.lavipeditum/db/lavipeditum_region.db";
	//数据库存放的文件夹
	public static final String PATHSTR = "data/data/com.jky.lavipeditum/db";
	
}


































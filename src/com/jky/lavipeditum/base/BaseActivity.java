package com.jky.lavipeditum.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

import com.jky.lavipeditum.util.WeiboAccessTokenKeeper;
import com.jky.lavipeditum.util.LavipeditumPreferences;

/**
 * 
 * @ClassName: BaseActivity 
 * @Description: Activity基类
 *
 * @author o0teamo0o
 * @date 2014年10月19日 下午10:23:56 
 *
 */
public abstract class BaseActivity extends Activity {

	public LavipeditumPreferences preferences; //全局共享首选项
	public Context context;  //上下文
	public WeiboAccessTokenKeeper tokenKeeper; //微博共享首选项

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//得到全局共享首选项
		preferences = new LavipeditumPreferences(this);
		//初始化上下文
		context = getApplicationContext();
		//操作weibo共享首选项
		tokenKeeper = new WeiboAccessTokenKeeper(this);
		
		initView(savedInstanceState);
		initData();
		initListener();
	}
	
	/**
	 * 
	 * @Title: initView 
	 * @Description: 初始化视图
	 * @param savedInstanceState
	 */
	protected abstract void initView(Bundle savedInstanceState);
	
	/**
	 * 
	 * @Title: initData 
	 * @Description: 加载数据
	 * @param savedInstanceState
	 */
	protected abstract void initData();
	
	/**
	 * 
	 * @Title: initListener 
	 * @Description: 初始化监听
	 */
	protected abstract void initListener();

}

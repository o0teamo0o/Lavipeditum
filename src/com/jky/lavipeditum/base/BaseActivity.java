package com.jky.lavipeditum.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

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

	public LavipeditumPreferences preferences; //共享首选项
	public Context context;  //上下文

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//得到共享首选项
		preferences = new LavipeditumPreferences(this);
		//初始化上下文
		context = getApplicationContext();
		
		initView(savedInstanceState);
		initData(savedInstanceState);
		initListener();
	}
	
	/**
	 * 
	 * @Title: initView 
	 * @Description: 初始化试图方法
	 * @param savedInstanceState
	 */
	protected abstract void initView(Bundle savedInstanceState);
	
	/**
	 * 
	 * @Title: initData 
	 * @Description: 加载数据
	 * @param savedInstanceState
	 */
	protected abstract void initData(Bundle savedInstanceState);
	
	/**
	 * 
	 * @Title: initListener 
	 * @Description: 初始化监听
	 */
	protected abstract void initListener();

}

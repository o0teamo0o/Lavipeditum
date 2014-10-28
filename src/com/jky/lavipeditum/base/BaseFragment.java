package com.jky.lavipeditum.base;

import com.jky.lavipeditum.activity.MainActivity;
import com.jky.lavipeditum.lib.slidingmenu.SlidingMenu;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 
 * @ClassName: BaseFragment 
 * @Description: Fragment基类 用来初始化一下共用数据
 *
 * @author o0teamo0o
 * @date 2014年10月21日 上午7:40:42 
 *
 */
public abstract class BaseFragment extends Fragment {

	public Context context; //初始化上下文
	public SlidingMenu slidingmenu;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getActivity();
		slidingmenu = ((MainActivity)getActivity()).slidingMenu;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = initView(inflater);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initData(savedInstanceState);
		initListener();
	}

	/**
	 * 
	 * @Title: initView 
	 * @Description: 初始化View
	 * @param inflater
	 * @return
	 */
	protected abstract View initView(LayoutInflater inflater);
	
	/**
	 * 
	 * @Title: initData 
	 * @Description: 初始化数据
	 * @param savedInstanceState
	 */
	protected abstract void initData(Bundle savedInstanceState);
	
	/**
	 * 
	 * @Title: initListener 
	 * @Description: 初始化监听事件
	 */
	protected abstract void initListener();
}

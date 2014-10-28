package com.jky.lavipeditum.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.jky.lavipeditum.LavipeditumApplication;
import com.jky.lavipeditum.R;
import com.jky.lavipeditum.adapter.MainFrameAdapter;
import com.jky.lavipeditum.base.BaseFragment;
import com.jky.lavipeditum.custom_vIew.CustomViewPager;

/**
 * 
 * @ClassName: MainFragment 
 * @Description: 主界面对Fragment
 *
 * @author o0teamo0o
 * @date 2014年10月21日 下午10:20:24 
 *
 */
public class MainFragment extends BaseFragment{

	private View view;
	private CustomViewPager cvp_main_group;
	private RadioGroup rg_mainfragment_group;

	@Override
	public View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.main_frame, null);
		
		cvp_main_group = (CustomViewPager) view.findViewById(R.id.cvp_main_group);
		rg_mainfragment_group = (RadioGroup) view.findViewById(R.id.rg_mainfragment_group);
		
		return view;
	}

	@Override
	public void initData(Bundle savedInstanceState) {
		List<Fragment> mainFragments = initMainFrameData();
		MainFrameAdapter adapter = new MainFrameAdapter(getChildFragmentManager(), mainFragments);
		cvp_main_group.setAdapter(adapter);
	}

	@Override
	protected void initListener() {
		
		//点击下面按钮切换不同的fragment
		rg_mainfragment_group.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				//主页
				case R.id.rb_home:
					cvp_main_group.setCurrentItem(0, false);
					break;
				//附近
				case R.id.rb_hereabout:
					cvp_main_group.setCurrentItem(1, false);
					break;
				//我的
				case R.id.rb_my:
					//判断是否登陆， 如果没有登陆应该先进入登陆界面
					if (LavipeditumApplication.isLogin) {
						cvp_main_group.setCurrentItem(2, false);
					}else{
						cvp_main_group.setCurrentItem(4, false); 
					}
					break;
				//设置
				case R.id.rb_setting:
					cvp_main_group.setCurrentItem(3, false);
					break;
					
				}
			}
		});
	}

	/**
	 * 
	 * @Title: initMainFrameData 
	 * @Description: 初始化Main框架的界面
	 */
	private List<Fragment> initMainFrameData() {
		List<Fragment> mainFragments = new ArrayList<Fragment>();
		
		//主页
		HomeFragment homeFragment = new HomeFragment();
		mainFragments.add(homeFragment);
		
		//附近
		HereaboutFragment hereaboutFragment = new HereaboutFragment();
		mainFragments.add(hereaboutFragment);
		
		//我的
		MyFragment myFragment = new MyFragment();
		mainFragments.add(myFragment);
		
		//设置
		SettingFragment settingFragment = new SettingFragment();
		mainFragments.add(settingFragment);
		
		//登陆
		LoginFragment loginFragment = new LoginFragment();
		mainFragments.add(loginFragment);
		return mainFragments;
	}
}

package com.jky.lavipeditum.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.jky.lavipeditum.LavipeditumApplication;
import com.jky.lavipeditum.R;
import com.jky.lavipeditum.activity.LoginActivity;
import com.jky.lavipeditum.adapter.MainFrameAdapter;
import com.jky.lavipeditum.base.BaseFragment;
import com.jky.lavipeditum.custom_view.CustomViewPager;
import com.jky.lavipeditum.custom_view.DirectionalViewPager;
import com.jky.lavipeditum.fragment.first.HereaboutFragment;
import com.jky.lavipeditum.fragment.first.HomeFragment;
import com.jky.lavipeditum.fragment.first.MyFragment;
import com.jky.lavipeditum.fragment.first.SettingFragment;
import com.jky.lavipeditum.util.Constants;
import com.jky.lavipeditum.util.Logger;

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
	private DirectionalViewPager cvp_main_group;
	private RadioGroup rg_mainfragment_group;
	public static View main_darkview; //弹出popupwindow时背景变暗的视图
	private HomeFragment homeFragment;
	private boolean seller;
	private MyFragment myFragment;

	@Override
	public View initView(LayoutInflater inflater, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.main_frame, null);
		
		cvp_main_group = (DirectionalViewPager) view.findViewById(R.id.cvp_main_group);
		rg_mainfragment_group = (RadioGroup) view.findViewById(R.id.rg_mainfragment_group);
		main_darkview = view.findViewById(R.id.main_darkview); 
		
		return view;
	}

	@Override
	public void initData(Bundle savedInstanceState) {
		List<Fragment> mainFragments = initMainFrameData();
		MainFrameAdapter adapter = new MainFrameAdapter(getChildFragmentManager(), mainFragments);
		cvp_main_group.setAdapter(adapter);
		//默认显示第主页
		cvp_main_group.setCurrentItem(Constants.HOME_PAGER);
		cvp_main_group.setOffscreenPageLimit(1);
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
					cvp_main_group.setCurrentItem(Constants.HOME_PAGER, false);
					break;
				//附近
				case R.id.rb_hereabout:
					cvp_main_group.setCurrentItem(Constants.HEREABOUT_PAGER, false);
					break;
				//我的
				case R.id.rb_my:
					//判断是否用户登陆或者商家登陆， 如果没有登陆应该先进入登陆界面
					if (LavipeditumApplication.isLogin || LavipeditumApplication.isSellerLogin) {
						cvp_main_group.setCurrentItem(Constants.MY_PAGER, false);
					}else{
						Intent intent = new Intent(context, LoginActivity.class);
						startActivityForResult(intent, Constants.GO_LOGIN_REQUESTCODE);
					} 
					break;
				//设置
				case R.id.rb_setting:
					cvp_main_group.setCurrentItem(Constants.SETTING_PAGER, false);
					break;
					
				}
			}
		});
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		//如果是普通用户登陆成功
		if (resultCode == Constants.LOGIN_SUCCESS && LavipeditumApplication.isLogin) {
			cvp_main_group.setCurrentItem(Constants.MY_PAGER, false);
		}
		
		//判断是否是的登陆界面返回
		else if (resultCode == Constants.LOGIN_BACK_RESULTCOCE) {
			//先判断一下当前viewpager当前显示的是那个下标
			int currentItem = cvp_main_group.getCurrentItem();
			RadioButton changeButton = (RadioButton) rg_mainfragment_group.getChildAt(currentItem);
			changeButton.setChecked(true);
			rg_mainfragment_group.invalidate();
		}
		
		//判断是否是定位城市返回的值
		else if (resultCode == Constants.LOCATION_CITY_RESULTCOCE) {
			homeFragment.getResultData(data);
		} 
		
		//判断是否是商家登陆成功的返回值
		else if (resultCode == Constants.SELLER_LOGIN_SUCCESS  && LavipeditumApplication.isSellerLogin) {
			seller = data.getBooleanExtra(Constants.SELLER, true);
			Bundle bundle = new Bundle();
			bundle.putBoolean(Constants.COOPERATION, false);
			myFragment.setArguments(bundle);
			cvp_main_group.setCurrentItem(Constants.MY_PAGER, false);
		}
	}

	/**
	 * 
	 * @Title: initMainFrameData 
	 * @Description: 初始化Main框架的界面
	 */
	private List<Fragment> initMainFrameData() {
		List<Fragment> mainFragments = new ArrayList<Fragment>();

		// 设置
		SettingFragment settingFragment = new SettingFragment();
		mainFragments.add(settingFragment);

		// 我的
		myFragment = new MyFragment();
		mainFragments.add(myFragment);

		// 附近
		HereaboutFragment hereaboutFragment = new HereaboutFragment();
		mainFragments.add(hereaboutFragment);

		// 主页
		homeFragment = new HomeFragment();
		mainFragments.add(homeFragment);
		
		return mainFragments;
	}
}

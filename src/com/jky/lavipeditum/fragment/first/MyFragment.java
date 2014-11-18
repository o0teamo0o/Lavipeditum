package com.jky.lavipeditum.fragment.first;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;

import com.jky.lavipeditum.LavipeditumApplication;
import com.jky.lavipeditum.R;
import com.jky.lavipeditum.adapter.SellerRegisterAdapter;
import com.jky.lavipeditum.base.BaseFragment;
import com.jky.lavipeditum.custom_view.DirectionalViewPager;
import com.jky.lavipeditum.fragment.second.SellerRegisterFirstFragment;
import com.jky.lavipeditum.fragment.second.SellerRegisterSecondFragment;
import com.jky.lavipeditum.fragment.second.SellerRegisterThreeFragment;
import com.jky.lavipeditum.util.Constants;

/**
 * 
 * @ClassName: MyFragment 
 * @Description: 我的界面
 *
 * @author o0teamo0o
 * @date 2014年10月24日 下午3:58:17 
 *
 */
public class MyFragment extends BaseFragment {
	
	private View view;
	private DirectionalViewPager dvp_register_pager;
	private boolean cooperation;

	@Override
	protected View initView(LayoutInflater inflater, Bundle savedInstanceState) {
		if (LavipeditumApplication.isLogin) {
			view = inflater.inflate(R.layout.my_client_fragment, null);
			
		}else if (LavipeditumApplication.isSellerLogin) {
			view = inflater.inflate(R.layout.my_seller_fragment, null);
			
			View ly_register = view.findViewById(R.id.ly_register);
			View ly_seller = view.findViewById(R.id.ly_seller);
			dvp_register_pager = (DirectionalViewPager) view.findViewById(R.id.dvp_register_pager);
			
			try {
				cooperation = getArguments().getBoolean(Constants.COOPERATION);
			} catch (Exception e) {
			}
			
			if (cooperation) {
				ly_register.setVisibility(View.GONE);
				ly_seller.setVisibility(View.VISIBLE);
			}else{
				ly_register.setVisibility(View.VISIBLE);
				ly_seller.setVisibility(View.GONE);
			}
			
			initRegisterData();
		}
		return view;
	}

	@Override
	protected void initData(Bundle savedInstanceState) {

	}
	
	/**
	 * 
	 * Title: initRegisterData
	 * Description: 初始化注册商家信息
	 */
	private void initRegisterData() {
		List<Fragment> registerPagers = initRegisterPager();
		
		SellerRegisterAdapter adapter = new SellerRegisterAdapter(getChildFragmentManager(), registerPagers);
		dvp_register_pager.setAdapter(adapter);
	}

	@Override
	protected void initListener() {

	}
	
	/**
	 * 
	 * Title: initRegisterPager
	 * Description: 初始化注册商家的页面信息
	 */
	private List<Fragment> initRegisterPager() {
		List<Fragment> fragments = new ArrayList<Fragment>();
		SellerRegisterFirstFragment firstFragment = new SellerRegisterFirstFragment();
		fragments.add(firstFragment);
		
		SellerRegisterSecondFragment secondFragment = new SellerRegisterSecondFragment();
		fragments.add(secondFragment);
		
		SellerRegisterThreeFragment threeFragment = new SellerRegisterThreeFragment();
		fragments.add(threeFragment);
		
		return fragments;
	}

}

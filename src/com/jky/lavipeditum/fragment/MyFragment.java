package com.jky.lavipeditum.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.jky.lavipeditum.LavipeditumApplication;
import com.jky.lavipeditum.R;
import com.jky.lavipeditum.base.BaseFragment;

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

	@Override
	protected View initView(LayoutInflater inflater) {
		if (LavipeditumApplication.isLogin) {
			view = inflater.inflate(R.layout.my_client_fragment, null);
		}else if (LavipeditumApplication.isSellerLogin) {
			
		}else{
			view = inflater.inflate(R.layout.my_client_fragment, null);
		}
		return view;
	}

	@Override
	protected void initData(Bundle savedInstanceState) {

	}

	@Override
	protected void initListener() {

	}

}

package com.jky.lavipeditum.fragment.second;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.jky.lavipeditum.R;
import com.jky.lavipeditum.base.BaseFragment;

/**
 * 
 * @ClassName: SellerRegisterThreeFragment 
 * @Description: 商家注册第三个界面

 * @author o0teamo0o
 * @date 2014年11月18日 下午3:12:29 
 *
 */
public class SellerRegisterThreeFragment extends BaseFragment {

	private View view;

	@Override
	protected View initView(LayoutInflater inflater, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.seller_register_three_pager, null);
		return view;
	}

	@Override
	protected void initData(Bundle savedInstanceState) {
		
	}

	@Override
	protected void initListener() {
		
	}

}

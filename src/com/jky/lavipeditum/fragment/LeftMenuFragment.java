package com.jky.lavipeditum.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.jky.lavipeditum.base.BaseFragment;

/**
 * 
 * @ClassName: LeftMenuFragment 
 * @Description: 左边slidingmenu菜单栏显示
 *
 * @author o0teamo0o
 * @date 2014年10月21日 下午10:53:20 
 *
 */
public class LeftMenuFragment extends BaseFragment {

	@Override
	public View initView(LayoutInflater inflater, Bundle savedInstanceState) {
		TextView tv = new TextView(getActivity());
		tv.setText(LeftMenuFragment.class.getSimpleName());
		return tv;
	}

	@Override
	public void initData(Bundle savedInstanceState) {

	}

	@Override
	protected void initListener() {
		
	}

}

package com.jky.lavipeditum.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.jky.lavipeditum.LavipeditumApplication;
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

	@Override
	protected View initView(LayoutInflater inflater) {
		TextView tv = new TextView(getActivity());
		tv.setText(MyFragment.class.getSimpleName());
		return tv;
	}

	@Override
	protected void initData(Bundle savedInstanceState) {

	}

	@Override
	protected void initListener() {

	}

}

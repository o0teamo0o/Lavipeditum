package com.jky.lavipeditum.fragment.first;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.jky.lavipeditum.base.BaseFragment;

/**
 * 
 * @ClassName: SettingFragment 
 * @Description: 设置界面
 *
 * @author o0teamo0o
 * @date 2014年10月24日 下午3:59:29 
 *
 */
public class SettingFragment extends BaseFragment {

	@Override
	protected View initView(LayoutInflater inflater, Bundle savedInstanceState) {
		TextView tv = new TextView(getActivity());
		tv.setText(SettingFragment.class.getSimpleName());
		return tv;
	}

	@Override
	protected void initData(Bundle savedInstanceState) {

	}

	@Override
	protected void initListener() {

	}

}

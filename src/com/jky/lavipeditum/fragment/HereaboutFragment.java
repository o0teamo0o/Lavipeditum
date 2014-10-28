package com.jky.lavipeditum.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.jky.lavipeditum.base.BaseFragment;

/**
 * 
 * @ClassName: HereaboutFragment 
 * @Description: 附近搜索的界面
 *
 * @author o0teamo0o
 * @date 2014年10月24日 下午3:57:23 
 *
 */
public class HereaboutFragment extends BaseFragment {

	@Override
	protected View initView(LayoutInflater inflater) {
		TextView tv = new TextView(getActivity());
		tv.setText(HereaboutFragment.class.getSimpleName());
		return tv;
	}

	@Override
	protected void initData(Bundle savedInstanceState) {

	}

	@Override
	protected void initListener() {

	}

}

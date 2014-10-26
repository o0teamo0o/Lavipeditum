package com.jky.lavipeditum.base;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * 
 * @ClassName: BasePagerAdapter
 * @Description: 最基础的PagerAdapter
 *
 * @author o0teamo0o
 * @date 2014年10月26日 下午5:38:39
 */
public class BasePagerAdapter extends PagerAdapter {

	private List<View> views;
	
	public BasePagerAdapter(List<View> views) {
		this.views = views;
	}
	
	@Override
	public int getCount() {
		return views.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object obj) {
		return view == obj;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(views.get(position));
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		container.addView(views.get(position));
		return views.get(position);
	}
}

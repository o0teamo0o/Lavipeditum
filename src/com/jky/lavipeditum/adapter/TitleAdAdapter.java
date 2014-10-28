package com.jky.lavipeditum.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * 
 * @ClassName: TitleAdAdapter 
 * @Description: 首页头部广告的适配器
 *
 * @author o0teamo0o
 * @date 2014年10月24日 上午12:07:20 
 *
 */
public class TitleAdAdapter extends PagerAdapter {

	private List<View> ads;
	
	public TitleAdAdapter(List<View> ads) {
		this.ads = ads;
	}

	@Override
	public int getCount() {
		return ads.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object obj) {
		return view == obj;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(ads.get(position));
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		container.addView(ads.get(position));
		return ads.get(position);
	}
	
	

}

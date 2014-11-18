package com.jky.lavipeditum.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * 
 * @ClassName: SellerRegisterAdapter 
 * @Description: 商家注册界面的适配器

 * @author o0teamo0o
 * @date 2014年11月18日 下午3:15:39 
 *
 */
public class SellerRegisterAdapter extends FragmentStatePagerAdapter {
	
	private List<Fragment> registerPagers;

	public SellerRegisterAdapter(FragmentManager fm, List<Fragment> registerPagers) {
		super(fm);
		this.registerPagers = registerPagers;
	}

	@Override
	public Fragment getItem(int position) {
		return registerPagers.get(position);
	}

	@Override
	public int getCount() {
		return registerPagers.size();
	}

}

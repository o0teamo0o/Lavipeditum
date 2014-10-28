package com.jky.lavipeditum.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class MainFrameAdapter extends FragmentStatePagerAdapter {
	
	private List<Fragment> mainFragments;

	public MainFrameAdapter(FragmentManager fm, List<Fragment> mainFragments) {
		super(fm);
		this.mainFragments = mainFragments; 
	}

	@Override
	public Fragment getItem(int position) {
		return mainFragments.get(position);
	}

	@Override
	public int getCount() {
		return mainFragments.size();
	}

}

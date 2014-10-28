package com.jky.lavipeditum.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.jky.lavipeditum.R;
import com.jky.lavipeditum.adapter.TitleAdAdapter;
import com.jky.lavipeditum.base.BaseFragment;
import com.jky.lavipeditum.lib.auto_scroll_view_pager.AutoScrollViewPager;
import com.jky.lavipeditum.lib.viewpager_Indicator.CirclePageIndicator;
import com.jky.lavipeditum.util.Logger;

/**
 * 
 * @ClassName: HomeFragment 
 * @Description: 主界面的显示 软件默认显示的第一个界面
 *
 * @author o0teamo0o
 * @date 2014年10月24日 下午3:55:54 
 *
 */
public class HomeFragment extends BaseFragment implements OnClickListener {

	private View view;
	private ImageView lv_left_menu, iv_saoyisao;
	private AutoScrollViewPager auto_viewPager;
	private CirclePageIndicator indicator;

	@Override
	public View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.home_fragment, null);
		
		lv_left_menu = (ImageView) view.findViewById(R.id.lv_left_menu);
		iv_saoyisao = (ImageView) view.findViewById(R.id.iv_saoyisao);
		auto_viewPager = (AutoScrollViewPager) view.findViewById(R.id.auto_viewPager);
		indicator = (CirclePageIndicator) view.findViewById(R.id.indicator);
		return view;
	}

	@Override
	public void initData(Bundle savedInstanceState) {
		initAD();
	}
	
	@Override
	protected void initListener() {
		lv_left_menu.setOnClickListener(this);
		iv_saoyisao.setOnClickListener(this);
	}

	/**
	 * 
	 * @Title: initAD 
	 * @Description: 初始化广告头信息
	 */
	private void initAD() {
		List<View> ads = new ArrayList<View>();
		ImageView ad1 = new ImageView(context);
		ad1.setImageResource(R.drawable.title_ad1);
		ads.add(ad1);
		
		ImageView ad2 = new ImageView(context);
		ad2.setImageResource(R.drawable.title_ad2);
		ads.add(ad2);
		
		ImageView ad3 = new ImageView(context);
		ad3.setImageResource(R.drawable.title_ad3);
		ads.add(ad3);
		
		TitleAdAdapter adapter = new TitleAdAdapter(ads);
		auto_viewPager.setAdapter(adapter);
		
		auto_viewPager.setInterval(4000); //设置切换时间间隔
		auto_viewPager.setScrollDurationFactor(3); //设置滑动时的延时时间
		auto_viewPager.setSlideBorderMode(AutoScrollViewPager.SLIDE_BORDER_MODE_CYCLE); //设置滑动类型
		auto_viewPager.startAutoScroll(); //开始滑动
		
		indicator.setViewPager(auto_viewPager);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		//左边菜单
		case R.id.lv_left_menu:
			//自动判断打开还是关闭左边菜单
			slidingmenu.toggle();
			break;
		//扫一扫
		case R.id.iv_saoyisao:
			
			break;
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		//在界面看不到的情况下停止ad广告栏停止滑动
		auto_viewPager.stopAutoScroll();
	}

}

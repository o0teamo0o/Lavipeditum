package com.jky.lavipeditum.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.os.Handler; 
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jky.lavipeditum.R;
import com.jky.lavipeditum.activity.LocationCityActivity;
import com.jky.lavipeditum.adapter.RegionAdapter;
import com.jky.lavipeditum.adapter.TitleAdAdapter;
import com.jky.lavipeditum.base.BaseFragment;
import com.jky.lavipeditum.bean.CitySortModel;
import com.jky.lavipeditum.bean.Region;
import com.jky.lavipeditum.engine.RegionInfoService;
import com.jky.lavipeditum.lib.auto_scroll_view_pager.AutoScrollViewPager;
import com.jky.lavipeditum.lib.viewpager_Indicator.CirclePageIndicator;
import com.jky.lavipeditum.util.Constants;
import com.jky.lavipeditum.util.Logger;
import com.jky.lavipeditum.util.ScreenUtils;

/**
 * 
 * @ClassName: HomeFragment 
 * @Description: 主界面的显示 软件默认显示的第一个界面
 *
 * @author o0teamo0o
 * @date 2014年10月24日 下午3:55:54 
 *
 */
public class HomeFragment extends BaseFragment implements OnClickListener, OnCheckedChangeListener {

	protected static final int SHOW_REGION = 0;
	private View view;
	private ImageView lv_left_menu, iv_saoyisao;
	private AutoScrollViewPager auto_viewPager;
	private CirclePageIndicator indicator;
	private CheckBox cb_city;
	private PopupWindow cityPopupWindow;
	private Animation animIn, animOut, lodingAnimation; //弹出popupwindow,背景变暗的动画
	private ImageView iv_loding;
	private ArrayList<Region> regions; //城市所对应的区域集合
	private GridView gv_region;
	private TextView tv_city_change;

	@Override
	public View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.home_fragment, null);
		
		lv_left_menu = (ImageView) view.findViewById(R.id.lv_left_menu);
		iv_saoyisao = (ImageView) view.findViewById(R.id.iv_saoyisao);
		auto_viewPager = (AutoScrollViewPager) view.findViewById(R.id.auto_viewPager);
		indicator = (CirclePageIndicator) view.findViewById(R.id.indicator);
		cb_city = (CheckBox) view.findViewById(R.id.cb_city); 
		
		return view;
	}

	@Override
	public void initData(Bundle savedInstanceState) {
		//加载popupwindow 进入和退出时的动画
		animIn = AnimationUtils.loadAnimation(getActivity(), R.anim.popupwindow_show_in_anim);
		animOut = AnimationUtils.loadAnimation(getActivity(), R.anim.popupwindow_show_out_anim);
		lodingAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.common_loading_rotate);
		
		initAD();
		initCityPopup();
	}
	
	@Override
	protected void initListener() {
		lv_left_menu.setOnClickListener(this);
		iv_saoyisao.setOnClickListener(this);
		cb_city.setOnCheckedChangeListener(this);
		tv_city_change.setOnClickListener(this);
	}
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			//显示当前城市的区域信息
			case SHOW_REGION:
				//隐藏 等待图标
				iv_loding.clearAnimation();
				iv_loding.setVisibility(View.GONE);
				
				//判断集合是否为空
				if (regions != null) {
					Logger.d(HomeFragment.class, "集合大小:"+regions.size());
					//设置数据
					RegionAdapter adapter = new RegionAdapter(getActivity(), regions);
					gv_region.setAdapter(adapter);
					gv_region.setVisibility(View.VISIBLE);
				}
				
				break;

			default:
				break;
			}
		};
	};

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
		//切换城市
		case R.id.tv_city_change:
			Intent intent = new Intent(getActivity(), LocationCityActivity.class);
			getParentFragment().startActivityForResult(intent, Constants.GO_LOCATION_CITY_REQUESTCODE);
			break;
		}
	}
	
	/**
	 * 
	 * Title: getResultData
	 * Description: 提供给父fragment来访问 
	 * 用意就是想子fragment接收不到onActivityResult时，让父fragment传递过来参数
	 * @param data
	 */
	public void getResultData(Intent data){
		CitySortModel city = (CitySortModel) data.getSerializableExtra("city");
		//保存数据到共享首选项
		preferences.setCityRegionCode(city.getCode());
		//修改城市标题文字
		cb_city.setText(city.getName());
		//把popupwindow关闭
		cityPopupWindow.dismiss();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		//在界面看不到的情况下停止ad广告栏停止滑动
		auto_viewPager.stopAutoScroll();
	}
	
	/**
	 * 
	 * Title: initCityPopup
	 * Description:初始化城市切换界面的popupwindow
	 */
	private void initCityPopup() {
		cityPopupWindow = new PopupWindow(context);
		View view = View.inflate(context, R.layout.home_fragment_ctiy, null);
		iv_loding = (ImageView) view.findViewById(R.id.iv_loding);
		gv_region = (GridView) view.findViewById(R.id.gv_region);
		tv_city_change = (TextView) view.findViewById(R.id.tv_city_change);
		
		cityPopupWindow.setContentView(view);
		cityPopupWindow.setBackgroundDrawable(new PaintDrawable());
		cityPopupWindow.setFocusable(true);
		
		//设置高度为包裹内容
		cityPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
		//设置宽度为手机宽度
		cityPopupWindow.setWidth(ScreenUtils.getScreenWidth(getActivity()));
		
		//设置关闭监听事件
		cityPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
			
			@Override
			public void onDismiss() {
				MainFragment.main_darkview.startAnimation(animOut);
				MainFragment.main_darkview.setVisibility(View.GONE);
				cb_city.setChecked(false);
				
				//停止动画
				iv_loding.clearAnimation();
			}
		});
	}

	/**
	 * 左上角城市切换的选择事件
	 */
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		//点击的城市切换按钮
		if (isChecked) {
			if (cityPopupWindow.isShowing()) {
				cityPopupWindow.dismiss();
			}else{
				cityPopupWindow.showAsDropDown(cb_city);
				cityPopupWindow.setAnimationStyle(-1);
				//背景变暗
				MainFragment.main_darkview.startAnimation(animIn);
				MainFragment.main_darkview.setVisibility(View.VISIBLE);
				
				//开始动画
				iv_loding.startAnimation(lodingAnimation);
				
				//开启一个子线程去查询当前城市的区
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						//查询数据库中当前城市的分区
						String code = preferences.getCityRegionCode();
						RegionInfoService reginInfoService = new RegionInfoService(getActivity());
						regions = reginInfoService.getRegions(code);
						handler.sendEmptyMessage(SHOW_REGION);
					}
				}).start();
			}
		}
	}
}

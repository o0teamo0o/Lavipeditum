package com.jky.lavipeditum.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.Window;

import com.baidu.mapapi.SDKInitializer;
import com.jky.lavipeditum.R;
import com.jky.lavipeditum.fragment.LeftMenuFragment;
import com.jky.lavipeditum.fragment.MainFragment;
import com.jky.lavipeditum.lib.slidingmenu.SlidingFragmentActivity;
import com.jky.lavipeditum.lib.slidingmenu.SlidingMenu;
import com.jky.lavipeditum.lib.slidingmenu.SlidingMenu.CanvasTransformer;
import com.jky.lavipeditum.util.Constants;
import com.jky.lavipeditum.util.LavipeditumPreferences;
import com.jky.lavipeditum.util.ToastUtil;

/**
 * 
 * @ClassName: MainActivity 
 * @Description: 主框架的主界面
 * 
 * @author o0teamo0o
 * @date 2014年11月19日 下午9:25:12 
 *
 */
public class MainActivity extends SlidingFragmentActivity {

	public SlidingMenu slidingMenu;
	public LavipeditumPreferences preferences; //全局共享首选项
	private BaiduMapSDKReceiver mapSDKReceiver; //百度地图的广播
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//得到全局共享首选项
		preferences = new LavipeditumPreferences(this);
		
		setContentView(R.layout.main);
		initView();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		
		//注册百度地图SDK广播监听者
		IntentFilter iFilter = new IntentFilter();
		iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
		iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
		mapSDKReceiver = new BaiduMapSDKReceiver();
		this.registerReceiver(mapSDKReceiver, iFilter);
	}

	/**
	 * 
	 * @Title: initView 
	 * @Description: 添加视图
	 */
	private void initView() {
		//设置slidingmenu的参数
		slidingMenu = getSlidingMenu(); //得到实例对象
 		slidingMenu.setMode(SlidingMenu.LEFT); //设置滑动模式
 		slidingMenu.setShadowWidthRes(R.dimen.shadow_width); //设置阴影图片的宽度
 		slidingMenu.setShadowDrawable(R.drawable.shadow); //设置阴影的图片资源
 		slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset); //SlidingMenuh滑出时主页面显示的剩余宽度
 		slidingMenu.setFadeDegree(0.35f); //设置淡入淡出的比例
 		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);  //设置滑动的区域
 		slidingMenu.setBehindCanvasTransformer(mtTransformer); //设置左边菜单动画
 		slidingMenu.setAnimationCacheEnabled(true); //开启动画
		
		//第一次上来默认显示主fragment
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, new MainFragment(), Constants.MAIN_FRAGMENT_NAME)
		.commit();
		
		//设置左边菜单
		setBehindContentView(R.layout.left_menu_frame);
		//替换左边菜单显示对fragment 
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.left_menu_frame, new LeftMenuFragment(), Constants.LEFT_FRAGMENT_NAME)
		.commit();
	}
	
	/**
	 * slidingmenu菜单划出来时显示的动画实现
	 */
	CanvasTransformer mtTransformer = new CanvasTransformer() {
		
		@Override
		public void transformCanvas(Canvas canvas, float percentOpen) {
			float scale = (float) (percentOpen*0.25 + 0.75);
			canvas.scale(scale, scale, canvas.getWidth()/2, canvas.getHeight()/2);
		}
	};
	
	/**
	 * 
	 * @ClassName: BaiduMapSDKReceiver 
	 * @Description: 百度地图的广播接收者
	 *
	 * @author o0teamo0o
	 * @date 2014年11月19日 下午9:15:27 
	 *
	 */
	private class BaiduMapSDKReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			//key 验证失败广播 
			if (action.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
				ToastUtil.showMessage(MainActivity.this, "key 验证出错,百度地图无法正常使用！");
			}
			//网络错误广播
			else if (action.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
				ToastUtil.showMessage(MainActivity.this, "当前无网络连接,部分功能无法使用!");
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		//取消监听SDK广播
		unregisterReceiver(mapSDKReceiver);
	}
}

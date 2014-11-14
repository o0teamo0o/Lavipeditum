package com.jky.lavipeditum.activity;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.Window;

import com.jky.lavipeditum.R;
import com.jky.lavipeditum.fragment.LeftMenuFragment;
import com.jky.lavipeditum.fragment.MainFragment;
import com.jky.lavipeditum.lib.slidingmenu.SlidingFragmentActivity;
import com.jky.lavipeditum.lib.slidingmenu.SlidingMenu;
import com.jky.lavipeditum.lib.slidingmenu.SlidingMenu.CanvasTransformer;
import com.jky.lavipeditum.util.Constants;
import com.jky.lavipeditum.util.LavipeditumPreferences;

public class MainActivity extends SlidingFragmentActivity {

	public SlidingMenu slidingMenu;
	public LavipeditumPreferences preferences; //全局共享首选项

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

}

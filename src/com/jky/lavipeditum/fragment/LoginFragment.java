package com.jky.lavipeditum.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Animation.AnimationListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jky.lavipeditum.R;
import com.jky.lavipeditum.base.BaseFragment;
import com.jky.lavipeditum.custom.Rotate3dAnimation;

/**
 * 
* @ClassName: LoginFragment
* @Description: 用于登陆和注册的界面

* @author o0teamo0o
* @date 2014年10月26日 下午9:15:50
*
 */
public class LoginFragment extends BaseFragment implements OnClickListener {

	private View view;
	private TextView tv_go_register;
	public boolean showRegister = false; //判断当前界面是注册界面还是登陆界面
	private ViewGroup container; //FrameLayout容器,用来3D翻转需要的使用的数据
	private LinearLayout ll_register, ll_login;
	private TextView tv_go_login;

	@Override
	protected View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.login_frame, null);
		
		container = (ViewGroup) view.findViewById(R.id.container);
		tv_go_register = (TextView) view.findViewById(R.id.tv_go_register);
		ll_register = (LinearLayout) view.findViewById(R.id.ll_register);
		ll_login = (LinearLayout) view.findViewById(R.id.ll_login);
		tv_go_login = (TextView) view.findViewById(R.id.tv_go_login);
		return view;
	}

	@Override
	protected void initData(Bundle savedInstanceState) {

	}

	@Override
	protected void initListener() {
		tv_go_register.setOnClickListener(this);
		tv_go_login.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		//去注册
		case R.id.tv_go_register:
			if (!showRegister) {
				applyRotation(0, 90);
				showRegister = true;
			}
			break;
		//去登陆
		case R.id.tv_go_login:
			if (showRegister) {
				applyRotation(0, 90);
				showRegister = false;
			}
			break;
		}
	}

	/**
	 * 
	 * @Title: applyRotation 
	 * @Description: 3D翻转实现
	 * @param start
	 * @param end
	 */
	private void applyRotation(int start, int end) { 
		final float centerX = container.getWidth() / 2.0f;
		final float centerY = container.getHeight() / 2.0f;
		
		Rotate3dAnimation rotation = new Rotate3dAnimation(start, end, centerX, centerY, 200.0f, true);
		rotation.setDuration(500);
		rotation.setInterpolator(new AccelerateInterpolator());
		rotation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				
				container.post(new Runnable() {

					@Override 
					public void run() {
						if (!showRegister) {
							ll_register.setVisibility(View.GONE);
							ll_login.setVisibility(View.VISIBLE);
						}else{
							ll_register.setVisibility(View.VISIBLE);
							ll_login.setVisibility(View.GONE);
						}
						
						Rotate3dAnimation rotate3dAnimation = new Rotate3dAnimation(-90, 0, centerX, centerY, 200.0f, false);
						rotate3dAnimation.setDuration(200);
						rotate3dAnimation.setInterpolator(new DecelerateInterpolator());
						container.startAnimation(rotate3dAnimation);
					}
				});
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
			
		});
		container.startAnimation(rotation);
	}

}

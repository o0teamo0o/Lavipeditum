package com.jky.lavipeditum.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.RequestLine;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jky.lavipeditum.R;
import com.jky.lavipeditum.base.BaseActivity;
import com.jky.lavipeditum.base.BasePagerAdapter;
import com.jky.lavipeditum.custom.Rotate3dAnimation;
import com.jky.lavipeditum.custom_view.CustomViewPager;
import com.jky.lavipeditum.lib.weibo.openapi.models.User;
import com.jky.lavipeditum.util.AccessTokenKeeper;
import com.jky.lavipeditum.util.Constants;
import com.jky.lavipeditum.util.Logger;
import com.jky.lavipeditum.util.ToastUtil;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.AsyncWeiboRunner;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.net.WeiboParameters;
import com.sina.weibo.sdk.utils.LogUtil;

/**
 * 
 * @ClassName: LoginActivity
 * @Description: 登陆的界面
 *
 * @author o0teamo0o
 * @date 2014年10月28日 上午11:22:30
 */
public class LoginActivity extends BaseActivity implements OnClickListener {

	private View view;
	private TextView tv_go_register;
	public boolean showRegister = false; // 判断当前界面是注册界面还是登陆界面
	private ViewGroup container; // FrameLayout容器,用来3D翻转需要的使用的数据
	private LinearLayout ll_register, ll_login;
	private TextView tv_go_login;
	private CustomViewPager cvp_register;
	private TextView tv_login_weibo;
	private WeiboAuth weiboAuth; //微博 Web 授权类，提供登陆等功能
	private SsoHandler ssoHandler; //注意：SsoHandler 仅当 SDK 支持 SSO 时有效
	private Oauth2AccessToken accessToken; //装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能
	
    
	
	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.login_frame);

		container = (ViewGroup) this.findViewById(R.id.container);
		tv_go_register = (TextView) this.findViewById(R.id.tv_go_register);
		ll_register = (LinearLayout) this.findViewById(R.id.ll_register);
		ll_login = (LinearLayout) this.findViewById(R.id.ll_login);
		tv_go_login = (TextView) this.findViewById(R.id.tv_go_login);
		tv_login_weibo = (TextView) this.findViewById(R.id.tv_login_weibo);
		
		cvp_register = (CustomViewPager) this.findViewById(R.id.cvp_register);
	}

	@Override
	protected void initData() {
		List<View> pagers = initRegisterPager();
		
		BasePagerAdapter adapter = new BasePagerAdapter(pagers);
		cvp_register.setAdapter(adapter);
	}
	
	@Override
	protected void initListener() {
		tv_go_register.setOnClickListener(this);
		tv_go_login.setOnClickListener(this);
		tv_login_weibo.setOnClickListener(this);
	}
	
	/**
	 * 
	 * @Title: initRegisterPager
	 * @Description: 初始化注册页面
	 * @param 
	 * @return void
	 * @throws
	 */
	private List<View> initRegisterPager() {
		List<View> pagers = new ArrayList<View>();
		View inputPager = View.inflate(context, R.layout.register_input_pager, null);
		pagers.add(inputPager);
		
		View verificationPager = View.inflate(context, R.layout.register_verification_pager, null);
		pagers.add(verificationPager);
		return pagers;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 去注册
		case R.id.tv_go_register:
			if (!showRegister) {
				applyRotation(0, -90);
				showRegister = true;
			}
			break;
		// 去登陆
		case R.id.tv_go_login:
			if (showRegister) {
				applyRotation(0, 90);
				showRegister = false;
			}
			break;
		//微博登陆
		case R.id.tv_login_weibo:
			//创建微博授权授权认证信息对象
			weiboAuth = new WeiboAuth(LoginActivity.this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
			
			/**
			 * SSO授权
			 * 有客户端时使用 SSO 授权登陆;无客户端时自动唤起 Web 授权
			 * 创建 SsoHandler 对象
			 */
			ssoHandler = new SsoHandler(LoginActivity.this, weiboAuth);
			
			//调用 SsoHandler#authorize 方法
			ssoHandler.authorize(new AuthListener());
			break;
		}
	}
	
	/**
	 * 
	 * @ClassName: AuthListener
	 * @Description: 微博登入按钮的监听器，接收授权结果。微博认证授权回调类。
	 *   1. SSO 授权时，需要在 {@link #onActivityResult} 中调用 {@link SsoHandler#authorizeCallBack} 后，
     *      该回调才会被执行。
     *    
     *   2. 非 SSO 授权时，当授权结束后，该回调就会被执行。
     *      当授权成功后，请保存该 access_token、expires_in、uid 等信息到 SharedPreferences 中。
	 *
	 * @author o0teamo0o
	 * @date 2014年10月27日 上午10:08:31
	 */
	private class AuthListener implements WeiboAuthListener{

		/**
		 * 登陆取消时的回调
		 */
		@Override
		public void onCancel() {
			ToastUtil.showMessage(context, "取消微博登陆");
		}

		/**
		 * 登陆成功时的回调
		 */
		@Override
		public void onComplete(Bundle values) {
			//// 从 Bundle 中解析 Token
			accessToken = Oauth2AccessToken.parseAccessToken(values);
			if (accessToken != null && accessToken.isSessionValid()) {
				String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date(accessToken.getExpiresTime()));
				String format = getString(R.string.weibosdk_token_to_string_format_1);
				ToastUtil.showMessage(context, String.format(format, accessToken.getToken(), date));
				
				// 保存 Token 到 SharedPreferences
				tokenKeeper.setAccessToken(accessToken);
				ToastUtil.showMessage(context, "授权成功");
				
				//得到用户信息
				getUserInfo(accessToken.getUid());
			}else{
				// 以下几种情况，您会收到 Code：
                // 1. 当您未在平台上注册的应用程序的包名与签名时；
                // 2. 当您注册的应用程序包名与签名不正确时；
                // 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
				String code = values.getString("code");
				String message = "授权失败!";
				if (!TextUtils.isEmpty(code)) {
					message = message + "\nObtained the code: " + code;
				}
				ToastUtil.showMessage(context, message);
			}
		}
		
		/**
		 * 登陆异常时回调
		 */
		@Override
		public void onWeiboException(WeiboException e) {
			//直接打印出错误结果
			ToastUtil.showMessage(context, e.getMessage());
		}
		
	}
	
	/**
	 * 当SSO授权Activity 退出时, 该方法被调用
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//SSO 授权回调
		//重要:发起SSO 登陆的Activity必须重写 onActivityResult
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}

	/**
	 * 
	 * @Title: getUserInfo
	 * @Description: 通过SSO得到的accessToken 查询用户信息
	 */
	public void getUserInfo(String uid) {
		WeiboParameters parameters = new WeiboParameters();
		parameters.put("uid", uid);
		requestAsync(Constants.SAPILIST.get(Constants.READ_USER), parameters, Constants.HTTPMETHOD_GET, requestListener);
	}
	
	/**
	 * 微博OpenAPI回调接口
	 */
	private RequestListener requestListener = new RequestListener() {
		
		/**
		 * 获取用户数据出错的回调
		 */
		@Override
		public void onWeiboException(WeiboException e) {
			ToastUtil.showMessage(context, e.getMessage());
		}
		
		/**
		 * 获取用数据成功的回调
		 */
		@Override
		public void onComplete(String response) {
			if (!TextUtils.isEmpty(response)) {
				Logger.i(LoginActivity.class, response);
				//调用User#parse 将 JSON串解析成User对象
				User user = User.parse(response);
				if (user != null) {
					ToastUtil.showMessage(context, "获取User信息成功，用户昵称：" + user.screen_name);
				}else{
					ToastUtil.showMessage(context, response);
				}
			}
		}
	};
	
	/**
	 * 
	 * @Title: requestAsync
	 * @Description: HTTP 异步请求
	 * @param url 请求地址
	 * @param params 请求参数
	 * @param httpMethod 请求方法
	 * @param listener 请求后的回调接口
	 */
	private void requestAsync(String url, WeiboParameters params,
			String httpMethod, RequestListener listener) {
		if (null == accessToken || TextUtils.isEmpty(url) || null == params
				|| TextUtils.isEmpty(httpMethod) || null == listener) {
			Logger.e(LoginActivity.class, "获取用户信息 requestAsync方法 参数错误!");
			return ;
		}
		
		params.put(Constants.KEY_ACCESS_TOKEN, accessToken.getToken());
		//异步请求数据 回调的数据会在监听里面
		AsyncWeiboRunner.requestAsync(url, params, httpMethod, listener);
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

		Rotate3dAnimation rotation = new Rotate3dAnimation(start, end, centerX,
				centerY, 200.0f, true);
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
						} else {
							ll_register.setVisibility(View.VISIBLE);
							ll_login.setVisibility(View.GONE);
						}

						Rotate3dAnimation rotate3dAnimation = new Rotate3dAnimation(
								-90, 0, centerX, centerY, 200.0f, false);
						rotate3dAnimation.setDuration(200);
						rotate3dAnimation
								.setInterpolator(new DecelerateInterpolator());
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
	
	/**
	 * 判断用户按键盘做的事情
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {   
			Intent intent = new Intent();
			setResult(Constants.LOGIN_BACK_RESULTCOCE, intent);
		}
		return super.onKeyDown(keyCode, event);
	}

}

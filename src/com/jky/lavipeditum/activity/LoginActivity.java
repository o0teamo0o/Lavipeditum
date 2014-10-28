package com.jky.lavipeditum.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.jky.lavipeditum.R;
import com.jky.lavipeditum.base.BaseActivity;
import com.jky.lavipeditum.base.BasePagerAdapter;
import com.jky.lavipeditum.base.BaseUserInfo;
import com.jky.lavipeditum.custom.Rotate3dAnimation;
import com.jky.lavipeditum.custom_view.ClearEditText;
import com.jky.lavipeditum.custom_view.CustomViewPager;
import com.jky.lavipeditum.lib.tencent.BaseUIListener;
import com.jky.lavipeditum.lib.weibo.openapi.models.User;
import com.jky.lavipeditum.util.Constants;
import com.jky.lavipeditum.util.ImageUtils;
import com.jky.lavipeditum.util.Logger;
import com.jky.lavipeditum.util.PhoneNumberUtil;
import com.jky.lavipeditum.util.ToastUtil;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.AsyncWeiboRunner;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.net.WeiboParameters;
import com.tencent.connect.UserInfo;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

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
	private TextView tv_login_weibo, tv_qq_login; //微博,QQ登陆按钮
	private WeiboAuth weiboAuth; //微博 Web 授权类，提供登陆等功能
	private SsoHandler ssoHandler; //注意：SsoHandler 仅当 SDK 支持 SSO 时有效
	private Oauth2AccessToken accessToken; //装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能
	private Tencent tencent; //Tencent类是SDK的主要实现类
	private ProgressBar pb_regihter_hint;
	private PopupWindow popupWindow;
	
    
	
	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.login_frame);

		container = (ViewGroup) this.findViewById(R.id.container);
		tv_go_register = (TextView) this.findViewById(R.id.tv_go_register);
		ll_register = (LinearLayout) this.findViewById(R.id.ll_register);
		ll_login = (LinearLayout) this.findViewById(R.id.ll_login);
		tv_go_login = (TextView) this.findViewById(R.id.tv_go_login);
		tv_login_weibo = (TextView) this.findViewById(R.id.tv_login_weibo);
		tv_qq_login = (TextView) this.findViewById(R.id.tv_qq_login);
		
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
		tv_qq_login.setOnClickListener(this);
		cvp_register.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (popupWindow != null && popupWindow.isShowing()) {
					popupWindow.dismiss();
				}
				return false;
			}
		});
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
		pb_regihter_hint = (ProgressBar) inputPager.findViewById(R.id.pb_regihter_hint);
		cet_regihter_phone = (ClearEditText) inputPager.findViewById(R.id.cet_regihter_phone);
		
		pb_regihter_hint.setOnClickListener(this);
		
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
			//创建微博授权认证信息对象
			weiboAuth = new WeiboAuth(LoginActivity.this, Constants.WEIBO_APP_KEY, Constants.WEIBO_REDIRECT_URL, Constants.WEIBO_SCOPE);
			
			/**
			 * SSO授权
			 * 有客户端时使用 SSO 授权登陆;无客户端时自动唤起 Web 授权
			 * 创建 SsoHandler 对象
			 */
			ssoHandler = new SsoHandler(LoginActivity.this, weiboAuth);
			
			//调用 SsoHandler#authorize 方法
			ssoHandler.authorize(new AuthListener());
			break;
		//腾讯登陆
		case R.id.tv_qq_login: 
			// Tencent类是SDK的主要实现类，开发者可通过Tencent类访问腾讯开放的OpenAPI。
			// 1.4版本:此处需新增参数，传入应用程序的全局context，可通过activity的getApplicationContext方法获取
			tencent = Tencent.createInstance(Constants.TENCENT_APP_ID, LoginActivity.this);
			
			//跳转登陆页面
			onClickLogin();
			break;
		//注册提示
		case R.id.pb_regihter_hint:
			showPopuwindow();
			break;
		}
	}

	/**
	 * 
	 * @Title: showPopuwindow
	 * @Description: 显示popubwindow 用来方便用户操作
	 */
	private void showPopuwindow() {
		View view = View.inflate(LoginActivity.this, R.layout.register_hint_popupwindow, null);
		
		//找到控件设置点击事件
		RadioGroup rg_select_number = (RadioGroup) view.findViewById(R.id.rg_select_number);
		//判断点击了popubwindow中的哪个按钮
		rg_select_number.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				//选择本机号码
				case R.id.rb_location:
					//通过系统服务得到电话管理者
					TelephonyManager tm = (TelephonyManager) LoginActivity.this.getSystemService(Context.TELEPHONY_SERVICE);
					//通过管理者得到电话号码
					String localNumber = tm.getLine1Number();
					String callNumber = PhoneNumberUtil.bridgingCallNumber(localNumber.substring(3, localNumber.length()));
					//最终设置给控件
					cet_regihter_phone.setText(callNumber);
					break;
				//选择联系人
				case R.id.rb_contacts:
					Intent intent = new Intent(LoginActivity.this, AddContactsPhoneNumberActivity.class);
					startActivityForResult(intent, 11);
					break;
				}
			}
		});
		
		//弹出的popupwindow
		if (popupWindow != null) {
			popupWindow.dismiss();
		}
		popupWindow = new PopupWindow(
				view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		
		popupWindow.setFocusable(false);
		popupWindow.setOutsideTouchable(true);
		
		//得到点击图片的位置
		int[] location = new int[2];
		pb_regihter_hint.getLocationInWindow(location);
		int imageWidth = pb_regihter_hint.getWidth();
		int imageHeight = pb_regihter_hint.getHeight();
		
		//显示到指定位置
		popupWindow.showAtLocation(pb_regihter_hint, Gravity.NO_GRAVITY, location[0] + imageWidth + 50 , location[1] + (imageHeight / 5));
	}
	
	/**
	 * 
	 * @Title: onClickLogin
	 * @Description: 跳转到腾讯登陆页面
	 */
	private void onClickLogin() {
		if (!tencent.isSessionValid()) {
			tencent.login(this, "all", loginListener);
		}else{
			tencent.logout(this);
		}
	}
	
	/**
	 * 腾讯登陆接口实现类
	 */
	IUiListener loginListener = new BaseUiListener(){

		@Override
		protected void doComplete(JSONObject values) {
			super.doComplete(values);
			initOpenidAndToken(values);
			//更新用户信息,得到用户头像
			updateUserInfo();
		}
		
	};
	
	/**
	 * 
	 * @Title: initOpenidAndToken
	 * @Description: 处理服务器返回的json格式
	 * @param values json格式数据
	 */
	protected void initOpenidAndToken(JSONObject jsonObject) {
		try {
			String token = jsonObject.getString(Constants.TENCENT_PARAM_ACCESS_TOKEN);
			String expires = jsonObject.getString(Constants.TENCENT_PARAM_EXPIRES_IN);
			String openId = jsonObject.getString(Constants.TENCENT_PARAM_OPEN_ID);
			if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires) && !TextUtils.isEmpty(openId)) {
				tencent.setAccessToken(token, expires);
				tencent.setOpenId(openId);
				Logger.e(LoginActivity.class, "腾讯唯一openId:" +openId);
				
				//获取用户具体信息
				getTencentUserInfo(tencent);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @Title: getTencentUserInfo
	 * @Description: 获取腾讯用户信息
	 * @param tencent2
	 */
	private void getTencentUserInfo(Tencent tencent) {
		if (tencent == null) {
			ToastUtil.showMessage(context, "tencent为空,无法获取用户信息!");
			return;
		}
		boolean ready = tencent.isSessionValid() && tencent.getQQToken().getOpenId() != null;
		if (ready) {
			UserInfo tencentUserInfo = new UserInfo(LoginActivity.this, tencent.getQQToken());
			tencentUserInfo.getUserInfo(new BaseUIListener(this, "get_simple_userinfo", tencent));
		}else{
			ToastUtil.showMessage(context, "参数状态异常,无法读取用户信息!");
		}
		
	}

	/**
	 * 
	 * @Title: updateUserInfo
	 * @Description: 更新用户信息,得到用户头像等...
	 */
	protected void updateUserInfo() {
		if (tencent != null && tencent.isSessionValid()) {
			IUiListener listener = new IUiListener() {
				
				/**
				 * 从服务器获取用户信息数据失败的回调
				 */
				@Override
				public void onError(UiError e) {
					ToastUtil.showMessage(context, e.errorDetail);
				}
				
				/**
				 * 从服务器获取数据成功的回调
				 */
				@Override
				public void onComplete(final Object response) {
					
					/**
					 * 因为需要联网,所以需要开启线程
					 */
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							JSONObject jsonObject = (JSONObject) response;
							if (jsonObject.has("figureurl")) {
								Bitmap bitmap = null;
								try {
									//从服务器获取头像
									bitmap = ImageUtils.getbitmap(jsonObject.getString("figureurl_qq_2"));
									//保存到SDCard本地
									if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
										File file = new File(Environment.getExternalStorageDirectory(), "head.jpg");
										saveBitmapLocation(bitmap, file);
									}else{
										ToastUtil.showMessage(context, "SDCard不可用!");
										//如果SDCard不可用,我们把图片存放到缓冲目录
										File dir = LoginActivity.this.getCacheDir();
										if (dir.exists()) {
											dir.mkdirs();
										}
										File file = new File(dir, "head.jpg");
										saveBitmapLocation(bitmap, file);
									}
									
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
						
						/**
						 * 
						 * @Title: saveBitmapLocation
						 * @Description: 把图片保存到本地
						 * @param bitmap 位图
						 * @param file 保存的文件
						 * @throws FileNotFoundException
						 */
						private void saveBitmapLocation(Bitmap bitmap, File file)
								throws FileNotFoundException {
							FileOutputStream fos = new FileOutputStream(file);
							if (bitmap.compress(CompressFormat.JPEG, 100, fos)) {
								Logger.d(LoginActivity.class, "头像保存到SDCard成功!");
							}
						}
					}).start();
				}
				
				/**
				 * 取消获取用户信息的回调
				 */
				@Override
				public void onCancel() {
					ToastUtil.showMessage(context, "取消后去用户信息!");
				}
			};
		}
	}
	
	/**
	 * 
	 * @ClassName: BaseUiListener
	 * @Description: 登陆的回调监听
	 *
	 * @author o0teamo0o
	 * @date 2014年10月27日 下午9:21:30
	 */
	private class BaseUiListener implements IUiListener{

		/**
		 * 
		 * @Title: onCancel
		 * @Description: 登陆取消的回调
		 */
		@Override
		public void onCancel() {
			ToastUtil.showMessage(context, "登陆被取消");
		}

		/**
		 * 
		 * @Title: onComplete
		 * @Description: 登陆成功的回调
		 * @param response
		 */
		@Override
		public void onComplete(Object response) {
			//判断服务器返回的数据是否为空
			if (null == response) {
				ToastUtil.showMessage(context, "返回为空,登陆失败!");
				return;
			}
			
			//解析服务器返回的json数据 把结合强转换成JSONObject对象
			JSONObject jsonObject = (JSONObject) response;
			if (null != jsonObject && jsonObject.length() == 0) {
				ToastUtil.showMessage(context, "返回为空,登陆失败!");
				return ;
			}
			//打印服务器返回的数据
			Logger.d(LoginActivity.class, response.toString());
			doComplete((JSONObject)response);
		}
		
		/**
		 * 
		 * @Title: doComplete
		 * @Description: 登陆完成之后得到的json 可以进行处理, 目的是把数据返回给调用者
		 * @param values 服务器得到的json数据
		 */
		protected void doComplete(JSONObject values) {

		}

		/**
		 * 
		 * @Title: onError
		 * @Description: 登陆错误的回调
		 * @param e 异常
		 */
		@Override
		public void onError(UiError e) {
			ToastUtil.showMessage(context, e.errorDetail);
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
				Logger.e(LoginActivity.class, "微博唯一Uid:" + accessToken.getUid());
				ToastUtil.showMessage(context, "授权成功");
				
				//得到用户信息
				getWeiboUserInfo(accessToken.getUid());
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
		 * 微博登陆异常时回调
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
		
		//腾讯授权登陆
		if (requestCode == Constants.TENCENT_REQUEST_API) {
			if (resultCode == Constants.TENCENT_RESULT_LOGIN) {
				tencent.handleLoginData(data, loginListener);
				Logger.d(LoginActivity.class, "onActivityResult handle logindata");
			}
		}
		//app内应用登陆
		else if (requestCode == Constants.TENCENT_REQUEST_APPBAR) {
			if (resultCode == Constants.TENCENT_RESULT_LOGIN) {
				updateUserInfo();
			}
		}
	}

	/**
	 * 
	 * @Title: getWeiboUserInfo
	 * @Description: 通过微博SSO得到的accessToken 查询用户信息
	 */
	public void getWeiboUserInfo(String uid) {
		WeiboParameters parameters = new WeiboParameters();
		parameters.put("uid", uid);
		requestAsync(Constants.SAPILIST.get(Constants.WEIBO_READ_USER), parameters, Constants.WEIBO_HTTPMETHOD_GET, requestListener);
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
					BaseUserInfo userInfo = new BaseUserInfo();
					userInfo.nickname = user.screen_name; //设置微博昵称
					userInfo.uid = accessToken.getUid(); //设置微博用户唯一标示
					userInfo.profile_image_url = user.avatar_large; //设置用户头像
					userInfo.gender = user.gender; //用户性别

					Logger.e(BaseUIListener.class, "昵称:"+userInfo.nickname+" \n用户id:"+userInfo.openId+" \n用户头像uri:"+userInfo.profile_image_url+" \n性别:"+userInfo.gender);
	                
				}else{
					ToastUtil.showMessage(context, response);
				}
			}
		}
	};
	private ClearEditText cet_regihter_phone;
	
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
		
		params.put(Constants.WEIBO_KEY_ACCESS_TOKEN, accessToken.getToken());
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

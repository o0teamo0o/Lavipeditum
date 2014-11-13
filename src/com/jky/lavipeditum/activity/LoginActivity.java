package com.jky.lavipeditum.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import com.jky.lavipeditum.LavipeditumApplication;
import com.jky.lavipeditum.R;
import com.jky.lavipeditum.base.BaseActivity;
import com.jky.lavipeditum.base.BasePagerAdapter;
import com.jky.lavipeditum.base.BaseUserInfo;
import com.jky.lavipeditum.custom.Rotate3dAnimation;
import com.jky.lavipeditum.custom_view.BootstrapButton;
import com.jky.lavipeditum.custom_view.ClearEditText;
import com.jky.lavipeditum.custom_view.CustomViewPager;
import com.jky.lavipeditum.lib.tencent.BaseUIListener;
import com.jky.lavipeditum.lib.weibo.openapi.models.User;
import com.jky.lavipeditum.util.Constants;
import com.jky.lavipeditum.util.ImageUtils;
import com.jky.lavipeditum.util.Logger;
import com.jky.lavipeditum.util.PhoneNumberUtil;
import com.jky.lavipeditum.util.PhoneValidator;
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

	private TextView tv_go_register;
	public boolean showRegister = false; // 判断当前界面是注册界面还是登陆界面
	private ViewGroup container; // FrameLayout容器,用来3D翻转需要的使用的数据
	private LinearLayout ll_register, ll_login;
	private TextView tv_go_login, tv_unreceive_identify, tv_identify_notify, tv_phone;
	private CustomViewPager cvp_register;
	private TextView tv_login_weibo, tv_qq_login; //微博,QQ登陆按钮
	private WeiboAuth weiboAuth; //微博 Web 授权类，提供登陆等功能
	private SsoHandler ssoHandler; //注意：SsoHandler 仅当 SDK 支持 SSO 时有效
	private Oauth2AccessToken accessToken; //装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能
	private Tencent tencent; //Tencent类是SDK的主要实现类
	private ProgressBar pb_regihter_hint;
	private PopupWindow popupWindow;
	private BootstrapButton bb_login, bb_identifying, bb_proving_login;
	private ClearEditText cet_username, cet_pwd, cet_regihter_phone, cet_proving;
	private List<PopupWindow> setAlertDialogs;
	private SmsEventHandler smsEventHandler; //短信回调接口
	private String callNumber; //格式化之后的电话号码
	private static final int SNED_SUCCESS = 0; //验证码发送成功
	private static final int PROVING_SUCCESS = 1; //验证码验证成功
	protected static final int CHANGE_PAGE = 2; //手机验证成功之后跳转界面
	private static final int RETRY_INTERVAL = 60; //倒计时计算手机验证
	protected static final int UPDATE_TIME = 3; //更新时间
	private int time = RETRY_INTERVAL; //临时计时
	private Timer timer; //定时器
	private String provingNumber;
	private String phoneNumber;
    
	
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
		bb_login = (BootstrapButton) this.findViewById(R.id.bb_login);
		cet_username = (ClearEditText) this.findViewById(R.id.cet_username);
		cet_pwd = (ClearEditText) this.findViewById(R.id.cet_pwd);
		
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
		bb_login.setOnClickListener(this);
		
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
	
	public Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			//验证码发送成功的提示
			case SNED_SUCCESS:
				Toast.makeText(LoginActivity.this, "验证码已发送", Toast.LENGTH_SHORT).show();
				tv_identify_notify.setText(Html.fromHtml("我们已发送<font color=#209526>验证码</font>短信到这个号码:"));
				tv_phone.setText("+86 " + phoneNumber);
				break;
			//验证码验证成功
			case PROVING_SUCCESS:
				Toast.makeText(LoginActivity.this, "验证通过,跳转中...", Toast.LENGTH_SHORT).show();
				if (timer != null) {
					timer.cancel();
				}
				break;
			//修改页面
			case CHANGE_PAGE:
				cvp_register.setCurrentItem(1);
				break;
			//更新时间
			case UPDATE_TIME:
				if (time == 0) {
					tv_unreceive_identify.setText(Html.fromHtml("<font color=#ff0000>收不到验证码?</font>"));
					tv_unreceive_identify.setClickable(true);
					timer.cancel();
				}else{
					tv_unreceive_identify.setText(Html.fromHtml("接收短信大约需要<font color=#209526>"+time+"</font>秒"));
				}
				break;
			}
		};
	};
	
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
		//填写手机页面
		View inputPager = View.inflate(context, R.layout.register_input_pager, null);
		pb_regihter_hint = (ProgressBar) inputPager.findViewById(R.id.pb_regihter_hint);
		cet_regihter_phone = (ClearEditText) inputPager.findViewById(R.id.cet_regihter_phone);
		bb_identifying = (BootstrapButton) inputPager.findViewById(R.id.bb_identifying);
		
		pb_regihter_hint.setOnClickListener(this);
		bb_identifying.setOnClickListener(this);
		
		pagers.add(inputPager);
		
		//验证码输入界面
		View verificationPager = View.inflate(context, R.layout.register_verification_pager, null);
		cet_proving = (ClearEditText) verificationPager.findViewById(R.id.cet_proving);
		bb_proving_login = (BootstrapButton) verificationPager.findViewById(R.id.bb_proving_login);
		tv_unreceive_identify = (TextView) verificationPager.findViewById(R.id.tv_unreceive_identify);
		tv_identify_notify = (TextView) verificationPager.findViewById(R.id.tv_identify_notify);
		tv_phone = (TextView) verificationPager.findViewById(R.id.tv_phone);
			
		//默认是不可点击的
		tv_unreceive_identify.setClickable(false);
		
		bb_proving_login.setOnClickListener(this);
		tv_unreceive_identify.setOnClickListener(this);
		pagers.add(verificationPager);
		return pagers;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 去注册
		case R.id.tv_go_register:
			if (!showRegister) {
				if (setAlertDialogs != null) {
					for (PopupWindow p : setAlertDialogs) {
						p.dismiss();
					}
				}
				applyRotation(0, 90);
				showRegister = true;
			}
			break;
		// 去登陆
		case R.id.tv_go_login:
			if (showRegister) {
				if (popupWindow != null && popupWindow.isShowing()) {
					popupWindow.dismiss();
				}
				if (setAlertDialogs != null) {
					for (PopupWindow p : setAlertDialogs) {
						p.dismiss();
					}
				}
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
		//登陆
		case R.id.bb_login:
			String userName = cet_username.getText().toString().trim();
			String password = cet_pwd.getText().toString().trim();
			
			//判断手机号码和密码是否为空
			if (TextUtils.isEmpty(userName)) {
				setAlertDialogs = cet_username.setAlertDialog(LoginActivity.this, "手机号码不能为空!");
			}else if (TextUtils.isEmpty(password)) {
				setAlertDialogs = cet_pwd.setAlertDialog(LoginActivity.this, "密码不能为空!");
			}
			break;
		//获取验证码
		case R.id.bb_identifying:
			getIdentifyingCode();
			break;
		//输入验证码
		case R.id.bb_proving_login:
			provingNumber = cet_proving.getText().toString().trim();
			if (TextUtils.isEmpty(provingNumber)) {
				setAlertDialogs = cet_proving.setAlertDialog(LoginActivity.this, "验证码不能为空!");
			}else{
				//提交验证码
				SMSSDK.submitVerificationCode(Constants.SMS_COUNTRY_ID, callNumber, provingNumber);
			}
			break;
		//重新获取验证码
		case R.id.tv_unreceive_identify:
			//提交验证码
			SMSSDK.submitVerificationCode(Constants.SMS_COUNTRY_ID, callNumber, provingNumber);
			break;
		}
	}

	/**
	 * 
	 * Title: getIdentifyingCode
	 * Description: 点击按钮获取验证码
	 */
	private void getIdentifyingCode() {
		phoneNumber = cet_regihter_phone.getText().toString().trim();
		
		//判断用户名是否为空
		if (TextUtils.isEmpty(phoneNumber)) {
			setAlertDialogs = cet_regihter_phone.setAlertDialog(LoginActivity.this, "手机号码不能为空!");
		}else {
			if (!new PhoneValidator(phoneNumber).isValid()) {
				setAlertDialogs = cet_regihter_phone.setAlertDialog(LoginActivity.this, "手机号码不正确!");
			}else{
				if (LavipeditumApplication.isNetWork) {
					//初始化SDK 短信SDK的入口
					SMSSDK.initSDK(LoginActivity.this, Constants.SMS_APPKEY, Constants.SMS_APP_SECRET);
					
					//注册短信回调接口
					smsEventHandler = new SmsEventHandler();
					SMSSDK.registerEventHandler(smsEventHandler);
					
					//弹出对话框确认手机号码
					final Dialog commonDialog = new Dialog(LoginActivity.this);
					
					//设置Dialog没有标题，在之后定义就会报错
					commonDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					
					commonDialog.setContentView(R.layout.smssdk_send_msg_dialog);
					
					TextView tv_dialog_hint = (TextView) commonDialog.findViewById(R.id.tv_dialog_hint);
					tv_dialog_hint.setText(Html.fromHtml("我们将发送<font color=#3cac17>验证码</font>短信到这个号码:"));
					TextView tv_phone = (TextView) commonDialog.findViewById(R.id.tv_phone);
					tv_phone.setText("+86 " + phoneNumber);
					
					//取消按钮
					Button btn_dialog_cancel = (Button) commonDialog.findViewById(R.id.btn_dialog_cancel);
					btn_dialog_cancel.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							commonDialog.cancel();
						}
					});
					
					//确定按钮
					Button btn_dialog_ok = (Button) commonDialog.findViewById(R.id.btn_dialog_ok);
					btn_dialog_ok.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							//消除popupwindow
							if (popupWindow != null) {
								popupWindow.dismiss();
							}
							tv_unreceive_identify.setText(Html.fromHtml("接收短信大约需要<font color=#209526>"+time+"</font>秒"));
							commonDialog.dismiss();
							//修改手机格式
							callNumber = PhoneNumberUtil.restoreCallNumber(phoneNumber);
							//请求获取短信验证码，在监听中返回
							SMSSDK.getVerificationCode(Constants.SMS_COUNTRY_ID, callNumber);
							
							//更新时间
							countDown();
						}
					});
					commonDialog.show();
				}else{
					ToastUtil.showMessage(LoginActivity.this, "当前没有网络,是否去设置!");
				}
			}
		}
	}
	
	/**
	 * 
	 * @ClassName: SmsEventHandler
	 * @Description: 注册 短信回调接口
	 *
	 * @author o0teamo0o
	 * @date 2014年11月3日 下午10:09:35
	 */
	private class SmsEventHandler extends EventHandler{

		/**
		 * 事件执行后调用
		 */
		@Override
		public void afterEvent(int event, int result, java.lang.Object data){
			super.afterEvent(event, result, data);
			//操作表示成功
			if (result == SMSSDK.RESULT_COMPLETE) {
				Logger.v(LoginActivity.class, "成功!");
				if (cvp_register.getCurrentItem() != 1) {
					handler.sendEmptyMessage(CHANGE_PAGE);
				}
				switch (event) {
				//返回支持发送验证码的国家列表
				case SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES:
					// data ArrayList<HashMap<String,Object>>
					System.out.println("返回支持发送验证码的国家列表");
					break;
				//请求发送验证码，无返回
				case SMSSDK.EVENT_GET_VERIFICATION_CODE:
					handler.sendEmptyMessage(SNED_SUCCESS);
					break;
				//校验验证码，返回校验的手机和国家代码
				case SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE:
					// data	HashMap<String,Object>
					@SuppressWarnings("unchecked")
					HashMap<String,Object> phoneMap = (HashMap<String, Object>) data;
					//得到国家
					String country = (String) phoneMap.get("country");
					String phone = (String) phoneMap.get("phone");
					
					//验证码验证成功 发送通知
					Message msg = new Message();
					msg.what = PROVING_SUCCESS;
					msg.obj = phone;
					handler.sendMessage(msg);
					//提交用户信息
					registerUser(country, phone);
					break;
				//获取手机内部的通信录列表
				case SMSSDK.EVENT_GET_CONTACTS:
					System.out.println("获取手机内部的通信录列表");
					//data ArrayList<HashMap<String,Object>>
					break;
				//提交应用内的用户资料
				case SMSSDK.EVENT_SUBMIT_USER_INFO:
					System.out.println("提交应用内的用户资料");
					break;
				//获取手机通信录在当前应用内的用户列表
				case SMSSDK.EVENT_GET_FRIENDS_IN_APP:
					System.out.println("获取手机通信录在当前应用内的用户列表");
					//data ArrayList<HashMap<String,Object>>
					break;
				//获取手机通信录在当前应用内的新用户个数
				case SMSSDK.EVENT_GET_NEW_FRIENDS_COUNT:
					System.out.println("获取手机通信录在当前应用内的新用户个数");
					//data Integer
					break;
				}
			}
			//操作表示失败
			else if (result == SMSSDK.RESULT_ERROR) {
				switch (event) {
				//获取手机联系人列表的事件ID
				case SMSSDK.EVENT_GET_CONTACTS:
					ToastUtil.showMessage(LoginActivity.this, "获取手机联系人列表失败!");
					break;
				//获取应用内好友列表的事件ID
				case SMSSDK.EVENT_GET_FRIENDS_IN_APP:
					ToastUtil.showMessage(LoginActivity.this, "获取应用内好友列表失败!");
					break;
				//获取应用内新好友个数的事件ID
				case SMSSDK.EVENT_GET_NEW_FRIENDS_COUNT:
					ToastUtil.showMessage(LoginActivity.this, "获取应用内新好友个数失败!");
					break;
				//获取国家列表的事件ID
				case SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES:
					ToastUtil.showMessage(LoginActivity.this, "获取国家列表失败!");
					break;
				//获取验证码的事件ID
				case SMSSDK.EVENT_GET_VERIFICATION_CODE:
					ToastUtil.showMessage(LoginActivity.this, "验证码不正确，请重新输入!");
					//切换到手机输入手机验证码页面
					cvp_register.setCurrentItem(0);
					break;
				//提交已注册成功的用户信息的事件ID
				case SMSSDK.EVENT_SUBMIT_USER_INFO:
					ToastUtil.showMessage(LoginActivity.this, "获取提交已注册成功的用户信息失败!");
					break;
				//提交验证码的的事件ID
				case SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE:
					ToastUtil.showMessage(LoginActivity.this, "提交的验证码失败,请重试!");
					break;
				//事件执行成功的ID
				case SMSSDK.RESULT_COMPLETE:
					ToastUtil.showMessage(LoginActivity.this, "事件执行成功!");
					break;
				//事件执行失败的ID 
				case SMSSDK.RESULT_ERROR:
					ToastUtil.showMessage(LoginActivity.this, "事件执行失败!");
					break;
				}
			}
		}

		/**
		 * 事件执行前调
		 */
		@Override
		public void beforeEvent(int event, java.lang.Object data) {
			super.beforeEvent(event, data);
		}

		/**
		 * 注册监听
		 */
		@Override
		public void onRegister() {
			super.onRegister();
		}

		/**
		 * 反注册监听
		 */
		@Override
		public void onUnregister() {
			super.onUnregister();
		}
		
	}
	
	/**
	 * 
	 * @Title: registerUser
	 * @Description: 提交短信注册用户信息
	 * @param country
	 * @param phone
	 */
	public void registerUser(String country, String phone) {
		Random rnd = new Random(); //随机参数一个数
		int id = Math.abs(rnd.nextInt()); //产生一个用户id
		String uid = String.valueOf(id); 
		String nickName = "Lavipeditum_User_" + uid; //产生一个昵称
		String avatar = Constants.AVATARS[id % 12]; //产生一个随机头像
		
		//提交用户信息，在监听中返回
		SMSSDK.submitUserInfo(uid, nickName, avatar, country, phone);
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
		RadioButton rb_contacts = (RadioButton) view.findViewById(R.id.rb_contacts);
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
					//跳转到添加联系人页面
					Intent intent = new Intent(LoginActivity.this, RegisterAddCallNumberActivity.class);
					startActivityForResult(intent, Constants.GO_CONSTACT_PAGER_REQUESTCODE);
					//rb_contacts.setChecked(checked);
					popupWindow.dismiss();
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
	 * Title: countDown
	 * Description: 更新倒计时时间
	 */
	protected void countDown() {
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				time--;
				handler.sendEmptyMessage(UPDATE_TIME);
			}
		}, 1000, 1000);
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
		//判断是否是联系人页面的返回
		if (resultCode == Constants.CONSTACT_PAGER_RESULTCODE) {
			if (data != null) {
				String constactsNumber = data.getStringExtra("friendPhoneNumber");
				Logger.d(LoginActivity.class, "constactsNumber: "+constactsNumber);
				String callNumber = PhoneNumberUtil.bridgingCallNumber(constactsNumber);
				//最终设置给控件
				cet_regihter_phone.setText(callNumber);
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
					userInfo.uid = user.id; //设置微博用户唯一标示
					userInfo.profile_image_url = user.avatar_large; //设置用户头像
					userInfo.gender = user.gender; //用户性别

					Logger.e(BaseUIListener.class, "昵称:"+userInfo.nickname+" \n用户id:"+userInfo.uid+" \n用户头像uri:"+userInfo.profile_image_url+" \n性别:"+userInfo.gender);
	                
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
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		//处理popopwindow的生命周期
		if (popupWindow != null && popupWindow.isShowing()) {
			popupWindow.dismiss();
		}
		//注销短信验证的回调接口
		if (smsEventHandler != null) {
			SMSSDK.unregisterEventHandler(smsEventHandler);
		}
	}

}

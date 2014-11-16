package com.jky.lavipeditum.activity;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.jky.lavipeditum.LavipeditumApplication;
import com.jky.lavipeditum.R;
import com.jky.lavipeditum.base.BaseActivity;
import com.jky.lavipeditum.custom_view.BootstrapButton;
import com.jky.lavipeditum.custom_view.ClearEditText;
import com.jky.lavipeditum.util.Constants;

/**
 * 
 * @ClassName: SellerLoginActivity 
 * @Description: 商家登陆页面

 * @author o0teamo0o
 * @date 2014年11月16日 下午2:16:59 
 *
 */
public class SellerLoginActivity extends BaseActivity implements OnClickListener {
	
	private ClearEditText cet_username, cet_pwd;
	private BootstrapButton bb_login;
	private List<PopupWindow> setAlertDialogs;
	private ImageView iv_back;

	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.seller_login);
		
		cet_username = (ClearEditText) this.findViewById(R.id.cet_username);
		cet_pwd = (ClearEditText) this.findViewById(R.id.cet_pwd);
		bb_login = (BootstrapButton) this.findViewById(R.id.bb_login);
		iv_back = (ImageView) this.findViewById(R.id.iv_back);
	}

	@Override
	protected void initData() {
		
	}

	@Override
	protected void initListener() {
		bb_login.setOnClickListener(this);
		iv_back.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		//返回
		case R.id.iv_back:
			Intent intent = new Intent();
			setResult(Constants.SELLER_LOGIN_BACK_REQUESTCODE, intent);
			SellerLoginActivity.this.finish();
			break;
		//商家登陆按钮
		case R.id.bb_login:
			String userName = cet_username.getText().toString().trim();
			String password = cet_pwd.getText().toString().trim();
			
			//判断手机号码和密码是否为空
			if (TextUtils.isEmpty(userName)) {
				setAlertDialogs = cet_username.setAlertDialog(SellerLoginActivity.this, "手机号码不能为空!");
			}else if (TextUtils.isEmpty(password)) {
				setAlertDialogs = cet_pwd.setAlertDialog(SellerLoginActivity.this, "密码不能为空!");
			}
			
			//判断密码去了
			
			//如果密码判断成功进入界面
			loginSuccess();
			break;
		}
	}
	
	/**
	 * 
	 * Title: loginSuccess
	 * Description:登陆成之后的跳转
	 */
	private void loginSuccess() {
		Intent intent = new Intent();
		intent.putExtra("seller", true);
		setResult(Constants.SELLER_LOGIN_SUCCESS, intent);
		LavipeditumApplication.isSellerLogin = true;
		SellerLoginActivity.this.finish();
	}

	/**
	 * 判断用户按键盘做的事情
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {   
			Intent intent = new Intent();
			intent.putExtra(Constants.SELLER, true);
			setResult(Constants.SELLER_LOGIN_BACK_REQUESTCODE, intent);
			SellerLoginActivity.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

}

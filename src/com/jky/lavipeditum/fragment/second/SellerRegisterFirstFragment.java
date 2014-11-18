package com.jky.lavipeditum.fragment.second;

import java.util.List;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.PopupWindow;

import com.jky.lavipeditum.R;
import com.jky.lavipeditum.base.BaseFragment;
import com.jky.lavipeditum.custom_view.BootstrapButton;
import com.jky.lavipeditum.custom_view.ClearEditText;
import com.jky.lavipeditum.custom_view.DirectionalViewPager;
import com.jky.lavipeditum.util.EmailValidator;
import com.jky.lavipeditum.util.PhoneValidator;

/**
 * 
 * @ClassName: SellerRegisterFirstFragment 
 * @Description: 商家注册第一个界面

 * @author o0teamo0o
 * @date 2014年11月18日 下午3:10:21 
 *
 */
public class SellerRegisterFirstFragment extends BaseFragment implements OnClickListener {

	private View view;
	private ClearEditText cet_username, cet_phone, cet_email, cet_other;
	private BootstrapButton bb_next;
	private List<PopupWindow> setAlertDialogs;
	private DirectionalViewPager dvp_register_pager;

	@Override
	protected View initView(LayoutInflater inflater, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.seller_register_one_pager, null);
		
		cet_username = (ClearEditText) view.findViewById(R.id.cet_username);
		cet_phone = (ClearEditText) view.findViewById(R.id.cet_phone);
		cet_email = (ClearEditText) view.findViewById(R.id.cet_email);
		cet_other = (ClearEditText) view.findViewById(R.id.cet_other);
		bb_next = (BootstrapButton) view.findViewById(R.id.bb_next);
		
		return view;
	}

	@Override
	protected void initData(Bundle savedInstanceState) {
		
	}

	@Override
	protected void initListener() {
		bb_next.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		//下一步
		case R.id.bb_next:
			String userName = cet_username.getText().toString().trim();
			String phone = cet_phone.getText().toString().trim();
			String email = cet_email.getText().toString().trim();
			String other = cet_other.getText().toString().trim();
			
			//判断用户名是否为空
			if (TextUtils.isEmpty(userName)) {
				setAlertDialogs = cet_username.setAlertDialog(getActivity(), "用户名不能为空!");
				getFocusable(cet_username);
				return;
			}
			//判断电话号码是否为空
			else if (TextUtils.isEmpty(phone)) {
				setAlertDialogs = cet_phone.setAlertDialog(getActivity(), "手机号不能为空!");
				getFocusable(cet_phone);
				return;
			}
			//判断手机号码格式是否正确
			else if (!new PhoneValidator(phone).isValid()) {
				setAlertDialogs = cet_phone.setAlertDialog(getActivity(), "手机号码不正确!");
				getFocusable(cet_phone);
				return;
			}
			//判断邮箱是否为空
			else if (TextUtils.isEmpty(email)) {
				setAlertDialogs = cet_email.setAlertDialog(getActivity(), "邮箱不能为空!");
				getFocusable(cet_email);
				return;
			}
			//判断邮箱格式是否正确
			else if (!new EmailValidator(email).isValid()) {
				setAlertDialogs = cet_email.setAlertDialog(getActivity(), "邮箱格式不正确!");
				getFocusable(cet_email);
				return;
			}
			
			//都填写正确之后 因为切换页面
			dvp_register_pager = (DirectionalViewPager) getParentFragment().getView().findViewById(R.id.dvp_register_pager);
			dvp_register_pager.setCurrentItem(1, false);
			break;
		}
	}

	/**
	 * 
	 * Title: getFocusable
	 * Description: 让控件获取焦点
	 * @param view
	 */
	private void getFocusable(View view) {
		view.setFocusable(true);
		view.setFocusableInTouchMode(true);
		view.requestFocus();
		view.requestFocusFromTouch();
	}

}

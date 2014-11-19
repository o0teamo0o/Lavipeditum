package com.jky.lavipeditum.fragment.second;

import java.util.List;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.PopupWindow;
import android.widget.RadioButton;

import com.jky.lavipeditum.R;
import com.jky.lavipeditum.base.BaseFragment;
import com.jky.lavipeditum.custom_view.BootstrapButton;
import com.jky.lavipeditum.custom_view.ClearEditText;
import com.jky.lavipeditum.custom_view.DirectionalViewPager;
import com.jky.lavipeditum.util.Constants;
import com.jky.lavipeditum.util.PhoneValidator;

/**
 * 
 * @ClassName: SellerRegisterSecondFragment 
 * @Description: 商家注册第二个界面

 * @author o0teamo0o
 * @date 2014年11月18日 下午3:11:55 
 *
 */
public class SellerRegisterSecondFragment extends BaseFragment implements OnClickListener {

	private View view;
	private ClearEditText cet_shop_name, cet_phone, cet_address, cet_detailed_address;
	private BootstrapButton bb_next;
	private List<PopupWindow> setAlertDialogs;
	private RadioButton rb_three;
	private View parentView;
 
	@Override
	protected View initView(LayoutInflater inflater, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.seller_register_two_pager, null);
		//父fragment
		parentView = getParentFragment().getView();
		
		cet_shop_name = (ClearEditText) view.findViewById(R.id.cet_shop_name);
		cet_phone = (ClearEditText) view.findViewById(R.id.cet_phone);
		cet_address = (ClearEditText) view.findViewById(R.id.cet_address);
		cet_detailed_address = (ClearEditText) view.findViewById(R.id.cet_detailed_address);
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
			String shopName = cet_shop_name.getText().toString().trim();
			String phone = cet_phone.getText().toString().trim();
			String address = cet_address.getText().toString().trim();
			String detailedAddress = cet_detailed_address.getText().toString().trim();
			
			//门店名的判断
			if (TextUtils.isEmpty(shopName)) {
				setAlertDialogs = cet_shop_name.setAlertDialog(getActivity(), "门店名不能为空!");
				getFocusable(cet_shop_name);
				return;
			}
			//门店电话号码的判断
			else if (TextUtils.isEmpty(phone)) {
				setAlertDialogs = cet_phone.setAlertDialog(getActivity(), "门店电话不能为空!");
				getFocusable(cet_phone);
				return;
			}
			//门店电话号码是否合格
			else if (!new PhoneValidator(phone).isValid()) {
				setAlertDialogs = cet_phone.setAlertDialog(getActivity(), "门店电话格式不正确!");
				getFocusable(cet_phone);
				return;
			}
			//门店详细地址的判断
			else if (TextUtils.isEmpty(address)) {
				setAlertDialogs = cet_address.setAlertDialog(getActivity(), "门店详细地址不能为空!");
				getFocusable(cet_address);
				return;
			}
			//门店信息描述的判断
			else if (TextUtils.isEmpty(detailedAddress)) {
				setAlertDialogs = cet_detailed_address.setAlertDialog(getActivity(), "门店信息描述不能为空!");
				getFocusable(cet_detailed_address);
				return;
			}
			//门店信息描述的字数判断
			else if (TextUtils.isEmpty(detailedAddress)) {
				setAlertDialogs = cet_detailed_address.setAlertDialog(getActivity(), "门店信息描述不能为空!");
				getFocusable(cet_detailed_address);
				return;
			}
			
			DirectionalViewPager dvp_register_pager = (DirectionalViewPager) parentView.findViewById(R.id.dvp_register_pager);
			dvp_register_pager.setCurrentItem(Constants.SELLERREGISTERTHREEFRAGMENT, false);
			//得到radiobutton
			rb_three = (RadioButton) parentView.findViewById(R.id.rb_three);
			rb_three.setChecked(true);
			
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
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if (setAlertDialogs != null) {
			for (PopupWindow p : setAlertDialogs) {
				p.dismiss();
			}
		}
	}

}

package com.jky.lavipeditum.fragment.second;

import java.util.ArrayList;
import java.util.Currency;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.jky.lavipeditum.R;
import com.jky.lavipeditum.activity.AddMarkerToMapActivity;
import com.jky.lavipeditum.activity.LocationCityActivity;
import com.jky.lavipeditum.adapter.RegionListViewAdapter;
import com.jky.lavipeditum.base.BaseFragment;
import com.jky.lavipeditum.bean.CurrentRegion;
import com.jky.lavipeditum.bean.Region;
import com.jky.lavipeditum.engine.RegionInfoService;
import com.jky.lavipeditum.util.Constants;
import com.jky.lavipeditum.util.ToastUtil;

/**
 * 
 * @ClassName: SellerRegisterThreeFragment 
 * @Description: 商家注册第三个界面

 * @author o0teamo0o
 * @date 2014年11月18日 下午3:12:29 
 *
 */
public class SellerRegisterThreeFragment extends BaseFragment implements OnClickListener, OnItemClickListener {

	protected static final int QUERY_SUCCESS = 0;
	private static final int CITY = 0;
	private static final int REGION = 1;
	private static final int STREET = 2;
	private View view;
	private View parentView;
	private CheckBox cb_city, cb_region, cb_street;
	private PopupWindow popupWindow;
	private ArrayList<Region> regions;
	private ImageView iv_loding;
	private ListView lv_region;
	private RegionListViewAdapter adapter;
	private int regionMode = -1;
	private Region region;
	private String regionCode = "-1";
	private String streetCode = "-1";
	private Button bt_makrer_map;
	private CurrentRegion currentRegion;
	
	private Handler hander = new Handler(){

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case QUERY_SUCCESS:
				if (regions != null && regions.size() >= 0) {
					adapter = new RegionListViewAdapter(getActivity(), regions);
				}
				
				iv_loding.setVisibility(View.GONE);
				lv_region.setVisibility(View.VISIBLE);
				lv_region.setAdapter(adapter);
				
				break;
			}
		};
	};

	@Override
	protected View initView(LayoutInflater inflater, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.seller_register_three_pager, null);
		parentView = getParentFragment().getView();
		
		cb_city = (CheckBox) view.findViewById(R.id.cb_city);
		cb_region = (CheckBox) view.findViewById(R.id.cb_region);
		cb_street = (CheckBox) view.findViewById(R.id.cb_street);
		bt_makrer_map = (Button) view.findViewById(R.id.bt_makrer_map);
		
		return view;
	}

	@Override
	protected void initData(Bundle savedInstanceState) {
		
	}

	@Override
	protected void initListener() {
		cb_city.setOnClickListener(this);
		cb_region.setOnClickListener(this);
		cb_street.setOnClickListener(this);
		bt_makrer_map.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		//城市
		case R.id.cb_city:
			regionMode = CITY;
			regionCode = "-1";
			streetCode = "-1";
			String provinceRegionCode = preferences.getProvinceRegionCode();
			loadRegion(provinceRegionCode);
			initPopupWindow(cb_city, provinceRegionCode);
			break;
		//区域
		case R.id.cb_region:
			regionMode = REGION;
			if (regionCode.equals("-1")) {
				ToastUtil.showMessage(getActivity(), "请先确定城市信息");
				return;
			}
			loadRegion(regionCode);
			initPopupWindow(cb_region, null);
			break;
		//街道
		case R.id.cb_street:
			regionMode = STREET;
			if (streetCode.equals("-1")) {
				ToastUtil.showMessage(getActivity(), "请先确定区域信息");
				return;
			}
			loadRegion(streetCode);
			initPopupWindow(cb_street, null);
			break;
		//地图上添加marker
		case R.id.bt_makrer_map:
			if (currentRegion == null) {
				ToastUtil.showMessage(getActivity(), "请先选择门店所在方位");
				return ;
			}
			Intent intent = new Intent(getActivity(), AddMarkerToMapActivity.class);
			intent.putExtra("currentRegion", currentRegion);
			startActivityForResult(intent, Constants.GO_BAIDUMAP_ADD_MARKER_REQUESTCODE);
			break;
		}
	}
	
	private void initPopupWindow(View view, String code) {
		if (popupWindow != null) {
			popupWindow.dismiss();
		}
		popupWindow = new PopupWindow(getActivity());
		View v = View.inflate(getActivity(), R.layout.seller_register_three_pager_popup, null);
		iv_loding = (ImageView) v.findViewById(R.id.iv_loding);
		lv_region = (ListView) v.findViewById(R.id.lv_region);
		
		//设置条目点击监听
		lv_region.setOnItemClickListener(this);
		
		//去掉默认的背景 
		//popupWindow.setBackgroundDrawable(new PaintDrawable());
		popupWindow.setContentView(v);
		
		//点击空白处的时候PopupWindow会消失
		popupWindow.setTouchable(true); 
		popupWindow.setOutsideTouchable(true);
		//如果focusable为false，在一个Activity弹出一个PopupWindow，按返回键，由于PopupWindow没有焦点，会直接退出Activity。如果focusable为true，PopupWindow弹出后，所有的触屏和物理按键都有PopupWindows处理。
		popupWindow.setFocusable(true);
		
		popupWindow.setWidth(view.getWidth());
		popupWindow.setHeight(350);
		
		popupWindow.showAsDropDown(view);
	}
	
	/**
	 * 
	 * Title: loadRegion
	 * Description: 查询具体城市，区域，街道
	 * @param code
	 */
	private void loadRegion(final String code){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				RegionInfoService regionInfoService = new RegionInfoService(getActivity());
				regions = regionInfoService.getRegions(code);
				hander.sendEmptyMessage(QUERY_SUCCESS);
			}
		}).start();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		region = regions.get(position);
		switch (regionMode) {
		//城市
		case CITY:
			currentRegion = new CurrentRegion();
			currentRegion.setCity(region.getName());
			
			cb_city.setText(region.getName());
			regionCode = region.getCode();
			
			//初始化操作
			cb_region.setText("区域");
			cb_street.setText("商圈");
			break;
		//区域
		case REGION:
			currentRegion.setRegion(region.getName());
			
			cb_region.setText(region.getName());
			streetCode = region.getCode();
			break;
		//街道
		case STREET:
			currentRegion.setStreen(region.getName());
			
			cb_street.setText(region.getName());
			break;
		}
		popupWindow.dismiss();
	}

}

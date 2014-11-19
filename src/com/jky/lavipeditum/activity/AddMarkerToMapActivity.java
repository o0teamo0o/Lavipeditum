package com.jky.lavipeditum.activity;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.jky.lavipeditum.R;
import com.jky.lavipeditum.adapter.RegionListViewAdapter;
import com.jky.lavipeditum.base.BaseActivity;
import com.jky.lavipeditum.bean.CurrentRegion;
import com.jky.lavipeditum.bean.Region;
import com.jky.lavipeditum.engine.RegionInfoService;
import com.jky.lavipeditum.util.ToastUtil;

/**
 * 
 * @ClassName: AddMarkerToMapActivity 
 * @Description: 商家添加标记到地图上的界面
 *
 * @author o0teamo0o
 * @date 2014年11月19日 下午9:09:15 
 *
 */
public class AddMarkerToMapActivity extends BaseActivity implements OnGetGeoCoderResultListener, OnClickListener, OnItemClickListener {
	
	protected static final int QUERY_SUCCESS = 0;
	private MapView bmapView;
	private CurrentRegion currentRegion;
	private CheckBox cb_city;
	private BaiduMap baiduMap;
	private PopupWindow popupWindow;
	private ImageView iv_loding, iv_search;
	private ListView lv_region;
	private ArrayList<Region> regions;
	private GeoCoder geoCoder;
	private EditText et_street;
	private LinearLayout ll_max_zoom, ll_min_zoom, ll_location;

	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.add_marker_to_map);
		
		currentRegion = (CurrentRegion) this.getIntent().getSerializableExtra("currentRegion");
		
		//地图初始化
		bmapView = (MapView)this.findViewById(R.id.bmapView);
		cb_city = (CheckBox) this.findViewById(R.id.cb_city);
		et_street = (EditText) this.findViewById(R.id.et_street);
		iv_search = (ImageView) this.findViewById(R.id.iv_search);
		ll_max_zoom = (LinearLayout) this.findViewById(R.id.ll_max_zoom);
		ll_min_zoom = (LinearLayout) this.findViewById(R.id.ll_min_zoom);
		ll_location = (LinearLayout) this.findViewById(R.id.ll_location);
		
		baiduMap = bmapView.getMap();
		
		//初始化搜索模块，注册事件监听
		geoCoder = GeoCoder.newInstance();
		geoCoder.setOnGetGeoCodeResultListener(this);
	}

	@Override
	protected void initData() {
		cb_city.setText(currentRegion.getCity());
		
		//设置是否允许定位图层
		baiduMap.setMyLocationEnabled(true);
		
		//初始话LocationClient 类
		client = new LocationClient(this);
		
		//设置定位参数
		LocationClientOption option = new LocationClientOption();
		//LocationMode.Hight_Accuracy 精确定位
		//option.setLocationMode(LocationMode.Hight_Accuracy);//设置定位模式
		//bdo9ll 返回的是百度的坐标
		option.setOpenGps(true); //打开gps
		option.setCoorType("bd09ll");//返回的定位结果是百度经纬度,默认值gcj02
		//option.setScanSpan(5000);//设置发起定位请求的间隔时间为5000ms
		option.setIsNeedAddress(true);//返回的定位结果包含地址信息
		option.setNeedDeviceDirect(true);//返回的定位结果包含手机机头的方向
		client.setLocOption(option);
		
		listener = new MyBDLocationListener();
	}

	@Override
	protected void initListener() {
		cb_city.setOnClickListener(this);
		iv_search.setOnClickListener(this);
		ll_max_zoom.setOnClickListener(this);
		ll_min_zoom.setOnClickListener(this);
		ll_location.setOnClickListener(this);
		
		//注册监听
		client.registerLocationListener(listener);
	}
	
	public Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case QUERY_SUCCESS:
				if (regions != null && regions.size() >= 0) {
					RegionListViewAdapter adapter = new RegionListViewAdapter(AddMarkerToMapActivity.this, regions);
					lv_region.setAdapter(adapter);
				}
				iv_loding.setVisibility(View.GONE);
				lv_region.setVisibility(View.VISIBLE);
				
				break;
			}
		};
	};
	private LocationClient client;
	private MyBDLocationListener listener;
	
	@Override
	protected void onStart() {
		super.onStart();
		//设置最大缩放级别, 因为只涉及到湖南地区
		baiduMap.setMaxAndMinZoomLevel(7, 19);
		
		//zoom 设置地图缩放级别 3~19 值越小地图显示范围越大
		MapStatus status = new MapStatus.Builder().zoom(16).build();
		
		//隐藏缩放控件
		bmapView.showZoomControls(false);
		
		//设置地图缩放级别
		baiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(status));
		
		geoCoder.geocode(new GeoCodeOption()
		.city(currentRegion.getCity())
				.address(currentRegion.getRegion() + currentRegion.getStreen()));
	};
	
	@Override
	protected void onResume() {
		super.onResume();
		bmapView.onResume();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		bmapView.onResume();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		bmapView.onDestroy();
	}

	/**
	 * 
	 * Title: onGetGeoCodeResult
	 * Description:地理编码查询结果回调函数
	 * @param result 地理编码查询结果
	 */
	@Override
	public void onGetGeoCodeResult(GeoCodeResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			ToastUtil.showMessage(AddMarkerToMapActivity.this, "抱歉,未能找到结果！");
			return ;
		}
		
		baiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result.getLocation()));
	}

	/**
	 * 
	 * Title: onGetReverseGeoCodeResult
	 * Description: 反地理编码查询结果回调函数 
	 * @param result 反地理编码查询结果
	 */
	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			ToastUtil.showMessage(AddMarkerToMapActivity.this, "抱歉,未能找到结果!");
			return ;
		}
		
		baiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result.getLocation()));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		//城市切换按钮
		case R.id.cb_city:
			String provinceRegionCode = preferences.getProvinceRegionCode();
			loadRegion(provinceRegionCode);
			initPopupWindow(cb_city, provinceRegionCode);
			break;
		//搜索
		case R.id.iv_search:
			String cityName = cb_city.getText().toString().trim();
			String street = et_street.getText().toString().trim();
			
			//Geo搜索
			geoCoder.geocode(new GeoCodeOption().city(cityName)
					.address(street));
			break;
		//放大地图
		case R.id.ll_max_zoom:
			perfomMaxZoom();
			break;
		//缩小地图
		case R.id.ll_min_zoom:
			perfomMinZoom();
			break;
		//定位自己
		case R.id.ll_location:
			client.start();
			break;
		}
	}
	
	/**
	 * 
	 * Title: perfomMaxZoom
	 * Description: 放大地图
	 */
	private void perfomMaxZoom() {
		//放大地图缩放级别
		MapStatusUpdate u = MapStatusUpdateFactory.zoomIn();
		baiduMap.animateMapStatus(u);
	}

	/**
	 * 
	 * Title: perfomMinZoom
	 * Description: 缩放地图
	 */
	private void perfomMinZoom() {
		//缩小地图缩放级别
		MapStatusUpdate u = MapStatusUpdateFactory.zoomOut();
		baiduMap.animateMapStatus(u);
	}

	/**
	 * 
	 * Title: loadRegion
	 * Description: 查询记录的城市
	 * @param provinceRegionCode
	 */
	private void loadRegion(final String code) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				RegionInfoService regionInfoService = new RegionInfoService(AddMarkerToMapActivity.this);
				regions = regionInfoService.getRegions(code);
				handler.sendEmptyMessage(QUERY_SUCCESS);
			}
		}).start();
	}

	private void initPopupWindow(View view, String code) {
		if (popupWindow != null) {
			popupWindow.dismiss();
		}
		PopupWindow popupWindow = new PopupWindow(AddMarkerToMapActivity.this);
		View v = View.inflate(AddMarkerToMapActivity.this, R.layout.seller_register_three_pager_popup, null);
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

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Region region = regions.get(position);
		cb_city.setText(region.getName());
		if (popupWindow != null) {
			popupWindow.dismiss();
		}
	}
	
	private class MyBDLocationListener implements BDLocationListener{

		/**
		 * 
		 * Title: onReceiveLocation
		 * Description: 提供者的位置
		 * @param location
		 */
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null){
				return ;
			}
			
			MyLocationData.Builder builder = new MyLocationData.Builder();
			//设置定位数据的精度信息，单位：米
			builder.accuracy(location.getRadius());
			//设置定位数据的方向信息
			builder.direction(location.getDirection());
			//设置定位数据的纬度
			builder.latitude(location.getLatitude());
			//设置定位数据的经度
			builder.longitude(location.getLongitude());
			
			//设置定位数据, 只有先允许定位图层后设置数据才会生效，参见 setMyLocationEnabled(boolean)
			baiduMap.setMyLocationData(builder.build());
			
			LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
			//移动到定位位置
			baiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLng(latLng));
		}
		
	}

}

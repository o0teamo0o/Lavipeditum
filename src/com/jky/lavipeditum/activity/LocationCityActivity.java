package com.jky.lavipeditum.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jky.lavipeditum.R;
import com.jky.lavipeditum.adapter.LocationCityAdapter;
import com.jky.lavipeditum.base.BaseActivity;
import com.jky.lavipeditum.bean.CitySortModel;
import com.jky.lavipeditum.bean.Region;
import com.jky.lavipeditum.custom_view.ClearEditText;
import com.jky.lavipeditum.custom_view.SideBar;
import com.jky.lavipeditum.custom_view.SideBar.OnTouchingLetterChangedListener;
import com.jky.lavipeditum.engine.RegionInfoService;
import com.jky.lavipeditum.util.CharacterParser;
import com.jky.lavipeditum.util.CityPinyinComparator;
import com.jky.lavipeditum.util.Constants;

/**
 * 
 * @ClassName: LocationCityActivity 
 * @Description: 定位城市的界面

 * @author o0teamo0o
 * @date 2014年11月14日 上午9:22:34 
 *
 */
public class LocationCityActivity extends BaseActivity implements OnTouchingLetterChangedListener, OnItemClickListener {
	
	protected static final int LOAD_CITYS = 0;
	private ImageView iv_loading, iv_back;
	private Animation animation;
	private ArrayList<Region> regions;
	private ListView lv_citys;
	private ClearEditText cet_input_cityname;
	private CharacterParser characterParser;
	private CityPinyinComparator cityPinyinComparator;
	private SideBar sideBar;
	private TextView dialog, tv_error;
	private List<CitySortModel> sourceDateList;
	private LocationCityAdapter adapter;

	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.location_city);
		
		iv_loading = (ImageView) this.findViewById(R.id.iv_loading);
		iv_back = (ImageView) this.findViewById(R.id.iv_back);
		lv_citys = (ListView) this.findViewById(R.id.lv_citys);
		cet_input_cityname = (ClearEditText) this.findViewById(R.id.cet_input_cityname);
		sideBar = (SideBar) this.findViewById(R.id.sidrbar); //快捷查找条
		dialog = (TextView) this.findViewById(R.id.dialog);
		tv_error = (TextView) this.findViewById(R.id.tv_error);
	}

	@Override
	protected void initData() {
		characterParser = CharacterParser.getInstance(); //实例化汉字转换拼音类
		cityPinyinComparator = new CityPinyinComparator(); //处理名字中带"#,@"的情况
		
		sideBar.setTextView(dialog);
		animation = AnimationUtils.loadAnimation(LocationCityActivity.this, R.anim.common_loading_rotate);
		//开启动画
		iv_loading.setAnimation(animation);
		
		loadCitys();
	}

	@Override
	protected void initListener() {
		sideBar.setOnTouchingLetterChangedListener(this);
		lv_citys.setOnItemClickListener(this);
		
		iv_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				LocationCityActivity.this.finish();
			}
		});
		
		//监听输入的拼音和汉字定位
		cet_input_cityname.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				//当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
				filterData(s.toString());
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
	}
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case LOAD_CITYS:
				iv_loading.clearAnimation();
				iv_loading.setVisibility(View.GONE);
				sideBar.setVisibility(View.VISIBLE);
				
				if (regions != null && regions.size() > 0) {
					initCitysData();
				}else{
					tv_error.setVisibility(View.VISIBLE);
				}
				break;
			}
		};
	};
	
	/**
	 * 
	 * Title: loadCitys
	 * Description:加载所以城市信息
	 */
	private void loadCitys() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				String code = preferences.getProvinceRegionCode();
				RegionInfoService regionInfoService = new RegionInfoService(LocationCityActivity.this);
				regions = regionInfoService.getRegions(code);
				handler.sendEmptyMessage(LOAD_CITYS);
			}
		}).start();
	}
	
	/**
	 * 
	 * Title: initCitysData
	 * Description: 从集合中过滤出城市名称
	 */
	public void initCitysData(){
		String[] cityNames = new String[regions.size()];
		for(int i=0; i<regions.size(); i++){
			cityNames[i] = regions.get(i).getName();
		}
		
		sourceDateList = filledData(cityNames);
		
		//根据a-z进行排序源数据
		Collections.sort(sourceDateList, cityPinyinComparator);

		adapter = new LocationCityAdapter(LocationCityActivity.this, sourceDateList);
		lv_citys.setAdapter(adapter);
		lv_citys.setVisibility(View.VISIBLE);
	}
	
	/**
	 * 为ListView填充数据
	 * @param date 需要转换成首字母的字符串数组
	 * @return
	 */
	private List<CitySortModel> filledData(String [] date){
		List<CitySortModel> mSortList = new ArrayList<CitySortModel>();
		
		for(int i=0; i<date.length; i++){
			CitySortModel contactsSortModel = new CitySortModel();
			contactsSortModel.setName(date[i]);
			contactsSortModel.setId(regions.get(i).getId());
			contactsSortModel.setCode(regions.get(i).getCode());
			contactsSortModel.setLevel(regions.get(i).getLevel());
			contactsSortModel.setParentId(regions.get(i).getParentId());
			
			//汉字转换成拼音
			String pinyin = characterParser.getSelling(date[i]);
			String sortString = pinyin.substring(0, 1).toUpperCase();
			
			// 正则表达式，判断首字母是否是英文字母
			if(sortString.matches("[A-Z]")){
				contactsSortModel.setContactsSortLetters(sortString.toUpperCase());
			}else{
				contactsSortModel.setContactsSortLetters("#");
			}
			
			mSortList.add(contactsSortModel);
		}
		return mSortList;
	}
	
	/**
	 * 根据输入框中的值来过滤数据并更新ListView
	 * @param filterStr
	 */
	private void filterData(String filterStr){
		List<CitySortModel> filterDateList = new ArrayList<CitySortModel>();
		
		if(TextUtils.isEmpty(filterStr)){
			filterDateList = sourceDateList;
		}else{
			filterDateList.clear();
			for(CitySortModel sortModel : sourceDateList){
				String name = sortModel.getName();
				if(name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())){
					filterDateList.add(sortModel);
				}
			}
		}
		
		// 根据a-z进行排序
		Collections.sort(filterDateList, cityPinyinComparator);
		adapter.setRegions(filterDateList);
	}

	/**
	 * 设置右侧触摸监听
	 */
	@Override
	public void onTouchingLetterChanged(String s) {
		//该字母首次出现的位置
		int position = adapter.getPositionForSection(s.charAt(0));
		if(position != -1){
			lv_citys.setSelection(position);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		CitySortModel city = sourceDateList.get(position);
		
		Intent intent = new Intent();
		intent.putExtra("city", city);
		System.out.println("cityName:" + city.getName());
		setResult(Constants.LOCATION_CITY_RESULTCOCE, intent);
		
		LocationCityActivity.this.finish();
	}

}

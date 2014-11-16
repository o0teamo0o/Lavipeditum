package com.jky.lavipeditum.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 
 * @ClassName: LavipeditumPreferences 
 * @Description: 共享首选项
 *
 * @author o0teamo0o
 * @date 2014年10月21日 上午9:27:02 
 *
 */
public class LavipeditumPreferences {
	
	private SharedPreferences preferences;
	
	public LavipeditumPreferences(Context context) {
		preferences = context.getSharedPreferences(Constants.PREFERENCES_NAME, Context.MODE_PRIVATE);
	}
	
	/**
	 * 
	 * Title: setRegionCode
	 * Description: 保存当前选择的城市code
	 * @param code
	 */
	public void setCityRegionCode(String code){
		preferences.edit().putString("code", code).commit();
	}
	
	/**
	 * 
	 * Title: getRegionCode
	 * Description: 返回当前保存的城市code 默认是长沙的id
	 * @return
	 */
	public String getCityRegionCode(){
		return preferences.getString("code", "4301");
	}
	
	/**
	 * 
	 * Title: setCityName
	 * Description: 保存城市名称
	 * @param cityName
	 */
	public void setCityName(String cityName){
		preferences.edit().putString("cityName", cityName).commit();
	}
	
	/**
	 * 
	 * Title: getCityName
	 * Description: 获取保存的城市信息 
	 * @return
	 */
	public String getCityName(){
		return preferences.getString("cityName", "长沙");
	}
	
	/**
	 * 
	 * Title: getProvinceCode
	 * Description: 返回当前保存的省份code 默认是湖南省
	 * @return
	 */
	public String getProvinceRegionCode(){
		return preferences.getString("province", "43");
	}
	
}

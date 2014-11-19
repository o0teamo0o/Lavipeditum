package com.jky.lavipeditum.bean;

import java.io.Serializable;

/**
 * 
 * @ClassName: CurrentRegion 
 * @Description: 当前被选中的城市信息
 *
 * @author o0teamo0o
 * @date 2014年11月19日 下午10:23:57 
 *
 */
public class CurrentRegion implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String city; //城市
	private String region; //区域
	private String streen; //街道
	
	public CurrentRegion() {
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getStreen() {
		return streen;
	}

	public void setStreen(String streen) {
		this.streen = streen;
	}
}

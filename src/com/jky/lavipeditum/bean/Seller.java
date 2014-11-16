package com.jky.lavipeditum.bean;

import java.io.Serializable;

/**
 * 
 * @ClassName: Seller 
 * @Description: 足浴通商家 实体bean

 * @author o0teamo0o
 * @date 2014年11月16日 下午2:58:09 
 *
 */
public class Seller implements Serializable{

	private static final long serialVersionUID = 1L;
	private int id; //商铺id
	private String sellerName; //卖家昵称
	private String shopName; //店铺名称
	private String QQ; //联系QQ
	private String phone; //联系电话
	private String email; //邮箱
	private String city; // 城市  三级联动 城市 区域 街道
	private String address; //详细地址
	private double latitude; //店铺维度
	private double longitude; //店铺经度
	
	public Seller() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSellerName() {
		return sellerName;
	}

	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getQQ() {
		return QQ;
	}

	public void setQQ(String qQ) {
		QQ = qQ;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
}

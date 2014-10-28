package com.jky.lavipeditum.bean;

/**
 * 
 * @ClassName: ContactsSortModel
 * @Description: 联系人分类实体类,主要用于排序
 *
 * @author o0teamo0o
 * @date 2014年10月29日 上午1:18:39
 */
public class ContactsSortModel {

	private String name; //联系人名字
	private String contactsSortLetters; //联系人拼音的首字母
	private String phone; 
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getContactsSortLetters() {
		return contactsSortLetters;
	}
	
	public void setContactsSortLetters(String contactsSortLetters) {
		this.contactsSortLetters = contactsSortLetters;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
}

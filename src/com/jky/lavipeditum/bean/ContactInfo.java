package com.jky.lavipeditum.bean;

/**
 * 
 * @ClassName: ContactInfo
 * @Description: 手机联系人实体bean
 *
 * @author o0teamo0o
 * @date 2014年10月29日 上午12:54:19
 */
public class ContactInfo {
	private String name; //联系人名称
	private String phone; //联系人电话
	
	public ContactInfo() {

	}

	public ContactInfo( String name, String phone) {
		super();
		this.name = name;
		this.phone = phone;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public String toString() {
		return "ContactInfo [name=" + name + ", phone=" + phone + "]";
	}
}

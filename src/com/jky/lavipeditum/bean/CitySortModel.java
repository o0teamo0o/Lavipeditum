package com.jky.lavipeditum.bean;

import java.io.Serializable;

/**
 * 
 * @ClassName: CitySortModel
 * @Description: 城市分类实体类,主要用于排序
 *
 * @author o0teamo0o
 * @date 2014年10月29日 上午1:18:39
 */
public class CitySortModel implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String name; //城市名字
	private String contactsSortLetters; //城市拼音的首字母
	private int id; //id 无太大用处
	private String code; //编码
	private String parentId; //用于查询的主要条件
	private int level; //级别 1.省份 2.市级 3.区级 4.县级
	
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	
}

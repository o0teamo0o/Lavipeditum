package com.jky.lavipeditum.bean;

import java.io.Serializable;

/**
 * 
 * @ClassName: Region 
 * @Description: 区域实体bean

 * @author o0teamo0o
 * @date 2014年11月13日 下午11:12:55 
 *
 */
public class Region implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int id; //id 无太大用处
	private String code; //编码
	private String parentId; //用于查询的主要条件
	private String name; //名字
	private int level; //级别 1.省份 2.市级 3.区级 4.县级
	
	public Region() {
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

}

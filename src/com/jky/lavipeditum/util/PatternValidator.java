package com.jky.lavipeditum.util;

import java.util.regex.Pattern;

import android.widget.EditText;

/**
 * 
 * @ClassName: PatternValidator
 * @Description: 通用匹配器,构造传入匹配器,可以执行匹配
 *
 * @author o0teamo0o
 * @date 2014年11月3日 下午3:18:52
 */
public class PatternValidator {

	private Pattern pattern;
	private String message;
	
	/**
	 * 构造传参
	 * @param pattern
	 */
	public PatternValidator(String message, Pattern pattern) {
		if (pattern == null) {
			throw new IllegalArgumentException("pattern must not be null");
		}
		this.pattern = pattern;
		this.message = message;
	}
	
	/**
	 * 
	 * @Title: isValid
	 * @Description: 通过传过来message 匹配一下
	 * @return
	 */
	public boolean isValid(){
		return pattern.matcher(message).matches();
	}
}

package com.jky.lavipeditum.util;

import java.util.regex.Pattern;

import android.os.Build;
import android.util.Patterns;

/**
 * 
 * @ClassName: EmailValidator 
 * @Description: 邮箱验证匹配器

 * @author o0teamo0o
 * @date 2014年11月18日 下午4:18:42 
 *
 */
public class EmailValidator extends PatternValidator{
	
	public EmailValidator(String _customErrorMessage) {
		super(_customErrorMessage, Build.VERSION.SDK_INT >= 8?Patterns.EMAIL_ADDRESS:Pattern.compile(
	            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
	            "\\@" +
	            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
	            "(" +
	                "\\." +
	                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
	            ")+"
	        ));
	}
}
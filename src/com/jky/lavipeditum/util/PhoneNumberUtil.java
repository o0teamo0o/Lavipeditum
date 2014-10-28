package com.jky.lavipeditum.util;

/**
 * 
 * @ClassName: PhoneNumberUtil
 * @Description: 电话号码工具类,用来格式化电话号码
 *
 * @author o0teamo0o
 * @date 2014年10月28日 下午11:58:06
 */
public class PhoneNumberUtil {
	
	/**
	 * 
	 * @Title: bridgingCallNumber
	 * @Description: 把电话号码组成桥接形式 如 138 3838 3838
	 * @param number 电话号码
	 * @return
	 */
	public static String bridgingCallNumber(String number){
		String numberHead = number.substring(0, 3);
		String numberBody = number.substring(3, 7);
		String numberTail = number.substring(7, number.length());
		return numberHead + " " + numberBody + " " + numberTail;
	}
	
	/**
	 * 
	 * @Title: restoreCallNumber
	 * @Description: 把桥接的电话号码重新拼接
	 * @param number 电话号码
	 * @return
	 */
	public static String restoreCallNumber(String number){
		String numberHead = number.substring(0, 3);
		String numberBody = number.substring(4, 8);
		String numberTail = number.substring(9, number.length());
		return numberHead + numberBody + numberTail;
	}
}

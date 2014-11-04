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
		//如果号码只有4位数 直接返回
		if (number.length() <= 4) {
			return number;
		}
		//如果读出来的手机号码是这种形式 1-864-334-1134需要特殊处理
		else if (number.contains("-")) {
			String phoneNumber = "";
			String[] split = number.split("-");
			for (int i = 0; i < split.length; i++) {
				Logger.d(PhoneNumberUtil.class, split[i]);
				phoneNumber = phoneNumber + split[i] + " ";
			}
			return phoneNumber;
		}else{
			String numberHead = number.substring(0, 3);
			String numberBody = number.substring(3, 7);
			String numberTail = number.substring(7, number.length());
			return numberHead + " " + numberBody + " " + numberTail;
		}
	}
	
	/**
	 * 
	 * @Title: restoreCallNumber
	 * @Description: 把桥接的电话号码重新拼接
	 * @param number 电话号码
	 * @return
	 */
	public static String restoreCallNumber(String number){
		//如果号码只有4位数 直接返回
		if (number.length() <= 4) {
			return number;
		}else{
			String numberHead = number.substring(0, 3);
			String numberBody;
			try {
				numberBody = number.substring(4, 8);
			} catch (Exception e) {
				return number;
			}
			String numberTail;
			try {
				numberTail = number.substring(9, number.length());
			} catch (Exception e) {
				return numberHead + numberBody;
			}
			return numberHead + numberBody + numberTail;
		}
	}
}

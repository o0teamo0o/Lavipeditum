package com.jky.lavipeditum.util;

import android.util.Log;

/**
 * 
 * @ClassName: Logger 
 * @Description: 管理log日志类
 *
 * @author o0teamo0o
 * @date 2014年10月23日 上午9:36:09 
 *
 */
@SuppressWarnings("rawtypes")
public class Logger {
	
	//日志级别，显示级别，参考android.util.Log的级别,　配置０全部显示，配置大于７全不显示
	public static final int LEVLE = 2;
	
	public static void v(Class clazz, String msg){
		if (LEVLE <= Log.VERBOSE) {
			Log.v(clazz.getSimpleName(), msg);
		}
	}
	
	public static void v(Class clazz, String msg, Throwable tr){
		if (LEVLE <= Log.VERBOSE) {
			Log.v(clazz.getSimpleName(), msg, tr);
		}
	}
	
	public static void d(Class clazz, String msg){
		if (LEVLE <= Log.DEBUG) {
			Log.d(clazz.getSimpleName(), msg);
		}
	}
	
	public static void d(Class clazz, String msg, Throwable tr){
		if (LEVLE <= Log.DEBUG) {
			Log.d(clazz.getSimpleName(), msg, tr);
		}
	}
	
	public static void i(Class clazz, String msg) {
		if (LEVLE <= Log.INFO)
			Logger.d(clazz, msg);
	}

	public static void i(Class clazz, String msg, Throwable tr) {
		if (LEVLE <= Log.INFO)
			Logger.d(clazz, msg, tr);
	}

	public static void w(Class clazz, String msg) {
		if (LEVLE <= Log.WARN)
			Log.w(clazz.getSimpleName(), msg);
	}

	public static void w(Class clazz, String msg, Throwable tr) {
		if (LEVLE <= Log.WARN)
			Log.w(clazz.getSimpleName(), msg, tr);
	}

	public static void w(Class clazz, Throwable tr) {
		if (LEVLE <= Log.WARN)
			Log.w(clazz.getSimpleName(), tr.getMessage(), tr);
	}

	public static void e(Class clazz, String msg) {
		if (LEVLE <= Log.ERROR)
			Log.e(clazz.getSimpleName(), msg);
	}

	public static void e(Class clazz, String msg, Throwable tr) {
		if (LEVLE <= Log.ERROR)
			Log.e(clazz.getSimpleName(), msg, tr);
	}

	public static void e(Class clazz, Throwable tr) {
		if (LEVLE <= Log.ERROR)
			Log.e(clazz.getSimpleName(), tr.getMessage(), tr);
	}

	public static void wtf(Class clazz, String msg) {
		if (LEVLE <= Log.ASSERT)
			Log.wtf(clazz.getSimpleName(), msg);
	}

	public static void wtf(Class clazz, Throwable tr) {
		if (LEVLE <= Log.ASSERT)
			Log.wtf(clazz.getSimpleName(), tr);
	}

	public static void wtf(Class clazz, String msg, Throwable tr) {
		if (LEVLE <= Log.ASSERT)
			Logger.wtf(clazz, msg, tr);
	}
}

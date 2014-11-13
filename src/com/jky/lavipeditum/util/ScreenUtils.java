package com.jky.lavipeditum.util;

import android.app.Activity;
import android.util.DisplayMetrics;

/**
 * 
 * @ClassName: ScreenUtils 
 * @Description: 获取屏幕像素相关的工具类
 * @author o0teamo0o
 *
 * @date 2014年11月13日 上午11:51:36 
 *
 */
public class ScreenUtils {

	private static int screenWidth; //屏幕的宽
    private static int screenHeight; //屏幕的高
    
    /**
     * 
     * Title: getScreenW
     * Description: 返回手机屏幕的宽
     * @param mActivity
     * @return
     */
    public static int getScreenWidth(Activity mActivity){
        if (screenWidth == 0){
            initScreen(mActivity);
        }
        return screenWidth;
    }
    
    /**
     * 
     * Title: getScreenH
     * Description: 返回手机屏幕的高
     * @param mActivity
     * @return
     */
    public static int getScreenHeight(Activity mActivity){
        if (screenHeight == 0){
            initScreen(mActivity);
        }
        return screenHeight;
    }
    
    /**
     * 
     * Title: initScreen
     * Description: 得到当前手机的屏幕的宽和高
     * @param mActivity
     */
    private static void initScreen(Activity mActivity){
        DisplayMetrics metric = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        screenWidth = metric.widthPixels;
        screenHeight = metric.heightPixels;
    }
}

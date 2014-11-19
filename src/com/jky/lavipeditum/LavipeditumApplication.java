package com.jky.lavipeditum;

import com.baidu.mapapi.SDKInitializer;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import android.app.Application;
import android.content.Context;

/**
 *  
 * @ClassName: LavipeditumApplication 
 * @Description:全局Application
 *
 * @author o0teamo0o
 * @date 2014年10月19日 下午10:05:16 
 *
 */
public class LavipeditumApplication extends Application {
	
	//判断是否登陆
	public static boolean isLogin = false;
	public static boolean isSellerLogin = false;
	//判断网络是否正常
	public static boolean isNetWork = true;

	@Override
	public void onCreate() {
		super.onCreate();
		
		initImageLoader(getApplicationContext());
		
		initBaiduMap();
	}

	/**
	 * 
	 * @Title: initImageLoader 
	 * @Description: 初始化ImageLoader
	 * @param applicationContext
	 */
	private void initImageLoader(Context context) {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
		.threadPriority(Thread.NORM_PRIORITY - 2) //设置线程的优先级
		.denyCacheImageMultipleSizesInMemory() //同一个Uri获取不同大小的图片，缓存到内存时，只缓存一个。默认会缓存多个不同的大小的相同图片
		.diskCacheFileNameGenerator(new Md5FileNameGenerator()) //被缓存文件的名字会用MD5加密处理
		.diskCacheSize(50 * 1024 * 1024) // 设置缓存大小50 Mb
		.tasksProcessingOrder(QueueProcessingType.LIFO)
		.writeDebugLogs() // Remove for release app
		.build();
		
		ImageLoader.getInstance().init(config);
	}
	
	/**
	 * 
	 * Title: initBaiduMap
	 * Description: 初始化百度地图
	 */
	private void initBaiduMap() {
		// 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
		SDKInitializer.initialize(this);
	}

}

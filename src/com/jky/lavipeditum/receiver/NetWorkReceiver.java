package com.jky.lavipeditum.receiver;

import com.jky.lavipeditum.LavipeditumApplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 
 * @ClassName: NetWorkReceiver
 * @Description: 检查网络的广播
 *
 * @author o0teamo0o
 * @date 2014年11月3日 下午4:31:41
 */
public class NetWorkReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		System.out.println("广播启动");
		if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
			//得到网络连接管理者
			ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			//得到你当前活动的网络对象
			NetworkInfo networkInfo = manager.getActiveNetworkInfo();
			
			//网络正常是可以正常工作的
			if (networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected()) {
				LavipeditumApplication.isNetWork = true;
				System.out.println("当前网络可用!");
			}else{
				LavipeditumApplication.isNetWork = false;
				System.out.println("当前网络不可用!");
			}
		}
	}

}

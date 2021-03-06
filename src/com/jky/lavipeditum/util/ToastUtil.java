package com.jky.lavipeditum.util;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

/**
 * 
 * @ClassName: ToastUtil
 * @Description: 自定义ToastUtil,重写系统的toast,可以不停的弹出
 *
 * @author o0teamo0o
 * @date 2014年10月27日 上午10:41:30
 */
public class ToastUtil {
	private static Handler handler = new Handler(Looper.getMainLooper());

	private static Toast toast = null;

	private static Object synObj = new Object();

	public static void showMessage(final Context act, final String msg) {
		showMessage(act, msg, Toast.LENGTH_SHORT);
	}

	public static void showMessage(final Context act, final String msg,
			final int len) {
		new Thread(new Runnable() {
			public void run() {
				handler.post(new Runnable() {
					@Override
					public void run() {
						synchronized (synObj) {
							if (toast != null) {
								toast.cancel();
								toast.setText(msg);
								toast.setDuration(len);
							} else {
								toast = Toast.makeText(act, msg, len);
							}
							toast.show();
						}
					}
				});
			}
		}).start();
	}
}

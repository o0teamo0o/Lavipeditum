package com.jky.lavipeditum.lib.tencent;


import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.jky.lavipeditum.util.Logger;
import com.jky.lavipeditum.util.ToastUtil;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

/**
 * 
 * @ClassName: BaseUIListener
 * @Description: 腾讯读取用户信息所需要用到的回调监听
 *
 * @author o0teamo0o
 * @date 2014年10月27日 下午11:18:53
 */
public class BaseUIListener implements IUiListener {
	private Context mContext;
	private String mScope;
	private boolean mIsCaneled;
	private static final int ON_COMPLETE = 0;
	private static final int ON_ERROR = 1;
	private static final int ON_CANCEL = 2;
	private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            //读取用户信息成功的回调消息
            case ON_COMPLETE:
                JSONObject response = (JSONObject)msg.obj;
                Logger.d(BaseUIListener.class, "用户信息读取成功:" + response.toString());
                ToastUtil.showMessage(mContext, "用户信息读取成功:"+response.toString());
                break;
            //读取用户信息错误的回调消息
            case ON_ERROR:
                UiError e = (UiError)msg.obj;
                Logger.d(BaseUIListener.class, "用户信息读取失败:" + "errorMsg:" + e.errorMessage
                        + "errorDetail:" + e.errorDetail);
                ToastUtil.showMessage(mContext, "用户信息读取失败:" + "errorMsg:" + e.errorMessage
                        + "errorDetail:" + e.errorDetail);
                break;
            //取消读取用户信息的回调
            case ON_CANCEL:
            	Logger.d(BaseUIListener.class, "取消读取用户信息!");
            	ToastUtil.showMessage(mContext, "取消读取用户信息!");
                break;
            }
        }	    
	};
	
	public BaseUIListener(Context mContext) {
		super();
		this.mContext = mContext;
	}

	
	public BaseUIListener(Context mContext, String mScope) {
		super();
		this.mContext = mContext;
		this.mScope = mScope;
	}
	
	public void cancel() {
		mIsCaneled = true;
	}


	@Override
	public void onComplete(Object response) {
		if (mIsCaneled) return;
	    Message msg = mHandler.obtainMessage();
	    msg.what = ON_COMPLETE;
	    msg.obj = response;
	    mHandler.sendMessage(msg);
	}

	@Override
	public void onError(UiError e) {
		if (mIsCaneled) return;
	    Message msg = mHandler.obtainMessage();
        msg.what = ON_ERROR;
        msg.obj = e;
        mHandler.sendMessage(msg);
	}

	@Override
	public void onCancel() {
		if (mIsCaneled) return;
	    Message msg = mHandler.obtainMessage();
        msg.what = ON_CANCEL;
        mHandler.sendMessage(msg);
	}

	public Context getmContext() {
		return mContext;
	}

	public void setmContext(Context mContext) {
		this.mContext = mContext;
	}

}


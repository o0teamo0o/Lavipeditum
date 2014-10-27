package com.jky.lavipeditum.util;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 
 * @ClassName: WeiboAccessTokenKeeper
 * @Description: 对Token进行保存操作的共享首先项
 *
 * @author o0teamo0o
 * @date 2014年10月27日 下午5:13:28
 */
public class WeiboAccessTokenKeeper {
	
	private SharedPreferences preferences;

	public WeiboAccessTokenKeeper(Context context) {
		preferences = context.getSharedPreferences(Constants.WEIBO_PREFERENCE_NAME, Context.MODE_APPEND);
	}
	
	/**
	 * 
	 * @Title: setAccessToken
	 * @Description: 保存Token 对象 到共享首选项
	 * @param token Token 对象
	 */
	public void setAccessToken(Oauth2AccessToken token){
		Editor edit = preferences.edit();
		edit.putString(Constants.WEIBO_KEY_UID, token.getUid());
		edit.putString(Constants.WEIBO_KEY_ACCESS_TOKEN, token.getToken());
		edit.putLong(Constants.WEIBO_KEY_EXPIRES_IN, token.getExpiresTime());
		edit.commit();
	}
	
	/**
	 * 
	 * @Title: getAccessToken
	 * @Description: 从共享首选项 读取Token信息
	 * @return Token对象
	 */
	public Oauth2AccessToken getAccessToken(){
		Oauth2AccessToken token = new Oauth2AccessToken();
		token.setUid(preferences.getString(Constants.WEIBO_KEY_UID,  ""));
		token.setToken(preferences.getString(Constants.WEIBO_KEY_ACCESS_TOKEN, ""));
		token.setExpiresTime(preferences.getLong(Constants.WEIBO_KEY_EXPIRES_IN, 0));
		return token;
	}
	
	/**
	 * 
	 * @Title: clearAccessToken
	 * @Description: 清空共享首选项的Token信息
	 */
	public void clearAccessToken(){
		Editor edit = preferences.edit();
		edit.clear();
		edit.commit();
	}
}

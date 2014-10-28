package com.jky.lavipeditum.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 
 * @ClassName: LavipeditumPreferences 
 * @Description: 共享首选项
 *
 * @author o0teamo0o
 * @date 2014年10月21日 上午9:27:02 
 *
 */
public class LavipeditumPreferences {
	
	private SharedPreferences preferences;
	
	public LavipeditumPreferences(Context context) {
		preferences = context.getSharedPreferences(Constants.PREFERENCES_NAME, Context.MODE_PRIVATE);
	}
}

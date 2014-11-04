package com.jky.lavipeditum.util;

import java.util.regex.Pattern;

import android.os.Build;
import android.util.Patterns;

/**
 * 
 * @ClassName: PhoneValidator
 * @Description: 电话号码匹配器
 *
 * @author o0teamo0o
 * @date 2014年11月3日 下午3:22:02
 */
public class PhoneValidator extends PatternValidator {

	public PhoneValidator(String phoneNumber) {
		super(phoneNumber, Build.VERSION.SDK_INT >= 8 ? Patterns.PHONE: Pattern.compile(
				"(\\+[0-9]+[\\- \\.]*)?"                    // +<digits><sdd>*
				+ "(\\([0-9]+\\)[\\- \\.]*)?"               // (<digits>)<sdd>*
				+ "([0-9][0-9\\- \\.][0-9\\- \\.]+[0-9])"));
	}

}

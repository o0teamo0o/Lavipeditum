package com.jky.lavipeditum.util;

import java.util.Comparator;

import com.jky.lavipeditum.bean.CitySortModel;
import com.jky.lavipeditum.bean.ContactsSortModel;

/**
 * 
 * @ClassName: CityPinyinComparator
 * @Description: 处理联系人名字中带"#,@"的情况
 *
 * @author o0teamo0o
 * @date 2014年10月29日 上午1:19:15
 */
public class CityPinyinComparator implements Comparator<CitySortModel> {

	@Override
	public int compare(CitySortModel o1, CitySortModel o2) {
		if(o1.getContactsSortLetters().equals("@") || o2.getContactsSortLetters().equals("#")){
			return -1;
		}else if( o1.getContactsSortLetters().equals("#") || o2.getContactsSortLetters().equals("@")){
			return 1;
		}else{
			return o1.getContactsSortLetters().compareTo(o2.getContactsSortLetters());
		}
	}
}

package com.jky.lavipeditum.engine;


import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.jky.lavipeditum.bean.ContactInfo;

/**
 * 
 * @ClassName: ContactInfoService
 * @Description: 查询手机联系人业务类
 *
 * @author o0teamo0o
 * @date 2014年10月29日 上午12:59:59
 */
public class ContactInfoService {

	private Context context;

	public ContactInfoService(Context context){
		this.context = context;
	}
	
	/**
	 * 获取系统通讯录联系人信息
	 * @return
	 */
	public List<ContactInfo> getContactInfos(){
		ContentResolver resolver = context.getContentResolver();
		//1.获得联系人的ID
		//2.根据联系人的ID获取联系人姓名
		//3.根据联系人的ID数据的type 得到对应的数据(电话,email)
		
		//声明一个集合用来存放联系人
		List<ContactInfo> infos = new ArrayList<ContactInfo>();
		ContactInfo info = null;
		
		Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
		//data表的Uri
		Uri dataUri = Uri.parse("content://com.android.contacts/data");
		Cursor cursor = resolver.query(uri, null, null, null, null);
		//如果查询的raw_context表中有数据
		while(cursor.moveToNext()){
			
			//判断是否是被删除的记录
			String deleted = cursor.getString(cursor.getColumnIndex("deleted"));
			if(deleted.equals("1")){ 
				continue;
			}
			String id = cursor.getString(cursor.getColumnIndex("_id"));
			info = new ContactInfo();
			//联系人姓名
			String name = cursor.getString(cursor.getColumnIndex("display_name"));
			//设置联系人姓名
			info.setName(name);
			//查询data表
			Cursor datacursor = resolver.query(dataUri, null, "raw_contact_id=?", new String[]{id}, null);
			while(datacursor.moveToNext()){
				//获取mimetype
				String type = datacursor.getString(datacursor.getColumnIndex("mimetype"));
				//如果是电话号码类型
				if("vnd.android.cursor.item/phone_v2".equals(type)){
					String number = datacursor.getString(datacursor.getColumnIndex("data1"));
					//遍历集合看是否有重复
					for (ContactInfo ContactInfo : infos) {
						if(ContactInfo.getPhone().equals(number)){
							info = null;
							break;
						}
					}
					if(info != null){
						info.setPhone(number);
					}
				}
			}
			datacursor.close();
			if(info != null){
				infos.add(info);
			}
			info = null;
		}
		cursor.close();
		return infos;
	}
}
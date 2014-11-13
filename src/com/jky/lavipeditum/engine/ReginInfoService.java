package com.jky.lavipeditum.engine;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jky.lavipeditum.bean.Region;
import com.jky.lavipeditum.db.CreateLocalDB;

/**
 * 
 * @ClassName: ReginInfoService 
 * @Description: 地区城市街道查询

 * @author o0teamo0o
 * @date 2014年11月13日 下午11:04:58 
 *
 */
public class ReginInfoService {

	private SQLiteDatabase db;
	
	public ReginInfoService(Context context) {
		this.db = new CreateLocalDB().openDatabase(context);
	}
	
	/**
	 * 
	 * Title: getZones
	 * Description: 返回指定城市所对应的区域街道
	 * @param parentId 城市code
	 * @return
	 */
	public ArrayList<Region> getRegions(String code){
		ArrayList<Region> regions = null;
		Cursor cursor = db.rawQuery("select * from hunan where parentId = ?", new String[]{code});
		if (cursor != null && cursor.getColumnCount() > 0) {
			regions = new ArrayList<Region>();
			while (cursor.moveToNext()) {
				Region region = new Region();
				region.setId(cursor.getInt(cursor.getColumnIndex("id")));
				region.setCode(cursor.getString(cursor.getColumnIndex("code")));
				region.setParentId(cursor.getString(cursor.getColumnIndex("parentId")));
				region.setName(cursor.getString(cursor.getColumnIndex("name")));
				region.setLevel(cursor.getInt(cursor.getColumnIndex("level")));
				regions.add(region);
			}
		}
		cursor.close();
		db.close();
		return regions;
	}
}

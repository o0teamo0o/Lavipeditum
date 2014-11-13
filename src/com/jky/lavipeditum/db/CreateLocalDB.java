package com.jky.lavipeditum.db;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import com.jky.lavipeditum.util.Constants;
import com.jky.lavipeditum.util.Logger;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;

/**
 * 
 * @ClassName: CreateLocalDB 
 * @Description: 创建本地数据库，目的就是从accets目录下的读取数据库然后写入到安装目录下
 * 
 * @author o0teamo0o
 * @date 2014年11月13日 下午10:27:53 
 *
 */
public class CreateLocalDB {

	public SQLiteDatabase openDatabase(Context context){
		File jhPath = new File(Constants.FILEPATH);
		//查看数据库文件是否存在
		if (jhPath.exists()) {
			Logger.d(CreateLocalDB.class, "数据库已经存在!");
			return SQLiteDatabase.openOrCreateDatabase(jhPath, null);
		}else{
			//不存在 先创建文件夹
			File path = new File(Constants.PATHSTR);
			Logger.d(CreateLocalDB.class, "pathStr:" + path);
			if (path.mkdir()) {
				Logger.d(CreateLocalDB.class, "保存数据库的文件夹创建成功！");
			}else{
				Logger.d(CreateLocalDB.class, "保存数据库的文件夹创建失败！");
			}
			
			try {
				//得到资源
				AssetManager am = context.getAssets();
				//得到数据库的输入流
				InputStream is = am.open("lavipeditum_region.db");
				//用输出流写到安装目录下
				FileOutputStream fos = new FileOutputStream(jhPath);
				//创建byte数组 用于1KB 写一次
				byte[] buffer = new byte[1024];
				int count = 0;
				while ((count = is.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
				}
				//最后关闭资源
				fos.flush();
				fos.close();
				is.close();
			} catch (Exception e) {
				return null;
			}
			
			// 如果没有这个数据库，我们利用递归再次调用自己创建数据库
			return openDatabase(context);
		}
	}
}

package com.jky.lavipeditum.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * 
 * @ClassName: ImageUtils
 * @Description: 腾讯登陆时需要处理头像图片的工具类
 *
 * @author o0teamo0o
 * @date 2014年10月27日 下午11:18:23
 */
public class ImageUtils {

	/**
	 * 根据一个网络连接(String)获取bitmap图像
	 * 
	 * @param imageUri
	 * @return
	 * @throws MalformedURLException
	 */
	public static Bitmap getbitmap(String imageUri) {
		Logger.d(ImageUtils.class, "getbitmap:" + imageUri);
		// 显示网络上的图片
		Bitmap bitmap = null;
		try {
			URL myFileUrl = new URL(imageUri);
			HttpURLConnection conn = (HttpURLConnection) myFileUrl
					.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();

			Logger.d(ImageUtils.class, "image download finished." + imageUri);
		} catch (IOException e) {
			e.printStackTrace();
			Logger.d(ImageUtils.class, "getbitmap bmp fail---");
			return null;
		}
		return bitmap;
	}
	
	/**
     * 以最省内存的方式读取图片
     */
    public static Bitmap readBitmap(final String path){
        try{
            FileInputStream stream = new FileInputStream(new File(path+"test.jpg"));
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inSampleSize = 8;
            opts.inPurgeable=true;
            opts.inInputShareable=true;
            Bitmap bitmap = BitmapFactory.decodeStream(stream , null, opts);
            return bitmap;
        }
        catch (Exception e){
            return null;
        }
    }
}

package com.jky.lavipeditum.base;

/**
 * 
 * @ClassName: BaseUserInfo
 * @Description: 本地数据的用户信息
 *
 * @author o0teamo0o
 * @date 2014年10月28日 上午10:33:11
 */
public class BaseUserInfo {

	public int id; //用户id 主键 唯一标示 客户端不做处理 表字段自增的数据,查询时可能用的到 多级表查询时用此字段查询
	public String nickname; //用户昵称  微博 /腾讯都有
	public String profile_image_url; //用户头像  微博 / 腾讯都有  
	public String uid; //微博的用户唯一标示
	public String openId; //腾讯的用户唯一标示与QQ号一一对应
	public String phone; //电话号码 本地服务器唯一标示  非常重要的属性 ,牵扯到支付功能 还有短信验证 本地客户端注册就是通过手机号码注册的
	public String created_at; //注册时间 以服务器系统时间为准 手机客户端这边不做处理
	public String gender; //性别
	
}

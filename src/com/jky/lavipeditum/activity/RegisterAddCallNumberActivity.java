package com.jky.lavipeditum.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jky.lavipeditum.R;
import com.jky.lavipeditum.adapter.ContactsNumberAdapter;
import com.jky.lavipeditum.base.BaseActivity;
import com.jky.lavipeditum.bean.ContactInfo;
import com.jky.lavipeditum.bean.ContactsSortModel;
import com.jky.lavipeditum.custom_view.SideBar;
import com.jky.lavipeditum.custom_view.SideBar.OnTouchingLetterChangedListener;
import com.jky.lavipeditum.engine.ContactInfoService;
import com.jky.lavipeditum.util.CharacterParser;
import com.jky.lavipeditum.util.Constants;
import com.jky.lavipeditum.util.ContactPinyinComparator;
import com.jky.lavipeditum.util.Logger;

/**
 * 
 * @ClassName: RegisterAddCallNumberActivity
 * @Description: 添加联系人电话号码页面
 *
 * @author o0teamo0o
 * @date 2014年10月29日 上午12:10:06
 */
public class RegisterAddCallNumberActivity extends BaseActivity implements OnClickListener, OnTouchingLetterChangedListener, OnItemClickListener {

	private ImageView iv_loading_outside, iv_back;
	private Timer timer;
	private Animation animation;
	private List<ContactInfo> infos;
	private TextView tv_contacts_null;
	private ListView contactsListView;
	private SideBar sideBar;
	private TextView dialog;
	private List<ContactsSortModel> sourceDateList;
	private CharacterParser characterParser;
	private ContactPinyinComparator contactPinyinComparator;
	private ContactsNumberAdapter adapter;
	
	private Handler handler = new Handler(){
		public void dispatchMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				//设置动画多少毫秒完成
				animation.setDuration(1000);
				//讲动画引用到iv控件上
				iv_loading_outside.startAnimation(animation);
				break;
			case 1:
				//停止定时
				timer.cancel();
				//停止动画
				iv_loading_outside.clearAnimation();
				//隐藏视图
				iv_loading_outside.setVisibility(View.GONE);
				
				//判断通讯录是否是空的
				if(infos.size() == 0){
					tv_contacts_null.setVisibility(View.VISIBLE);
				}else{
					//显示视图
					contactsListView.setVisibility(View.VISIBLE);
					sideBar.setVisibility(View.VISIBLE);
					//初始化联系人数据
					initConstactsData();
				}
				break;
			}
			
		};
	};

	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.register_add_callnumber);
		
		iv_loading_outside = (ImageView) findViewById(R.id.iv_loading_outside); //等待进度条
		iv_back = (ImageView) this.findViewById(R.id.iv_back);
		sideBar = (SideBar) findViewById(R.id.sidrbar); //快捷查找条
		dialog = (TextView) findViewById(R.id.dialog);
		sideBar.setTextView(dialog);
		contactsListView = (ListView) findViewById(R.id.country_lvcountry); //ListView
		tv_contacts_null = (TextView) this.findViewById(R.id.tv_contacts_null); //通讯录为空的视图
		
	}

	protected void initConstactsData() {
		//从集合中把联系人名字过滤出来
		String[] contactNames = new String[infos.size()];
		for(int i=0; i<infos.size(); i++){
			contactNames[i] = infos.get(i).getName();
		}
		
		sourceDateList = filledData(contactNames);
		
		//根据a-z进行排序源数据
		Collections.sort(sourceDateList, contactPinyinComparator);
		adapter = new ContactsNumberAdapter(this, sourceDateList);
		contactsListView.setAdapter(adapter);
	}

	@Override
	protected void initData() {
		characterParser = CharacterParser.getInstance(); //实例化汉字转换拼音类
		contactPinyinComparator = new ContactPinyinComparator(); //处理名字中带"#,@"的情况
		
		lodingAnimation();
		lodingContactsInfo();
	}
	
	@Override
	protected void initListener() {
		iv_back.setOnClickListener(this);
		sideBar.setOnTouchingLetterChangedListener(this);
		contactsListView.setOnItemClickListener(this);
	}
	
	/**
	 * 为ListView填充数据
	 * @param date 需要转换成首字母的字符串数组
	 * @return
	 */
	private List<ContactsSortModel> filledData(String [] date){
		List<ContactsSortModel> mSortList = new ArrayList<ContactsSortModel>();
		
		for(int i=0; i<date.length; i++){
			ContactsSortModel ContactsSortModel = new ContactsSortModel();
			ContactsSortModel.setName(date[i]);
			ContactsSortModel.setPhone(infos.get(i).getPhone());
			//汉字转换成拼音
			String pinyin = characterParser.getSelling(date[i]);
			String sortString = pinyin.substring(0, 1).toUpperCase();
			
			// 正则表达式，判断首字母是否是英文字母
			if(sortString.matches("[A-Z]")){
				ContactsSortModel.setContactsSortLetters(sortString.toUpperCase());
			}else{
				ContactsSortModel.setContactsSortLetters("#");
			}
			
			mSortList.add(ContactsSortModel);
		}
		return mSortList;
		
	}

	/**
	 * 
	 * @Title: lodingAnimation
	 * @Description: 显示等待动画
	 */
	private void lodingAnimation() {
		animation = AnimationUtils.loadAnimation(this, R.anim.common_loading_rotate);
		//开启动画
		iv_loading_outside.startAnimation(animation);
		timer = new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				//通知视图等待视图动画
				handler.sendEmptyMessage(0);
			}
		}, 0, 1000);
	}
	
	/**
	 * 
	 * @Title: lodingContactsInfo
	 * @Description: 加载手机联系人信息
	 */
	private void lodingContactsInfo() {
		//开启一个线程去得到联系人信息
		new Thread(){
			public void run() {
				//得到联系人集合
				ContactInfoService service = new ContactInfoService(RegisterAddCallNumberActivity.this);
				infos = service.getContactInfos();
				//通知等待动画关闭
				handler.sendEmptyMessage(1);
			};
		}.start();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		//返回按钮
		case R.id.iv_back:
			RegisterAddCallNumberActivity.this.finish();
			break;
		}
	}
	
	/**
	 * 设置右侧触摸监听
	 */
	@Override
	public void onTouchingLetterChanged(String s) {
		//该字母首次出现的位置
		int position = adapter.getPositionForSection(s.charAt(0));
		if(position != -1){
			contactsListView.setSelection(position);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		//这里要利用adapter.getIntem(position)来获取当前position所对应的对象
		String friendPhoneNumber = ((ContactsSortModel)adapter.getItem(position)).getPhone();
		String friendName = ((ContactsSortModel)adapter.getItem(position)).getName();
		Intent intent = new Intent();
		intent.putExtra("friendName", friendName);
		intent.putExtra("friendPhoneNumber", friendPhoneNumber);
		setResult(Constants.CONSTACT_PAGER_RESULTCODE, intent);
		//取消继续显示
		RegisterAddCallNumberActivity.this.finish();
	}

}

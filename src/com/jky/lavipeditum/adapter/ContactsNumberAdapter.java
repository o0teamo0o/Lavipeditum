package com.jky.lavipeditum.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jky.lavipeditum.R;
import com.jky.lavipeditum.bean.ContactsSortModel;

/**
 * 
 * @ClassName: ContactsNumberAdapter
 * @Description: 联系人号码适配器
 *
 * @author o0teamo0o
 * @date 2014年10月29日 上午1:23:05
 */
public class ContactsNumberAdapter extends BaseAdapter {
	private List<ContactsSortModel> contactsSortModels = null;
	private Context mContext;
	
	public ContactsNumberAdapter(Context mContext, List<ContactsSortModel> contactsSortModels) {
		this.mContext = mContext;
		this.contactsSortModels = contactsSortModels;
	}

	@Override
	public int getCount() {
		return contactsSortModels.size();
	}

	@Override
	public Object getItem(int position) {
		return contactsSortModels.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		ContactsSortModel mContent = contactsSortModels.get(position);
		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.addcontactsnumber_item, null);
			viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.title);
			viewHolder.tvLetter = (TextView) convertView.findViewById(R.id.catalog);
			viewHolder.tvPhone = (TextView) convertView.findViewById(R.id.tv_phone);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		//根据position获取分类的首字母的Char ascii值
		int section = getSectionForPosition(position);
		
		//如果当前位置等于该分类首字母的Char的位置，则认为是第一次出现
		if(position == getPositionForSection(section)){
			viewHolder.tvLetter.setVisibility(View.VISIBLE);
			viewHolder.tvLetter.setText(mContent.getContactsSortLetters());
		}else{
			viewHolder.tvLetter.setVisibility(View.GONE);
		}
		
		viewHolder.tvTitle.setText(contactsSortModels.get(position).getName());
		viewHolder.tvPhone.setText(contactsSortModels.get(position).getPhone());
		
		//空实现
		viewHolder.tvLetter.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});
		return convertView;
	}
	
	final static class ViewHolder{
		TextView tvLetter;
		TextView tvTitle;
		TextView tvPhone;
	}
	
	/**
	 * 根据ListView的当前位置获取分类的首字母的Char ascii值
	 */
	public int getSectionForPosition(int position) {
		return contactsSortModels.get(position).getContactsSortLetters().charAt(0);
	}

	/**
	 * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
	 */
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = contactsSortModels.get(i).getContactsSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}
		
		return -1;
	}

}

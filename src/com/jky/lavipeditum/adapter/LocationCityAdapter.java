package com.jky.lavipeditum.adapter;

import java.util.ArrayList;
import java.util.List;

import com.jky.lavipeditum.R;
import com.jky.lavipeditum.bean.CitySortModel;
import com.jky.lavipeditum.bean.Region;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 
 * @ClassName: LocationCityAdapter 
 * @Description: 定位城市的适配器

 * @author o0teamo0o
 * @date 2014年11月14日 上午10:05:42 
 *
 */
public class LocationCityAdapter extends BaseAdapter {

	private Context context;
	private List<CitySortModel> regions;
	
	public LocationCityAdapter(Context context, List<CitySortModel> regions) {
		this.context = context;
		this.regions = regions;
	}

	/**
	 * 当ListView数据发生变化时,调用此方法来更新ListView
	 * @param regions
	 */
	public void setRegions(List<CitySortModel> regions) {
		this.regions = regions;
		LocationCityAdapter.this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return regions.size();
	}

	@Override
	public Object getItem(int position) {
		return regions.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.location_city_item, null);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.tvLetter = (TextView) convertView.findViewById(R.id.catalog);
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		//根据position获取分类的首字母的Char ascii值
		int section = getSectionForPosition(position);
		
		//如果当前位置等于该分类首字母的Char的位置，则认为是第一次出现
		if(position == getPositionForSection(section)){
			holder.tvLetter.setVisibility(View.VISIBLE);
			holder.tvLetter.setText(regions.get(position).getContactsSortLetters());
		}else{
			holder.tvLetter.setVisibility(View.GONE);
		}
		
		
		holder.title.setText(regions.get(position).getName());
		
		return convertView;
	}
	
	public class ViewHolder{
		TextView tvLetter;
		TextView title;
	}
	
	/**
	 * 根据ListView的当前位置获取分类的首字母的Char ascii值
	 */
	public int getSectionForPosition(int position) {
		return regions.get(position).getContactsSortLetters().charAt(0);
	}
	
	/**
	 * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
	 */
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = regions.get(i).getContactsSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}
		return -1;
	}

}

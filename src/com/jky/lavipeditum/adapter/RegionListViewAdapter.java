package com.jky.lavipeditum.adapter;

import java.util.ArrayList;

import com.jky.lavipeditum.R;
import com.jky.lavipeditum.bean.Region;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

/**
 * 
 * @ClassName: RegionListViewAdapter 
 * @Description: 地区适配器

 * @author o0teamo0o
 * @date 2014年11月18日 下午11:20:53 
 *
 */
public class RegionListViewAdapter extends BaseAdapter {
	
	private Context context;
	private ArrayList<Region> regions;
	
	public RegionListViewAdapter(Context context, ArrayList<Region> regions) {
		this.context = context;
		this.regions = regions;
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
			convertView = View.inflate(context, R.layout.region_register_three_pager_popup_item, null);
			holder.tv_zone = (TextView) convertView.findViewById(R.id.tv_zone);
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.tv_zone.setText(regions.get(position).getName());
		
		return convertView;
	}

	public class ViewHolder{
		TextView tv_zone;
	}
}

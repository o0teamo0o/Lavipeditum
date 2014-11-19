package com.jky.lavipeditum.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.jky.lavipeditum.R;
import com.jky.lavipeditum.bean.Region;

/**
 * 
 * @ClassName: RegionGridViewPager 
 * @Description: 城市地区的适配器

 * @author o0teamo0o
 * @date 2014年11月14日 上午12:11:17 
 *
 */
public class RegionGridViewAdapter extends BaseAdapter {
	
	private ArrayList<Region> regions;
	private Context context;
	
	public RegionGridViewAdapter(Context context, ArrayList<Region> regions) {
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
			convertView = View.inflate(context, R.layout.home_fragment_ctiy_item, null);
			holder.bt_zone = (Button) convertView.findViewById(R.id.bt_zone);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.bt_zone.setText(regions.get(position).getName());
		
		return convertView;
	}
	
	public class ViewHolder{
		Button bt_zone;
	}

}

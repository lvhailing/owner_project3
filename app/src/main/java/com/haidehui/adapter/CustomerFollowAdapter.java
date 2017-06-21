package com.haidehui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.util.Log;

import com.haidehui.R;
import com.haidehui.model.Tracking2B;
import com.haidehui.network.types.MouldList;

public class CustomerFollowAdapter extends BaseAdapter {
	private Context mContext;
	private MouldList<Tracking2B> list;
	private LayoutInflater inflater;


	public CustomerFollowAdapter(Context context, MouldList<Tracking2B> list) {
		this.mContext = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return list==null? 0 : list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		Log.i("aaa","集合数量："+list.size());
		Holder holder = null;
		if (convertView == null) {
			holder = new Holder();
			convertView = inflater.inflate(R.layout.ac_customer_follow_item, null);
			holder.item_name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.item_time = (TextView) convertView.findViewById(R.id.tv_time);
			convertView.setTag(holder);

		} else {
			holder = (Holder) convertView.getTag();
		}
		Tracking2B bean = list.get(position);
		holder.item_name.setText(bean.getCustomerName());
		holder.item_time.setText(bean.getEditTime());
		return convertView;
	}

	class Holder {
		TextView item_name;
		TextView item_time;
	}
}
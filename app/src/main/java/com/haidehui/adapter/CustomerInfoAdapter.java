package com.haidehui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.haidehui.R;
import com.haidehui.bean.ResultCustomerInfolistBean;
import com.haidehui.network.types.MouldList;

public class CustomerInfoAdapter extends BaseAdapter {
	private Context mContext;
	private MouldList<ResultCustomerInfolistBean> list;
	private LayoutInflater inflater;


	public CustomerInfoAdapter(Context context, MouldList<ResultCustomerInfolistBean> list) {
		this.mContext = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return list.size();
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
		ResultCustomerInfolistBean bean = list.get(position);
		Holder holder = null;
		if (convertView == null) {
			holder = new Holder();
			convertView = inflater.inflate(R.layout.ac_customer_info_item, null);
			holder.item_name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.item_phone = (TextView) convertView.findViewById(R.id.tv_phone);
			convertView.setTag(holder);

		} else {
			holder = (Holder) convertView.getTag();
		}
		holder.item_name.setText(bean.getCustomerName());
		holder.item_phone.setText(bean.getCustomerPhone());
		return convertView;
	}

	class Holder {
		TextView item_name;
		TextView item_phone;
	}
}
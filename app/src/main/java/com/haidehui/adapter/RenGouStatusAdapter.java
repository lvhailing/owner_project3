package com.haidehui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.haidehui.R;
import com.haidehui.bean.ResultRenGouStatuslistBean;
import com.haidehui.network.types.MouldList;

public class RenGouStatusAdapter extends BaseAdapter {
	private Context mContext;
	private MouldList<ResultRenGouStatuslistBean> list;
	private LayoutInflater inflater;


	public RenGouStatusAdapter(Context context, MouldList<ResultRenGouStatuslistBean> list) {
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
		ResultRenGouStatuslistBean bean = list.get(position);
		Holder holder = null;
		if (convertView == null) {
			holder = new Holder();
			convertView = inflater.inflate(R.layout.ac_rengou_status_item, null);
			holder.item_project = (TextView) convertView.findViewById(R.id.tv_project);
			holder.item_name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.item_money = (TextView) convertView.findViewById(R.id.tv_money);
			convertView.setTag(holder);

		} else {
			holder = (Holder) convertView.getTag();
		}
		holder.item_project.setText(bean.getProject());
		holder.item_name.setText(bean.getName());
		holder.item_money.setText(bean.getMoney());
		return convertView;
	}

	class Holder {
		TextView item_project;
		TextView item_name;
		TextView item_money;
	}
}
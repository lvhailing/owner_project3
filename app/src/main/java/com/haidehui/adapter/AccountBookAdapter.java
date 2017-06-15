package com.haidehui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.haidehui.R;
import com.haidehui.bean.ResultAccountBooklistBean;
import com.haidehui.bean.ResultCustomerInfolistBean;
import com.haidehui.network.types.MouldList;

public class AccountBookAdapter extends BaseAdapter {
	private Context mContext;
	private MouldList<ResultAccountBooklistBean> list;
	private LayoutInflater inflater;


	public AccountBookAdapter(Context context, MouldList<ResultAccountBooklistBean> list) {
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
		ResultAccountBooklistBean bean = list.get(position);
		Holder holder = null;
		if (convertView == null) {
			holder = new Holder();
			convertView = inflater.inflate(R.layout.ac_account_book_item, null);
			holder.item_line = (TextView) convertView.findViewById(R.id.tv_line);
			holder.item_info = (TextView) convertView.findViewById(R.id.tv_info);
			holder.item_money = (TextView) convertView.findViewById(R.id.tv_money);
			holder.item_time = (TextView) convertView.findViewById(R.id.tv_time);
			holder.item_status = (TextView) convertView.findViewById(R.id.tv_status);
			convertView.setTag(holder);

		} else {
			holder = (Holder) convertView.getTag();
		}
		holder.item_info.setText(bean.getInfo());
		holder.item_money.setText(bean.getMoney());
		holder.item_time.setText(bean.getTime());
		if (bean.getStatus().equals("finished")){
			holder.item_line.setBackgroundColor(mContext.getResources().getColor(R.color.bg_btn_orange));
			holder.item_status.setText("结佣完成");
			holder.item_status.setTextColor(mContext.getResources().getColor(R.color.gray_d));
		}else{
			holder.item_line.setBackgroundColor(mContext.getResources().getColor(R.color.txt_vertical_line));
			holder.item_status.setText("结佣中");
			holder.item_status.setTextColor(mContext.getResources().getColor(R.color.txt_vertical_line));
		}
		return convertView;
	}

	class Holder {
		TextView item_line;
		TextView item_info;
		TextView item_money;
		TextView item_time;
		TextView item_status;
	}
}
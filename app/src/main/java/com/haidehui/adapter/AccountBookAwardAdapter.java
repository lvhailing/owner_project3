package com.haidehui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.haidehui.R;
import com.haidehui.model.AccountBookAward3B;
import com.haidehui.model.AccountBookWithDraw3B;
import com.haidehui.network.types.MouldList;

public class AccountBookAwardAdapter extends BaseAdapter {
	private Context mContext;
	private MouldList<AccountBookAward3B> list;
	private LayoutInflater inflater;


	public AccountBookAwardAdapter(Context context, MouldList<AccountBookAward3B> list) {
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
		AccountBookAward3B bean = list.get(position);
		Holder holder = null;
		if (convertView == null) {
			holder = new Holder();
			convertView = inflater.inflate(R.layout.ac_account_book_item, null);
			holder.item_line = (TextView) convertView.findViewById(R.id.tv_line);
			holder.item_reward_type = (TextView) convertView.findViewById(R.id.tv_info);
			holder.item_money = (TextView) convertView.findViewById(R.id.tv_money);
			holder.item_time = (TextView) convertView.findViewById(R.id.tv_time);
			holder.item_status = (TextView) convertView.findViewById(R.id.tv_status);
			convertView.setTag(holder);

		} else {
			holder = (Holder) convertView.getTag();
		}
		if(bean.getRewardType().equals("download")){
			holder.item_reward_type.setText("下载奖励");
		}else if(bean.getRewardType().equals("recommend")){
			holder.item_reward_type.setText("推荐奖励");
		}
		holder.item_money.setText("+"+bean.getRewardAmount());
		holder.item_time.setText(bean.getCreateTime());
		if (bean.getRewardStatus().equals("init")){
			holder.item_status.setText("已发放");
			/*holder.item_line.setBackgroundColor(mContext.getResources().getColor(R.color.txt_vertical_line));
			holder.item_status.setTextColor(mContext.getResources().getColor(R.color.txt_vertical_line));*/
		}else{
			holder.item_status.setText("已完成");
			/*holder.item_line.setBackgroundColor(mContext.getResources().getColor(R.color.bg_btn_orange));
			holder.item_status.setTextColor(mContext.getResources().getColor(R.color.gray_d));*/
		}
		return convertView;
	}

	class Holder {
		TextView item_line;
		TextView item_reward_type;
		TextView item_money;
		TextView item_time;
		TextView item_status;
	}
}
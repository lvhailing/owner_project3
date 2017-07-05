package com.haidehui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.haidehui.R;
import com.haidehui.bean.ResultCustomerFollowDetailslistBean;
import com.haidehui.network.types.MouldList;

public class CustomerFollowDetailsAdapter extends BaseAdapter {
	private Context mContext;
	private MouldList<ResultCustomerFollowDetailslistBean> list;
	private LayoutInflater inflater;
	private OnEditListener listener;


	public CustomerFollowDetailsAdapter(Context context, MouldList<ResultCustomerFollowDetailslistBean> list, OnEditListener listener) {
		this.mContext = context;
		this.list = list;
		this.listener = listener;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return list==null?0:list.size();
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
		final ResultCustomerFollowDetailslistBean bean = list.get(position);
		Holder holder = null;
		if (convertView == null) {
			holder = new Holder();
			convertView = inflater.inflate(R.layout.ac_customer_follow_details_item, null);
			holder.checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);
			holder.tv_details = (TextView) convertView.findViewById(R.id.tv_details);
			convertView.setTag(holder);

		} else {
			holder = (Holder) convertView.getTag();
		}
		holder.tv_details.setText(bean.getDetails());
		if (bean.getIsChecked().equals("true")) {
			holder.checkbox.setButtonDrawable(R.mipmap.img_follow_checked);
			holder.checkbox.setChecked(true);
		} else {
			holder.checkbox.setButtonDrawable(R.mipmap.img_follow_uncheck);
			holder.checkbox.setChecked(false);

		}
		holder.checkbox.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean checked = ((CheckBox) v).isChecked();
				if (checked) {
					((CheckBox) v).setButtonDrawable(R.mipmap.img_follow_checked);
				} else {
					((CheckBox) v).setButtonDrawable(R.mipmap.img_follow_uncheck);
				}
				listener.onCheckBox(position, checked);
			}
		});
		return convertView;
	}
	public interface OnEditListener {
		public void onCheckBox(int position, boolean isChecked);
	}

	class Holder {
		CheckBox checkbox;
		TextView tv_details;
	}
}
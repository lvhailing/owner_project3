package com.haidehui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.haidehui.R;
import com.haidehui.model.ExplainOrder3B;
import com.haidehui.model.OneLevelRecomendation3B;
import com.haidehui.network.types.MouldList;
import com.nostra13.universalimageloader.core.ImageLoader;

// 预约说明会 列表 Adapter
public class ExplainOrderAdapter extends BaseAdapter {
    private MouldList<ExplainOrder3B> list;
    private Context context;
    private LayoutInflater inflater;
    private ImageLoader imageLoader = ImageLoader.getInstance();

    public ExplainOrderAdapter(Context context, MouldList<ExplainOrder3B> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int arg0) {
        return list.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null) {
            holder = new Holder();
            convertView = inflater.inflate(R.layout.item_one_level_recommendation, null);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_phone = (TextView) convertView.findViewById(R.id.tv_phone);
            holder.tv_recommend_status = (TextView) convertView.findViewById(R.id.tv_recommend_status);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        String customerName = list.get(position).getCustomerName();
        String customerPhone = list.get(position).getCustomerPhone();
        String meetingTime = list.get(position).getMeetingTime();

        if (!TextUtils.isEmpty(customerName)) {
            holder.tv_name.setText(customerName);
        } else {
            holder.tv_name.setText("--");
        }

        if (!TextUtils.isEmpty(customerPhone)) {
            holder.tv_phone.setText(customerPhone);
        } else {
            holder.tv_phone.setText("--");
        }

        if (!TextUtils.isEmpty(meetingTime)) {
            holder.tv_recommend_status.setText(meetingTime);
        } else {
            holder.tv_recommend_status.setText("--");
        }
        return convertView;
    }

    class Holder {
        TextView tv_name; // 姓名
        TextView tv_phone; // 电话
        TextView tv_recommend_status; // 参会日期

    }
}

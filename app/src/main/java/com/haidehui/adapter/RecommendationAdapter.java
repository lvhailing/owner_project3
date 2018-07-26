package com.haidehui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.haidehui.R;
import com.haidehui.model.InvestmentGuide3B;
import com.haidehui.model.OneLevelRecomendation3B;
import com.haidehui.network.types.MouldList;
import com.nostra13.universalimageloader.core.ImageLoader;

import static com.haidehui.uitls.ImageLoaderManager.options;

// 我的事业合伙人-- 一级推荐 列表 Adapter
public class RecommendationAdapter extends BaseAdapter {
    private MouldList<OneLevelRecomendation3B> list;
    private Context context;
    private LayoutInflater inflater;
    private ImageLoader imageLoader = ImageLoader.getInstance();

    public RecommendationAdapter(Context context, MouldList<OneLevelRecomendation3B> list) {
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
            convertView = inflater.inflate(R.layout.item_recommendation, null);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_phone = (TextView) convertView.findViewById(R.id.tv_phone);
//            holder.tv_recommend_status = (TextView) convertView.findViewById(R.id.tv_recommend_status);
            holder.tv_work_unit = (TextView) convertView.findViewById(R.id.tv_work_unit);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        String realName = list.get(position).getRealName();
        String mobile = list.get(position).getMobile();
//        String recommendationStatus = list.get(position).getCheckStatus();
        String workUnit = list.get(position).getWorkUnit();

//        if (recommendationStatus.equals("init")) {
//            recommendationStatus = "未认证";
//        } else if (recommendationStatus.equals("submit")) {
//            recommendationStatus = "待认证";
//        } else if (recommendationStatus.equals("success")) {
//            recommendationStatus = "认证成功";
//        } else if (recommendationStatus.equals("fail")) {
//            recommendationStatus = "认证失败";
//        }
        if (!TextUtils.isEmpty(realName)) {
            holder.tv_name.setText(realName);
        } else {
            holder.tv_name.setText("--");
        }

        if (!TextUtils.isEmpty(mobile)) {
            holder.tv_phone.setText(mobile);
        } else {
            holder.tv_phone.setText("--");
        }

        if (!TextUtils.isEmpty(workUnit)) {
            holder.tv_work_unit.setText(workUnit);
        } else {
            holder.tv_work_unit.setText("--");
        }

//        if (!TextUtils.isEmpty(recommendationStatus)) {
//            holder.tv_recommend_status.setText(recommendationStatus);
//        } else {
//            holder.tv_recommend_status.setText("--");
//        }
        return convertView;
    }

    class Holder {
        TextView tv_name; // 姓名
        TextView tv_phone; // 电话
//        TextView tv_recommend_status; // 认证状态
        TextView tv_work_unit; // 工作单位

    }
}

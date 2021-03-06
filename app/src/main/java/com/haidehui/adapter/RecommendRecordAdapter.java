package com.haidehui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.haidehui.R;
import com.haidehui.model.ResultRecommendRecordItemContentBean;
import com.haidehui.network.types.MouldList;
import com.haidehui.uitls.StringUtil;

/**
 * 邀请记录
 * Created by hasee on 2017/6/14.
 */
public class RecommendRecordAdapter extends BaseAdapter {

    private MouldList<ResultRecommendRecordItemContentBean> list;
    private Context context;
    private LayoutInflater inflater;

    public RecommendRecordAdapter(Context context, MouldList<ResultRecommendRecordItemContentBean> list) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder = null;
        if (view == null) {
            holder = new Holder();
            view = inflater.inflate(R.layout.item_recommend_record, null);
            holder.tv_recommend_record_friend = (TextView) view.findViewById(R.id.tv_recommend_record_friend);
            holder.tv_recommend_record_level = (TextView) view.findViewById(R.id.tv_recommend_record_level);
            holder.tv_recommend_record_account = (TextView) view.findViewById(R.id.tv_recommend_record_account);

            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        holder.tv_recommend_record_friend.setText(StringUtil.replaceSubString(list.get(i).getMobile()));
        holder.tv_recommend_record_level.setText(list.get(i).getUserLevel() + "级推荐");

        if (!TextUtils.isEmpty(list.get(i).getRewardAmount())) {
            if (list.get(i).getRewardAmount().equals("0")) {
                holder.tv_recommend_record_account.setText("0.00");
            } else {
                holder.tv_recommend_record_account.setText(list.get(i).getRewardAmount() + "元");
            }
        } else {
            holder.tv_recommend_record_account.setText("0.00");
        }

        return view;
    }

    class Holder {
        TextView tv_recommend_record_friend;
        TextView tv_recommend_record_level;
        TextView tv_recommend_record_account;
    }

}

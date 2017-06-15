package com.haidehui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.haidehui.R;
import com.haidehui.model.ResultMessageContentBean;
import com.haidehui.network.types.MouldList;

/**
 * 邀请记录
 * Created by hasee on 2017/6/14.
 */
public class RecommendRecordAdapter extends BaseAdapter{

    private MouldList<ResultMessageContentBean> list;
    private Context context;
    private LayoutInflater inflater;

    public RecommendRecordAdapter(Context context,MouldList<ResultMessageContentBean> list) {
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
        if(view==null){
            holder = new Holder();
            view = inflater.inflate(R.layout.ac_recommend_record_item,null);
            holder.tv_recommend_record_friend = (TextView) view.findViewById(R.id.tv_recommend_record_friend);
            holder.tv_recommend_record_level = (TextView) view.findViewById(R.id.tv_recommend_record_level);
            holder.tv_recommend_record_account = (TextView) view.findViewById(R.id.tv_recommend_record_account);

            view.setTag(holder);
        }else{
            holder = (Holder) view.getTag();
        }

        holder.tv_recommend_record_friend.setText(list.get(i).getName());
        holder.tv_recommend_record_level.setText(list.get(i).getDate());
        holder.tv_recommend_record_account.setText(list.get(i).getNum());

        return view;
    }

    class Holder{

        private TextView tv_recommend_record_friend;
        private TextView tv_recommend_record_level;
        private TextView tv_recommend_record_account;



    }

}

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
 * 其他消息列表
 * Created by hasee on 2017/6/8.
 */
public class MessageOtherAdapter extends BaseAdapter{

    private Context context;
    private MouldList<ResultMessageContentBean> list;
    private LayoutInflater inflater;

    public MessageOtherAdapter(Context context, MouldList<ResultMessageContentBean> list) {
        this.context = context;
        this.list = list;
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
        if(view == null){
            holder = new Holder();
            view = inflater.inflate(R.layout.ac_message_other_item,null);
            holder.tv_message_other_item_name = (TextView) view.findViewById(R.id.tv_message_other_item_name);
            holder.tv_message_other_item_date = (TextView) view.findViewById(R.id.tv_message_other_item_date);
            view.setTag(holder);
        }else{
            holder = (Holder) view.getTag();
        }

        holder.tv_message_other_item_name.setText(list.get(i).getName());
        holder.tv_message_other_item_date.setText(list.get(i).getNum());


        return view;
    }

    class Holder{
        private TextView tv_message_other_item_name;
        private TextView tv_message_other_item_date;


    }

}
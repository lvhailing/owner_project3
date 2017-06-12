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
 * 通告消息列表
 * Created by hasee on 2017/6/8.
 */
public class MessageNoticeAdapter extends BaseAdapter{

    private Context context;
    private MouldList<ResultMessageContentBean> list;
    private LayoutInflater inflater;

    public MessageNoticeAdapter(Context context, MouldList<ResultMessageContentBean> list) {
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
            view = inflater.inflate(R.layout.ac_message_notice_item,null);
            holder.tv_message_notice_item_name = (TextView) view.findViewById(R.id.tv_message_notice_item_name);
            holder.tv_message_notice_item_time = (TextView) view.findViewById(R.id.tv_message_notice_item_time);
            holder.tv_message_notice_item_content = (TextView) view.findViewById(R.id.tv_message_notice_item_content);
            view.setTag(holder);
        }else{
            holder = (Holder) view.getTag();
        }

        holder.tv_message_notice_item_name.setText(list.get(i).getName());
        holder.tv_message_notice_item_time.setText(list.get(i).getNum());
        holder.tv_message_notice_item_content.setText(list.get(i).getContent());


        return view;
    }

    class Holder{
        private TextView tv_message_notice_item_name;
        private TextView tv_message_notice_item_time;
        private TextView tv_message_notice_item_content;


    }

}

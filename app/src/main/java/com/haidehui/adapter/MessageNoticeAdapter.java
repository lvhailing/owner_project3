package com.haidehui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.haidehui.R;
import com.haidehui.model.ResultMessageContentBean;
import com.haidehui.model.ResultMessageItemContentBean;
import com.haidehui.network.types.MouldList;

/**
 * 通告消息列表
 * Created by hasee on 2017/6/8.
 */
public class MessageNoticeAdapter extends BaseAdapter{

    private Context context;
    private MouldList<ResultMessageItemContentBean> list;
    private LayoutInflater inflater;

    public MessageNoticeAdapter(Context context, MouldList<ResultMessageItemContentBean> list) {
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
            holder.iv_message_notice_isread = (ImageView) view.findViewById(R.id.iv_message_notice_isread);
            view.setTag(holder);
        }else{
            holder = (Holder) view.getTag();
        }

        holder.tv_message_notice_item_name.setText(list.get(i).getTitle());
        holder.tv_message_notice_item_time.setText(list.get(i).getSendTime());
        holder.tv_message_notice_item_content.setText(list.get(i).getDescription());

        if(list.get(i).getReadState().equals("yes")){
            holder.iv_message_notice_isread.setVisibility(View.GONE);
        }else if(list.get(i).getReadState().equals("no")){
            holder.iv_message_notice_isread.setVisibility(View.VISIBLE);
        }else{
            holder.iv_message_notice_isread.setVisibility(View.GONE);
        }

        return view;
    }

    class Holder{
        private TextView tv_message_notice_item_name;
        private TextView tv_message_notice_item_time;
        private TextView tv_message_notice_item_content;
        private ImageView iv_message_notice_isread;


    }

}

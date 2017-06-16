package com.haidehui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.haidehui.R;
import com.haidehui.model.ResultMessageContentBean;
import com.haidehui.model.ResultMyBankListContentItemBean;
import com.haidehui.network.types.MouldList;

/**
 * 我的银行卡
 * Created by hasee on 2017/6/14.
 */
public class MyBankAdapter extends BaseAdapter{

    private Context context;
    private MouldList<ResultMyBankListContentItemBean> list;
    private LayoutInflater inflater;

    public MyBankAdapter(Context context, MouldList<ResultMyBankListContentItemBean> list) {
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
            view = inflater.inflate(R.layout.ac_mybank_item,null);
            holder.tv_bank_bankname = (TextView) view.findViewById(R.id.tv_bank_bankname);
            holder.tv_bank_username = (TextView) view.findViewById(R.id.tv_bank_username);
            holder.tv_bank_num = (TextView) view.findViewById(R.id.tv_bank_num);
            view.setTag(holder);
        }else{

            holder = (Holder) view.getTag();
        }
        holder.tv_bank_bankname.setText(list.get(i).getBankName());
        holder.tv_bank_username.setText(list.get(i).getRealName());
        holder.tv_bank_num.setText(list.get(i).getBankCardNum());

        return view;
    }

    class Holder{
        private TextView tv_bank_bankname;
        private TextView tv_bank_username;
        private TextView tv_bank_num;


    }

}

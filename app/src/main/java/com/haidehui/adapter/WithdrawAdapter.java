package com.haidehui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.haidehui.R;
import com.haidehui.model.ResultMyBankListContentItemBean;
import com.haidehui.network.types.MouldList;

/**
 * 提现--选择银行卡
 * Created by hasee on 2017/6/14.
 */
public class WithdrawAdapter extends BaseAdapter{

    private Context context;
    private MouldList<ResultMyBankListContentItemBean> list;
    private LayoutInflater inflater;

    public WithdrawAdapter(Context context, MouldList<ResultMyBankListContentItemBean> list) {
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
            view = inflater.inflate(R.layout.ac_withdraw_choosebank_item,null);
            holder.tv_bank_banknum = (TextView) view.findViewById(R.id.tv_bank_banknum);
            holder.tv_bank_username = (TextView) view.findViewById(R.id.tv_bank_username);
            holder.tv_bank_name = (TextView) view.findViewById(R.id.tv_bank_name);
            view.setTag(holder);
        }else{

            holder = (Holder) view.getTag();
        }
        holder.tv_bank_banknum.setText(list.get(i).getBankCardNum());
        holder.tv_bank_username.setText(list.get(i).getRealName());
        holder.tv_bank_name.setText(list.get(i).getBankName());

        return view;
    }

    class Holder{
        private TextView tv_bank_banknum;
        private TextView tv_bank_username;
        private TextView tv_bank_name;


    }

}

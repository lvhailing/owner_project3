package com.haidehui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.haidehui.R;
import com.haidehui.model.BoutiqueHouse2B;
import com.haidehui.network.types.MouldList;
import com.nostra13.universalimageloader.core.ImageLoader;

// 发现-- 投资指南列表 Adapter
public class ProductRoadShowAdapter extends BaseAdapter {

    private static final String tag = "recommendProductAdapter";
    private MouldList<BoutiqueHouse2B> list;
    private Context context;
    private LayoutInflater inflater;
    private ImageLoader imageLoader = ImageLoader.getInstance();

    public ProductRoadShowAdapter(Context context, MouldList<BoutiqueHouse2B> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
//		Log.e(tag, "list=="+list.size());
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int arg0) {
        return getItem(arg0);
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
            convertView = inflater.inflate(R.layout.item_product_road_shou, null);
            holder.iv_product_road_show = (ImageView) convertView.findViewById(R.id.iv_product_road_show);
            holder.tv_product_road_show_title = (TextView) convertView.findViewById(R.id.tv_product_road_show_title);
            holder.tv_release_time = (TextView) convertView.findViewById(R.id.tv_release_time);
            holder.tv_guest_speaker = (TextView) convertView.findViewById(R.id.tv_guest_speaker);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        return convertView;
    }

    class Holder {
        ImageView iv_product_road_show; // 图片
        TextView tv_product_road_show_title; // 标题
        TextView tv_release_time; // 发布日期
        TextView tv_guest_speaker; // 演讲嘉宾

    }
}

package com.haidehui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.haidehui.R;
import com.haidehui.model.InvestmentGuide3B;
import com.haidehui.network.types.MouldList;
import com.nostra13.universalimageloader.core.ImageLoader;

import static com.haidehui.uitls.ImageLoaderManager.options;

// 发现-- 投资指南列表 Adapter
public class InvestmentGuideAdapter extends BaseAdapter {

    private static final String tag = "recommendProductAdapter";
    private MouldList<InvestmentGuide3B> list;
    private Context context;
    private LayoutInflater inflater;
    private ImageLoader imageLoader = ImageLoader.getInstance();

    public InvestmentGuideAdapter(Context context, MouldList<InvestmentGuide3B> list) {
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
            convertView = inflater.inflate(R.layout.item_investment_guide, null);
            holder.iv_guide_photo = (ImageView) convertView.findViewById(R.id.iv_guide_photo);
            holder.tv_guide_title = (TextView) convertView.findViewById(R.id.tv_guide_title);
            holder.tv_guide_detail = (TextView) convertView.findViewById(R.id.tv_guide_detail);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        //加载左侧图片
        ImageLoader.getInstance().displayImage(list.get(position).getPicture(), holder.iv_guide_photo, options);

//        holder.tv_guide_title.setText(list.get(position).getTitile());
        String titile = list.get(position).getTitile();
        Log.i("fff", "标题："+titile);
//        holder.tv_guide_detail .setText(list.get(position).getGraphicDetails());
        return convertView;
    }

    class Holder {
        ImageView iv_guide_photo; // 左侧图片
        TextView tv_guide_title; // 标题
        TextView tv_guide_detail; // 详情简介

    }
}

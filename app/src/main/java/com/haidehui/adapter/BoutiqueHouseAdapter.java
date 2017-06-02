package com.haidehui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.haidehui.R;
import com.haidehui.model.BoutiqueHouse2B;
import com.haidehui.network.types.MouldList;
import com.nostra13.universalimageloader.core.ImageLoader;

// 精品房源 Adapter
public class BoutiqueHouseAdapter extends BaseAdapter {

    private static final String tag = "recommendProductAdapter";
    private MouldList<BoutiqueHouse2B> list;
    private Context context;
    private LayoutInflater inflater;
    private ImageLoader imageLoader = ImageLoader.getInstance();

    public BoutiqueHouseAdapter(Context context, MouldList<BoutiqueHouse2B> list) {
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
            convertView = inflater.inflate(R.layout.item_boutique_house, null);
//            holder.tv_hot_product_content_title = (TextView) convertView.findViewById(R.id.tv_hot_product_content_title);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        return convertView;
    }

    class Holder {
        TextView tv_hot_product_content_Third_two;

    }
}

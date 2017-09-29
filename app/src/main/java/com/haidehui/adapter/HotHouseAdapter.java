package com.haidehui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.haidehui.R;
import com.haidehui.model.BoutiqueHouse2B;
import com.haidehui.model.HotHouse2B;
import com.haidehui.model.HotHouse3B;
import com.haidehui.network.types.MouldList;
import com.nostra13.universalimageloader.core.ImageLoader;

import static com.haidehui.uitls.ImageLoaderManager.options;

// 最热房源 Adapter
public class HotHouseAdapter extends BaseAdapter {
    private MouldList<HotHouse3B> list;
    private Context context;
    private LayoutInflater inflater;
    private ImageLoader imageLoader = ImageLoader.getInstance();

    public HotHouseAdapter(Context context, MouldList<HotHouse3B> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
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
        Log.i("sss", "position :" + position + " item :" + list.get(position));
        Holder holder = null;
        if (convertView == null) {
            holder = new Holder();
            convertView = inflater.inflate(R.layout.item_hot_house, null);
            holder.iv_boutique_house = (ImageView) convertView.findViewById(R.id.iv_boutique_house);
            holder.tv_house_name = (TextView) convertView.findViewById(R.id.tv_house_name);
            holder.tv_house_area = (TextView) convertView.findViewById(R.id.tv_house_area);
            holder.tv_house_price = (TextView) convertView.findViewById(R.id.tv_house_price);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        //加载左侧图片
        ImageLoader.getInstance().displayImage(list.get(position).getPath(), holder.iv_boutique_house, options);

        String houseType = list.get(position).getHouseType();
        String area = list.get(position).getArea();
        String houseName = list.get(position).getName();
        String price = list.get(position).getPrice();

        if (!TextUtils.isEmpty(houseName)) {
            holder.tv_house_name.setText(houseName);
        } else {
            holder.tv_house_name.setText("--");
        }
        if (!TextUtils.isEmpty(houseType) && !TextUtils.isEmpty(area)) {
            holder.tv_house_area.setText(houseType + " / " + area);
        } else {
            holder.tv_house_area.setText("--");
        }
        if (!TextUtils.isEmpty(price)) {
            holder.tv_house_price.setText(price + "万元");
        } else {
            holder.tv_house_price.setText("--");
        }

        return convertView;
    }

    class Holder {
        ImageView iv_boutique_house;
        TextView tv_house_name;
        TextView tv_house_area;
        TextView tv_house_price;

    }
}

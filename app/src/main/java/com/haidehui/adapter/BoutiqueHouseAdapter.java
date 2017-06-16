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
import com.haidehui.model.HomeIndex3B;
import com.haidehui.network.types.MouldList;
import com.nostra13.universalimageloader.core.ImageLoader;

import static com.haidehui.uitls.ImageLoaderManager.options;

// 首页--精品房源 Adapter
public class BoutiqueHouseAdapter extends BaseAdapter {

    private static final String tag = "recommendProductAdapter";
    private MouldList<HomeIndex3B> list;
    private Context context;
    private LayoutInflater inflater;
    private ImageLoader imageLoader = ImageLoader.getInstance();

    public BoutiqueHouseAdapter(Context context, MouldList<HomeIndex3B> list) {
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
        Holder holder = null;
        if (convertView == null) {
            holder = new Holder();
            convertView = inflater.inflate(R.layout.item_boutique_house, null);
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

        holder.tv_house_name.setText(list.get(position).getName());
        holder.tv_house_area.setText(houseType + "/" + area+"㎡");
        holder.tv_house_price.setText(list.get(position).getPrice() + "万元");

        return convertView;
    }

    class Holder {
        ImageView iv_boutique_house;
        TextView tv_house_name;
        TextView tv_house_area;
        TextView tv_house_price;

    }
}

package com.haidehui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.haidehui.R;
import com.haidehui.model.OverseaProjectDetailHouseList3B;
import com.haidehui.model.OverseaProjectDetailPdfList3B;
import com.haidehui.network.types.MouldList;
import com.nostra13.universalimageloader.core.ImageLoader;

import static com.haidehui.uitls.ImageLoaderManager.options;

// 海外项目详情页--项目材料 Adapter
public class AttachmentAdapter extends BaseAdapter {
    private MouldList<OverseaProjectDetailPdfList3B> list;
    private Context context;
    private LayoutInflater inflater;
    private ImageLoader imageLoader = ImageLoader.getInstance();

    public AttachmentAdapter(Context context, MouldList<OverseaProjectDetailPdfList3B> list) {
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
            convertView = inflater.inflate(R.layout.item_oversea_project_attachment, null);
            holder.tv_attachment_name = (TextView) convertView.findViewById(R.id.tv_attachment_name);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }


        String name = list.get(position).getName();
        String houseType = list.get(position).getPath();

        if (!TextUtils.isEmpty(name)) {
            holder.tv_attachment_name.setText(name+".pdf");
        } else {
            holder.tv_attachment_name.setText("--");
        }

        return convertView;
    }

    class Holder {
        TextView tv_attachment_name;

    }
}

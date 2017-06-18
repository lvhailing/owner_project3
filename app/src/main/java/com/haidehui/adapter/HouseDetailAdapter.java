package com.haidehui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import static com.haidehui.uitls.ImageLoaderManager.options;

// 房源详情页轮播图Adapter
public class HouseDetailAdapter extends PagerAdapter {
    private final Context context;
    private List<String> urls = null;

    public HouseDetailAdapter(Context context, List<String> urls) {
        this.context = context;
        this.urls = urls;
    }

    @Override
    public int getCount() {
        return urls == null ? 0 : urls.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        final ImageView iv = new ImageView(context);
        String uri = urls.get(position);
        ImageLoader.getInstance().displayImage(uri, iv, options);

        iv.setScaleType(ImageView.ScaleType.FIT_XY);
        container.addView(iv);
        return iv;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}

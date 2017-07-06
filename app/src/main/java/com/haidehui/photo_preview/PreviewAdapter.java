package com.haidehui.photo_preview;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.haidehui.photo_preview.ScaleImageView.ImageSource;
import com.haidehui.photo_preview.fresco.ImageLoader;
import com.haidehui.photo_preview.fresco.OnBitmapGetListener;

import java.util.List;

/**
 * 阅览页面适配器
 */

public class PreviewAdapter extends PagerAdapter {
    private List<String> urls = null;

    public PreviewAdapter(List<String> urls) {
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
        final PicturePreviewPageView pageView = new PicturePreviewPageView(container.getContext());
        pageView.setMaxScale(15);
        ImageLoader.getInstance().loadImageLocalOrNet(urls.get(position), new OnBitmapGetListener() {
            @Override
            public void getBitmap(Bitmap bitmap) {
                pageView.setOriginImage(ImageSource.cachedBitmap(bitmap));
            }
        });
        pageView.setBackgroundColor(Color.TRANSPARENT);
        container.addView(pageView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        pageView.setTag(position);
        return pageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }


}

package com.haidehui.photo_preview;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.text.format.Formatter;
import android.util.Log;
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
    public Object instantiateItem(final ViewGroup container, final int position) {

        final PicturePreviewPageView pageView = new PicturePreviewPageView(container.getContext());
        pageView.setMaxScale(15);
        ImageLoader.getInstance().loadImageLocalOrNet(urls.get(position), new OnBitmapGetListener() {
            @Override
            public void getBitmap(Bitmap bitmap) {
//                Log.i("aabb", "可用内存：" + getAvailMemory(container.getContext()));
//                Log.i("aabb", "压缩前：图片大小" + (bitmap.getByteCount() / 1024) + "KB，宽度为" + bitmap.getWidth() + "，高度为" + bitmap.getHeight());

                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth()*2 / 3, bitmap.getHeight()*2 / 3, true);
//                Log.i("aabb", "压缩后：图片大小" + (scaledBitmap.getByteCount() / 1024) + "KB,宽度为" + scaledBitmap.getWidth() + ",高度为" + scaledBitmap.getHeight());

                pageView.setOriginImage(ImageSource.cachedBitmap(scaledBitmap));
            }
        });
        pageView.setBackgroundColor(Color.TRANSPARENT);
        container.addView(pageView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        pageView.setTag(position);
        return pageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((PicturePreviewPageView) object);
    }

    private String getAvailMemory(Context context) {// 获取android当前可用内存大小

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        //mi.availMem; 当前系统的可用内存

        return Formatter.formatFileSize(context, mi.availMem);// 将获取的内存大小规格化
    }

}

package com.haidehui.photo_preview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.haidehui.photo_preview.ScaleImageView.ImageSource;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

import java.util.List;

/**
 * 阅览页面适配器
 * Created by lmnrenbc on 2017/5/12.
 */

public class PreviewAdapter extends PagerAdapter {
    private Context context;
    private List<String> urls = null;

    public PreviewAdapter(Context context, List<String> urls) {
        this.context = context;
        this.urls = urls;
    }

    @Override
    public int getCount() {
        return urls.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        final PicturePreviewPageView pageView = new PicturePreviewPageView(container.getContext());
        pageView.setMaxScale(15);
        ImageLoader.getInstance().displayImage(urls.get(position), new ImageView(context), new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                Log.i("aaa", "bitmap" + bitmap);
                pageView.setOriginImage(ImageSource.cachedBitmap(bitmap));
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });
        pageView.setBackgroundColor(Color.TRANSPARENT);
        container.addView(pageView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        pageView.setTag(position);
        return pageView;

//        final DragImageView dragImageView = new DragImageView(context);
//        ImageLoader.getInstance().displayImage(urls.get(position), new ImageView(context), ImageLoaderManager.options, new SimpleImageLoadingListener() {
//            @Override
//            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
//                Drawable mDrawable = new BitmapDrawable(bitmap);
//                dragImageView.setmDrawable(mDrawable);
//            }
//        });
//        container.addView(dragImageView);
//        return dragImageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }


}

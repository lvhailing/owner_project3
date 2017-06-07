package com.haidehui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.haidehui.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

import static com.haidehui.uitls.ImageLoaderManager.options;

/**
 *
 */

public class MyAdapter extends PagerAdapter {
    private final Context context;
    private List<ImageView> imageViews;//存放imgView的集合
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private List<String> urls = null;
    private ImageViewListener mImageViewListener;
    private int imgaeNum = 0;
    private ImageView imageView;

    public MyAdapter(Context context,List<String> urls) {
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
    public Object instantiateItem(ViewGroup container, int position) {
        final ImageView iv;
        int ids[] = {R.drawable.banner_one, R.drawable.banner_two, R.drawable.banner_three, R.drawable.banner_four};
        imageViews = new ArrayList<ImageView>();
        if (urls.size() == 0) {
            imgaeNum = ids.length;
        } else {
            imgaeNum = urls.size();
        }

        if (urls.size() < 3) {
            for (int k = 0; k < 4; k++) {
                for (int i = 0; i < urls.size(); i++) {
                    imageView = new ImageView(context);
                    // imageView.setBackground(new
                    // BitmapDrawable(images.get(i).getBitmap()));
                    imageViews.add(imageView);
                }
            }
        }

        if (imageViews.size() == 0) {
            for (int i = 0; i < ids.length; i++) {
                imageView = new ImageView(context);
                imageView.setBackgroundResource(ids[i]);
                imageViews.add(imageView);
            }
        }
        if (position < 0) {
            position = imageViews.size() + position;
        }
        final int postItem = position;
        if (urls.size() > 0) {
            iv = imageViews.get(position % imageViews.size());

            String uri =urls.get(position);

            imageLoader.displayImage(uri, iv, options);

        } else {
            if (imageViews.size() > 0) {
                iv = imageViews.get(position % imageViews.size());
            } else {
                iv = imageViews.get(position);
            }
        }
        if (mImageViewListener != null) {
            iv.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    mImageViewListener.onImageClick(postItem % imageViews.size(), iv);
                }
            });
        }
        ViewGroup parent = (ViewGroup) iv.getParent();
        if (parent != null) {
            parent.removeAllViews();
        }
        iv.setScaleType(ImageView.ScaleType.FIT_XY);

        container.addView(iv);
        return iv;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    /**
     * @param listener 点击事件的回调
     */

    public void setOnImageListener(ImageViewListener listener) {
        this.mImageViewListener = listener;
    }

    /**
     * 轮播图点击事件的回调接口
     */
    public interface ImageViewListener {

        void onImageClick(int postion, View imageView);
    }
}

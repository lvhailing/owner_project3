package com.haidehui.widget;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.haidehui.R;
import com.haidehui.model.ResultCycleIndex2B;
import com.haidehui.network.types.MouldList;
import com.haidehui.photo_preview.fresco.ImageLoader;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 轮播图 自定义的ViewPager
 */
public class MyRollViewPager extends ViewPager {
    private Context context;
    private List<View> dotList = new ArrayList<View>();
    private int lastPosition = 0;
    private MyClickListener myClickListener;
    private LinearLayout mPointContainer;
    private boolean isCycle = false;
    private MouldList<ResultCycleIndex2B> imageList; // 用于保存后台返回的图片的集合
    private int ids[] = {R.mipmap.bg_home_carousel_figure_normal, R.mipmap.bg_home_carousel_figure_normal}; // 假如后台没返回图片，则使用默认图片

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    setCurrentItem(getCurrentItem() + 1);
                    break;
            }
        }
    };

    /**
     * @param context 构造函数
     */
    public MyRollViewPager(Context context, MouldList<ResultCycleIndex2B> images) {
        super(context);

        this.context = context;
        this.imageList = images;

        //设置滑动属性
        setVpSpeed();

        this.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                int index = position % dotList.size();
                dotList.get(index).setBackgroundResource(R.drawable.dot_focus_red);
                dotList.get(lastPosition).setBackgroundResource(R.drawable.dot_normal);
                lastPosition = index;
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                switch (arg0) {
                    case 0:
                        if (isCycle) {
                            handler.sendEmptyMessageDelayed(0, 2000);
                        }
                        break;
                }
            }
        });
    }

    /**
     * @param isCycle true 循环 false 静止
     */
    public void setCycle(boolean isCycle) {
        this.isCycle = isCycle;
    }

    /**
     * @param container 设置轮播图的小圆点容器
     */
    public void setRollPointContainer(LinearLayout container) {
        this.mPointContainer = container;

        //初始化小圆点
        initDot();
    }

    /**
     * 开启轮播图的方法
     */
    public void startRoll() {
        MyPagerAdapter mpAdapter = new MyPagerAdapter();
        setAdapter(mpAdapter);
        if (isCycle) {
            //如果是无限循环 则开始滚动
            handler.sendEmptyMessageDelayed(0, 2000);
        }
    }

    /**
     * viewpager的水平滑动时间
     */
    private void setVpSpeed() {
        try {
            Field mField;
            mField = ViewPager.class.getDeclaredField("mScroller"); // 反射
            mField.setAccessible(true);
            FixedSpeedScroller mScroller = new FixedSpeedScroller(context, new AccelerateInterpolator());
            mScroller.setmDuration(500); // 500ms 数值越大时间越长
            mField.set(MyRollViewPager.this, mScroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 控制轮播图的点
     */
    private void initDot() {

        //清除集合里小圆点实体 确保不重复
        dotList.clear();
        //清除界面小圆点图像 确保不重复
        mPointContainer.removeAllViews();

        //小圆点的个数 如果后台返回的地址集合无数据，则取默认图片数组
        int pointNum = (imageList == null || imageList.size() == 0) ? ids.length : imageList.size();

        for (int i = 0; i < pointNum; i++) {
            View view = new View(context);
            if (i == 0) {
                //第一个小圆点点亮
                view.setBackgroundResource(R.drawable.dot_focus_red);
            } else {
                view.setBackgroundResource(R.drawable.dot_normal);
            }
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(18, 18);
            layoutParams.setMargins(8, 0, 8, 0);
            mPointContainer.addView(view, layoutParams);
            dotList.add(view);
        }
//        dotList.get(0).setBackgroundResource(R.drawable.dot_focus_red);

    }

    private class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            final SimpleDraweeView iv = new SimpleDraweeView(context);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);

            if (imageList != null && imageList.size() > 0) {
                //后台返回了地址集合
                ImageLoader.getInstance().loadImageLocalOrNet(iv, imageList.get(position % imageList.size()).getPicture());

                //设置点击回调
                if (myClickListener != null) {
                    iv.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            myClickListener.onMyClick(position % imageList.size());
                        }
                    });
                }
            } else {
                //取默认图片
                iv.setBackgroundResource(ids[position % ids.length]);
            }

            container.addView(iv);

            return iv;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //回收超出范围的iv
            container.removeView((SimpleDraweeView) object);
        }
    }

    /**
     * 轮播图点击事件的回调接口
     */
    public interface MyClickListener {
        void onMyClick(int position);
    }

    public void setOnMyListener(MyClickListener listener) {
        this.myClickListener = listener;
    }

}

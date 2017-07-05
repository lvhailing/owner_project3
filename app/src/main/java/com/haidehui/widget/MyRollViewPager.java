package com.haidehui.widget;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.haidehui.R;
import com.haidehui.model.ResultCycleIndex2B;
import com.haidehui.network.types.MouldList;
import com.haidehui.photo_preview.fresco.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * 轮播图 自定义的ViewPager
 */
public class MyRollViewPager extends ViewPager {
    private Context context;
    private List<View> dotList = new ArrayList<View>();
    private MyPagerAdapter mpAdapter;
    private MyClickListener myClickListener;
    private LinearLayout mPointContainer;
    private boolean isCycle = false;
    private MouldList<ResultCycleIndex2B> picList; //用于保存后台返回的图片的集合
    private int ids[] = {R.mipmap.bg_home_carousel_figure_normal, R.mipmap.bg_home_carousel_figure_normal}; // 假如后台没返回图片，则使用默认图片

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    setCurrentItem(getCurrentItem() + 1);
                    //启动下一次定时
                    handler.sendEmptyMessageDelayed(0, 2000);
                    break;
            }
        }
    };

    /**
     * @param context 构造函数
     */
    public MyRollViewPager(Context context, MouldList<ResultCycleIndex2B> pics, LinearLayout container) {
        super(context);

        this.context = context;
        this.picList = pics;
        this.mPointContainer = container;

        this.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // 每滑动一次 改变一次所有小圆点的状态
                resetCircleState(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    /**
     * @param pics 第二次获取到后台数据后，需要将本类的数据更新下
     */
    public void setPicList(MouldList<ResultCycleIndex2B> pics) {
        this.picList = pics;
    }

    /**
     * @param isCycle true 循环 false 静止
     */
    public void setCycle(boolean isCycle) {
        this.isCycle = isCycle;
    }

    /**
     * 第一次进来，开启滚动
     */
    public void startRoll() {
        mpAdapter = new MyPagerAdapter();
        setAdapter(mpAdapter);

        //初始化小圆点
        initDot();
        //重新设置小圆点状态
        resetCircleState(getCurrentItem());

        //如果是无限循环
        if (isCycle) {
            //移除当前定时（移除之前的message，防止再次进入时message重复）
            handler.removeCallbacksAndMessages(null);   //切记参数传null
            //启动本次定时（开始发送定时message）
            handler.sendEmptyMessageDelayed(0, 2000);
        }
    }

    /**
     * 第二次获取到后台数据后 重启滚动
     */
    public void reStartRoll() {
        //第二次获取到后台数据后

        // 直接刷新这个adapter
        mpAdapter.notifyDataSetChanged();

        //清除集合里小圆点实体 确保不重复
        dotList.clear();
        //清除界面小圆点图像 确保不重复
        mPointContainer.removeAllViews();
        //之后，再重新初始化小圆点
        initDot();
        //重新设置小圆点状态
        resetCircleState(getCurrentItem());

        //如果是无限循环
        if (isCycle) {
            //移除当前定时（移除之前的message，防止再次进入时message重复）
            handler.removeCallbacksAndMessages(null);   //切记参数传null
            //启动本次定时（开始发送定时message）
            handler.sendEmptyMessageDelayed(0, 2000);
        }
    }

    //重新设置小圆点状态
    private void resetCircleState(int position) {
        //因为是无限循环 position会无限增大，所以%
        int index = position % dotList.size();
        for (int i = 0; i < dotList.size(); i++) {
            dotList.get(i).setBackgroundResource(R.drawable.dot_normal);
        }
        dotList.get(index).setBackgroundResource(R.drawable.dot_focus_red);
    }

    /**
     * 控制轮播图的点
     */
    private void initDot() {
        //小圆点的个数 如果后台返回的地址集合无数据，则取默认图片数组
        int pointNum = (picList == null || picList.size() == 0) ? ids.length : picList.size();
        for (int i = 0; i < pointNum; i++) {
            View view = new View(context);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(18, 18);
            layoutParams.setMargins(8, 0, 8, 0);
            mPointContainer.addView(view, layoutParams);
            dotList.add(view);
        }
    }

    //切换到其他tab时，需要移除当前定时
    public void pause() {
        //如果是无限循环
        if (isCycle) {
            //移除当前定时（移除之前的message，防止再次进入时message重复）
            handler.removeCallbacksAndMessages(null);   //切记参数传null
        }
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

            if (picList != null && picList.size() > 0) {
                //后台返回了地址集合
                ImageLoader.getInstance().loadImageLocalOrNet(iv, picList.get(position % picList.size()).getPicture());

                //设置了点击回调，则回调activity
                if (myClickListener != null) {
                    iv.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            myClickListener.onMyClick(position % picList.size());
                        }
                    });
                }
            } else {
                // 取默认图片
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
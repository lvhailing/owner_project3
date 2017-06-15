package com.haidehui.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haidehui.R;
import com.haidehui.network.BaseParams;
import com.haidehui.network.BaseRequester;
import com.haidehui.network.HtmlRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 底部导航--房源模块
 */
public class HouseResourcesFragment extends Fragment implements OnClickListener {
    private Context context;
    private View mView;
    private RelativeLayout rl_house_resources_type, rl_house_resources_price, rl_house_function; // 顶部的类型、价格、功能
    private ImageView iv_select_type, iv_select_price, iv_select_function; // 顶部的类型、价格、功能后面的小箭头
    private View v_hidden; // 隐藏的 类型、价格、功能布局背景
    private LinearLayout ll_hidden_type, ll_hidden_price, ll_hidden_function; // 点击顶部的类型、价格、功能等时对应的布局
    private TextView tv1, tv2, tv3, tv4, tv5, tv6; // 顶部的类型( 公寓，商铺，别墅，学区，土地，庄园)
    private TextView tv_1, tv_2, tv_3, tv_4, tv_5, tv_6, tv_7; // 顶部的价格( 不限，50万元以下，50-100万元，100-200万元，200-500万元，500-1000万元,1000万以上)
    private TextView tv1_func, tv2_func, tv3_func, tv4_func, tv5_func; // 顶部的功能( 投资，自住，度假，海景，移民)
    private Button btn_type_reset, btn_type_sure; // 类型布局里面的重置，确定按钮
    private Button btn_func_reset, btn_func_sure; // 功能布局里面的重置，确定按钮

    private boolean isShow = false;
    private boolean isOpened = false;   //动画是否开启
    private int currentFlag;  //当前选择哪个按钮  1、类型按钮  2、价格按钮  3、功能按钮
    private List<String> functions = new ArrayList<>();
    private List<String> types = new ArrayList<>();
    private String function = "";
    private String type = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_house_resources, container, false);
            try {
                initView(mView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (mView.getParent() != null) {
                ((ViewGroup) mView.getParent()).removeView(mView);
            }
        }

        return mView;
    }

    private void initView(View mView) {
        context = getActivity();
        rl_house_resources_type = (RelativeLayout) mView.findViewById(R.id.rl_house_resources_type);
        rl_house_resources_price = (RelativeLayout) mView.findViewById(R.id.rl_house_resources_price);
        rl_house_function = (RelativeLayout) mView.findViewById(R.id.rl_house_function);
        iv_select_type = (ImageView) mView.findViewById(R.id.iv_select_type);
        iv_select_price = (ImageView) mView.findViewById(R.id.iv_select_price);
        iv_select_function = (ImageView) mView.findViewById(R.id.iv_select_function);
        v_hidden = mView.findViewById(R.id.v_hidden);
        ll_hidden_type = (LinearLayout) mView.findViewById(R.id.ll_hidden_type);
        ll_hidden_price = (LinearLayout) mView.findViewById(R.id.ll_hidden_price);
        ll_hidden_function = (LinearLayout) mView.findViewById(R.id.ll_hidden_function);

        // 类型展开布局里的按钮
        tv1 = (TextView) mView.findViewById(R.id.tv1);
        tv2 = (TextView) mView.findViewById(R.id.tv2);
        tv3 = (TextView) mView.findViewById(R.id.tv3);
        tv4 = (TextView) mView.findViewById(R.id.tv4);
        tv5 = (TextView) mView.findViewById(R.id.tv5);
        tv6 = (TextView) mView.findViewById(R.id.tv6);
        btn_type_reset = (Button) mView.findViewById(R.id.btn_type_reset);
        btn_type_sure = (Button) mView.findViewById(R.id.btn_type_sure);

        // 价格展开布局
        tv_1 = (TextView) mView.findViewById(R.id.tv_1);
        tv_2 = (TextView) mView.findViewById(R.id.tv_2);
        tv_3 = (TextView) mView.findViewById(R.id.tv_3);
        tv_4 = (TextView) mView.findViewById(R.id.tv_4);
        tv_5 = (TextView) mView.findViewById(R.id.tv_5);
        tv_6 = (TextView) mView.findViewById(R.id.tv_6);
        tv_7 = (TextView) mView.findViewById(R.id.tv_7);

        // 功能展开布局里的按钮
        tv1_func = (TextView) mView.findViewById(R.id.tv1_func);
        tv2_func = (TextView) mView.findViewById(R.id.tv2_func);
        tv3_func = (TextView) mView.findViewById(R.id.tv3_func);
        tv4_func = (TextView) mView.findViewById(R.id.tv4_func);
        tv5_func = (TextView) mView.findViewById(R.id.tv5_func);
        btn_func_reset = (Button) mView.findViewById(R.id.btn_func_reset);
        btn_func_sure = (Button) mView.findViewById(R.id.btn_func_sure);


        v_hidden.setOnClickListener(this);

        rl_house_resources_type.setOnClickListener(this);
        rl_house_resources_price.setOnClickListener(this);
        rl_house_function.setOnClickListener(this);

        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        tv3.setOnClickListener(this);
        tv4.setOnClickListener(this);
        tv5.setOnClickListener(this);
        tv6.setOnClickListener(this);
        btn_type_reset.setOnClickListener(this);
        btn_type_sure.setOnClickListener(this);

        tv_1.setOnClickListener(this);
        tv_2.setOnClickListener(this);
        tv_3.setOnClickListener(this);
        tv_4.setOnClickListener(this);
        tv_5.setOnClickListener(this);
        tv_6.setOnClickListener(this);
        tv_7.setOnClickListener(this);

        tv1_func.setOnClickListener(this);
        tv2_func.setOnClickListener(this);
        tv3_func.setOnClickListener(this);
        tv4_func.setOnClickListener(this);
        tv5_func.setOnClickListener(this);
        btn_func_reset.setOnClickListener(this);
        btn_func_sure.setOnClickListener(this);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (getActivity() != null) {
                requestData();
            }

        } else {

        }

    }

    @Override
    public void onResume() {
        requestData();
        super.onResume();

    }

    //开启动画
    private void openShopping(LinearLayout ll_hidden_layout) {
        ObjectAnimator oa = ObjectAnimator.ofFloat(ll_hidden_layout, "translationY", -ll_hidden_layout.getMeasuredHeight(), 0f);
        oa.setDuration(200);
        oa.start();
        v_hidden.setVisibility(View.VISIBLE);
        ll_hidden_layout.setVisibility(View.VISIBLE);
        isOpened = true;
    }

    //关闭动画
    private void closeShopping(final LinearLayout ll_hidden_layout) {
        ObjectAnimator oa = ObjectAnimator.ofFloat(ll_hidden_layout, "translationY", 0f, -ll_hidden_layout.getMeasuredHeight());
        oa.setDuration(200);
        oa.start();
        oa.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                v_hidden.setVisibility(View.GONE);
                ll_hidden_layout.setVisibility(View.GONE);
                resetLayout(ll_hidden_layout);
            }
        });
        isOpened = false;
    }

    //将布局重置到原始位置
    private void resetLayout(final LinearLayout ll_hidden_layout) {
        ObjectAnimator oa = ObjectAnimator.ofFloat(ll_hidden_layout, "translationY", -ll_hidden_layout.getMeasuredHeight(), 0f);
        oa.setDuration(0);
        oa.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_house_resources_type:  // 类型
                if (isOpened) {
                    //动画是开启状态
                    if (currentFlag == 1) {
                        //类型处于展开状态，则需关闭动画，且箭头置成向下
                        iv_select_type.setBackgroundResource(R.mipmap.icon_oversea_down);
                        closeShopping(ll_hidden_type);
                    } else {
                        //价格或功能处于展开状态
                        ll_hidden_type.setVisibility(View.VISIBLE);
                        ll_hidden_price.setVisibility(View.GONE);
                        ll_hidden_function.setVisibility(View.GONE);
                        iv_select_type.setBackgroundResource(R.mipmap.icon_oversea_up);
                    }
                } else {
                    //动画是关闭状态
                    openShopping(ll_hidden_type);
                    iv_select_type.setBackgroundResource(R.mipmap.icon_oversea_up);
                }

                currentFlag = 1;

                //无论价格、功能现状如何，都让它们箭头向下
                iv_select_price.setBackgroundResource(R.mipmap.icon_oversea_down);
                iv_select_function.setBackgroundResource(R.mipmap.icon_oversea_down);

                break;
            case R.id.rl_house_resources_price:  // 价格
                if (isOpened) {
                    //动画是开启状态
                    if (currentFlag == 2) {
                        //价格处于展开状态，则需关闭动画，且箭头置成向下
                        iv_select_price.setBackgroundResource(R.mipmap.icon_oversea_down);
                        closeShopping(ll_hidden_price);
                    } else {
                        //类型或功能处于展开状态
                        ll_hidden_type.setVisibility(View.GONE);
                        ll_hidden_price.setVisibility(View.VISIBLE);
                        ll_hidden_function.setVisibility(View.GONE);
                        iv_select_price.setBackgroundResource(R.mipmap.icon_oversea_up);
                    }
                } else {
                    //动画是关闭状态
                    openShopping(ll_hidden_price);
                    iv_select_price.setBackgroundResource(R.mipmap.icon_oversea_up);
                }

                currentFlag = 2;

                //无论类型、功能现状如何，都让它们箭头向下
                iv_select_type.setBackgroundResource(R.mipmap.icon_oversea_down);
                iv_select_function.setBackgroundResource(R.mipmap.icon_oversea_down);

                break;
            case R.id.rl_house_function:  // 功能
                if (isOpened) {
                    //动画是开启状态
                    if (currentFlag == 3) {
                        //功能处于展开状态，则需关闭动画，且箭头置成向下
                        iv_select_function.setBackgroundResource(R.mipmap.icon_oversea_down);
                        closeShopping(ll_hidden_function);
                    } else {
                        //价格或类型处于展开状态
                        ll_hidden_type.setVisibility(View.GONE);
                        ll_hidden_price.setVisibility(View.GONE);
                        ll_hidden_function.setVisibility(View.VISIBLE);
                        iv_select_function.setBackgroundResource(R.mipmap.icon_oversea_up);
                    }
                } else {
                    //动画是关闭状态
                    openShopping(ll_hidden_function);
                    iv_select_function.setBackgroundResource(R.mipmap.icon_oversea_up);
                }

                currentFlag = 3;

                //无论类型、价格现状如何，都让它们箭头向下
                iv_select_type.setBackgroundResource(R.mipmap.icon_oversea_down);
                iv_select_price.setBackgroundResource(R.mipmap.icon_oversea_down);

                break;
            case R.id.v_hidden:  // 隐藏布局 关闭动画
                if (currentFlag == 1) {
                    closeShopping(ll_hidden_type);
                } else if (currentFlag == 2) {
                    closeShopping(ll_hidden_price);
                } else {
                    closeShopping(ll_hidden_function);
                }

                iv_select_type.setBackgroundResource(R.mipmap.icon_oversea_down);
                iv_select_price.setBackgroundResource(R.mipmap.icon_oversea_down);
                iv_select_function.setBackgroundResource(R.mipmap.icon_oversea_down);

                break;
            case R.id.tv1:  // 类型：公寓
                clickTypeItem(tv1, "gy");
                break;
            case R.id.tv2: // 类型：商铺
                clickTypeItem(tv2, "sp");
                break;
            case R.id.tv3:  // 类型： 别墅
                clickTypeItem(tv3, "bs");
                break;
            case R.id.tv4:  // 类型：学区
                clickTypeItem(tv4, "xq");
                break;
            case R.id.tv5: // 类型：土地
                clickTypeItem(tv5, "td");
                break;
            case R.id.tv6:  // 类型： 庄园
                clickTypeItem(tv6, "zy");
                break;
            case R.id.btn_type_reset:  // 类型： 重置
                clickTypeBtnReset();
                break;
            case R.id.btn_type_sure:  // 类型： 确定

                break;
            case R.id.tv1_func:  // 功能：投资
                clickFunctionItem(tv1_func, "touzi");
                break;
            case R.id.tv2_func:  // 功能：自住
                clickFunctionItem(tv2_func, "zizhu");
                break;
            case R.id.tv3_func:  // 功能：度假
                clickFunctionItem(tv3_func, "dj");
                break;
            case R.id.tv4_func:  // 功能：海景
                clickFunctionItem(tv4_func, "hj");
                break;
            case R.id.tv5_func:  // 功能：移民
                clickFunctionItem(tv5_func, "ym");
                break;
            case R.id.btn_func_reset:  // 功能： 重置
                clickFunctionBtnReset();
                break;
            case R.id.btn_func_sure:  // 功能： 确定

                break;

            default:
                break;
        }
    }

    // 类型里的每个按钮被选时调的方法
    private void clickTypeItem(TextView tv, String item) {
        if (functions.contains(item)) {
            //添加过
            functions.remove(item);
            tv.setTextColor(getResources().getColor(R.color.txt_black));
            tv.setBackgroundResource(R.drawable.shape_center_gray_white);
        } else {
            //未添加过
            functions.add(item);
            tv.setTextColor(getResources().getColor(R.color.txt_orange));
            tv.setBackgroundResource(R.drawable.shape_center_orange_white);
        }
        StringBuffer sb = new StringBuffer();
        for (String str : types) {
            sb.append(str);
            sb.append(",");
        }
        String strResult = sb.toString();
        type = strResult.substring(0, strResult.length());
    }

    // 类型里的重置按钮点击时调的方法
    private void clickTypeBtnReset() {
        types.clear();
       changTextColorAndBg(tv1);
       changTextColorAndBg(tv2);
       changTextColorAndBg(tv3);
       changTextColorAndBg(tv4);
       changTextColorAndBg(tv5);
       changTextColorAndBg(tv6);
    }

    // 功能里的每个按钮被选时调的方法
    private void clickFunctionItem(TextView tv, String item) {
        if (functions.contains(item)) {
            //添加过
            functions.remove(item);
            tv.setTextColor(getResources().getColor(R.color.txt_black));
            tv.setBackgroundResource(R.drawable.shape_center_gray_white);
        } else {
            //未添加过
            functions.add(item);
            tv.setTextColor(getResources().getColor(R.color.txt_orange));
            tv.setBackgroundResource(R.drawable.shape_center_orange_white);
        }
        StringBuffer sb = new StringBuffer();
        for (String str : functions) {
            sb.append(str);
            sb.append(",");
        }
        String strResult = sb.toString();
        function = strResult.substring(0, strResult.length());
    }

    // 功能里的重置按钮点击时调的方法
    private void clickFunctionBtnReset() {
        functions.clear();
        changTextColorAndBg(tv1_func);
        changTextColorAndBg(tv2_func);
        changTextColorAndBg(tv3_func);
        changTextColorAndBg(tv4_func);
        changTextColorAndBg(tv5_func);
    }

    // 重置textView 的颜色和背景
    private void changTextColorAndBg(TextView tv) {
        tv.setTextColor(getResources().getColor(R.color.txt_black));
        tv.setBackgroundResource(R.drawable.shape_center_gray_white);
    }


    //我的主页面数据
    private void requestData() {
        Map<String, Object> param = new HashMap<>();
        param.put("type", "android");
        HtmlRequest.getMineData(context, param, new BaseRequester.OnRequestListener() {
            @Override
            public void onRequestFinished(BaseParams params) {
                if (params.result != null) {


                } else {
                    Toast.makeText(context, "加载失败，请确认网络通畅", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}

package com.haidehui.fragment;

import android.content.Context;
import android.content.Intent;
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
import com.haidehui.act.CustomerInfoActivity;
import com.haidehui.act.MyInfoActivity;
import com.haidehui.act.PartnerIdentifyActivity;
import com.haidehui.network.BaseParams;
import com.haidehui.network.BaseRequester;
import com.haidehui.network.HtmlRequest;

import java.util.HashMap;
import java.util.Map;

import static com.haidehui.R.id.btn_type_reset;
import static com.haidehui.R.id.tv_customer_follow;
import static com.haidehui.R.id.tv_rengou_state;

/**
 * 底部导航--房源模块
 */
public class HouseResourcesFragment extends Fragment implements OnClickListener {
    private Context context;
    private View mView;

    private RelativeLayout rl_house_resources_type, rl_house_resources_price, rl_house_function; // 顶部的类型、价格、功能
    private ImageView iv_select_type, iv_select_price, iv_select_function; // 顶部的类型、价格、功能后面的小箭头
    private View v_hidden; // 隐藏的类型或状态布局背景
    private LinearLayout ll_hidden_type, ll_hidden_price, ll_hidden_function; // 点击顶部的类型、价格、功能等时对应的布局
    private TextView tv1,tv2,tv3,tv4,tv5,tv6; // 顶部的类型( 公寓，商铺，别墅，学区，土地，庄园)
    private TextView tv_1,tv_2,tv_3,tv_4,tv_5,tv_6,tv_7; // 顶部的价格( 不限，50万元以下，50-100万元，100-200万元，200-500万元，500-1000万元,1000万以上)
    private TextView tv1_func,tv2_func,tv3_func,tv4_func,tv5_func; // 顶部的功能( 投资，自住，度假，海景，移民)
    private Button btn_type_reset,btn_type_sure; // 类型布局里面的重置，确定按钮
    private Button btn_func_reset,btn_func_sure; // 功能布局里面的重置，确定按钮

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

        tv1 = (TextView) mView.findViewById(R.id.tv1);
        tv2 = (TextView) mView.findViewById(R.id.tv2);
        tv3 = (TextView) mView.findViewById(R.id.tv3);
        tv4 = (TextView) mView.findViewById(R.id.tv4);
        tv5 = (TextView) mView.findViewById(R.id.tv5);
        tv6 = (TextView) mView.findViewById(R.id.tv6);
        btn_type_reset = (Button) mView.findViewById(R.id.btn_type_reset);
        btn_type_sure = (Button) mView.findViewById(R.id.btn_type_sure);

        tv_1 = (TextView) mView.findViewById(R.id.tv_1);
        tv_2 = (TextView) mView.findViewById(R.id.tv_2);
        tv_3 = (TextView) mView.findViewById(R.id.tv_3);
        tv_4 = (TextView) mView.findViewById(R.id.tv_4);
        tv_5 = (TextView) mView.findViewById(R.id.tv_5);
        tv_6 = (TextView) mView.findViewById(R.id.tv_6);
        tv_7 = (TextView) mView.findViewById(R.id.tv_7);

        tv1_func = (TextView) mView.findViewById(R.id.tv1_func);
        tv2_func = (TextView) mView.findViewById(R.id.tv2_func);
        tv3_func = (TextView) mView.findViewById(R.id.tv3_func);
        tv4_func = (TextView) mView.findViewById(R.id.tv4_func);
        tv5_func = (TextView) mView.findViewById(R.id.tv5_func);

        btn_func_reset = (Button) mView.findViewById(R.id.btn_func_reset);
        btn_func_sure = (Button) mView.findViewById(R.id.btn_func_sure);


        rl_house_resources_type.setOnClickListener(this);
        rl_house_resources_price.setOnClickListener(this);
        rl_house_function.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_email://跳转邮件

                break;
            case R.id.layout_my_info://跳转我的信息
                Intent i_my_info = new Intent(context, MyInfoActivity.class);
                i_my_info.putExtra("name", "明天你好");
                startActivity(i_my_info);
                break;
            case R.id.tv_customer_info://跳转客户信息
                Intent i_customer_info = new Intent(context, CustomerInfoActivity.class);
                startActivity(i_customer_info);
                break;
            case tv_customer_follow://跳转客户跟踪

                break;
            case tv_rengou_state://跳转认购状态

                break;
            case R.id.layout_identify://跳转事业合伙人认证
                Intent i_identify = new Intent(context, PartnerIdentifyActivity.class);
                startActivity(i_identify);
                break;
            case R.id.layout_account_book://跳转我的账本
                Intent i_account_book = new Intent(context, PartnerIdentifyActivity.class);
                startActivity(i_account_book);
                break;

            default:
                break;
        }
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

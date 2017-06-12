package com.haidehui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haidehui.R;

/**
 * 底部导航---产品
 */
public class EssentialInfoFragment extends Fragment {
    private View mView;
    private Context context;
    private TextView tv_house_name; // 名称
    private TextView tv_house_function; // 功能
    private TextView tv_house_type; // 类型
    private TextView tv_decoration_standard; // 装修标准
    private TextView tv_house_year; // 年代
    private TextView tv_house_floor; // 楼层
    private TextView tv_property_fee; // 装修费用
    private TextView tv_house_description; // 房源描述

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_essential_info, container, false);
            try {
                initView(mView);
                initData();
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

    @Override
    public void onResume() {
        super.onResume();
    }


    private void initView(View mView) {
        context = getActivity();
        tv_house_name = (TextView) mView.findViewById(R.id.tv_house_name);
        tv_house_function = (TextView) mView.findViewById(R.id.tv_house_function);
        tv_house_type = (TextView) mView.findViewById(R.id.tv_house_type);
        tv_decoration_standard = (TextView) mView.findViewById(R.id.tv_decoration_standard);
        tv_house_year = (TextView) mView.findViewById(R.id.tv_house_year);
        tv_house_floor = (TextView) mView.findViewById(R.id.tv_house_floor);
        tv_property_fee = (TextView) mView.findViewById(R.id.tv_property_fee);
        tv_house_description = (TextView) mView.findViewById(R.id.tv_house_description);

    }

    private void initData() {
    }


}

package com.haidehui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haidehui.R;
import com.haidehui.act.AccountBookActivity;
import com.haidehui.act.CustomerFollowActivity;
import com.haidehui.act.CustomerInfoActivity;
import com.haidehui.act.HotHouseActivity;
import com.haidehui.act.MyInfoActivity;
import com.haidehui.act.PartnerIdentifyActivity;
import com.haidehui.act.RenGouStatusActivity;
import com.haidehui.network.BaseParams;
import com.haidehui.network.BaseRequester;
import com.haidehui.network.HtmlRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * 底部导航 我的
 */
public class MineFragment extends Fragment implements OnClickListener {
    private Context context;
    private View mView;
    private RelativeLayout layout_email;
    private TextView tv_email_number;
    private RelativeLayout layout_my_info;
    private TextView tv_name;
    private TextView tv_phone;
    private TextView tv_customer_info;
    private TextView tv_customer_follow;
    private TextView tv_rengou_state;
    private RelativeLayout layout_indentify;
    private RelativeLayout layout_account_book;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_mine, container, false);
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

    private void initView(View mView) {
        context=getActivity();
        layout_email= (RelativeLayout) mView.findViewById(R.id.layout_email);
        tv_email_number= (TextView) mView.findViewById(R.id.tv_email_number);
        layout_my_info = (RelativeLayout) mView.findViewById(R.id.layout_my_info);
        tv_name= (TextView) mView.findViewById(R.id.tv_name);
        tv_phone= (TextView) mView.findViewById(R.id.tv_phone);
        tv_customer_info= (TextView) mView.findViewById(R.id.tv_customer_info);
        tv_customer_follow= (TextView) mView.findViewById(R.id.tv_customer_follow);
        tv_rengou_state= (TextView) mView.findViewById(R.id.tv_rengou_state);
        layout_indentify= (RelativeLayout) mView.findViewById(R.id.layout_identify);
        layout_account_book=(RelativeLayout) mView.findViewById(R.id.layout_account_book);
    }
    private void initData() {
        layout_email.setOnClickListener(this);
        layout_my_info.setOnClickListener(this);
        tv_customer_info.setOnClickListener(this);
        tv_customer_follow.setOnClickListener(this);
        tv_rengou_state.setOnClickListener(this);
        layout_indentify.setOnClickListener(this);
        layout_account_book.setOnClickListener(this);

    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if(getActivity()!=null){
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
                i_my_info.putExtra("name","明天你好");
                startActivity(i_my_info);
                break;
            case R.id.tv_customer_info://跳转客户信息
                Intent i_customer_info = new Intent(context, CustomerInfoActivity.class);
                startActivity(i_customer_info);
                break;
            case R.id.tv_customer_follow://跳转客户跟踪
                Intent i_customer_follow = new Intent(context, CustomerFollowActivity.class);
                startActivity(i_customer_follow);
                break;
            case R.id.tv_rengou_state://跳转认购状态
                Intent i_rengou = new Intent(context, RenGouStatusActivity.class);
                startActivity(i_rengou);
                break;
            case R.id.layout_identify://跳转事业合伙人认证
                Intent i_identify = new Intent(context, PartnerIdentifyActivity.class);
                startActivity(i_identify);
                break;
            case R.id.layout_account_book://跳转我的账本
                Intent i_account_book = new Intent(context, AccountBookActivity.class);
                startActivity(i_account_book);
                break;

            default:
                break;
        }
    }

    //我的主页面数据
    private void requestData() {
        Map<String, Object> param = new HashMap<>();
        param.put("userId", "17021511395798036131");
        HtmlRequest.getMineData(context, param, new BaseRequester.OnRequestListener() {
                    @Override
                    public void onRequestFinished(BaseParams params) {
                        if (params.result != null) {


                        } else {
                            Toast.makeText(context, "加载失败，请确认网络通畅",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
    }

}

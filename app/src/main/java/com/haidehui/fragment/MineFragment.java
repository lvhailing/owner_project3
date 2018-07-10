package com.haidehui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haidehui.R;
import com.haidehui.activity.AccountBookActivity;
import com.haidehui.activity.CustomerInfoActivity;
import com.haidehui.activity.CustomerTrackingActivity;
import com.haidehui.activity.ExplainOrderListActivity;
import com.haidehui.activity.MessageActivity;
import com.haidehui.activity.MyBankActivity;
import com.haidehui.activity.MyBusinessPartnerActivity;
import com.haidehui.activity.MyInfoActivity;
import com.haidehui.activity.PartnerIdentifyActivity;
import com.haidehui.activity.RecommendActivity;
import com.haidehui.activity.RenGouStatusActivity;
import com.haidehui.activity.SettingActivity;
import com.haidehui.model.MineData2B;
import com.haidehui.model.ResultMessageInfoContentBean;
import com.haidehui.network.BaseParams;
import com.haidehui.network.BaseRequester;
import com.haidehui.network.HtmlRequest;
import com.haidehui.uitls.DESUtil;
import com.haidehui.uitls.PreferenceUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import android.widget.ImageView;

/**
 * 底部导航--- 我的模块
 */
public class MineFragment extends Fragment implements OnClickListener {
    private Context context;
    private View mView;
    private ImageView iv_message; // 消息图标
    private ImageView iv_photo; // 头像
    private ImageView iv_sign;
    private TextView tv_message_total; // 消息总数
    private TextView tv_real_name; // 用户姓名
    private TextView tv_code; // 推荐码
    private TextView tv_total_commission; // 帐本金额
    private TextView tv_customer_info; // 客户信息
    private TextView tv_customer_follow; // 客户跟踪
    private TextView tv_subscription_state; // 认购状态
    private RelativeLayout rl_my_info; // 个人信息布局
    private RelativeLayout rl_business_partner_certification; // 事业合伙人认证
    private RelativeLayout rl_account_book; // 我的帐本
    private RelativeLayout rl_my_bankcard; // 我的银行卡
    private RelativeLayout rl_mine_setting;    // 设置
    private String checkStatus = "init";   // 认证状态
    private String userId = "";
    private ResultMessageInfoContentBean bean;  // 消息参数
    private MineData2B data;
//    private CircularImage iv_photo;
    /**
     * 图片保存SD卡位置
     */
    private final static String IMG_PATH = Environment.getExternalStorageDirectory() + "/haidehui/imgs/";
    private int messageInt;
    private String status; // 认证状态
    private RelativeLayout rl_recommend; // 推荐海德汇APP给朋友
    private RelativeLayout rl_explain_order; // 预约说明会
    private RelativeLayout rl_business_partner; // 我的事业合伙人

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (getActivity() != null) {
                requestData();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_mine, container, false);
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

        try {
            checkStatus = DESUtil.decrypt(PreferenceUtil.getCheckStatus());
            userId = DESUtil.decrypt(PreferenceUtil.getUserId());

        } catch (Exception e) {
            e.printStackTrace();
        }
        bean = new ResultMessageInfoContentBean();

        context = getActivity();
        iv_message = (ImageView) mView.findViewById(R.id.iv_message);
        iv_photo = (ImageView) mView.findViewById(R.id.iv_photo);
        iv_sign = (ImageView) mView.findViewById(R.id.iv_sign);

        tv_message_total = (TextView) mView.findViewById(R.id.tv_message_total);
        tv_real_name = (TextView) mView.findViewById(R.id.tv_real_name);
        tv_code = (TextView) mView.findViewById(R.id.tv_code);
        tv_total_commission = (TextView) mView.findViewById(R.id.tv_total_commission);
        tv_customer_info = (TextView) mView.findViewById(R.id.tv_customer_info);
        tv_customer_follow = (TextView) mView.findViewById(R.id.tv_customer_follow);
        tv_subscription_state = (TextView) mView.findViewById(R.id.tv_subscription_state);

        rl_my_info = (RelativeLayout) mView.findViewById(R.id.rl_my_info);
        rl_business_partner_certification = (RelativeLayout) mView.findViewById(R.id.rl_business_partner_certification);
        rl_account_book = (RelativeLayout) mView.findViewById(R.id.rl_account_book);
        rl_my_bankcard = (RelativeLayout) mView.findViewById(R.id.rl_my_bankcard);
        rl_recommend = (RelativeLayout) mView.findViewById(R.id.rl_recommend);
        rl_mine_setting = (RelativeLayout) mView.findViewById(R.id.rl_mine_setting);
        rl_explain_order = (RelativeLayout) mView.findViewById(R.id.rl_explain_order);
        rl_business_partner = (RelativeLayout) mView.findViewById(R.id.rl_business_partner);

        iv_message.setOnClickListener(this);
        tv_message_total.setOnClickListener(this);
        rl_my_info.setOnClickListener(this);
        tv_customer_info.setOnClickListener(this);
        tv_customer_follow.setOnClickListener(this);
        tv_subscription_state.setOnClickListener(this);
        rl_business_partner_certification.setOnClickListener(this);
        rl_account_book.setOnClickListener(this);
        rl_my_bankcard.setOnClickListener(this);
        rl_recommend.setOnClickListener(this);
        rl_mine_setting.setOnClickListener(this);
        rl_explain_order.setOnClickListener(this);
        rl_business_partner.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        requestData();
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        if (data != null) {
            status = data.getCheckStatus();
        }
        switch (v.getId()) {
            case R.id.tv_message_total:
            case R.id.iv_message:// 跳转消息页面
                intent = new Intent(context, MessageActivity.class); // 消息页面
                startActivity(intent);
                break;
            case R.id.rl_my_info://跳转我的信息
                if (data != null) {
                    if (data.getHeadPhoto() != null && data.getRealName() != null) {
                        intent = new Intent(context, MyInfoActivity.class);
                        intent.putExtra("headPhoto", data.getHeadPhoto());
                        intent.putExtra("realName", data.getRealName());
                        startActivity(intent);
                    }
                }
                break;
            case R.id.tv_customer_info://跳转客户信息
                intent = new Intent(context, CustomerInfoActivity.class);
                intent.putExtra("checkStatus",status);
                startActivity(intent);
                break;
            case R.id.tv_customer_follow://跳转客户跟踪列表
                intent = new Intent(context, CustomerTrackingActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_subscription_state://跳转认购状态
                 intent = new Intent(context, RenGouStatusActivity.class);
                 startActivity(intent);
                break;
            case R.id.rl_explain_order://跳转预约说明会
                intent = new Intent(context, ExplainOrderListActivity.class);
                intent.putExtra("checkStatus",status);
                startActivity(intent);
                break;
            case R.id.rl_business_partner_certification://跳转事业合伙人认证
                intent = new Intent(context, PartnerIdentifyActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_account_book://跳转我的账本
                intent = new Intent(context, AccountBookActivity.class);
                intent.putExtra("checkStatus",status);
                startActivity(intent);
                break;
            case R.id.rl_my_bankcard:  // 我的银行卡
                intent = new Intent(context, MyBankActivity.class);
                intent.putExtra("checkStatus",status);
                startActivity(intent);
                break;
            case R.id.rl_business_partner:  // 点击我的事业合伙人 跳转推荐列表
                 intent = new Intent(context, MyBusinessPartnerActivity.class);
                 startActivity(intent);
                break;
            case R.id.rl_recommend: // 推荐海德汇APP给朋友
                 intent = new Intent(context, RecommendActivity.class);
                 startActivity(intent);
                break;
            case R.id.rl_mine_setting:  // 设置页面
                intent = new Intent(context, SettingActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }
    }

    //我的主页面数据
    private void requestData() {
        try {
            userId = DESUtil.decrypt(PreferenceUtil.getUserId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        HashMap<String, Object> param = new HashMap<>();
        param.put("userId", userId);
        HtmlRequest.getMineData(context, param, new BaseRequester.OnRequestListener() {
            @Override
            public void onRequestFinished(BaseParams params) {
                if (params.result == null) {
//                    Toast.makeText(context, "加载失败，请确认网络通畅", Toast.LENGTH_LONG).show();
                    return;
                }
                data = (MineData2B) params.result;
                if (data != null) {
                    setData(data);
                }
            }
        });
    }

    private void setData(MineData2B data) {
        if (data.getMessageTotal() != null) {
            messageInt = Integer.parseInt(data.getMessageTotal());
        }
        if (messageInt > 9) {
            tv_message_total.setVisibility(View.VISIBLE);
            tv_message_total.setText("9+");
            tv_message_total.setTextSize(10f);
        } else {
            if (messageInt == 0) {
                tv_message_total.setVisibility(View.GONE);
            } else {
                tv_message_total.setVisibility(View.VISIBLE);
                tv_message_total.setText(data.getMessageTotal());
            }

        }
        tv_real_name.setText(data.getRealName());
        tv_code.setText("我的推荐码："+data.getRecommendCode());
        tv_total_commission.setText(data.getTotalCommission() + "元");

        String url = data.getHeadPhoto();
        if (!TextUtils.isEmpty(url)) {
            new ImageViewService().execute(url);
        } else {
            iv_photo.setImageDrawable(getResources().getDrawable(R.mipmap.user_icon));
        }
        String status = data.getCheckStatus();
        if ("success".equals(status)) {
            iv_sign.setImageResource(R.mipmap.img_identified);
        } else {
            iv_sign.setImageResource(R.mipmap.img_not_identify);
        }

        if(!TextUtils.isEmpty(data.getIdNo())) {
            try {
                PreferenceUtil.setIdNo(DESUtil.encrypt(data.getIdNo()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            PreferenceUtil.setIdNo("");
        }
    }

    /**
     * 获取网落图片资源
     * @return
     */
    class ImageViewService extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap data = getImageBitmap(params[0]);
            return data;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            if (result != null) {
                iv_photo.setImageBitmap(result);
                saveBitmap(result);
            } else {
                iv_photo.setImageDrawable(getResources().getDrawable(R.mipmap.user_icon));
            }
        }

    }

    private Bitmap getImageBitmap(String url) {
        URL imgUrl = null;
        Bitmap bitmap = null;
        try {
            imgUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) imgUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private Uri saveBitmap(Bitmap bm) {
        File tmpDir = new File(IMG_PATH);
        if (!tmpDir.exists()) {
            tmpDir.mkdirs();
        }
        File img = new File(IMG_PATH + "Test.png");
        try {
            FileOutputStream fos = new FileOutputStream(img);
            bm.compress(Bitmap.CompressFormat.PNG, 70, fos);
            fos.flush();
            fos.close();
            return Uri.fromFile(img);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

}

package com.haidehui.act;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.haidehui.R;
import com.haidehui.dialog.IntroductionsDialog;
import com.haidehui.fragment.AwardDetailsFragment;
import com.haidehui.fragment.CommissionDetailsFragment;
import com.haidehui.fragment.EssentialInfoFragment;
import com.haidehui.fragment.WithdrawDetailsFragment;
import com.haidehui.model.AccountBookCommission2B;
import com.haidehui.network.BaseParams;
import com.haidehui.network.BaseRequester;
import com.haidehui.network.HtmlRequest;
import com.haidehui.uitls.StringUtil;
import com.haidehui.widget.TitleBar;

import java.util.LinkedHashMap;


/**
 *  我的 --- 我的账本
 */
public class AccountBookActivity extends BaseActivity implements View.OnClickListener {
    private ImageView img_back;
    private ImageView img_income_intro;
    private TextView tv_not_give;
    private TextView tv_total_give;
    private TextView tv_gived;
    private TextView tv_commission;
    private TextView tv_award;
    private TextView tv_withdraw;
    private Resources mResource;

    private CommissionDetailsFragment fragment1;
    private AwardDetailsFragment fragment2;
    private WithdrawDetailsFragment fragment3;

    private Button btn_withdraw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.ac_account_book);
        initTopTitle();
        initView();
        initData();
        requestData();
    }

    private void initTopTitle() {
        TitleBar title = (TitleBar) findViewById(R.id.rl_title);
        title.setVisibility(View.GONE);
    }

    private void initView() {
        mResource = getResources();
        img_back= (ImageView) findViewById(R.id.img_back);
        img_income_intro= (ImageView) findViewById(R.id.img_income_intro);
        tv_not_give= (TextView) findViewById(R.id.tv_not_give);
        tv_total_give= (TextView) findViewById(R.id.tv_total_give);
        tv_gived= (TextView) findViewById(R.id.tv_gived);
        tv_commission= (TextView) findViewById(R.id.tv_commission);
        tv_award= (TextView) findViewById(R.id.tv_award);
        tv_withdraw= (TextView) findViewById(R.id.tv_withdraw);
        btn_withdraw= (Button) findViewById(R.id.btn_withdraw);

        StringUtil.changeButtonStyleThree(tv_commission,tv_award,tv_withdraw,
                R.id.tv_commission,mResource);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        fragment1 = new CommissionDetailsFragment();
        transaction.replace(R.id.fragment_container, fragment1);
        transaction.commit();
    }

    private void initData() {
        img_back.setOnClickListener(this);
        img_income_intro.setOnClickListener(this);
        tv_commission.setOnClickListener(this);
        tv_award.setOnClickListener(this);
        tv_withdraw.setOnClickListener(this);
        btn_withdraw.setOnClickListener(this);


    }
    @Override
    public void onClick(View v) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        switch (v.getId()){
            case R.id.img_back:
                finish();
                break;
            case R.id.img_income_intro:
                IntroductionsDialog dialog =new IntroductionsDialog(this);
                dialog.show();
                break;
            case R.id.tv_commission:
                StringUtil.changeButtonStyleThree(tv_commission,tv_award,tv_withdraw,
                        R.id.tv_commission,mResource);
                hideFragment(transaction);
                fragment1 = new CommissionDetailsFragment();
                transaction.replace(R.id.fragment_container, fragment1);
                transaction.commit();
                break;
            case R.id.tv_award:
                StringUtil.changeButtonStyleThree(tv_commission, tv_award, tv_withdraw,
                        R.id.tv_award, mResource);
                hideFragment(transaction);
                fragment2 = new AwardDetailsFragment();
                transaction.replace(R.id.fragment_container, fragment2);
                transaction.commit();
                break;
            case R.id.tv_withdraw:
                StringUtil.changeButtonStyleThree(tv_commission, tv_award, tv_withdraw,
                        R.id.tv_withdraw, mResource);
                hideFragment(transaction);
                fragment3 = new WithdrawDetailsFragment();
                transaction.replace(R.id.fragment_container, fragment3);
                transaction.commit();
                break;

            case R.id.btn_withdraw:

                Intent i_withdraw = new Intent(this,WithdrawActivity.class);          //  提现
                startActivity(i_withdraw);

                break;

            default:

                break;
        }
    }


    private void requestData() {
        LinkedHashMap<String, Object> param = new LinkedHashMap<>();
        param.put("userId", userId);
        HtmlRequest.getAccountBookData(this, param, new BaseRequester.OnRequestListener() {
                    @Override
                    public void onRequestFinished(BaseParams params) {
                        if (params.result == null) {
                            Toast.makeText(mContext, "加载失败，请确认网络通畅", Toast.LENGTH_LONG).show();
                            return;
                        }
                        AccountBookCommission2B data = (AccountBookCommission2B) params.result;
                        setData(data);
                    }
                }
        );
    }
    private void setData(AccountBookCommission2B data) {
        tv_not_give.setText(data.getAvailableCommission());
        tv_gived.setText(data.getSendedCommission());
        tv_total_give.setText(data.getTotalCommission());
    }

    // 去除所有的Fragment
    private void hideFragment(FragmentTransaction transaction) {
        if (fragment1 != null) {
            transaction.remove(fragment1);
        }
        if (fragment2 != null) {
            transaction.remove(fragment2);
        }
        if (fragment3 != null) {
            transaction.remove(fragment3);
        }
    }


}

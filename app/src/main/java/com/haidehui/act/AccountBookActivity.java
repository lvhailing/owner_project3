package com.haidehui.act;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.haidehui.R;
import com.haidehui.adapter.AccountBookAwardAdapter;
import com.haidehui.adapter.AccountBookCommissionAdapter;
import com.haidehui.adapter.AccountBookWithdrawAdapter;
import com.haidehui.dialog.IntroductionsDialog;
import com.haidehui.model.AccountBookAward2B;
import com.haidehui.model.AccountBookAward3B;
import com.haidehui.model.AccountBookCommission2B;
import com.haidehui.model.AccountBookCommission3B;
import com.haidehui.model.AccountBookWithDraw3B;
import com.haidehui.model.AccountBookWithdraw2B;
import com.haidehui.network.BaseParams;
import com.haidehui.network.BaseRequester;
import com.haidehui.network.HtmlRequest;
import com.haidehui.network.types.MouldList;
import com.haidehui.uitls.StringUtil;
import com.haidehui.widget.MyListView;
import com.haidehui.widget.TitleBar;

import java.util.HashMap;
import java.util.Map;


/**
 *  我的 --- 我的账本
 */
public class AccountBookActivity extends BaseActivity implements View.OnClickListener {
    private ScrollView scrollview;
    private ImageView img_back;
    private ImageView img_income_intro;
    private TextView tv_not_give;
    private TextView tv_total_give;
    private TextView tv_gived;
    private TextView tv_commission;
    private TextView tv_award;
    private TextView tv_withdraw;
    private MyListView lv_commission;
    private MyListView lv_award;
    private MyListView lv_withdraw;
    private AccountBookCommissionAdapter adapterCommission;
    private AccountBookAwardAdapter adapterAward;
    private AccountBookWithdrawAdapter adapterWithdraw;
    private MouldList<AccountBookCommission3B> commissionList;
    private MouldList<AccountBookAward3B> awardList;
    private MouldList<AccountBookWithDraw3B> withdrawlist;
    private Resources mResource;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.ac_account_book);
        initTopTitle();
        initView();
        initData();
        requestData();
        DefaultView();
    }

    private void initTopTitle() {
        TitleBar title = (TitleBar) findViewById(R.id.rl_title);
        title.setVisibility(View.GONE);
    }

    private void initView() {
        mResource = getResources();
        scrollview= (ScrollView) findViewById(R.id.scrollview);
        img_back= (ImageView) findViewById(R.id.img_back);
        img_income_intro= (ImageView) findViewById(R.id.img_income_intro);
        tv_not_give= (TextView) findViewById(R.id.tv_not_give);
        tv_total_give= (TextView) findViewById(R.id.tv_total_give);
        tv_gived= (TextView) findViewById(R.id.tv_gived);
        tv_commission= (TextView) findViewById(R.id.tv_commission);
        tv_award= (TextView) findViewById(R.id.tv_award);
        tv_withdraw= (TextView) findViewById(R.id.tv_withdraw);

        lv_commission= (MyListView) findViewById(R.id.lv_commission);
        lv_award= (MyListView) findViewById(R.id.lv_award);
        lv_withdraw= (MyListView) findViewById(R.id.lv_withdraw);

        lv_commission.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position,
                                    long id) {
                Intent intent = new Intent(AccountBookActivity.this, CommissionDetailsActivity.class);
                intent.putExtra("id", commissionList.get(position - 1).getId());
                startActivity(intent);
            }
        });
        lv_award.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position,
                                    long id) {
                Intent intent = new Intent(AccountBookActivity.this, AwardDetailsActivity.class);
                intent.putExtra("id", awardList.get(position - 1).getId());
                startActivity(intent);
            }
        });
        lv_withdraw.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position,
                                    long id) {
                Intent intent = new Intent(AccountBookActivity.this, WithDrawDetailsActivity.class);
                intent.putExtra("id", withdrawlist.get(position - 1).getId());
                startActivity(intent);
            }
        });

    }

    private void initData() {
        img_back.setOnClickListener(this);
        img_income_intro.setOnClickListener(this);
        tv_commission.setOnClickListener(this);
        tv_award.setOnClickListener(this);
        tv_withdraw.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                finish();
                break;
            case R.id.img_income_intro:
                IntroductionsDialog dialog =new IntroductionsDialog(this);
                dialog.show();
                break;
            case R.id.tv_commission:
                initPageData("commission");
                StringUtil.changeButtonStyleThree(tv_commission,tv_award,tv_withdraw,
                        R.id.tv_commission,mResource);
                break;
            case R.id.tv_award:
                initPageData("award");
                StringUtil.changeButtonStyleThree(tv_commission, tv_award, tv_withdraw,
                        R.id.tv_award, mResource);
                break;
            case R.id.tv_withdraw:
                initPageData("withdraw");
                StringUtil.changeButtonStyleThree(tv_commission, tv_award, tv_withdraw,
                        R.id.tv_withdraw, mResource);
                break;
        }
    }
    private void initPageData(String status) {
        if (status.equals("commission")) {
            lv_commission.setVisibility(View.VISIBLE);
            lv_award.setVisibility(View.GONE);
            lv_withdraw.setVisibility(View.GONE);
            requestCommissionList();
            scrollview.smoothScrollTo(0, 0);
        } else if (status.equals("award")) {
            lv_award.setVisibility(View.VISIBLE);
            lv_commission.setVisibility(View.GONE);
            lv_withdraw.setVisibility(View.GONE);
            requestAwardList();
            scrollview.smoothScrollTo(0, 0);
        }else if(status.equals("withdraw")) {
            lv_withdraw.setVisibility(View.VISIBLE);
            lv_award.setVisibility(View.GONE);
            lv_commission.setVisibility(View.GONE);
            requestWithDrawList();
            scrollview.smoothScrollTo(0, 0);
        }
    }

    private void DefaultView() {
        initPageData("commission");
        StringUtil.changeButtonStyleThree(tv_commission, tv_award, tv_withdraw,
                R.id.tv_commission, mResource);
    }

    /**
     * 动态设置ListView的高度
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(Context context,ListView listView,int dividerHeight) {
        if(listView == null)
            return;
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += (listItem.getMeasuredHeight()+dividerHeight);
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * listAdapter.getCount())+5;

        listView.setLayoutParams(params);
    }

    private void requestData() {
        Map<String, Object> param = new HashMap<>();
        param.put("userId", "17021511395798036131");
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
     private void requestCommissionList() {  // 获取佣金收益列表数据
        Map<String, Object> param = new HashMap<>();
        param.put("userId", "17021511395798036131");

        try {
            HtmlRequest.getCommissionList(mContext, param, new BaseRequester.OnRequestListener() {
                @Override
                public void onRequestFinished(BaseParams params) {
                    if (params.result == null) {
                        Toast.makeText(mContext, "加载失败，请确认网络通畅", Toast.LENGTH_LONG).show();
                        return;
                    }
                    AccountBookCommission2B data = (AccountBookCommission2B) params.result;
                    commissionList = data.getList();
                    adapterCommission = new AccountBookCommissionAdapter(AccountBookActivity.this, commissionList);
                    lv_commission.setAdapter(adapterCommission);
                    setListViewHeightBasedOnChildren(AccountBookActivity.this, lv_commission, 0);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void requestAwardList() {  // 获取活动奖励列表数据
        Map<String, Object> param = new HashMap<>();
        param.put("userId", "17021511395798036131");

        try {
            HtmlRequest.getAwardList(mContext, param, new BaseRequester.OnRequestListener() {
                @Override
                public void onRequestFinished(BaseParams params) {
                    if (params.result == null) {
                        Toast.makeText(mContext, "加载失败，请确认网络通畅", Toast.LENGTH_LONG).show();
                        return;
                    }
                    AccountBookAward2B data = (AccountBookAward2B) params.result;
                    awardList = data.getList();
                    adapterAward = new AccountBookAwardAdapter(AccountBookActivity.this, awardList);
                    lv_commission.setAdapter(adapterAward);
                    setListViewHeightBasedOnChildren(AccountBookActivity.this, lv_commission, 0);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void requestWithDrawList() {  // 获取提现记录列表数据
        Map<String, Object> param = new HashMap<>();
        param.put("userId", "17021511395798036131");

        try {
            HtmlRequest.getWithdrawList(mContext, param, new BaseRequester.OnRequestListener() {
                @Override
                public void onRequestFinished(BaseParams params) {
                    if (params.result == null) {
                        Toast.makeText(mContext, "加载失败，请确认网络通畅", Toast.LENGTH_LONG).show();
                        return;
                    }
                    AccountBookWithdraw2B data = (AccountBookWithdraw2B) params.result;
                    withdrawlist = data.getList();
                    adapterWithdraw = new AccountBookWithdrawAdapter(AccountBookActivity.this, withdrawlist);
                    lv_commission.setAdapter(adapterWithdraw);
                    setListViewHeightBasedOnChildren(AccountBookActivity.this, lv_commission, 0);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

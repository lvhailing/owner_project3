package com.haidehui.act;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.haidehui.R;
import com.haidehui.fragment.SubcribeFragment;
import com.haidehui.fragment.TurnSignFragment;
import com.haidehui.fragment.UnSubcribeFragment;
import com.haidehui.uitls.StringUtil;
import com.haidehui.widget.TitleBar;


/**
 *  我的 --- 认购状态
 */
public class RenGouStatusActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_subscribed;
    private TextView tv_to_sign;
    private TextView tv_unsubscribe;
    private Resources mResource;

    private SubcribeFragment fragment1;
    private TurnSignFragment fragment2;
    private UnSubcribeFragment fragment3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.ac_rengou_status);
        initTopTitle();
        initView();
        initData();
    }

    private void initTopTitle() {
        TitleBar title = (TitleBar) findViewById(R.id.rl_title);
        title.showLeftImg(true);
        title.setTitle(getResources().getString(R.string.title_null))
                .setLogo(R.drawable.icons, false).setIndicator(R.drawable.back)
                .setCenterText(getResources().getString(R.string.title_rengou_status))
                .showMore(false).setOnActionListener(new TitleBar.OnActionListener() {

            @Override
            public void onMenu(int id) {
            }

            @Override
            public void onBack() {
                finish();
            }

            @Override
            public void onAction(int id) {

            }
        });
    }

    private void initView() {
        mResource = getResources();
        tv_subscribed= (TextView) findViewById(R.id.tv_subscribed);
        tv_to_sign= (TextView) findViewById(R.id.tv_to_sign);
        tv_unsubscribe= (TextView) findViewById(R.id.tv_unsubscribe);

        StringUtil.changeButtonStyleRenGou(tv_subscribed, tv_to_sign, tv_unsubscribe,
                R.id.tv_subscribed, mResource);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        fragment1 = new SubcribeFragment();
        transaction.replace(R.id.fragment_container, fragment1);
        transaction.commit();

    }

    private void initData() {
        tv_subscribed.setOnClickListener(this);
        tv_to_sign.setOnClickListener(this);
        tv_unsubscribe.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        switch (v.getId()){
            case R.id.tv_subscribed:
                StringUtil.changeButtonStyleRenGou(tv_subscribed, tv_to_sign, tv_unsubscribe,
                        R.id.tv_subscribed, mResource);
                hideFragment(transaction);
                fragment1 = new SubcribeFragment();
                transaction.replace(R.id.fragment_container, fragment1);
                transaction.commit();
                break;
            case R.id.tv_to_sign:
                StringUtil.changeButtonStyleRenGou(tv_subscribed, tv_to_sign, tv_unsubscribe,
                        R.id.tv_to_sign, mResource);
                hideFragment(transaction);
                fragment2 = new TurnSignFragment();
                transaction.replace(R.id.fragment_container, fragment2);
                transaction.commit();
                break;
            case R.id.tv_unsubscribe:
                StringUtil.changeButtonStyleRenGou(tv_subscribed, tv_to_sign, tv_unsubscribe,
                        R.id.tv_unsubscribe, mResource);
                hideFragment(transaction);
                fragment3 = new UnSubcribeFragment();
                transaction.replace(R.id.fragment_container, fragment3);
                transaction.commit();
                break;
        }
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

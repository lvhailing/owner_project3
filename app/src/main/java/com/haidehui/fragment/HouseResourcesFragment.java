package com.haidehui.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haidehui.R;
import com.haidehui.act.HouseDetailActivity;
import com.haidehui.adapter.HouseResourceListAdapter;
import com.haidehui.model.HouseList2B;
import com.haidehui.model.HouseList3B;
import com.haidehui.network.BaseParams;
import com.haidehui.network.BaseRequester;
import com.haidehui.network.HtmlRequest;
import com.haidehui.network.types.MouldList;
import com.haidehui.uitls.ViewUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    private TextView tv_house_resources_type, tv_house_resources_price, tv_house_function; // 顶部的类型、价格、功能
    private TextView tv1, tv2, tv3, tv4, tv5, tv6; // 顶部的类型( 公寓，商铺，别墅，学区，土地，庄园)
    private TextView tv_1, tv_2, tv_3, tv_4, tv_5, tv_6, tv_7; // 顶部的价格( 不限，50万元以下，50-100万元，100-200万元，200-500万元，500-1000万元,1000万以上)
    private TextView tv1_func, tv2_func, tv3_func, tv4_func, tv5_func; // 顶部的功能( 投资，自住，度假，海景，移民)
    private TextView tv_no_house;
    private Button btn_type_reset, btn_type_sure; // 类型布局里面的重置，确定按钮
    private Button btn_func_reset, btn_func_sure; // 功能布局里面的重置，确定按钮

    private boolean isShow = false;
    private boolean isOpened = false;   //动画是否开启
    private int currentFlag;  //当前选择哪个按钮  1、类型按钮  2、价格按钮  3、功能按钮
    private List<String> functions = new ArrayList<>();
    private List<String> types = new ArrayList<>();
    //    private String functionSelected = "";
    //    private String typeSelected = "";
    private MouldList<HouseList3B> totalList = new MouldList<>();
    private PullToRefreshListView listView;
    private HouseResourceListAdapter mAdapter;
    private int currentPage = 1;    //当前页
    private String houseCatagory; // 类型
    private String houseFunction; // 功能
    private String housePrice = "1"; // 价格
    private MouldList<HouseList3B> everyList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_house_resources, container, false);
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
        context = getActivity();

        rl_house_resources_type = (RelativeLayout) mView.findViewById(R.id.rl_house_resources_type);
        rl_house_resources_price = (RelativeLayout) mView.findViewById(R.id.rl_house_resources_price);
        rl_house_function = (RelativeLayout) mView.findViewById(R.id.rl_house_function);
        tv_house_resources_type = (TextView) mView.findViewById(R.id.tv_house_resources_type);
        tv_house_resources_price = (TextView) mView.findViewById(R.id.tv_house_resources_price);
        tv_house_function = (TextView) mView.findViewById(R.id.tv_house_function);
        iv_select_type = (ImageView) mView.findViewById(R.id.iv_select_type);
        iv_select_price = (ImageView) mView.findViewById(R.id.iv_select_price);
        iv_select_function = (ImageView) mView.findViewById(R.id.iv_select_function);

        tv_no_house = (TextView) mView.findViewById(R.id.tv_no_house);
        listView = (PullToRefreshListView) mView.findViewById(R.id.listview);

        //PullToRefreshListView  上滑加载更多及下拉刷新
        ViewUtils.slideAndDropDown(listView);

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

    private void initData() {
        houseCatagory = ""; //首次默认"" ，代表全部类型
        houseFunction = ""; //首次默认"" ，代表全部功能

        mAdapter = new HouseResourceListAdapter(context, totalList);
        listView.setAdapter(mAdapter);

        requestGetHouseList();

        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                if (refreshView.isHeaderShown()) {
                    //下拉刷新
                    currentPage = 1;
                } else {
                    //上划加载下一页
                    currentPage++;
                }
                requestGetHouseList();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Intent intent = new Intent(context, HouseDetailActivity.class);
                intent.putExtra("hid", totalList.get(position - 1).getHid());
                startActivity(intent);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

        currentPage = 1;
        houseCatagory = ""; //首次默认"" ，代表全部类型
        housePrice = "1"; // 首次默认价格传1
        houseFunction = ""; //首次默认"" ，代表全部功能
        requestGetHouseList();
        listView.getRefreshableView().setSelection(0);
//        Log.i("hh", "房源---Fragment----onResume");
    }

    public void requestDefaultData() {
        //重置类型里所有按钮状态
        clickTypeBtnReset();

        //将类型集合清空
        types.clear();
        houseCatagory = ""; //首次默认"" ，代表全部类型
        tv_house_resources_type.setText("类型");
        tv_house_resources_type.setTextColor(getResources().getColor(R.color.txt_black));

        //重置功能里所有按钮状态
        clickFunctionBtnReset();

        housePrice = "1"; // 首次默认价格传1
        resetPriceItemColor();
        tv_house_resources_price.setText("价格");
        tv_house_resources_price.setTextColor(getResources().getColor(R.color.txt_black));
        tv_1.setText("不限");
        tv_1.setTextColor(getResources().getColor(R.color.txt_orange));

        //将类型集合清空
        functions.clear();
        houseFunction = ""; //首次默认"" ，代表全部功能
        tv_house_function.setText("功能");
        tv_house_function.setTextColor(getResources().getColor(R.color.txt_black));

        currentPage = 1;
        requestGetHouseList();
        listView.getRefreshableView().setSelection(0);
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
                tv_no_house.setVisibility(View.GONE);
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
                tv_no_house.setVisibility(View.GONE);
                if (isOpened) {
                    tv_1.setTextColor(getResources().getColor(R.color.txt_orange));
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
                tv_no_house.setVisibility(View.GONE);
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
            case R.id.tv1:  // 类型：公寓（flat）
                clickTypeItem(tv1, "flat");
                break;
            case R.id.tv2: // 类型：商铺（shops）
                clickTypeItem(tv2, "shops");
                break;
            case R.id.tv3:  // 类型： 别墅（villa）
                clickTypeItem(tv3, "villa");
                break;
            case R.id.tv4:  // 类型：学区（schoolDistrict）
                clickTypeItem(tv4, "schoolDistrict");
                break;
            case R.id.tv5: // 类型：土地（land）
                clickTypeItem(tv5, "land");
                break;
            case R.id.tv6:  // 类型： 庄园（manor）
                clickTypeItem(tv6, "manor");
                break;
            case R.id.btn_type_reset:  // 类型： 重置
                //重置所有按钮状态
                clickTypeBtnReset();

                //将类型集合清空
                types.clear();
                //为接口字段赋值""
                houseCatagory = "";
                currentPage = 1;

                //上面的“类型”文字及颜色还原
                tv_house_resources_type.setText("类型");
                tv_house_resources_type.setTextColor(getResources().getColor(R.color.txt_black));
                break;
            case R.id.btn_type_sure:  // 类型： 确定
                //点确定时，请求接口
                currentPage = 1;
                requestGetHouseList();
                listView.getRefreshableView().setSelection(0);

                //该按钮被点击了 则类型一定处于展开状态，此时需关闭动画，且箭头置成向下
                iv_select_type.setBackgroundResource(R.mipmap.icon_oversea_down);
                closeShopping(ll_hidden_type);
                break;
            case R.id.tv_1:  // 价格： 不限（1）
                tv_house_resources_price.setText("价格");
                tv_house_resources_price.setTextColor(getResources().getColor(R.color.txt_black));
                resetPriceItemColor();
                tv_1.setTextColor(getResources().getColor(R.color.txt_orange));

                // 调接口请求当前价格数据
                housePrice = "1";
                currentPage = 1;
                requestGetHouseList();
                listView.getRefreshableView().setSelection(0);

                //价格处于展开状态，则需关闭动画，且箭头置成向下
                iv_select_price.setBackgroundResource(R.mipmap.icon_oversea_down);
                closeShopping(ll_hidden_price);
                break;
            case R.id.tv_2:  // 价格：50万元以下（2）
                tv_house_resources_price.setText("50万元...");
                tv_house_resources_price.setTextColor(getResources().getColor(R.color.txt_orange));
                resetPriceItemColor();
                tv_2.setTextColor(getResources().getColor(R.color.txt_orange));

                // 调接口请求当前价格数据
                housePrice = "2";
                currentPage = 1;
                requestGetHouseList();
                listView.getRefreshableView().setSelection(0);

                //价格处于展开状态，则需关闭动画，且箭头置成向下
                iv_select_price.setBackgroundResource(R.mipmap.icon_oversea_down);
                closeShopping(ll_hidden_price);
                break;
            case R.id.tv_3:  // 价格： 50-100万元（3）
                tv_house_resources_price.setText("50-1...");
                tv_house_resources_price.setTextColor(getResources().getColor(R.color.txt_orange));
                resetPriceItemColor();
                tv_3.setTextColor(getResources().getColor(R.color.txt_orange));

                // 调接口请求当前价格数据
                housePrice = "3";
                currentPage = 1;
                requestGetHouseList();
                listView.getRefreshableView().setSelection(0);

                //价格处于展开状态，则需关闭动画，且箭头置成向下
                iv_select_price.setBackgroundResource(R.mipmap.icon_oversea_down);
                closeShopping(ll_hidden_price);
                break;
            case R.id.tv_4:  // 价格： 100-200万元（4）
                tv_house_resources_price.setText("100-2...");
                tv_house_resources_price.setTextColor(getResources().getColor(R.color.txt_orange));
                resetPriceItemColor();
                tv_4.setTextColor(getResources().getColor(R.color.txt_orange));

                // 调接口请求当前价格数据
                housePrice = "4";
                currentPage = 1;
                requestGetHouseList();
                listView.getRefreshableView().setSelection(0);

                //价格处于展开状态，则需关闭动画，且箭头置成向下
                iv_select_price.setBackgroundResource(R.mipmap.icon_oversea_down);
                closeShopping(ll_hidden_price);
                break;
            case R.id.tv_5:  // 价格： 200-500万元（5）
                tv_house_resources_price.setText("200-5...");
                tv_house_resources_price.setTextColor(getResources().getColor(R.color.txt_orange));
                resetPriceItemColor();
                tv_5.setTextColor(getResources().getColor(R.color.txt_orange));

                // 调接口请求当前价格数据
                housePrice = "5";
                currentPage = 1;
                requestGetHouseList();
                listView.getRefreshableView().setSelection(0);

                //价格处于展开状态，则需关闭动画，且箭头置成向下
                iv_select_price.setBackgroundResource(R.mipmap.icon_oversea_down);
                closeShopping(ll_hidden_price);
                break;
            case R.id.tv_6:  // 价格： 500-1000万元（6）
                tv_house_resources_price.setText("500-1...");
                tv_house_resources_price.setTextColor(getResources().getColor(R.color.txt_orange));
                resetPriceItemColor();
                tv_6.setTextColor(getResources().getColor(R.color.txt_orange));

                // 调接口请求当前价格数据
                housePrice = "6";
                currentPage = 1;
                requestGetHouseList();
                listView.getRefreshableView().setSelection(0);

                //价格处于展开状态，则需关闭动画，且箭头置成向下
                iv_select_price.setBackgroundResource(R.mipmap.icon_oversea_down);
                closeShopping(ll_hidden_price);
                break;
            case R.id.tv_7:  // 价格： 1000万元以上（7）
                tv_house_resources_price.setText("1000万元...");
                tv_house_resources_price.setTextColor(getResources().getColor(R.color.txt_orange));
                resetPriceItemColor();
                tv_7.setTextColor(getResources().getColor(R.color.txt_orange));

                // 调接口请求当前价格数据
                housePrice = "7";
                currentPage = 1;
                requestGetHouseList();
                listView.getRefreshableView().setSelection(0);

                //价格处于展开状态，则需关闭动画，且箭头置成向下
                iv_select_price.setBackgroundResource(R.mipmap.icon_oversea_down);
                closeShopping(ll_hidden_price);
                break;
            case R.id.tv1_func:  // 功能：投资（investment）
                clickFunctionItem(tv1_func, "investment");
                break;
            case R.id.tv2_func:  // 功能：自住（selfOccupation）
                clickFunctionItem(tv2_func, "selfOccupation");
                break;
            case R.id.tv3_func:  // 功能：度假（holiday）
                clickFunctionItem(tv3_func, "holiday");
                break;
            case R.id.tv4_func:  // 功能：海景（seascape）
                clickFunctionItem(tv4_func, "seascape");
                break;
            case R.id.tv5_func:  // 功能：移民（immigrant）
                clickFunctionItem(tv5_func, "immigrant");
                break;
            case R.id.btn_func_reset:  // 功能： 重置
                //重置所有按钮状态
                clickFunctionBtnReset();

                //将类型集合清空
                functions.clear();
                //为接口字段赋值""
                houseFunction = ""; //首次默认"" ，代表全部功能
                currentPage = 1;

                //上面的“功能”文字及颜色还原
                tv_house_function.setText("功能");
                tv_house_function.setTextColor(getResources().getColor(R.color.txt_black));

                break;
            case R.id.btn_func_sure:  // 功能： 确定
                //点确定时，请求接口
                currentPage = 1;
                requestGetHouseList();
                listView.getRefreshableView().setSelection(0);

                //功能处于展开状态，则需关闭动画，且箭头置成向下
                iv_select_function.setBackgroundResource(R.mipmap.icon_oversea_down);
                closeShopping(ll_hidden_function);

                break;

            default:
                break;
        }
    }

    /**
     * 类型里的每个按钮被选时调的方法
     *
     * @param tv
     * @param typeEnglish
     */
    private void clickTypeItem(TextView tv, String typeEnglish) {
        if (types.contains(typeEnglish)) {
            //添加过
            types.remove(typeEnglish);
            tv.setTextColor(getResources().getColor(R.color.txt_black));
            tv.setBackgroundResource(R.drawable.shape_center_gray_white);
        } else {
            //未添加过
            types.add(typeEnglish);
            tv.setTextColor(getResources().getColor(R.color.txt_orange));
            tv.setBackgroundResource(R.drawable.shape_center_orange_white);
        }
        StringBuffer sb = new StringBuffer();
        for (String str : types) {
            sb.append(str);
            sb.append(",");
        }
        String strResultType = sb.toString();
        // 每选择一次类型 就为接口字段赋值一次
        houseCatagory = strResultType.equals("") ? "" : strResultType.substring(0, strResultType.length() - 1);

        //设置上面的类型文字
        if (types.size() == 0) {
            tv_house_resources_type.setText("类型");
            tv_house_resources_type.setTextColor(getResources().getColor(R.color.txt_black));
        } else if (types.size() == 1) {
            String selectedType = getOnlyTypeItem();
            tv_house_resources_type.setText(selectedType);
            tv_house_resources_type.setTextColor(getResources().getColor(R.color.txt_orange));
        } else if (types.size() >= 2) {
            tv_house_resources_type.setText("多选");
            tv_house_resources_type.setTextColor(getResources().getColor(R.color.txt_orange));
        }
    }

    private String getOnlyTypeItem() {
        String result;
        switch (houseCatagory) {
            case "flat":
                result = "公寓";
                break;
            case "shops":
                result = "商铺";
                break;
            case "villa":
                result = "别墅";
                break;
            case "schoolDistrict":
                result = "学区";
                break;
            case "land":
                result = "土地";
                break;
            case "manor":
                result = "庄园";
                break;
            default:
                result = "";
        }
        return result;
    }

    /**
     * 价格里的item被选时调的方法
     */
    private void resetPriceItemColor() {
        tv_1.setTextColor(getResources().getColor(R.color.txt_black));
        tv_2.setTextColor(getResources().getColor(R.color.txt_black));
        tv_3.setTextColor(getResources().getColor(R.color.txt_black));
        tv_4.setTextColor(getResources().getColor(R.color.txt_black));
        tv_5.setTextColor(getResources().getColor(R.color.txt_black));
        tv_6.setTextColor(getResources().getColor(R.color.txt_black));
        tv_7.setTextColor(getResources().getColor(R.color.txt_black));
    }

    /**
     * 类型里的重置按钮点击时调的方法
     */
    private void clickTypeBtnReset() {
        changTextColorAndBg(tv1);
        changTextColorAndBg(tv2);
        changTextColorAndBg(tv3);
        changTextColorAndBg(tv4);
        changTextColorAndBg(tv5);
        changTextColorAndBg(tv6);
    }

    /**
     * 功能里的每个按钮被选时调的方法
     *
     * @param tv
     * @param functionEnglish
     */
    private void clickFunctionItem(TextView tv, String functionEnglish) {
        if (functions.contains(functionEnglish)) {
            //添加过
            functions.remove(functionEnglish);
            tv.setTextColor(getResources().getColor(R.color.txt_black));
            tv.setBackgroundResource(R.drawable.shape_center_gray_white);
        } else {
            //未添加过
            functions.add(functionEnglish);
            tv.setTextColor(getResources().getColor(R.color.txt_orange));
            tv.setBackgroundResource(R.drawable.shape_center_orange_white);
        }
        StringBuffer sb = new StringBuffer();
        for (String str : functions) {
            sb.append(str);
            sb.append(",");
        }
        String strResultFunction = sb.toString();
        //每选择一次功能 就为接口字段赋值一次
        houseFunction = strResultFunction.equals("") ? "" : strResultFunction.substring(0, strResultFunction.length() - 1);

        //设置上面的类型文字
        if (functions.size() == 0) {
            tv_house_function.setText("功能");
            tv_house_function.setTextColor(getResources().getColor(R.color.txt_black));
        } else if (functions.size() == 1) {
            String selectedFunc = getOnlyFuncItem();
            tv_house_function.setText(selectedFunc);
            tv_house_function.setTextColor(getResources().getColor(R.color.txt_orange));
        } else if (functions.size() >= 2) {
            tv_house_function.setText("多选");
            tv_house_function.setTextColor(getResources().getColor(R.color.txt_orange));
        }
    }

    private String getOnlyFuncItem() {
        String result;
        switch (houseFunction) {
            case "investment":
                result = "投资";
                break;
            case "selfOccupation":
                result = "自住";
                break;
            case "holiday":
                result = "度假";
                break;
            case "seascape":
                result = "海景";
                break;
            case "immigrant":
                result = "移民";
                break;

            default:
                result = "";
        }
        return result;
    }

    /**
     * 功能里的重置功能按钮点击时调的方法
     */
    private void clickFunctionBtnReset() {
        functions.clear();
        changTextColorAndBg(tv1_func);
        changTextColorAndBg(tv2_func);
        changTextColorAndBg(tv3_func);
        changTextColorAndBg(tv4_func);
        changTextColorAndBg(tv5_func);
    }

    /**
     * 重置textView 的颜色和背景
     *
     * @param tv
     */
    private void changTextColorAndBg(TextView tv) {
        tv.setTextColor(getResources().getColor(R.color.txt_black));
        tv.setBackgroundResource(R.drawable.shape_center_gray_white);
    }

    /**
     * 获取房源列表数据
     */
    private void requestGetHouseList() {
        HashMap<String, Object> param = new HashMap<>();
        param.put("page", currentPage);
        param.put("housePrice", housePrice);
        param.put("houseFunction", houseFunction);
        param.put("houseCatagory", houseCatagory);

        HtmlRequest.getHouseList(context, param, new BaseRequester.OnRequestListener() {
            @Override
            public void onRequestFinished(BaseParams params) {
                if (params.result == null) {
                    Toast.makeText(context, "加载失败，请确认网络通畅", Toast.LENGTH_LONG).show();
                    listView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            listView.onRefreshComplete();
                        }
                    }, 1000);
                    return;
                }

                HouseList2B data = (HouseList2B) params.result;
                everyList = data.getList();

                if ((everyList == null || everyList.size() == 0) && currentPage != 1) {
                    Toast.makeText(context, "已经到最后一页", Toast.LENGTH_SHORT).show();
                }

                if (currentPage == 1) {
                    //刚进来时 加载第一页数据，或下拉刷新 重新加载数据 。这两种情况之前的数据都清掉
                    totalList.clear();
                }
                totalList.addAll(everyList);

                if (totalList == null || totalList.size() <= 0) {
                    tv_no_house.setVisibility(View.VISIBLE);
                }

                //刷新数据
                mAdapter.notifyDataSetChanged();

                listView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        listView.onRefreshComplete();
                    }
                }, 1000);
            }
        });

    }

}

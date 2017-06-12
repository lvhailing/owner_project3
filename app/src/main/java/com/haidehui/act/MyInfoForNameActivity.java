package com.haidehui.act;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.haidehui.R;
import com.haidehui.widget.TitleBar;


/**
 *  我的 --- 我的信息（修改姓名）
 */
public class MyInfoForNameActivity extends BaseActivity implements View.OnClickListener {
    private EditText edt_name;
    private Button btn_save;
    private String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.ac_info_name);

        initTopTitle();
        initView();
        initData();
    }

    private void initTopTitle() {
        TitleBar title = (TitleBar) findViewById(R.id.rl_title);
        title.showLeftImg(true);
        title.setTitle(getResources().getString(R.string.title_null))
                .setLogo(R.drawable.icons, false).setIndicator(R.drawable.back)
                .setCenterText(getResources().getString(R.string.title_my_info))
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
        name=getIntent().getStringExtra("name");
        edt_name= (EditText) findViewById(R.id.edt_name);
        btn_save= (Button) findViewById(R.id.btn_save);

        edt_name.setText(name);
    }

    private void initData() {
        btn_save.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_save:
                Intent intent =new Intent(this,MyInfoActivity.class);
                intent.putExtra("name",edt_name.getText().toString());
                setResult(2000,intent);
                finish();
                break;
        }
    }

   /* private void requestListData() {  // 获取最热房源列表数据
        Map<String, Object> param = new HashMap<>();
        param.put("currentPage", currentPage + "");

        try {
            HtmlRequest.getHotHouseData(mContext, param, new BaseRequester.OnRequestListener() {
                @Override
                public void onRequestFinished(BaseParams params) {
                    if (params.result == null) {
                        Toast.makeText(mContext, "加载失败，请确认网络通畅", Toast.LENGTH_LONG).show();
                        listView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                listView.onRefreshComplete();
                            }
                        }, 1000);
                        return;
                    }

                    HotHouse2B data = (HotHouse2B) params.result;
                    MouldList<HotHouse3B> everyList = data.getList();
                    if ((everyList == null || everyList.size() == 0) && currentPage != 1) {
                        Toast.makeText(mContext, "已经到最后一页", Toast.LENGTH_SHORT).show();
                    }

                    if (currentPage == 1) {
                        //刚进来时 加载第一页数据，或下拉刷新 重新加载数据 。这两种情况之前的数据都清掉
                        totalList.clear();
                    }
                    totalList.addAll(everyList);

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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

*/

}

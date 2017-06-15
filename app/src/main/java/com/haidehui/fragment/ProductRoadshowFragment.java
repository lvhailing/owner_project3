package com.haidehui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.haidehui.R;
import com.haidehui.adapter.InvestmentGuideAdapter;
import com.haidehui.adapter.ProductRoadShowAdapter;
import com.haidehui.model.BoutiqueHouse2B;
import com.haidehui.network.types.MouldList;
import com.haidehui.widget.MyListView;

import static com.haidehui.fragment.HomeFragment.setListViewHeightBasedOnChildren;

// 发现--产品路演 列表页
public class ProductRoadshowFragment extends Fragment {
    private View mView;
    private FragmentActivity context;
    private MyListView myListView;
    private ProductRoadShowAdapter myAdapter;
    private MouldList<BoutiqueHouse2B> list; // 精品房源数据

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_product_road_show, container, false);
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
        myListView = (MyListView) mView.findViewById(R.id.myListView);
    }

    private void initData() {
        myAdapter = new ProductRoadShowAdapter(context, list);
        myListView.setAdapter(myAdapter);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

            }
        });
        setListViewHeightBasedOnChildren(getActivity(), myListView, 0);

    }

}

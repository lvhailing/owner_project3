package com.haidehui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.haidehui.R;

public class MineFragment extends Fragment implements OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vp = inflater.inflate(R.layout.fragment_mine, container, false);
        return vp;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.more_layout_about:
//
//                break;
        }
    }
}

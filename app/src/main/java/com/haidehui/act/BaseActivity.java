package com.haidehui.act;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.haidehui.R;
import com.haidehui.uitls.DESUtil;
import com.haidehui.uitls.PreferenceUtil;

/**
 * Created by junde on 2017/5/27.
 */

public class BaseActivity extends FragmentActivity {
    public BaseActivity mContext;   //Activity 上下文
    public String userId = null;
    public String token = null;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.base);

        mContext = this;

        try {
            userId = DESUtil.decrypt(PreferenceUtil.getUserId());
            token = DESUtil.decrypt(PreferenceUtil.getToken());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void baseSetContentView(int layoutResId) {
        LinearLayout llContent = (LinearLayout) findViewById(R.id.content);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(layoutResId, null);
        llContent.addView(v);
    }

    public void startLoading() {
        if (dialog == null) {
            dialog = new ProgressDialog(mContext);
            dialog.show();
        }else if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    public void stopLoading() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null) {
            dialog = null;
        }
    }
}

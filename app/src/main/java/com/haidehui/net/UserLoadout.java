package com.haidehui.net;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.haidehui.act.MainActivity;
import com.haidehui.model.ResultLoginOffContentBean;
import com.haidehui.network.BaseParams;
import com.haidehui.network.BaseRequester;
import com.haidehui.network.HtmlRequest;
import com.haidehui.uitls.PreferenceUtil;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


public class UserLoadout {

	private Context context;
	private String userId;

	public UserLoadout(Context context,String userId) {
		this.context = context;
		this.userId = userId;
	}

	public void requestData() {
		LinkedHashMap<String, Object> param = new LinkedHashMap<>();
		param.put("userId", userId);
		HtmlRequest.loginoff(context,param, new BaseRequester.OnRequestListener() {

			@Override
			public void onRequestFinished(BaseParams params) {
				ResultLoginOffContentBean b = (ResultLoginOffContentBean) params.result;
				// if (b != null) {
				// if (Boolean.parseBoolean(b.getFlag())) {
//				PreferenceUtil.setAutoLoginAccount("");
				PreferenceUtil.setAutoLoginPwd("");
				PreferenceUtil.setLogin(false);
//				PreferenceUtil.setPhone("");
				PreferenceUtil.setUserId("");
//				PreferenceUtil.setUserNickName("");
				PreferenceUtil.setCookie("");
				PreferenceUtil.setToken("");
				// i.putExtra("result", "exit");
				// setResult(9, i);
				Intent tent = new Intent("vjinkeexit");// 广播的标签，一定要和需要接受的一致。
				tent.putExtra("result", "exit");
				context.sendBroadcast(tent);// 发送广播
				Toast.makeText(context, "退出成功", Toast.LENGTH_LONG).show();
				Intent i_account = new Intent();
				i_account.setClass(context, MainActivity.class);
				i_account.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				i_account.putExtra("selectPage", 3);
				context.startActivity(i_account);
				// } else {
				// Toast.makeText(context, b.getMsg(), Toast.LENGTH_LONG)
				// .show();
				// }
				// }
			}
		});
	}
}

package com.haidehui.net;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.gson.Gson;
import com.haidehui.common.Urls;
import com.haidehui.model.ResultUserLoginBean;
import com.haidehui.network.HtmlRequest;
import com.haidehui.network.http.AsyncHttpClient;
import com.haidehui.network.http.AsyncHttpResponseHandler;
import com.haidehui.network.http.PersistentCookieStore;
import com.haidehui.uitls.DESUtil;
import com.haidehui.uitls.PreferenceUtil;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

public class UserLogin extends Observable {
	private static UserLogin instance;
	private static final int MODE_PRIVATE = 0;
	private UserLogin() {

	}

	public void notifyObservers(Object data) {
		this.setChanged();
		super.notifyObservers(data);
	}

	public static UserLogin getInstance() {
		if (instance == null) {
			instance = new UserLogin();
		}
		return instance;
	}

	public void userlogining(final Context context, final String mobile,
			final String password, String token) {
		AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
		// asyncHttpClient.addHeader("x-client-version",
		// SystemInfo.sVersionName);
		// asyncHttpClient.addHeader("x-platform", "android");
		// asyncHttpClient.addHeader("x-machine-id",
		// SystemInfo.getImei(context));
		// asyncHttpClient.addHeader("client-tgt", "");
		
		asyncHttpClient.addHeader("accept", "application/json");
		
//		asyncHttpClient.addHeader("content-Type", "application/json");
		
		asyncHttpClient.setTimeout(6000);
		
		String STORE_NAME = "PushTestReceiver"; 
	    SharedPreferences settings =context.getSharedPreferences(STORE_NAME, MODE_PRIVATE);
	    String appid = settings.getString("channelId","");

		Map<String, Object> param = new HashMap<>();
		param.put("mobile", mobile);
		param.put("password", password);

		String data = HtmlRequest.getResult(param);
		PersistentCookieStore myCookieStore = new PersistentCookieStore(context);
		asyncHttpClient.setCookieStore(myCookieStore);
		HttpEntity entity = null;
		try {
			entity = new StringEntity(data);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		asyncHttpClient.post(context, Urls.URL_LOGIN, entity,
				"application/json", new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(String content) {
						super.onSuccess(content);

					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							String content) {
						super.onSuccess(statusCode, headers, content);
						for (int i = 0; i < headers.length; i++) {
							if (headers[i].getName().equals("Set-Cookie")) {
								String id = headers[i].getValue();
								try {
									PreferenceUtil.setCookie(DESUtil
											.encrypt(id));
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
						String result = null;
						try {
							result = DESUtil.decrypt(content.toString());
						} catch (Exception e) {
							e.printStackTrace();
						}

						Gson json = new Gson();
						ResultUserLoginBean b = json.fromJson(
								result.toString(), ResultUserLoginBean.class);
						if (b.getData() != null) {
							if (Boolean.parseBoolean(b.getData().getFlag())) {
								try {
									PreferenceUtil.setAutoLoginAccount(DESUtil
											.encrypt(mobile));
									PreferenceUtil.setAutoLoginPwd(DESUtil
											.encrypt(password));
									PreferenceUtil.setPhone(DESUtil.encrypt(b
											.getData().getMobile()));
									PreferenceUtil.setUserId(DESUtil.encrypt(b
											.getData().getUserId()));
									if(!TextUtils.isEmpty(b.getData()
											.getRealName())){
										PreferenceUtil.setUserRealName(DESUtil.encrypt(b.getData()
												.getRealName()));
									}

									if(!TextUtils.isEmpty(b
											.getData().getIdNo())){
										PreferenceUtil.setIdNo(DESUtil.encrypt(b
												.getData().getIdNo()));
									}

									if(!TextUtils.isEmpty(b
											.getData().getCheckStatus())){
										PreferenceUtil.setCheckStatus(DESUtil.encrypt(b
												.getData().getCheckStatus()));
									}

									PreferenceUtil.setLogin(true);
								} catch (Exception e) {
									e.printStackTrace();
								}
							} else {
								Toast.makeText(context, b.getData().getMessage(),
										Toast.LENGTH_LONG).show();
							}
							
						} else {
							
//							PreferenceUtil.setAutoLoginAccount("");
							PreferenceUtil.setAutoLoginPwd("");
							PreferenceUtil.setLogin(false);
							PreferenceUtil.setPhone("");
							PreferenceUtil.setUserId("");
//							PreferenceUtil.setUserNickName("");
							PreferenceUtil.setCookie("");
						}
						notifyObservers(b.getData());

					}

					@Override
					@Deprecated
					public void onFailure(Throwable error) {
						super.onFailure(error);
					}

					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
						ResultUserLoginBean b = new ResultUserLoginBean();
						b.setMsg("登陆失败");
//						Toast.makeText(context, content, Toast.LENGTH_LONG)
//								.show();
						Toast.makeText(context, "加载失败，请确认网络通畅", Toast.LENGTH_LONG).show();//加载失败，请确认网络通畅
//						PreferenceUtil.setAutoLoginAccount("");
						PreferenceUtil.setAutoLoginPwd("");
						PreferenceUtil.setLogin(false);
						PreferenceUtil.setPhone("");
						PreferenceUtil.setUserId("");
//						PreferenceUtil.setUserNickName("");
						PreferenceUtil.setCookie("");
						notifyObservers(b.getData());
					}

				});
	}
}

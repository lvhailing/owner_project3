/*
 * Copyright (C) 2010 The MobileSecurePay Project
 * All right reserved.
 * author: shiqun.shi@alipay.com
 */

package com.haidehui.uitls;

import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import com.haidehui.R;

import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import onekeyshare.OnekeyShare;
import onekeyshare.PlatformListFakeActivity;

/**
 *  分享时调用的方法
 */
public final class ShareUtil {
    private static String way;    // 渠道标志

    public static void sharedSDK(final Context context, final String title, final String text, final String url) {
        final OnekeyShare oks = new OnekeyShare();

        oks.disableSSOWhenAuthorize();// 关闭sso授权

        oks.setOnShareButtonClickListener(new PlatformListFakeActivity.OnShareButtonClickListener() {
            @Override
            public void onClick(View v, List<Object> checkPlatforms) {
                String string = checkPlatforms.toString();
                oks.setSilent(false);
                if (string.contains("WechatMoments")) {
                    way = "weixin";            //微信朋友圈
                    oks.setText(text);
                    oks.setTitleUrl(url);
                    oks.setUrl(url);
                    oks.setTitle(title);
                    oks.setImagePath(Environment.getExternalStorageDirectory() + "/haidehui/imgs/haidehui.png");
                } else if (string.contains("Wechat")) {
                    way = "weixinFr";        //微信好友
                    oks.setText(text);
                    oks.setTitle(title);
                    oks.setTitleUrl(url);
                    oks.setUrl(url);
//					oks.setImagePath("/sdcard/vjinke/imgs/test.jpg");
                    oks.setImagePath(Environment.getExternalStorageDirectory() + "/haidehui/imgs/haidehui.png");
                } else if (string.contains("QZone")) {
                    way = "Qzone";
                    oks.setText(text);
                    oks.setTitle(title);
                    oks.setTitleUrl(url);
                    oks.setUrl(url);
                } else if (string.contains("SinaWeibo")) {
                    way = "sinablog";
                    oks.setText(text);
//					oks.setTitleUrl(url);
                    oks.setUrl(url);

                    oks.setSilent(false);
                } else if (string.contains("TencentWeibo")) {
                    way = "tencentblog";
                    oks.setText(text);
                    oks.setTitleUrl(url);
                    oks.setUrl(url);
                } else if (string.contains("QQ")) {
                    way = "QQ";
                    oks.setText(text);
                    oks.setTitle(title);
                    oks.setImagePath(Environment.getExternalStorageDirectory() + "/haidehui/imgs/haidehui.png");
					oks.setTitleUrl(url);
                    // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
                    // oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
                    // url仅在微信（包括好友和朋友圈）中使用
                    oks.setUrl(url);
                    // comment是我对这条分享的评论，仅在人人网和QQ空间使用
                    // oks.setComment("我是测试评论文本");
                    // site是分享此内容的网站名称，仅在QQ空间使用
                    oks.setSite(context.getString(R.string.app_name));
                    // siteUrl是分享此内容的网站地址，仅在QQ空间使用
//                    oks.setSiteUrl(url);
                } else if (string.contains("Email")) {
                    way = "email";
                    oks.setText(text);
                    oks.setTitleUrl(url);
                    oks.setUrl(url);
                } else if (string.contains("ShortMessage")) {
                    way = "sms";
                    oks.setText(text+url);
                    oks.setTitleUrl(url);
                    oks.setUrl(url);
                }
            }

        });

        // 分享时Notification的图标和文字 2.5.9以后的版本不调用此方法
        // oks.setNotification(R.drawable.ic_launcher,
        // getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用

        oks.setTitle(context.getString(R.string.share));
//		oks.setImagePath("/sdcard/vjinke/imgs/test.jpg");//确保SDcard下面存在此张图片
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用

        // oks.setTitleUrl("http://www.vjinke.com");

        // text是分享文本，所有平台都需要这个字段
        Bitmap enableLogo = BitmapFactory.decodeResource(context.getResources(), R.mipmap.logo_lianjie);
        String label = "复制链接";
        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View v) {
                StringBuffer randomNum = new StringBuffer();
                for (int i = 0; i < 6; i++) {
                    int t = (int) (Math.random() * 10);
                    randomNum.append(t);
                }
                ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(url);
                Toast.makeText(context, "复制成功", Toast.LENGTH_SHORT).show();
            }
        };
        oks.setCustomerLogo(enableLogo, enableLogo, label, listener);

        oks.setCallback(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {

                if (platform.getName().equals("WechatMoments")) {

                } else if (platform.getName().equals("Wechat")) {
                    Toast.makeText(context, "weixin share", Toast.LENGTH_SHORT).show();

                } else if (platform.getName().equals("QZone")) {


                } else if (platform.getName().equals("SinaWeibo")) {


                } else if (platform.getName().equals("ShortMessage")) {


                } else if (platform.getName().equals("QQ")) {

                }
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {

            }

            @Override
            public void onCancel(Platform platform, int i) {

            }
        });

//        oks.getCallback();

//        if (!TextUtils.isEmpty(recommendCode)) {
//        }
        // 启动分享GUI
        oks.show(context);
    }
}

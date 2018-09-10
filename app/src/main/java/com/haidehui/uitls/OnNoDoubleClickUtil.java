package com.haidehui.uitls;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class OnNoDoubleClickUtil {
    private static long lastClickTime = 0;//上次点击的时间
    private static int spaceTime = 1000;//时间间隔(两次点击按钮之间的点击间隔不能少于1000毫秒)

    public static boolean isFastClick() {
        long currentTime = System.currentTimeMillis();//当前系统时间
        boolean isAllowClick;//是否允许点击

        if (currentTime - lastClickTime > spaceTime) {
            isAllowClick = true;
        } else {
            isAllowClick = false;
        }

        lastClickTime = currentTime;
        return isAllowClick;
    }


}

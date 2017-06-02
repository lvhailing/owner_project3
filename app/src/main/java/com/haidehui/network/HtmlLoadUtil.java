package com.haidehui.network;

import com.google.gson.Gson;
import com.haidehui.model.CheckVersionBean;
import com.haidehui.uitls.DESUtil;
import com.haidehui.uitls.MD5;

import java.io.File;

public class HtmlLoadUtil {

    private static String getResult(Object data) {
        Gson gson = new Gson();
        String md5 = MD5.stringToMD5(gson.toJson(data));
        String result = null;
        try {
            EcryptBean b = new EcryptBean(md5, data);
            String encrypt = gson.toJson(b);
            result = DESUtil.encrypt(encrypt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static String getResultNoEncrypt(Object data) {
        Gson gson = new Gson();
        String md5 = MD5.stringToMD5(gson.toJson(data));
        String result = null;
        try {
            EcryptBean b = new EcryptBean(md5, data);
            result = gson.toJson(b);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 检查版本
     *
     * @param type
     * @return
     */
    public static String checkVersion(String type) {
        CheckVersionBean b = new CheckVersionBean(type);
        return getResult(b);
    }
}

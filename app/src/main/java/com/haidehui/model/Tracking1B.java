package com.haidehui.model;


import com.haidehui.network.types.IMouldType;
import com.haidehui.network.types.MouldList;

public class Tracking1B implements IMouldType {
    private String check;
    private String code;
    private String msg;
    private MouldList<Tracking2B>  data;

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public MouldList<Tracking2B> getData() {
        return data;
    }

    public void setData(MouldList<Tracking2B> data) {
        this.data = data;
    }
}
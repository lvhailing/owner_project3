package com.haidehui.model;


import com.haidehui.network.types.IMouldType;

public class AwardDetails1B implements IMouldType {
    private String check;
    private String code;
    private String msg;
    private AwardDetails2B data;

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

    public AwardDetails2B getData() {
        return data;
    }

    public void setData(AwardDetails2B data) {
        this.data = data;
    }
}
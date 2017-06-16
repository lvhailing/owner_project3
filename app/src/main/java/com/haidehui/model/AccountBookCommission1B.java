package com.haidehui.model;


import com.haidehui.network.types.IMouldType;

public class AccountBookCommission1B implements IMouldType {
    private String check;
    private String code;
    private String msg;
    private AccountBookCommission2B data;

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

    public AccountBookCommission2B getData() {
        return data;
    }

    public void setData(AccountBookCommission2B data) {
        this.data = data;
    }
}
package com.haidehui.model;


import com.haidehui.network.types.IMouldType;

public class SubmitCustomer2B implements IMouldType {
    private String flag;
    private String message;
    private String msg;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
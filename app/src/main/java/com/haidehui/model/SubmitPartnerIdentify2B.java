package com.haidehui.model;


import com.haidehui.network.types.IMouldType;

public class SubmitPartnerIdentify2B implements IMouldType {
    private String flag;
    private String message;

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
}
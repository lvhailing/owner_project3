package com.haidehui.model;


import com.haidehui.network.types.IMouldType;
import com.haidehui.network.types.MouldList;

public class AccountBookCommission2B implements IMouldType {
    private MouldList<AccountBookCommission3B> list;
    private String availableCommission;
    private String sendedCommission;
    private String totalCommission;
    private String flag;
    private String msg;

    public MouldList<AccountBookCommission3B> getList() {
        return list;
    }

    public void setList(MouldList<AccountBookCommission3B> list) {
        this.list = list;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getAvailableCommission() {
        return availableCommission;
    }

    public void setAvailableCommission(String availableCommission) {
        this.availableCommission = availableCommission;
    }

    public String getSendedCommission() {
        return sendedCommission;
    }

    public void setSendedCommission(String sendedCommission) {
        this.sendedCommission = sendedCommission;
    }

    public String getTotalCommission() {
        return totalCommission;
    }

    public void setTotalCommission(String totalCommission) {
        this.totalCommission = totalCommission;
    }
}
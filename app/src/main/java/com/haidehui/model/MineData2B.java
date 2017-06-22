package com.haidehui.model;


import com.haidehui.network.types.IMouldType;

public class MineData2B implements IMouldType {
    private String messageTotal;
    private String mobile;
    private String realName;
    private String totalCommission;
    private String headPhoto;
    private String checkStatus;


    public String getMessageTotal() {
        return messageTotal;
    }

    public void setMessageTotal(String messageTotal) {
        this.messageTotal = messageTotal;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getTotalCommission() {
        return totalCommission;
    }

    public void setTotalCommission(String totalCommission) {
        this.totalCommission = totalCommission;
    }

    public String getHeadPhoto() {
        return headPhoto;
    }

    public void setHeadPhoto(String headPhoto) {
        this.headPhoto = headPhoto;
    }

    public String getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(String checkStatus) {
        this.checkStatus = checkStatus;
    }
}
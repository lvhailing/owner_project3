package com.haidehui.model;


import com.haidehui.network.types.IMouldType;

public class MineData2B implements IMouldType {
    private String messageTotal; // 消息总数
    private String mobile; // 手机号
    private String realName; // 用户姓名
    private String totalCommission; // 账本额度值
    private String headPhoto; // 头像
    private String checkStatus; // 认证状态
    private String idNo;        //  身份证号

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

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
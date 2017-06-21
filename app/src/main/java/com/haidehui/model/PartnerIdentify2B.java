package com.haidehui.model;


import com.haidehui.network.types.IMouldType;

public class PartnerIdentify2B implements IMouldType {
    private String busiCardPhoto;
    private String checkStatus;
    private String email;
    private String idNo;
    private String idcardFront;
    private String mobile;
    private String realName;
    private String workProvince;
    private String workUnit;

    public String getBusiCardPhoto() {
        return busiCardPhoto;
    }

    public void setBusiCardPhoto(String busiCardPhoto) {
        this.busiCardPhoto = busiCardPhoto;
    }

    public String getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(String checkStatus) {
        this.checkStatus = checkStatus;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getIdcardFront() {
        return idcardFront;
    }

    public void setIdcardFront(String idcardFront) {
        this.idcardFront = idcardFront;
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

    public String getWorkProvince() {
        return workProvince;
    }

    public void setWorkProvince(String workProvince) {
        this.workProvince = workProvince;
    }

    public String getWorkUnit() {
        return workUnit;
    }

    public void setWorkUnit(String workUnit) {
        this.workUnit = workUnit;
    }
}
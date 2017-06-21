package com.haidehui.model;


import com.haidehui.network.types.IMouldType;

public class AwardDetails2B implements IMouldType {
    private String createTime;
    private String houseNum;
    private String projectName;
    private String remark;
    private String rewardAmount;
    private String rewardStatus;
    private String rewardType;
    private String serialNumber;
    private String tradeCode;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getHouseNum() {
        return houseNum;
    }

    public void setHouseNum(String houseNum) {
        this.houseNum = houseNum;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRewardAmount() {
        return rewardAmount;
    }

    public void setRewardAmount(String rewardAmount) {
        this.rewardAmount = rewardAmount;
    }

    public String getRewardStatus() {
        return rewardStatus;
    }

    public void setRewardStatus(String rewardStatus) {
        this.rewardStatus = rewardStatus;
    }

    public String getRewardType() {
        return rewardType;
    }

    public void setRewardType(String rewardType) {
        this.rewardType = rewardType;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getTradeCode() {
        return tradeCode;
    }

    public void setTradeCode(String tradeCode) {
        this.tradeCode = tradeCode;
    }
}
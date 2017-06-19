package com.haidehui.model;


import com.haidehui.network.types.IMouldType;

/**
 * 房源列表  接收后台返回数据
 */
public class HouseList3B implements IMouldType {
    private String hCoverImg; // 房源展示图片地址
    private String hid; // 房源id
    private String hName; // 房源名称
    private String hType; // 居室
    private String hArea; // 面积
    private String hPrice; // 价格

    public String gethCoverImg() {
        return hCoverImg;
    }

    public void sethCoverImg(String hCoverImg) {
        this.hCoverImg = hCoverImg;
    }

    public String getHid() {
        return hid;
    }

    public void setHid(String hid) {
        this.hid = hid;
    }

    public String gethName() {
        return hName;
    }

    public void sethName(String hName) {
        this.hName = hName;
    }

    public String gethType() {
        return hType;
    }

    public void sethType(String hType) {
        this.hType = hType;
    }

    public String gethArea() {
        return hArea;
    }

    public void sethArea(String hArea) {
        this.hArea = hArea;
    }

    public String gethPrice() {
        return hPrice;
    }

    public void sethPrice(String hPrice) {
        this.hPrice = hPrice;
    }
}


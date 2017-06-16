package com.haidehui.model;


import com.haidehui.network.types.IMouldType;

/**
 * 最热房源列表  接收后台返回数据
 */
public class HotHouse3B implements IMouldType {
    private String hid; // 房源id
    private String path; //房源展示图片地址
    private String name; //房源名称
    private String houseType; // 居室
    private String area; //面积
    private String price; //价格

    public String getHid() {
        return hid;
    }

    public void setHid(String hid) {
        this.hid = hid;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHouseType() {
        return houseType;
    }

    public void setHouseType(String houseType) {
        this.houseType = houseType;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}


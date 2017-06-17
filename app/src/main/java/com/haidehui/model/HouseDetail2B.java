package com.haidehui.model;


import com.haidehui.network.types.IMouldType;
import com.haidehui.network.types.MouldList;

import java.util.ArrayList;
import java.util.List;

// 最热房源列表
public class HouseDetail2B implements IMouldType {
    private ArrayList<String> houseImg; // 房源图片列表
    private String hid; // 房源编号
    private String price; // 价格
    private String area; // 面积
    private String houseType; // 居室
    private String commissionRate; // 返佣比例
    private String location; // 位置
    private String locationImg; // 位置图片
    private String name; // 房源名称
    private String function; // 功能
    private String catagory; // 类型
    private String decorateStandard; // 装修标准
    private String time; // 年代
    private String story; // 楼层
    private String propertyFee; // 物业费
    private String houseDesc; // 房源描述
    private String flag;
    private String msg;

    public ArrayList<String> getHouseImg() {
        return houseImg;
    }

    public void setHouseImg(ArrayList<String> houseImg) {
        this.houseImg = houseImg;
    }

    public String getHid() {
        return hid;
    }

    public void setHid(String hid) {
        this.hid = hid;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getHouseType() {
        return houseType;
    }

    public void setHouseType(String houseType) {
        this.houseType = houseType;
    }

    public String getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(String commissionRate) {
        this.commissionRate = commissionRate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocationImg() {
        return locationImg;
    }

    public void setLocationImg(String locationImg) {
        this.locationImg = locationImg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getCatagory() {
        return catagory;
    }

    public void setCatagory(String catagory) {
        this.catagory = catagory;
    }

    public String getDecorateStandard() {
        return decorateStandard;
    }

    public void setDecorateStandard(String decorateStandard) {
        this.decorateStandard = decorateStandard;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public String getPropertyFee() {
        return propertyFee;
    }

    public void setPropertyFee(String propertyFee) {
        this.propertyFee = propertyFee;
    }

    public String getHouseDesc() {
        return houseDesc;
    }

    public void setHouseDesc(String houseDesc) {
        this.houseDesc = houseDesc;
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
}
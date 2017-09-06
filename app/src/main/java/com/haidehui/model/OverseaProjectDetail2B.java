package com.haidehui.model;


import com.haidehui.network.types.IMouldType;
import com.haidehui.network.types.MouldList;

import java.util.ArrayList;

// 海外项目详情
public class OverseaProjectDetail2B implements IMouldType {
    private MouldList<OverseaProjectDetailHouseList3B> list; // 相关房源列表
    private String pid; // 项目编号
    private String projectImg; // 项目展示图片地址
    private String name; // 项目名字
    private String chineseName; // 项目中文名称（8.25 新增字段）
    private String city; // 项目所在地区（8.25 新增字段）
    private String price; // 项目价格
    private String area; // 项目面积
    private String location; // 项目位置
    private String category; // 类型
    private String total; // 项目体量
    private String decorateStandard; // 装修标准
    private String projectDesc; // 项目描述
    private String houseType; // 项目居室
    private ArrayList<String> houseTypeImg; // 项目居室图片
    private ArrayList<String> projectPlanImg; // 项目规划图片
    private String supportFacility; // 配套设施
    private String geographyLocation; // 地理位置
    private MouldList<OverseaProjectDetailPdfList3B> attachment; //  项目附件
    private String flag;
    private String msg;

    public MouldList<OverseaProjectDetailHouseList3B> getList() {
        return list;
    }

    public void setList(MouldList<OverseaProjectDetailHouseList3B> list) {
        this.list = list;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getProjectImg() {
        return projectImg;
    }

    public void setProjectImg(String projectImg) {
        this.projectImg = projectImg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChineseName() {
        return chineseName;
    }

    public void setChineseName(String chineseName) {
        this.chineseName = chineseName;
    }

    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getDecorateStandard() {
        return decorateStandard;
    }

    public void setDecorateStandard(String decorateStandard) {
        this.decorateStandard = decorateStandard;
    }

    public String getProjectDesc() {
        return projectDesc;
    }

    public void setProjectDesc(String projectDesc) {
        this.projectDesc = projectDesc;
    }

    public String getHouseType() {
        return houseType;
    }

    public void setHouseType(String houseType) {
        this.houseType = houseType;
    }

    public ArrayList<String> getHouseTypeImg() {
        return houseTypeImg;
    }
    public void setHouseTypeImg(ArrayList<String> houseTypeImg) {
        this.houseTypeImg = houseTypeImg;
    }

    public ArrayList<String> getProjectPlanImg() {
        return projectPlanImg;
    }

    public void setProjectPlanImg(ArrayList<String> projectPlanImg) {
        this.projectPlanImg = projectPlanImg;
    }

    public String getSupportFacility() {
        return supportFacility;
    }

    public void setSupportFacility(String supportFacility) {
        this.supportFacility = supportFacility;
    }

    public String getGeographyLocation() {
        return geographyLocation;
    }

    public void setGeographyLocation(String geographyLocation) {
        this.geographyLocation = geographyLocation;
    }

    public MouldList<OverseaProjectDetailPdfList3B> getAttachment() {
        return attachment;
    }

    public void setAttachment(MouldList<OverseaProjectDetailPdfList3B> attachment) {
        this.attachment = attachment;
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
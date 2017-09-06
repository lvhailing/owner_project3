package com.haidehui.model;


import com.haidehui.network.types.IMouldType;

// 海外项目详情
public class OverseaProjectDetailPdfList3B implements IMouldType {

    private String name; // 项目附件名称
    private String path; // 项目附件地址

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
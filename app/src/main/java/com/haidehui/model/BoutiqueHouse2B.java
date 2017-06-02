package com.haidehui.model;


import com.haidehui.network.types.IMouldType;

public class BoutiqueHouse2B implements IMouldType {
    private String picture;
    private String url;
    private String description;

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
package com.haidehui.model;


import com.haidehui.network.types.IMouldType;

public class VersionMo implements IMouldType{
    private String version;
    private String url;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}

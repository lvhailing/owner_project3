package com.haidehui.model;


import com.haidehui.network.types.IMouldType;

public class ResultCheckVersionContentBean implements IMouldType {
	private String version;
	private String url;
	private String forcedUpgrade;

	public String getForcedUpgrade() {
		return forcedUpgrade;
	}

	public void setForcedUpgrade(String forcedUpgrade) {
		this.forcedUpgrade = forcedUpgrade;
	}

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

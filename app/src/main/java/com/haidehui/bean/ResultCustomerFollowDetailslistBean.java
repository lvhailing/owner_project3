package com.haidehui.bean;


import com.haidehui.network.types.IMouldType;

public class ResultCustomerFollowDetailslistBean implements IMouldType {
	private String isChecked;
	private String details;

	public String getIsChecked() {
		return isChecked;
	}

	public void setIsChecked(String isChecked) {
		this.isChecked = isChecked;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}
}

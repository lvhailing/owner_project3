package com.haidehui.bean;


import com.haidehui.network.types.IMouldType;

public class ResultCustomerFollowlistBean implements IMouldType {
	private String name;
	private String time;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
}

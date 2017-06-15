package com.haidehui.bean;


import com.haidehui.network.types.IMouldType;

public class ResultAccountBooklistBean implements IMouldType {
	private String info;
	private String money;
	private String time;
	private String status;

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}

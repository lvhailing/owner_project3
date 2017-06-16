package com.haidehui.bean;


import com.haidehui.network.types.IMouldType;

public class ResultRenGouStatuslistBean implements IMouldType {
	private String project;
	private String name;
	private String money;

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}
}

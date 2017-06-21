package com.haidehui.model;


import com.haidehui.network.types.IMouldType;

public class RenGou3B implements IMouldType {
	private String id;
	private String projectName;
	private String customerName;
	private String downpaymentAmount;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getDownpaymentAmount() {
		return downpaymentAmount;
	}

	public void setDownpaymentAmount(String downpaymentAmount) {
		this.downpaymentAmount = downpaymentAmount;
	}
}

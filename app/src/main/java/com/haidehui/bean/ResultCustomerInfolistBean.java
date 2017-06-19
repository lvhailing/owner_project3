package com.haidehui.bean;


import com.haidehui.network.types.IMouldType;

public class ResultCustomerInfolistBean implements IMouldType {
	private String id;
	private String customerName;
	private String customerPhone;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerPhone() {
		return customerPhone;
	}

	public void setCustomerPhone(String customerPhone) {
		this.customerPhone = customerPhone;
	}
}

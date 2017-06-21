package com.haidehui.model;


import com.haidehui.network.types.IMouldType;

public class CustomerInfo3B implements IMouldType {
	private String customerId;
	private String customerName;
	private String customerPhone;

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
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
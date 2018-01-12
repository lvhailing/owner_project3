package com.haidehui.model;


import com.haidehui.network.types.IMouldType;

public class ExplainOrder3B implements IMouldType {
	private String id; //预约说明会id
	private String customerName; //客户姓名
	private String customerPhone; //客户电话
	private String meetingTime; //参加日期（yyyy-mm-dd）

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

	public String getMeetingTime() {
		return meetingTime;
	}

	public void setMeetingTime(String meetingTime) {
		this.meetingTime = meetingTime;
	}
}
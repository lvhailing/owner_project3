package com.haidehui.model;


import com.haidehui.network.types.IMouldType;

public class AccountBookWithDraw3B implements IMouldType {
	private String cashNum;
	private String cashStatus;
	private String createTime;
	private String id;

	public String getCashNum() {
		return cashNum;
	}

	public void setCashNum(String cashNum) {
		this.cashNum = cashNum;
	}

	public String getCashStatus() {
		return cashStatus;
	}

	public void setCashStatus(String cashStatus) {
		this.cashStatus = cashStatus;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}

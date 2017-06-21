package com.haidehui.model;


import com.haidehui.network.types.IMouldType;

public class ResultRecommendInfoContentBean implements IMouldType {

	private String recommendCode;
	private String total;
	private String totalAmount;

	public String getRecommendCode() {
		return recommendCode;
	}

	public void setRecommendCode(String recommendCode) {
		this.recommendCode = recommendCode;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}
}
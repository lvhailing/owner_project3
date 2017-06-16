package com.haidehui.model;


import com.haidehui.network.types.IMouldType;

public class ResultWithdrawInfoContentBean implements IMouldType {

	private String cashNum;			//	可提现金额
	private String rewardStatus;		//	init 未激活/ sended已发放

	public String getCashNum() {
		return cashNum;
	}

	public void setCashNum(String cashNum) {
		this.cashNum = cashNum;
	}

	public String getRewardStatus() {
		return rewardStatus;
	}

	public void setRewardStatus(String rewardStatus) {
		this.rewardStatus = rewardStatus;
	}
}
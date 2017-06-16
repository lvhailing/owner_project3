package com.haidehui.model;


import com.haidehui.network.types.IMouldType;

public class AccountBookAward3B implements IMouldType {
	private String createTime;
	private String id;
	private String rewardAmount;
	private String rewardStatus;
	private String rewardType;

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

	public String getRewardAmount() {
		return rewardAmount;
	}

	public void setRewardAmount(String rewardAmount) {
		this.rewardAmount = rewardAmount;
	}

	public String getRewardStatus() {
		return rewardStatus;
	}

	public void setRewardStatus(String rewardStatus) {
		this.rewardStatus = rewardStatus;
	}

	public String getRewardType() {
		return rewardType;
	}

	public void setRewardType(String rewardType) {
		this.rewardType = rewardType;
	}
}

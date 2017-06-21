package com.haidehui.model;


import com.haidehui.network.types.IMouldType;
import com.haidehui.network.types.MouldList;

public class ResultRecommendRecordItemContentBean implements IMouldType {

	private String mobile;		//	手机号-我的推荐好友
	private String userLevel;		//	推荐级别
	private String rewardAmount;		//	获得推荐奖励

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getUserLevel() {
		return userLevel;
	}

	public void setUserLevel(String userLevel) {
		this.userLevel = userLevel;
	}

	public String getRewardAmount() {
		return rewardAmount;
	}

	public void setRewardAmount(String rewardAmount) {
		this.rewardAmount = rewardAmount;
	}
}
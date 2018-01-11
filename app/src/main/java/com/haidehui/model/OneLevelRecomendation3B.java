package com.haidehui.model;


import com.haidehui.network.types.IMouldType;

// 事业合伙人一级推荐列表
public class OneLevelRecomendation3B implements IMouldType {
	private String userId ; // 用户id
	private String mobile ; // 手机号
	private String realName ; // 姓名
	private String checkStatus ; //  init未认证（未填写认证信息） submit待认证(提交认证信息待审核) success - 认证成功 fail - 认证失败

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}
}

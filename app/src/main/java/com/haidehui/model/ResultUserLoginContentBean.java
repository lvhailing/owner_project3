package com.haidehui.model;


import com.haidehui.network.types.IMouldType;

public class ResultUserLoginContentBean implements IMouldType {

	private String flag;		//		true/false,true为登录成功，false为登录失败
	private String message;		//		登录返回信息
	private String userId;		//	用户ID
	private String mobile;		//	手机号
	private String realName;		//	真实姓名
	private String idNo;		//	身份证号（没认证，身份证是空）
	private String checkStatus;		//	认证状态：init未认证（注册后未填写认证信息）	submit待认证(提交认证信息待审核)	success - 认证成功(后台审核通过)	fail - 认证失败(后台审核未通过)

	private String nickName;
	private String phone;
	private String openAccountStatus;
	private String token;

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

	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

	public String getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getOpenAccountStatus() {
		return openAccountStatus;
	}

	public void setOpenAccountStatus(String openAccountStatus) {
		this.openAccountStatus = openAccountStatus;
	}

	// public UserLoginResultBean(String flag, String message){
	// setFlag(flag);
	// setMessage(message);
	// }
	// {flag=true, message=, nickName=aaaasw, userId=14120415074007298439}


	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

}
package com.haidehui.model;


import com.haidehui.network.types.IMouldType;

public class ResultUserLoginContentBean implements IMouldType {

	private String flag;
	private String message;
	private String userId;
	private String nickName;
	private String phone;
	private String openAccountStatus;
	private String token;

	public ResultUserLoginContentBean(String flag, String message, String userId, String nickName, String phone, String openAccountStatus) {
		this.flag = flag;
		this.message = message;
		this.userId = userId;
		this.nickName = nickName;
		this.phone = phone;
		this.openAccountStatus = openAccountStatus;
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
	public ResultUserLoginContentBean(String flag, String message,
			String userId, String nickName, String phone) {
		setFlag(flag);
		setMessage(message);
		setUserId(userId);
		setNickName(nickName);
		setPhone(phone);
	}

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
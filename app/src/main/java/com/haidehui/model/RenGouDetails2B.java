package com.haidehui.model;


import com.haidehui.network.types.IMouldType;

public class RenGouDetails2B implements IMouldType {
    private String customerName;
    private String customerPhone;
    private String status;
    private String projectName;
    private String roomNumber;
    private String downpaymentAmount;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getDownpaymentAmount() {
        return downpaymentAmount;
    }

    public void setDownpaymentAmount(String downpaymentAmount) {
        this.downpaymentAmount = downpaymentAmount;
    }
}
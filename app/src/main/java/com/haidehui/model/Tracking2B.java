package com.haidehui.model;


import com.haidehui.network.types.IMouldType;
import com.haidehui.network.types.MouldList;

public class Tracking2B implements IMouldType {
    private String customerId;
    private String customerName;
    private String customerPhone;
    private String editTime;
    private String customerTrackingId;
    private String houseProject;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
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

    public String getEditTime() {
        return editTime;
    }

    public void setEditTime(String editTime) {
        this.editTime = editTime;
    }

    public String getCustomerTrackingId() {
        return customerTrackingId;
    }

    public void setCustomerTrackingId(String customerTrackingId) {
        this.customerTrackingId = customerTrackingId;
    }

    public String getHouseProject() {
        return houseProject;
    }

    public void setHouseProject(String houseProject) {
        this.houseProject = houseProject;
    }
}
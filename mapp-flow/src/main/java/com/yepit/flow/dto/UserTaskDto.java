package com.yepit.flow.dto;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/11/2.
 */
public class UserTaskDto implements Serializable {
    private static final long serialVersionUID = 1125330759459852101L;
    private String businessName ;
    private String taskId;
    private String assigement;//受理人员
    private String createDate;

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getAssigement() {
        return assigement;
    }

    public void setAssigement(String assigement) {
        this.assigement = assigement;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getCreateCustomerManager() {
        return createCustomerManager;
    }

    public void setCreateCustomerManager(String createCustomerManager) {
        this.createCustomerManager = createCustomerManager;
    }

    private String createCustomerManager;//提交客户经理
}

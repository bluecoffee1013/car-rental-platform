package com.yepit.flow.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/11/2.
 */
@Data
public class FlowTaskDto implements Serializable{
    private static final long serialVersionUID = -5971350475963830095L;

    String taskName ;

    String taskId;

    String userName;

    String userId = "0";//用户Id
    String customerManager = "0";
    String firstTrialManager = "0";//初审人员
    String secondRiskManager = "0";//复审人员
    String thirdRiskManager = "0";//面签人员
    String financialManager = "0";//退款会记
    String channelManager = "0";//渠道家访经理
    String insuranceManager = "0";//保险确认经理
    String finalJudgment = "0";//终审经理
    String makeLoans = "0";//放款会计
    String sendCarManager = "0";//发放车辆经理
    String afterSaleManager = "0";//售后安装GPRS经理

    String result = "1";//处理结果0：拒绝、1：同意、2：资料不全

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCustomerManager() {
        return customerManager;
    }

    public void setCustomerManager(String customerManager) {
        this.customerManager = customerManager;
    }

    public String getFirstTrialManager() {
        return firstTrialManager;
    }

    public void setFirstTrialManager(String firstTrialManager) {
        this.firstTrialManager = firstTrialManager;
    }

    public String getSecondRiskManager() {
        return secondRiskManager;
    }

    public void setSecondRiskManager(String secondRiskManager) {
        this.secondRiskManager = secondRiskManager;
    }

    public String getThirdRiskManager() {
        return thirdRiskManager;
    }

    public void setThirdRiskManager(String thirdRiskManager) {
        this.thirdRiskManager = thirdRiskManager;
    }

    public String getFinancialManager() {
        return financialManager;
    }

    public void setFinancialManager(String financialManager) {
        this.financialManager = financialManager;
    }

    public String getChannelManager() {
        return channelManager;
    }

    public void setChannelManager(String channelManager) {
        this.channelManager = channelManager;
    }

    public String getInsuranceManager() {
        return insuranceManager;
    }

    public void setInsuranceManager(String insuranceManager) {
        this.insuranceManager = insuranceManager;
    }

    public String getFinalJudgment() {
        return finalJudgment;
    }

    public void setFinalJudgment(String finalJudgment) {
        this.finalJudgment = finalJudgment;
    }

    public String getMakeLoans() {
        return makeLoans;
    }

    public void setMakeLoans(String makeLoans) {
        this.makeLoans = makeLoans;
    }

    public String getSendCarManager() {
        return sendCarManager;
    }

    public void setSendCarManager(String sendCarManager) {
        this.sendCarManager = sendCarManager;
    }

    public String getAfterSaleManager() {
        return afterSaleManager;
    }

    public void setAfterSaleManager(String afterSaleManager) {
        this.afterSaleManager = afterSaleManager;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}

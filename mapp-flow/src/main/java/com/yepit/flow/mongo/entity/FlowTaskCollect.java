package com.yepit.flow.mongo.entity;

import com.yepit.flow.dto.DocInfo;
import lombok.Data;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.Date;
import java.util.List;

/**
 * 每一个工单的最终汇总
 */
@Entity("flow_task_collect")
@Data
public class FlowTaskCollect {

    public Long getBusId() {
        return busId;
    }

    public void setBusId(Long busId) {
        this.busId = busId;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public List<DocInfo> getDocList() {
        return docList;
    }

    public void setDocList(List<DocInfo> docList) {
        this.docList = docList;
    }

    public Long getDeposit() {
        return deposit;
    }

    public void setDeposit(Long deposit) {
        this.deposit = deposit;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getAppointmentPlace() {
        return appointmentPlace;
    }

    public void setAppointmentPlace(String appointmentPlace) {
        this.appointmentPlace = appointmentPlace;
    }

    public String getHomeVisitTime() {
        return homeVisitTime;
    }

    public void setHomeVisitTime(String homeVisitTime) {
        this.homeVisitTime = homeVisitTime;
    }

    public String getHomeVisitPlace() {
        return homeVisitPlace;
    }

    public void setHomeVisitPlace(String homeVisitPlace) {
        this.homeVisitPlace = homeVisitPlace;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public String getIsInsurance() {
        return isInsurance;
    }

    public void setIsInsurance(String isInsurance) {
        this.isInsurance = isInsurance;
    }

    /**
     * 业务流程id
     */
    @Id
    private Long busId;

    /**
     * 创建客户经理id
     */
    private String createUserId;

    /**
     * 创建客户经理姓名
     */
    private String createUserName;

    /**
     * 任务创建时间
     */
    private Date createTime;

    /**
     * 任务结束时间
     */
    private Date finishTime;

    /**
     * 上传的文件
     */
    private List<DocInfo> docList;

    /**
     * 押金
     */
    private Long deposit;

    /**
     * 预约时间
     */
    private String appointmentTime;

    /**
     * 预约地点
     */
    private String appointmentPlace;

    /**
     * 家访时间
     */
    private String homeVisitTime;

    /**
     * 家访地点
     */
    private String homeVisitPlace;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 客户手机号
     */
    private String customerMobile;

    /**
     * 保险是否已出单
     */
    private String isInsurance;
}

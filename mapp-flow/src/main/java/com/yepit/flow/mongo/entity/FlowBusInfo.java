package com.yepit.flow.mongo.entity;

import lombok.Data;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.Date;

@Data
@Entity("flow_business_info")
public class FlowBusInfo {

    @Id
    private String doneId;

    /**
     * 流程节点ID
     */
    private String flowNodeId;

    /**
     * 流程节点名称
     */
    private String flowNodeName;

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
     * 业务流程id
     */
    private Long busId;

    /**
     * 任务节点ID
     */
    private String taskId;

    /**
     * 当前节点处理人id
     */
    private Long doneUserId;

    /**
     * 当前节点处理人姓名
     */
    private String doneUserName;

    /**
     * 节点处理提交时间
     */
    private Date doneTime;

    /**
     * 处理结果0、拒绝 1、同意，2、补全资料
     */
    private String result;

    /**
     * 备注信息
     */
    private String remark;

    /**
     * 详细的表单信息
     */
    private FlowTaskDetailInfo formDetail;
}

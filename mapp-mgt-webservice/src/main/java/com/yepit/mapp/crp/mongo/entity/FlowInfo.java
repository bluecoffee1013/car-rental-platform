package com.yepit.mapp.crp.mongo.entity;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

/**
 * 流程定义
 * @author qianlong
 */
@Entity("flow_info")
public class FlowInfo {

    @Id
    private Integer flowId;

    private String flowName;

    public Integer getFlowId() {
        return flowId;
    }

    public void setFlowId(Integer flowId) {
        this.flowId = flowId;
    }

    public String getFlowName() {
        return flowName;
    }

    public void setFlowName(String flowName) {
        this.flowName = flowName;
    }
}

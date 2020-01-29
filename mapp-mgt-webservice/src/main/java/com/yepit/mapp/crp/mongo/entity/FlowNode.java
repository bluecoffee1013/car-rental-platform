package com.yepit.mapp.crp.mongo.entity;

import lombok.Data;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

/**
 * 流程节点定义
 * @author qianlong
 */
@Data
@Entity("flow_node")
public class FlowNode {

    @Id
    private Long nodeId;

    private Integer flowId;

    private String nodeName;

    private String nodeAliasName;

    private Long roleId;

    private String roleName;
}

package com.yepit.mapp.crp.mongo.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class FlowPrivilege implements Serializable {

    private static final long serialVersionUID = 6519568728254482605L;

    private Long nodeId;

    private String nodeName;

    private String nodeAliasName;
}

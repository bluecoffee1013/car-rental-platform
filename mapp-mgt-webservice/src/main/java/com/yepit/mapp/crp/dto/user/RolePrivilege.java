package com.yepit.mapp.crp.dto.user;

import lombok.Data;

import java.io.Serializable;

@Data
public class RolePrivilege implements Serializable {

    private static final long serialVersionUID = -4009723940709985052L;

    private Long roleId;

    private Long nodeId;

    private String nodeName;
}

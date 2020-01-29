package com.yepit.mapp.crp.dto.user;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户在流程中的权限
 * @author qianlong
 */
@Data
@ApiModel
public class UserPrivilegeDTO implements Serializable {

    private static final long serialVersionUID = 4575221554857823250L;

    private Long adminId;

    private Long roleId;

    private String roleName;

    private Long nodeId;

    private String nodeName;

}

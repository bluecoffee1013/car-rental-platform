package com.yepit.mapp.crp.dto.user;

import com.yepit.mapp.crp.dto.system.AdminDTO;
import com.yepit.mapp.crp.dto.system.RoleDTO;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
@ApiModel
public class LoginResp implements Serializable {

    private String accessToken;

    private AdminDTO userInfo;

    private RoleDTO role;

    private List<RolePrivilege> userPrivileges;
}

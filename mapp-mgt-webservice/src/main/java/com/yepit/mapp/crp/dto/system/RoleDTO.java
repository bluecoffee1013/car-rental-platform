package com.yepit.mapp.crp.dto.system;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel
public class RoleDTO {

    private Long roleId;

    private String roleName;
}

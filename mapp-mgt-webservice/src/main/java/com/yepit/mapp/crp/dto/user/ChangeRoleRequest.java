package com.yepit.mapp.crp.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 修改角色请求
 */
@ApiModel
@Data
public class ChangeRoleRequest implements Serializable {

    @ApiModelProperty(hidden = true)
    private Long userid;

    private Long newRoleId;

    private List<Long> flowNodeIds;
}

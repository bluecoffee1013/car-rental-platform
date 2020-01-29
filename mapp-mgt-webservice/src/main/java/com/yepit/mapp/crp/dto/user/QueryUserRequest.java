package com.yepit.mapp.crp.dto.user;

import com.yepit.mapp.framework.dto.common.BaseRequest;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel
public class QueryUserRequest extends BaseRequest {

    private String mobile;

    private String email;

    private Integer status;

    private Integer roleId;

}

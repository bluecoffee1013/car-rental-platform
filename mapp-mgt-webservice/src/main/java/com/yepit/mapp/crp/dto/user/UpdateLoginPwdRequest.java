package com.yepit.mapp.crp.dto.user;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel
public class UpdateLoginPwdRequest {

    private String loginName;

    private String oldPwd;

    private String newPwd;

}

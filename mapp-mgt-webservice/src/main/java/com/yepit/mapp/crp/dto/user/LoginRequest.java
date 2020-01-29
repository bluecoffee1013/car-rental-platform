package com.yepit.mapp.crp.dto.user;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel
@Data
public class LoginRequest {

    private String loginName;

    private String loginPwd;

}

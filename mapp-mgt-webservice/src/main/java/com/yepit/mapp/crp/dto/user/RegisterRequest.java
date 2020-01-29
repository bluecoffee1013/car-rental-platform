package com.yepit.mapp.crp.dto.user;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * 注册信息
 * @author qianlong
 */
@Data
@ApiModel
public class RegisterRequest implements Serializable {

    private String phoneNumber;

    private String realName;

    private Long roleId;

    private String authCode;

    private String password;

}

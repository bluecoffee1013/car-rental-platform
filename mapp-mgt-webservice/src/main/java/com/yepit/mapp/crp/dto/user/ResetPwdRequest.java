package com.yepit.mapp.crp.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 重新设置密码请求
 * @author qianlong
 */
@Data
public class ResetPwdRequest implements Serializable {

    private String loginName;

    private String newPwd;

    private String confirmPwd;

    private String otp;
}

package com.yepit.mapp.crp.dto.system;

import com.yepit.mapp.framework.dto.common.BaseRequest;

/**
 * Created by qianlong on 2017/8/19.
 */
public class AdminLoginRequest {

    private static final long serialVersionUID = -5569458060626435297L;

    private String loginName;
    private String loginPwd;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getLoginPwd() {
        return loginPwd;
    }

    public void setLoginPwd(String loginPwd) {
        this.loginPwd = loginPwd;
    }
}

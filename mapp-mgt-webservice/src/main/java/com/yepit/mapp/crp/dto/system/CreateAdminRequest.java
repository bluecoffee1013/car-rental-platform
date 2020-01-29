package com.yepit.mapp.crp.dto.system;


import com.yepit.mapp.framework.dto.common.BaseRequest;

/**
 * Created by qianlong on 2017/8/20.
 */
public class CreateAdminRequest extends BaseRequest {

    private static final long serialVersionUID = 1232442216811509097L;

    private String loginName;

    private String loginPwd;

    private String realname;

    private String mobile;

    private String email;

    private String officePhone;

    private Long roleId;

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

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOfficePhone() {
        return officePhone;
    }

    public void setOfficePhone(String officePhone) {
        this.officePhone = officePhone;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}

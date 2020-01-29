package com.yepit.mapp.crp.dto.system;

import com.yepit.mapp.framework.dto.common.BaseRequest;

public class UpdateAdminRoleRequest extends BaseRequest {

    private static final long serialVersionUID = -2347972254858865756L;

    private Long adminId;

    private Long newRoleId;

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public Long getNewRoleId() {
        return newRoleId;
    }

    public void setNewRoleId(Long newRoleId) {
        this.newRoleId = newRoleId;
    }
}

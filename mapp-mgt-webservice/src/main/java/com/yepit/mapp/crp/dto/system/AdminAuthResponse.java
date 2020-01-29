package com.yepit.mapp.crp.dto.system;

import java.io.Serializable;

/**
 *
 * @author qianlong
 * @date 2017/10/22
 */
public class AdminAuthResponse implements Serializable{

    private static final long serialVersionUID = -907601389800909819L;

    private AdminDTO adminInfo;

    private String accessToken;

    public AdminDTO getAdminInfo() {
        return adminInfo;
    }

    public void setAdminInfo(AdminDTO adminInfo) {
        this.adminInfo = adminInfo;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}

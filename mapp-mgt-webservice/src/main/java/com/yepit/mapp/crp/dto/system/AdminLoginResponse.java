package com.yepit.mapp.crp.dto.system;

import com.yepit.mapp.crp.domain.system.SysAdmin;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author qianlong
 * @date 2017/8/19
 */
public class AdminLoginResponse implements Serializable {

    private static final long serialVersionUID = -1645887770175580886L;

    private String accessToken;
    private SysAdmin admin;
    private List<MenuDTO> adminMenus;

    public SysAdmin getAdmin() {
        return admin;
    }

    public void setAdmin(SysAdmin admin) {
        this.admin = admin;
    }

    public List<MenuDTO> getAdminMenus() {
        return adminMenus;
    }

    public void setAdminMenus(List<MenuDTO> adminMenus) {
        this.adminMenus = adminMenus;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}

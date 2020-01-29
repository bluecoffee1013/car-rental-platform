package com.yepit.mapp.crp.domain.system;

public class SysRoleAuth {
    private Long roleAuthId;

    private Long roleId;

    private String menuId;

    public Long getRoleAuthId() {
        return roleAuthId;
    }

    public void setRoleAuthId(Long roleAuthId) {
        this.roleAuthId = roleAuthId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId == null ? null : menuId.trim();
    }
}
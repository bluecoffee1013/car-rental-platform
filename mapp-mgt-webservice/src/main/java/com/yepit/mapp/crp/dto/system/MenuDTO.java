package com.yepit.mapp.crp.dto.system;

import com.yepit.mapp.crp.domain.system.SysMenu;

import java.io.Serializable;
import java.util.List;

/**
 * Created by qianlong on 2017/8/20.
 */
public class MenuDTO implements Serializable {

    private static final long serialVersionUID = -3895671007817238945L;

    private SysMenu parentMenu;
    private List<SysMenu> childrenMenu;

    public SysMenu getParentMenu() {
        return parentMenu;
    }

    public void setParentMenu(SysMenu parentMenu) {
        this.parentMenu = parentMenu;
    }

    public List<SysMenu> getChildrenMenu() {
        return childrenMenu;
    }

    public void setChildrenMenu(List<SysMenu> childrenMenu) {
        this.childrenMenu = childrenMenu;
    }
}

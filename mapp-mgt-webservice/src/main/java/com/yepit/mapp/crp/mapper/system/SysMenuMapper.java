package com.yepit.mapp.crp.mapper.system;


import com.yepit.mapp.crp.domain.system.SysMenu;

import java.util.List;

public interface SysMenuMapper {
    int deleteByPrimaryKey(String menuId);

    int insert(SysMenu record);

    int insertSelective(SysMenu record);

    SysMenu selectByPrimaryKey(String menuId);

    int updateByPrimaryKeySelective(SysMenu record);

    int updateByPrimaryKey(SysMenu record);

    List<SysMenu> listByCond(SysMenu sysMenu);
}
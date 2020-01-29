package com.yepit.mapp.crp.mapper.system;



import com.yepit.mapp.crp.domain.system.SysMenu;
import com.yepit.mapp.crp.domain.system.SysRoleAuth;

import java.util.List;

public interface SysRoleAuthMapper {
    int deleteByPrimaryKey(Long roleAuthId);

    int insert(SysRoleAuth record);

    int insertSelective(SysRoleAuth record);

    SysRoleAuth selectByPrimaryKey(Long roleAuthId);

    int updateByPrimaryKeySelective(SysRoleAuth record);

    int updateByPrimaryKey(SysRoleAuth record);

    List<SysMenu> listByRoleId(Long roleId);
}
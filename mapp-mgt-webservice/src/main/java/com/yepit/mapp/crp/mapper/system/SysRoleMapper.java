package com.yepit.mapp.crp.mapper.system;

import com.yepit.mapp.crp.domain.system.SysRole;

import java.util.List;

public interface SysRoleMapper {
    int deleteByPrimaryKey(Long roleId);

    int insert(SysRole record);

    int insertSelective(SysRole record);

    SysRole selectByPrimaryKey(Long roleId);

    int updateByPrimaryKeySelective(SysRole record);

    int updateByPrimaryKey(SysRole record);

    List<SysRole> listByCond(SysRole queryCond);
}
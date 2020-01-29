package com.yepit.mapp.crp.mapper.system;


import com.yepit.mapp.crp.domain.system.SysAdminRole;

import java.util.List;

public interface SysAdminRoleMapper {
    int deleteByPrimaryKey(Long adminRoleId);

    int insert(SysAdminRole record);

    int insertSelective(SysAdminRole record);

    SysAdminRole selectByPrimaryKey(Long adminRoleId);

    int updateByPrimaryKeySelective(SysAdminRole record);

    int updateByPrimaryKey(SysAdminRole record);

    List<SysAdminRole> listByCond(SysAdminRole cond);
}
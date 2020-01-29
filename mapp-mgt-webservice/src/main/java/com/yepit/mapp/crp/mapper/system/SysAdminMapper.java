package com.yepit.mapp.crp.mapper.system;


import com.yepit.mapp.crp.domain.system.SysAdmin;
import com.yepit.mapp.crp.dto.system.PageSearchAdminRequest;
import com.yepit.mapp.crp.dto.system.PageSearchAdminResponse;

import java.util.List;

public interface SysAdminMapper {
    int deleteByPrimaryKey(Long adminId);

    int insert(SysAdmin record);

    int insertSelective(SysAdmin record);

    SysAdmin selectByPrimaryKey(Long adminId);

    int updateByPrimaryKeySelective(SysAdmin record);

    int updateByPrimaryKey(SysAdmin record);

    List<SysAdmin> listByCond(SysAdmin cond);

    List<PageSearchAdminResponse> pageListByCond(SysAdmin cond);

    List<PageSearchAdminResponse> searchByCond(PageSearchAdminRequest cond);

}
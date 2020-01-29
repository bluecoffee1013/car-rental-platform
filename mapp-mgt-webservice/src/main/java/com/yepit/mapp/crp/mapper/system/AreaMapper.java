package com.yepit.mapp.crp.mapper.system;


import com.yepit.mapp.crp.domain.system.Area;

import java.util.List;

public interface AreaMapper {
    int deleteByPrimaryKey(Integer areaId);

    int insert(Area record);

    int insertSelective(Area record);

    Area selectByPrimaryKey(Integer areaId);

    int updateByPrimaryKeySelective(Area record);

    int updateByPrimaryKey(Area record);

    List<Area> listByCond(Area queryCond);
}
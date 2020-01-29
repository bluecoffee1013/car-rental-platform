package com.yepit.mapp.crp.service;

import com.yepit.mapp.crp.domain.system.Area;
import com.yepit.mapp.crp.mapper.system.AreaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by qianlong on 2017/10/3.
 */
@Service
public class AreaService {

    @Autowired
    private AreaMapper areaMapper;

    private static List<Area> allAreas = null;

    private static List<Area> allProvinces = null;

    private static HashMap<Integer,Area> allAreaMap = null;

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.DEFAULT, rollbackFor = {RuntimeException.class, Exception.class})
    public void addArea(Area area) throws Exception{
        areaMapper.insertSelective(area);
    }

    /**
     * 获取所有地区信息
     * @return
     * @throws Exception
     */
    public List<Area> getAllAreas() throws Exception{
        if(allAreas == null || allAreas.size() == 0){
            this.areaInit();
        }
        return allAreas;
    }

    /**
     * 初始化所有地区信息
     */
    public void areaInit() throws Exception{
        if(allAreas == null || allAreas.size() == 0){
            allAreas = areaMapper.listByCond(new Area());
            for(Area area:allAreas){
                allAreaMap = new HashMap<Integer,Area>();
                allAreaMap.put(area.getAreaId(),area);
            }
        }
    }

    /**
     * 根据主键查询区域信息
     * @param areaId
     * @return
     * @throws Exception
     */
    public Area getAreaById(Integer areaId) throws Exception{
        if(allAreas == null || allAreas.size() == 0 || allAreaMap == null || allAreaMap.size() == 0){
            areaInit();
        }
        for(Area area:allAreas){
            if(areaId == area.getAreaId()){
                return area;
            }
        }
        return null;
    }

    /**
     * 获取所有省份信息
     * @return
     * @throws Exception
     */
    public List<Area> getAllProvinces() throws Exception{
        if(allProvinces == null || allProvinces.size() == 0){
            areaInit();
            allProvinces = new ArrayList<Area>();
            for(Area area:allAreas){
                if(area.getAreaLevel() == 1){
                    allProvinces.add(area);
                }
            }
        }

        return allProvinces;
    }

    /**
     * 根据省份/直辖市中文名称获取下属一级地市
     * @param provinceName
     * @return
     * @throws Exception
     */
    public List<Area> getCityByProvinceName(String provinceName) throws Exception{
        allProvinces = getAllProvinces();
        Integer provinceId = null;
        for(Area province:allAreas){
            if(province.equals(provinceName)){
                provinceId = province.getAreaId();
            }
        }

        if(provinceId == null){
            return null;
        }
        Area queryCond = new Area();
        queryCond.setParentAreaId(provinceId);

        List<Area> cityList = areaMapper.listByCond(queryCond);
        return cityList;
    }

    /**
     * 根据省份/直辖市的区域ID获取下属一级地市
     * @param provinceId
     * @return
     * @throws Exception
     */
    public List<Area> getCityByProvinceId(Integer provinceId) throws Exception{
        allProvinces = getAllProvinces();
        if(provinceId == null){
            return null;
        }
        Area queryCond = new Area();
        queryCond.setParentAreaId(provinceId);

        List<Area> cityList = areaMapper.listByCond(queryCond);
        return cityList;
    }
}

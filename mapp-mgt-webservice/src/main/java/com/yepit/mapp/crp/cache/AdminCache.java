package com.yepit.mapp.crp.cache;

import com.yepit.mapp.crp.domain.system.SysAdmin;
import com.yepit.mapp.crp.dto.system.AdminDTO;
import com.yepit.mapp.crp.mapper.system.SysAdminMapper;
import com.yepit.mapp.framework.cache.BaseCache;
import com.yepit.mapp.framework.exception.CacheException;
import com.yepit.mapp.framework.util.JsonUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * Created by qianlong on 2017/10/22.
 */
@Service
public class AdminCache extends BaseCache {

    @Autowired
    private SysAdminMapper sysAdminMapper;

    static final String ADMIN_LOGIN_NAME_KEY = "sysAdmin";

    static final String ADMIN_ID_KEY = "sysAdminId";
    @Override
    public void reload() throws CacheException {
        try{
            redisUtils.remove(ADMIN_LOGIN_NAME_KEY);
            redisUtils.remove(ADMIN_ID_KEY);
            List<SysAdmin> adminList = sysAdminMapper.listByCond(new SysAdmin());
            if (adminList != null && adminList.size() > 0) {
                HashMap<String, String> sysAdminMap = new HashMap<String, String>();
                HashMap<String, String> sysAdminIdMap = new HashMap<String, String>();
                for (SysAdmin sysAdmin : adminList) {
                    AdminDTO adminDTO = new AdminDTO();
                    BeanUtils.copyProperties(sysAdmin,adminDTO);
                    sysAdminMap.put(sysAdmin.getLoginName(), JsonUtils.Object2Json(adminDTO));
                    sysAdminIdMap.put(sysAdmin.getAdminId().toString(), JsonUtils.Object2Json(adminDTO));
                }
                redisUtils.hmset(ADMIN_LOGIN_NAME_KEY, sysAdminMap);
                redisUtils.hmset(ADMIN_ID_KEY, sysAdminIdMap);
            }
        }catch (Exception ex){
            ex.printStackTrace();
            throw new CacheException("999998", "刷新缓存数据异常:" + ex.getMessage());
        }
    }

    /**
     * 根据登录名获取管理员对象
     * @param loginName
     * @return
     * @throws Exception
     */
    public AdminDTO getAdminByLoginName(String loginName) throws CacheException {
        if(StringUtils.isBlank(loginName)){
            return null;
        }
        try{
            Object value = redisUtils.hmget("sysAdmin", loginName);
            if(value == null){
                return null;
            }
            AdminDTO admin = JsonUtils.jsonToJavaBean(value.toString(),AdminDTO.class);
            return admin;
        }catch (Exception ex){
            throw new CacheException("999998", "刷新缓存数据异常:" + ex.getMessage());
        }
    }

    /**
     * 根据用户ID获取管理员对象
     * @param adminId
     * @return
     * @throws Exception
     */
    public AdminDTO getAdminById(String adminId) throws CacheException {
        if(StringUtils.isBlank(adminId)){
            return null;
        }
        try{
            Object value = redisUtils.hmget(ADMIN_ID_KEY, adminId);
            if(value == null){
                return null;
            }
            AdminDTO admin = JsonUtils.jsonToJavaBean(value.toString(),AdminDTO.class);
//            AdminDTO admin = (AdminDTO)value;
            return admin;
        }catch (Exception ex){
            throw new CacheException("999998", "刷新缓存数据异常:" + ex.getMessage());
        }
    }
}

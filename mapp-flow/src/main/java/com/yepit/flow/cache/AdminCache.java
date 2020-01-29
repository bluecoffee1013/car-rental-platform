package com.yepit.flow.cache;

import com.yepit.flow.dto.AdminDTO;
import com.yepit.mapp.framework.cache.BaseCache;
import com.yepit.mapp.framework.exception.CacheException;
import com.yepit.mapp.framework.util.JsonUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class AdminCache extends BaseCache {

    static final String ADMIN_ID_KEY = "sysAdminId";

    /**
     * 由子类实现
     */
    @Override
    public void reload() throws Exception {

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
            return admin;
        }catch (Exception ex){
            throw new CacheException("999998", "刷新缓存数据异常:" + ex.getMessage());
        }
    }
}

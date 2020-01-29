package com.yepit.mapp.crp.cache;

import com.yepit.mapp.crp.domain.system.SysRole;
import com.yepit.mapp.crp.dto.system.RoleDTO;
import com.yepit.mapp.crp.mapper.system.SysRoleMapper;
import com.yepit.mapp.framework.cache.BaseCache;
import com.yepit.mapp.framework.exception.CacheException;
import com.yepit.mapp.framework.util.RedisUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class RoleCache extends BaseCache {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    private final static String ROLE_KEY = "sysRole";

    private final static String ROLE_LIST_KEY = "sysRoleList";

    /**
     * 由子类实现
     */
    @Override
    public void reload() throws Exception {
        try{
            redisUtils.remove(ROLE_KEY);
            redisUtils.remove(ROLE_LIST_KEY);
            List<SysRole> roleList = sysRoleMapper.listByCond(new SysRole());
            if (roleList != null && roleList.size() > 0) {
                HashMap<String, String> sysRoleMap = new HashMap<String, String>();
                List<RoleDTO> roleDtoList = new ArrayList<RoleDTO>();
                for (SysRole sysRole : roleList) {
                    sysRoleMap.put(sysRole.getRoleId().toString(), sysRole.getRoleName());
                    //系统管理员角色不放缓存
                    if(sysRole.getRoleId() != 1){
                        RoleDTO roleDTO = new RoleDTO();
                        BeanUtils.copyProperties(sysRole,roleDTO);
                        roleDtoList.add(roleDTO);
                    }
                }
                redisUtils.hmset(ROLE_KEY, sysRoleMap);
                redisUtils.set(ROLE_LIST_KEY, roleDtoList);
            }
        }catch (Exception ex){
            ex.printStackTrace();
            throw new CacheException("999998", "刷新缓存数据异常:" + ex.getMessage());
        }
    }

    /**
     * 根据roleId获取roleName
     * @param roleId
     * @return
     */
    public String getRoleNameByRoleId(String roleId){
        Object obj = redisUtils.hmget(ROLE_KEY,roleId);
        if(obj != null){
            return (String)obj;
        }
        return null;
    }

    /**
     * 返回除系统管理员外的所有角色
     * @return
     */
    public List<RoleDTO> getAllRole(){
        Object obj = redisUtils.get(ROLE_LIST_KEY);
        if(obj != null){
            return (List<RoleDTO>) obj;
        }
        return null;
    }
}

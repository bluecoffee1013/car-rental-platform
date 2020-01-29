package com.yepit.mapp.framework.cache;

import com.yepit.mapp.framework.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by qianlong on 2017/10/22.
 */
@Service
public abstract class BaseCache {

    @Autowired
    protected RedisUtils redisUtils;

    /**
     * 由子类实现
     */
    abstract public void reload() throws Exception;
}

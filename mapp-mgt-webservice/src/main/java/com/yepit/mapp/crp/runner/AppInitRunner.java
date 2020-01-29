package com.yepit.mapp.crp.runner;

import com.yepit.mapp.crp.cache.AdminCache;
import com.yepit.mapp.crp.cache.RoleCache;
import com.yepit.mapp.crp.service.FlowInfoService;
import com.yepit.mapp.crp.service.FlowNodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(value=1)
public class AppInitRunner implements CommandLineRunner {

    @Autowired
    private AdminCache adminCache;

    @Autowired
    private RoleCache roleCache;

    @Autowired
    private FlowInfoService flowInfoService;

    @Autowired
    private FlowNodeService flowNodeService;

    @Override
    public void run(String... strings) throws Exception {
        try {
            adminCache.reload();
            roleCache.reload();
            flowInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 流程定义初始化
     * @throws Exception
     */
    private void flowInit() throws Exception{
        flowInfoService.flowInit();
        flowNodeService.flowNodeInit();
    }
}

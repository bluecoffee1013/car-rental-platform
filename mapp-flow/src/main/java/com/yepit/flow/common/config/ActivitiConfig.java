package com.yepit.flow.common.config;

/**
 * Created by Administrator on 2018/10/26.
 */

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;


@Configuration
@ImportResource(locations={"classpath:activiti.cfg.xml"})
public class ActivitiConfig {
}

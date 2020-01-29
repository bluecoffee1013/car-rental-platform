package com.yepit.flow;

import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@SpringBootApplication
@EnableScheduling
@ComponentScan({"com.yepit.mapp.framework", "com.yepit.flow"})
@ServletComponentScan
@EnableCaching
@Configuration
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
@EnableTransactionManagement
@MapperScan("com.yepit.flow.bo")
@EnableDiscoveryClient
public class FlowApplication extends SpringBootServletInitializer {

	public static void main(String[] args) throws Exception {
		System.out.println("------------FlowApplication is start---------------");
		SpringApplication.run(FlowApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		logger.info("----------FlowApplication configure-------");
		return builder.sources(this.getClass());
	}

}

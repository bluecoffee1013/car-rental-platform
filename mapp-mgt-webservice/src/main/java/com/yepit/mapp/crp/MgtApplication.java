package com.yepit.mapp.crp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableScheduling
@ComponentScan({"com.yepit"})
@ServletComponentScan
@EnableCaching
@Configuration
@EnableAutoConfiguration
@MapperScan({"com.yepit.mapp.crp.mapper"})
@EnableMongoRepositories({"com.yepit.mapp.crp.mongo.dao"})
@EnableTransactionManagement
@EnableDiscoveryClient
public class MgtApplication extends SpringBootServletInitializer {

    public static void main(String[] args) throws Exception {
        System.out.println("------------MgtApplication is start---------------");
        SpringApplication.run(MgtApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        logger.info("----------MgtApplication configure-------");
        return builder.sources(this.getClass());
    }

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

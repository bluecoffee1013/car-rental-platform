package com.yepit.mapp.crp.common.config;

import com.yepit.mapp.crp.common.interceptor.UserAuthInterceptor;
import com.yepit.mapp.crp.common.resolver.LoginUserHandlerMethodArgumentResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

/**
 * Created by qianlong on 2017/8/20.
 * @author
 */
@Configuration
public class AppConfigurer extends WebMvcConfigurationSupport {

    private static final Logger logger = LoggerFactory.getLogger(AppConfigurer.class);

    @Bean
    UserAuthInterceptor appInterceptor() {
        return new UserAuthInterceptor();
    }

    @Autowired
    private LoginUserHandlerMethodArgumentResolver loginUserHandlerMethodArgumentResolver;

//    @Bean
//    public PageHelper pageHelper() {
//        logger.info("MyBatis PageHelper Register");
//        PageHelper pageHelper = new PageHelper();
//        Properties p = new Properties();
//        p.setProperty("offsetAsPageNum", "true");
//        p.setProperty("rowBoundsWithCount", "true");
//        p.setProperty("pageSizeZero", "true");
//        p.setProperty("reasonable", "false");
//        pageHelper.setProperties(p);
//        return pageHelper;
//    }

    /**
     * 发现如果继承了WebMvcConfigurationSupport，则在yml中配置的相关内容会失效。
     * 需要重新指定静态资源
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        super.addResourceHandlers(registry);
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        /**
         * 多个拦截器组成一个拦截器链
         * addPathPatterns 用于添加拦截规则
         * excludePathPatterns 用户排除拦截
         */
        registry.addInterceptor(appInterceptor()).addPathPatterns("/**")
                .excludePathPatterns("/swagger-ui.html/**")
                .excludePathPatterns("/swagger-resources/**")
                .excludePathPatterns("/v2/api-docs/**")
                .excludePathPatterns("/configuration/**");
        super.addInterceptors(registry);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedHeaders("*")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "DELETE", "PUT")
                .maxAge(3600);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(loginUserHandlerMethodArgumentResolver);
    }

}

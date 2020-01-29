package com.yepit.mapp.crp.common.config;

import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * Swagger配置
 *
 * @author Mark sunlightcs@gmail.com
 * @since 3.0.0 2018-01-16
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig{

    @Bean
    public Docket createRestApi() {
        ParameterBuilder aParameterBuilder = new ParameterBuilder();
        aParameterBuilder.parameterType("header")
                .name("accessToken")
                .description("用户登录的token")
                .modelRef(new ModelRef("string"))
                .required(false).build();
        List<Parameter> aParameters = new ArrayList<Parameter>();
        aParameters.add(aParameterBuilder.build());

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                //加了ApiOperation注解的类，生成接口文档
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                //包下的类，生成接口文档
                //.apis(RequestHandlerSelectors.basePackage("com.envision.controller"))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(aParameters);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("神买车租赁平台APIs-平台类部分")
                .description("YepIT")
                .termsOfServiceUrl("*")
                .version("1.0.0")
                .build();
    }

}
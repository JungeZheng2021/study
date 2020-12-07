package com.aimsphm.nuclear.common.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;


/**
 * @Package: com.aimsphm.nuclear.common.config
 * @Description: <接口配置类>
 * @Author: MILLA
 * @CreateDate: 2018/4/8 9:10
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/3/3 9:10
 * @Version: 1.0
 */
@EnableSwagger2
@Configuration
@EnableKnife4j
@ConditionalOnProperty(prefix = "spring.config", name = "enableSwagger2", havingValue = "true")
public class SwaggerConfig {

    @Value("${swagger.enable.active:true}")
    private boolean enableSwagger;
    @Value("${swagger.base.package:com.aimsphm.nuclear}")
    private String basePackage;

    @Bean
    public Docket createRestApi() {
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(new ParameterBuilder()
                .name("Authorization")
                .description("认证token")
                .modelRef(new ModelRef("string"))
                .defaultValue("eyJhbGciOiJIUzUxMiJ9.eyJhdXRob3JpdGllcyI6IlJPTEVfQURNSU4sUk9MRV9VU0VSIiwic3ViIjoiYWRtaW4iLCJsb2NhbGUiOiJDTiIsImV4cCI6MTY0Mzg3Nzc3NH0.sYAxvL1E4-DL81wFMzmvGNZftLmCA4418DDwJx5tg_SSl_DbISa7IAxVw7lNs9_AsG_3PA6uzV_54MTFtngqaQ")
                .parameterType("header")
                .required(false)
                .build());
        return new Docket(DocumentationType.SWAGGER_2).enable(enableSwagger)
                .apiInfo(apiInfo())
                .globalOperationParameters(parameters)
                .select()
                // 需要扫描的包路径
                .apis(RequestHandlerSelectors.basePackage(basePackage))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //接口文档名称
                .title("接口列表 v1.1.0")
                //接口描述
                .description("接口测试")
                // 接口地址
                .termsOfServiceUrl("http://localhost:8080/doc.html")
                //联系人
                .contact(new Contact("AIMS", "https://www.aimsphm.com/", "info@aimsphm.com"))
                .version("1.1.0")
                .build();
    }
}
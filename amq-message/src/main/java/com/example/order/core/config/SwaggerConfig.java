package com.example.order.core.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * <p>
 * 功能描述: Swagger调试配置
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2021/12/06 17:23
 */
@EnableSwagger2
@Configuration
@EnableKnife4j
public class SwaggerConfig {

    @Value("${swagger.enable.active:true}")
    private boolean enableSwagger;
    @Value("${swagger.base.package:com.example}")
    private String basePackage;

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2).enable(enableSwagger)
                .apiInfo(apiInfo())
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
                .contact(new Contact("ARVIN", "https://www.arvin.com/", "service@arvin.com"))
                .version("1.1.0")
                .build();
    }
}
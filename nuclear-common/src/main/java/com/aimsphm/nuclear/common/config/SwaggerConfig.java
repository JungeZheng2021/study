package com.aimsphm.nuclear.common.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
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
public class SwaggerConfig implements WebMvcConfigurer {

    @Value("${swagger.enable.active:true}")
    private boolean enableSwagger;
    @Value("${swagger.base.package:com.aimsphm.nuclear}")
    private String basePackage;
//    @Autowired
//    private HandlerInterceptor interceptor;//使用父类添加白名单

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
                .apis(RequestHandlerSelectors.basePackage(basePackage)) // 需要扫描的包路径
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("接口列表 v1.1.0") //接口文档名称
                .description("接口测试") //接口描述
                .termsOfServiceUrl("http://localhost:8080/doc.html") // 接口地址
                .contact(new Contact("AIMS", "https://www.aimsphm.com/", "info@aimsphm.com"))//联系人
                .version("1.1.0")
                .build();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }


    //配置拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        if (!enableSwagger) {
//            registry.addInterceptor(interceptor).addPathPatterns("/**");
//            return;
//        }
//        //启用swagger
//        ArrayList<String> list = Lists.newArrayList();
//        list.add("/swagger-resources/**");
//        list.add("/swagger-ui.html");
//        list.add("/swagger-resources");
//        list.add("/v2/api-docs");
//        list.add("/webjars/**");
//        list.add("/error");
//        registry.addInterceptor(interceptor).addPathPatterns("/**").excludePathPatterns(list);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowCredentials(true)
                .allowedHeaders("*")
                .allowedMethods("*");
    }
}

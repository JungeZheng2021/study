package com.aimsphm.nuclear.common.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;
import java.util.TimeZone;

/**
 * @Package: com.study.auth.config.core
 * @Description: <Web全局配置类>
 * @Author: MILLA
 * @CreateDate: 2020/09/04 14:42
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/09/04 14:42
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Configuration
public class WebGlobalConfig extends WebMvcConfigurationSupport {

    @Value("${spring.jackson.time-zone:GMT+8}")
    private String timeZone;
//
//    @Autowired
//    private HandlerInterceptor interceptor;

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter json = new MappingJackson2HttpMessageConverter();
        ObjectMapper mapper = new ObjectMapper();
        //配置字段对应不上自动放弃该字段
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //设置时区
        mapper.setTimeZone(TimeZone.getTimeZone(timeZone));
        //空值不输出
        mapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
        json.setObjectMapper(mapper);
        converters.add(json);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }


    /**
     * 配置拦截器
     *
     * @param registry
     */
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

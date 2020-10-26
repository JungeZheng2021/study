package com.aimsphm.nuclear.core.feign;

import feign.codec.Decoder;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.core.feign
 * @Description: <>
 * @Author: milla
 * @CreateDate: 2020/10/19 12:54
 * @UpdateUser: milla
 * @UpdateDate: 2020/10/19 12:54
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Component
@FeignClient(value = "python-tornado-xyweb", configuration = TornadoFeign.FeignTestConfiguration.class)
public interface TornadoFeign {
    @GetMapping("test")
    Object testTornado(@RequestParam(required = false, value = "n") Integer number);

    class FeignTestConfiguration {
        @Bean
        public Decoder textPlainDecoder() {
            return new SpringDecoder(() -> new HttpMessageConverters(new CustomMappingJackson2HttpMessageConverter()));
        }
    }

    class CustomMappingJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter {
        @Override
        public void setSupportedMediaTypes(List<MediaType> supportedMediaTypes) {
            super.setSupportedMediaTypes(Collections.singletonList(MediaType.TEXT_HTML));
        }
    }
}
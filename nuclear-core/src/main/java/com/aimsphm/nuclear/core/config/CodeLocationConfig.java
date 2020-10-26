package com.aimsphm.nuclear.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Package: com.aimsphm.nuclear.core.config
 * @Description: <测点位置配置类>
 * @Author: MILLA
 * @CreateDate: 2020/6/28 10:54
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/6/28 10:54
 * @UpdateRemark: <目前弃用>
 * @Version: 1.0
 */
@Configuration
@ConfigurationProperties(prefix = CodeLocationConfig.CONF_PREFIX)
public class CodeLocationConfig {
    //默认前缀
    public static final String CONF_PREFIX = "point.location";

    private Map<String, String> properties;

    public Map<String, String> getProperties() {
        return properties;
    }

    public List<String> getPropertiesByKey(String key) {
        String s = properties.get(key);
        if (Objects.nonNull(s) && s.length() > 0) {
            if (s.contains(",")) {
                String[] split = s.split(",");
                return Arrays.asList(split);
            }
            return Stream.of(s).collect(Collectors.toList());
        }
        return null;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }
}

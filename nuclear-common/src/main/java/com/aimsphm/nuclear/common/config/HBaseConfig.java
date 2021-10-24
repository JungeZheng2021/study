package com.aimsphm.nuclear.common.config;

import com.aimsphm.nuclear.common.exception.CustomMessageException;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 功能描述:hbase配置信息
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020-03-05 14:42
 */
@Configuration
@ConfigurationProperties(prefix = HBaseConfig.CONF_PREFIX)
@Slf4j
@ConditionalOnProperty(prefix = "spring.config", name = "enableHBase", havingValue = "true")
public class HBaseConfig {
    public static final String CONF_PREFIX = "hbase.config";

    private Map<String, String> properties;

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    @Bean
    public Connection connection() throws IOException {
        org.apache.hadoop.conf.Configuration conf = HBaseConfiguration.create();
        if (Objects.isNull(properties) || properties.isEmpty()) {
            throw new CustomMessageException("Hbase config can not be null");
        }
        for (Map.Entry<String, String> confEntry : properties.entrySet()) {
            conf.set(confEntry.getKey(), confEntry.getValue());
        }
        //消除sasl认证
        System.setProperty("zookeeper.sasl.client", "false");
        conf.set("hbase.client.ipc.pool.size", "10");
        conf.set("hbase.hconnection.threads.max", "30");
        ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 30, 2000, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(100));
        return ConnectionFactory.createConnection(conf, executor);
    }
}

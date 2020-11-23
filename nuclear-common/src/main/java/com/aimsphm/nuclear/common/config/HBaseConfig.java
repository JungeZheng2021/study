package com.aimsphm.nuclear.common.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Package: com.aimsphm.nuclear.hbase.config
 * @Description: <hbase配置信息>
 * @Author: MILLA
 * @CreateDate: 2020/3/5 13:16
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/3/5 13:16
 * @UpdateRemark: <>
 * @Version: 1.0
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
        Assert.isTrue(properties != null && !properties.isEmpty(), "Hbase config can not be null");

        for (Map.Entry<String, String> confEntry : properties.entrySet()) {
            conf.set(confEntry.getKey(), confEntry.getValue());
        }
        //消除sasl认证
        System.setProperty("zookeeper.sasl.client", "false");
        conf.set("hbase.client.ipc.pool.size", "10");
        conf.set("hbase.hconnection.threads.max", "30");
        ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 30, 2000, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(100));
        Connection connection = ConnectionFactory.createConnection(conf, executor);
        return connection;
    }
}

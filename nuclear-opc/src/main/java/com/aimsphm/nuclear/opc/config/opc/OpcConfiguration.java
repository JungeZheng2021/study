package com.aimsphm.nuclear.opc.config.opc;

import com.aimsphm.nuclear.opc.client.Browser;
import com.aimsphm.nuclear.opc.client.OpcClient;
import com.aimsphm.nuclear.opc.config.rabbit.MqttGateway;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.openscada.opc.lib.da.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;

import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * @ClassName OpcConfiguration
 * @Description opc的连接随着程序启动而运行
 * @Author Mr.zhang
 * @Date 2019/11/4 9:35
 */
@Slf4j
//@Component
public class OpcConfiguration implements CommandLineRunner {

    @Value("${spring.progId}")
    private String progId;

    @Value("${spring.user}")
    private String user;

    @Value("${spring.host}")
    private String host;

    @Value("${spring.password}")
    private String password;

    @Value("${spring.domain}")
    private String domain;

    @Autowired
    private MqttGateway mqttGateway;

    @Override
    public void run(String... args) {
        OpcClient client = new OpcClient();
        client.connectServer(host, progId, user, password, domain);
        Server server = client.getServer();
        try {
            //每1秒异步读取一次opcServer的数据
            Browser.readAsync(server, new ScheduledThreadPoolExecutor(2), 1000L
                    , dataItems -> mqttGateway.sendToMqtt(JSON.toJSONString(dataItems), "pi/many"));
        } catch (Throwable throwable) {
            log.error(throwable.getMessage());
        }
    }

}

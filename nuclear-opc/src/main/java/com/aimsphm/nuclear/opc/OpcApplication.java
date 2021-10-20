package com.aimsphm.nuclear.opc;

import com.aimsphm.nuclear.opc.client.MqPushClient;
import com.aimsphm.nuclear.opc.job.MqClientPushJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@SpringBootApplication(scanBasePackages = {"com.aimsphm.nuclear"})
@Slf4j
public class OpcApplication {

    static ScheduledThreadPoolExecutor scheduledExecutor = new ScheduledThreadPoolExecutor(50);
    @Autowired
    private MqClientPushJob pushJob;

    public static void main(String[] args) {
        SpringApplication.run(OpcApplication.class, args);
    }

    @Autowired
    private MqPushClient client;

    void execute() {
        String topic = "pi.many";
        String rootPath = "/data/tianwan/pushData/";
        //每次执行完当前任务之后等待100秒之后再次执行
        scheduledExecutor.scheduleWithFixedDelay(() -> pushJob.execute(rootPath + "mtsdata.csv", topic, 1000L), 1000, 1000, TimeUnit.MILLISECONDS);
        scheduledExecutor.scheduleWithFixedDelay(() -> pushJob.execute(rootPath + "otherdata1.csv", topic, 1000L), 1000, 1000, TimeUnit.MILLISECONDS);
        scheduledExecutor.scheduleWithFixedDelay(() -> pushJob.execute(rootPath + "pumpdata.csv", topic, 1000L), 1000, 1000, TimeUnit.MILLISECONDS);
        scheduledExecutor.scheduleWithFixedDelay(() -> pushJob.execute(rootPath + "turbine_2.csv", topic, 1000L), 1000, 1000, TimeUnit.MILLISECONDS);

        scheduledExecutor.scheduleWithFixedDelay(() -> pushJob.execute(rootPath + "simulation1.csv", topic, 1000L), 1000, 1000, TimeUnit.MILLISECONDS);
        scheduledExecutor.scheduleWithFixedDelay(() -> pushJob.execute(rootPath + "simulation2.csv", topic, 1000L), 1000, 1000, TimeUnit.MILLISECONDS);

        //油液
        scheduledExecutor.scheduleWithFixedDelay(() -> pushJob.executeOli(rootPath + "tw_oil_data.csv", "JSNPC.Upload", 60 * 1000L), 1000, 1000, TimeUnit.MILLISECONDS);
        //声学
        scheduledExecutor.scheduleWithFixedDelay(() -> pushJob.executeOli(rootPath + "pac_data.csv", "JSNPC.Upload", 10 * 60 * 1000L), 1000, 1000, TimeUnit.MILLISECONDS);

        scheduledExecutor.scheduleWithFixedDelay(() -> pushJob.execute(rootPath + "1s_data.csv", "pi.data", 1000L), 1000, 1000, TimeUnit.MILLISECONDS);
        scheduledExecutor.scheduleWithFixedDelay(() -> pushJob.executeOli(rootPath + "10s_data.csv", "JSNPC.Upload", 10 * 1000L), 1000, 1000, TimeUnit.MILLISECONDS);
        scheduledExecutor.scheduleWithFixedDelay(() -> pushJob.executeOli(rootPath + "10min_data.csv", "JSNPC.Upload", 10 * 60 * 1000L), 1000, 1000, TimeUnit.MILLISECONDS);
    }
}

package com.aimsphm.nuclear.opc;

import com.aimsphm.nuclear.opc.client.MqPushClient;
import com.aimsphm.nuclear.opc.job.MqClientPushJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
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

    @PostConstruct
    void execute() {
        String topic = "pi.many";
        String rootPath = "/data/tianwan/pushData/";
        //每次执行完当前任务之后等待100秒之后再次执行
        scheduledExecutor.scheduleWithFixedDelay(() -> pushJob.execute(rootPath + "mtsdata.csv", topic, 1000L), 1000, 1000, TimeUnit.MILLISECONDS);
        scheduledExecutor.scheduleWithFixedDelay(() -> pushJob.execute(rootPath + "otherdata1.csv", topic, 1000L), 1000, 1000, TimeUnit.MILLISECONDS);
        scheduledExecutor.scheduleWithFixedDelay(() -> pushJob.execute(rootPath + "pumpdata.csv", topic, 1000L), 1000, 1000, TimeUnit.MILLISECONDS);
        scheduledExecutor.scheduleWithFixedDelay(() -> pushJob.execute(rootPath + "turbine_2.csv", topic, 1000L), 1000, 1000, TimeUnit.MILLISECONDS);

//        scheduledExecutor.scheduleWithFixedDelay(() -> pushJob.execute(rootPath + "simulation1.csv", topic, 1000L), 1000, 1000, TimeUnit.MILLISECONDS);
//        scheduledExecutor.scheduleWithFixedDelay(() -> pushJob.execute(rootPath + "simulation2.csv", topic, 1000L), 1000, 1000, TimeUnit.MILLISECONDS);

        //        scheduledExecutor.scheduleWithFixedDelay(() -> pushJob.executeFromDatabase("pi.data", tagList), 1000, 1000, TimeUnit.MILLISECONDS);
//        scheduledExecutor.scheduleWithFixedDelay(() -> pushJob.executeFromDatabase1("JSNPC.Download", tagList), 1000, 5 * 60 * 60 * 1000, TimeUnit.MILLISECONDS);
        //变压器
        scheduledExecutor.scheduleWithFixedDelay(() -> pushJob.execute(rootPath + "ZAS_2020_11_06_2021_01_06_1s_part2.csv", topic, 1000L), 1000, 1000, TimeUnit.MILLISECONDS);
        scheduledExecutor.scheduleWithFixedDelay(() -> pushJob.execute(rootPath + "ZAS_sensordata_2020_11_6_2021_1_6_1s.csv", topic, 1000L), 1000, 1000, TimeUnit.MILLISECONDS);
        scheduledExecutor.scheduleWithFixedDelay(() -> pushJob.execute(rootPath + "20ZAS-ET01-X01-MWHO.csv", topic, 1000L), 1000, 1000, TimeUnit.MILLISECONDS);
//        //油液
        scheduledExecutor.scheduleWithFixedDelay(() -> pushJob.executeOli(rootPath + "tw_oil_data.csv", "JSNPC.Upload", 60 * 1000L), 1000, 1000, TimeUnit.MILLISECONDS);
        //声学
        scheduledExecutor.scheduleWithFixedDelay(() -> pushJob.executeOli(rootPath + "pac_data.csv", "JSNPC.Upload", 10 * 60 * 1000L), 1000, 1000, TimeUnit.MILLISECONDS);

        scheduledExecutor.scheduleWithFixedDelay(() -> pushJob.execute(rootPath + "1s_data.csv", "pi.data", 1000L), 1000, 1000, TimeUnit.MILLISECONDS);
        scheduledExecutor.scheduleWithFixedDelay(() -> pushJob.executeOli(rootPath + "10s_data.csv", "JSNPC.Upload", 10 * 1000L), 1000, 1000, TimeUnit.MILLISECONDS);
        scheduledExecutor.scheduleWithFixedDelay(() -> pushJob.executeOli(rootPath + "10min_data.csv", "JSNPC.Upload", 10 * 60 * 1000L), 1000, 1000, TimeUnit.MILLISECONDS);
    }

//    @PostConstruct
//    void test() throws InterruptedException {
//        while (true) {
//            int nasLevel = ThreadLocalRandom.current().ints(7, 12).findFirst().getAsInt();
//            int month = ThreadLocalRandom.current().ints(10, 30).findFirst().getAsInt();
//            int var46 = ThreadLocalRandom.current().ints(35, 50).findFirst().getAsInt();
//            int var49 = ThreadLocalRandom.current().ints(20, 30).findFirst().getAsInt();
//            int hours = ThreadLocalRandom.current().ints(0, 24).findFirst().getAsInt();
//            int mins = ThreadLocalRandom.current().ints(0, 59).findFirst().getAsInt();
//            int seconds = ThreadLocalRandom.current().ints(0, 59).findFirst().getAsInt();
//            String s = "{\"d\":[{\"tag\":\"var41\",\"value\":" + var49 + ".28},{\"tag\":\"var38\",\"value\":" + var49 + ".32},{\"tag\":\"var42\",\"value\":0." + var49 + "634},{\"tag\":\"var43\",\"value\":" + var46 + ".74}" +
//                    ",{\"tag\":\"var45\",\"value\":0." + nasLevel + "},{\"tag\":\"var46\",\"value\":" + var46 + "},{\"tag\":\"var53\",\"value\":" + hours + "},{\"tag\":\"var47\",\"value\":" + hours + "},{\"tag\":\"var48\",\"value\":" + hours + "}" +
//                    ",{\"tag\":\"var49\",\"value\":" + var49 + "},{\"tag\":\"var51\",\"value\":" + nasLevel + "},{\"tag\":\"var70\",\"value\":" + hours + "},{\"tag\":\"var71\",\"value\":0},{\"tag\":\"var72\",\"value\":0},{\"tag\":\"var73\",\"value\":2},{\"tag\":\"var74\",\"value\":0},{\"tag\":\"var75\",\"value\":4}]," +
//                    "\"ts\":\"2021-10-" + month + "T0" + hours + ":" + mins + ":" + seconds + "+0000\"}";
//            //            String format = DateUtils.format(s, new Date());
//            System.out.println(s);
//            client.send2Mq(s, ".ST.3N031909071006.CurDataA");
//            client.send2Mq(s, "pi.many");
//        }
//    }
}

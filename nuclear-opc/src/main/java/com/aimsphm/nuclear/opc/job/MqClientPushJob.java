package com.aimsphm.nuclear.opc.job;

import com.aimsphm.nuclear.opc.client.MqPushClient;
import com.aimsphm.nuclear.opc.model.DataItem;
import com.aimsphm.nuclear.common.entity.CommonMeasurePointDO;
import com.aimsphm.nuclear.common.service.CommonMeasurePointService;
import com.aimsphm.nuclear.opc.model.PacketDTO;
import com.aimsphm.nuclear.opc.model.SensorDataDTO;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Package: com.aimphm.nuclear.opc.job
 * @Description: <>
 * @Author: milla
 * @CreateDate: 2020/10/22 09:43
 * @UpdateUser: milla
 * @UpdateDate: 2020/10/22 09:43
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Component
@Slf4j
public class MqClientPushJob {
    @Autowired
    private MqPushClient client;

    @Autowired
    private CommonMeasurePointService pointServiceExt;

    /**
     * 定时器 每10秒执行一次
     */
    public void execute(String path, String topic, Long sleepTime) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                log.error("文件目录不存在:{}", path);
                return;
            }
            readDataFromFile(file, topic, sleepTime);
        } catch (Exception e) {
            log.error("执行异常-文件数据:{}", e);
        }
    }

    public void executeOli(String path, String topic, Long sleepTime) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                log.error("文件目录不存在:{}", path);
                return;
            }
            readDataFromFileOil(file, topic, sleepTime);
        } catch (Exception e) {
            log.error("执行异常-文件数据:{}", e);
        }
    }


    private void readDataFromFileOil(File file, String topic, Long sleepTime) throws IOException, InterruptedException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            String header = reader.readLine();
            String[] split = header.split(SEPARATOR);
            List<String> headers = Arrays.asList(split);
            Map<String, Map<String, Double>> pointValues = Maps.newHashMap();
            while ((line = reader.readLine()) != null) {
                pointValues.clear();
                long time = System.currentTimeMillis();
                String[] values = line.split(SEPARATOR);
                for (int i = 0; i < values.length; i++) {
                    double value = Double.parseDouble(values[i]);
                    String pointId = headers.get(i);
                    int middleIndex = pointId.indexOf("-N-");
                    String feature = pointId.substring(middleIndex + 3);
                    String sensorCode = pointId.substring(0, middleIndex + 2);
                    pointValues.putIfAbsent(sensorCode, Maps.newHashMap());
                    pointValues.get(sensorCode).putIfAbsent(feature, value);
                }
                if (MapUtils.isEmpty(pointValues)) {
                    continue;
                }
                pointValues.entrySet().stream().forEach(x -> {
                    String sensorCode = x.getKey();
                    Map<String, Double> value = x.getValue();
                    SensorDataDTO data = new SensorDataDTO();
                    PacketDTO dto = new PacketDTO();
                    data.setPacket(dto);
                    data.setType(13);
                    dto.setTagStatus("0");
                    dto.setSensorCode(sensorCode);
                    dto.setFeaturesResult(value);
                    dto.setTimestamp(time);
                    client.send2Mq(JSON.toJSONString(data), topic);
                });
                Thread.sleep(sleepTime);
                log.info("文件目录为：{}数据发送成功,测点个数：{}", file.getName(), headers.size());
            }
        }
    }
//
//    public void execute1(String path, String topic, long start) {
//        try {
//            File file = new File(path);
//            if (!file.exists()) {
//                log.error("文件目录不存在:{}", path);
//                return;
//            }
//            readDataFromFile1(path, topic, start);
//        } catch (Exception e) {
//            log.error("执行异常-文件数据:{}", e);
//        }
//    }
//
//    private void readDataFromFile1(String path, String topic, long start) throws IOException, InterruptedException {
//        BufferedReader reader = new BufferedReader(new FileReader(new File(path)));
//        String line;
//        String header = reader.readLine();
//        String[] split = header.split(SEPARATOR);
//        List<String> headers = Arrays.asList(split);
//        ArrayList<DataItem> dataItems = new ArrayList<>();
//        while ((line = reader.readLine()) != null) {
//            dataItems.clear();
//            String[] values = line.split(SEPARATOR);
//            start += 1000;
//            for (int i = 0; i < values.length; i++) {
//                double value = Double.parseDouble(values[i]);
//                DataItem item = new DataItem();
//                item.setItemId(headers.get(i));
//                item.setValue(value);
//                item.setTimestamp(start);
//                dataItems.add(item);
//            }
//            Thread.sleep(30L);
//            client.send2Mq(dataItems, topic);
//            log.info("文件目录为：{}数据发送成功,测点个数：{}", path, headers.size());
//        }
//    }

    public List<String> pointList() {
        LambdaQueryWrapper<CommonMeasurePointDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CommonMeasurePointDO::getPointType, 1);
        List<CommonMeasurePointDO> list = pointServiceExt.list(wrapper);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return list.stream().map(item -> item.getPointId()).collect(Collectors.toList());
    }

    public void executeFromDatabase(String topic, List<String> list) {
        try {
            if (CollectionUtils.isEmpty(list)) {
                return;
            }
            Random random = new Random();
            ArrayList<DataItem> dataItems = new ArrayList<>();
            list.stream().forEach(item -> {
                DataItem dataItem = new DataItem();
                dataItem.setTimestamp(System.currentTimeMillis());
                if ("6M2DVC004MI".equals(item)) {
                    dataItem.setValue(42.4 + 0.1 * random.nextDouble());
                } else {
                    dataItem.setValue(Math.abs(random.nextInt(500) * random.nextDouble()));
                }
                dataItem.setItemId(item);
                dataItems.add(dataItem);
            });
            client.send2Mq(dataItems, topic);
        } catch (Exception e) {
            log.error("执行异常-数据库数据:{}", e);
        }
    }


    final static String SEPARATOR = ",";

    private void readDataFromFile(File file, String topic, Long sleepTime) throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            String header = reader.readLine();
            String[] split = header.split(SEPARATOR);
            List<String> headers = Arrays.asList(split);
            ArrayList<DataItem> dataItems = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                dataItems.clear();
                String[] values = line.split(SEPARATOR);
                for (int i = 0; i < values.length; i++) {
                    String point = headers.get(i);
                    double value = Double.parseDouble(values[i]);
                    if (StringUtils.equalsAny(point, "20ZAS-ET-1A-71H", "20ZAS-ET-1A-71L", "20ZAS-EP-ET101B-DCFA")) {
                        value = 1.0D;
                    }
                    DataItem item = new DataItem();
                    item.setItemId(point);
                    item.setValue(value);
                    item.setTimestamp(System.currentTimeMillis());
                    dataItems.add(item);
                }
                if (file.getName().contains("ZAS_sensordata")) {
                    DataItem item = new DataItem();
                    item.setItemId("20ZAS-ET01-I02-DCN");
                    item.setValue(1.0);
                    item.setTimestamp(System.currentTimeMillis());
                    dataItems.add(item);
//                log.info("新增测点{}", item);
                }
                client.send2Mq(dataItems, topic);
                Thread.sleep(sleepTime);
                log.info("文件目录为：{}数据发送成功,测点个数：{}", file.getName(), headers.size());
            }
        }
    }

    public void executeFromDatabase1(String topic, List<String> tagList) {
        String message = "{\n" +
                "  \"type\": 21,\n" +
                "  \"packet\": {\n" +
                "    \"edgeCode\": \"6M2RCV002CR1-N\",\n" +
                "    \"timeStamp\": 1,\n" +
                "    \"configCommand\": {\n" +
                "      \"resetAbrasion\": 1,\n" +
                "      \"viscosityCalculMethod\": 0\n" +
                "    }\n" +
                "  }\n" +
                "}";
        System.out.println("发送：" + message);
        client.send2Mq(message, topic);
    }
}

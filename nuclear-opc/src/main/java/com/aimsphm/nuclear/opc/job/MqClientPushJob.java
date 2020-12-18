package com.aimsphm.nuclear.opc.job;

import com.aimsphm.nuclear.opc.client.MqPushClient;
import com.aimsphm.nuclear.opc.model.DataItem;
import com.aimsphm.nuclear.common.entity.CommonMeasurePointDO;
import com.aimsphm.nuclear.common.service.CommonMeasurePointService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
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
    public void execute(String path, String topic) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                log.error("文件目录不存在:{}", path);
                return;
            }
            readDataFromFile(path, topic);
        } catch (Exception e) {
            log.error("执行异常-文件数据:{}", e);
        }
    }

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
                dataItem.setValue(Math.abs(random.nextInt(500) * random.nextDouble()));
                dataItem.setItemId(item);
                dataItems.add(dataItem);
            });
            client.send2Mq(dataItems, topic);
        } catch (Exception e) {
            log.error("执行异常-数据库数据:{}", e);
        }
    }


    final static String SEPARATOR = ",";

    private void readDataFromFile(String path, String topic) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(new File(path)));
        String line;
        String header = reader.readLine();
        String[] split = header.split(SEPARATOR);
        List<String> headers = Arrays.asList(split);
        ArrayList<DataItem> dataItems = new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            dataItems.clear();
            String[] values = line.split(SEPARATOR);
            for (int i = 0; i < values.length; i++) {
                double value = Double.parseDouble(values[i]);
                DataItem item = new DataItem();
                item.setItemId(headers.get(i));
                item.setValue(value);
                item.setTimestamp(System.currentTimeMillis());
                dataItems.add(item);
            }
            client.send2Mq(dataItems, topic);
            Thread.sleep(1000L);
            log.info("文件目录为：{}数据发送成功,测点个数：{}", path, headers.size());
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

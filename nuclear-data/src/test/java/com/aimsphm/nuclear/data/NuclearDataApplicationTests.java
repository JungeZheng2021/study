package com.aimsphm.nuclear.data;

import com.aimsphm.nuclear.common.entity.vo.MeasurePointVO;
import com.aimsphm.nuclear.data.mapper.MeasurePointVOMapper;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@RunWith(SpringRunner.class)
public class NuclearDataApplicationTests {
    @Autowired
    private MeasurePointVOMapper mapper;

    @Test
    public void contextLoads() {
        //1：PI测点
        List<MeasurePointVO> measurePointVOS = mapper.selectMeasurePointsByTagId(1, "20CCS-MP-01B-TMP4");
        System.out.println(measurePointVOS);

    }


}

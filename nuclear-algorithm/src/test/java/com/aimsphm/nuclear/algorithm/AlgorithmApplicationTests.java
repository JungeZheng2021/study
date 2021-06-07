package com.aimsphm.nuclear.algorithm;

import com.aimsphm.nuclear.algorithm.entity.bo.PointEstimateDataBO;
import com.aimsphm.nuclear.algorithm.service.AlgorithmService;
import com.aimsphm.nuclear.common.service.CommonDeviceService;
import com.aimsphm.nuclear.common.util.HBaseUtil;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static com.aimsphm.nuclear.common.constant.HBaseConstant.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AlgorithmApplicationTests {
    @Resource
    private AlgorithmService algorithmService;

    @Resource
    private CommonDeviceService deviceService;
    @Resource
    private HBaseUtil hBase;

    @Test
    public void contextLoads() throws InterruptedException, IOException {
        List<PointEstimateDataBO> dataList = Lists.newArrayList();
        int count = 0;
        Long s = System.currentTimeMillis();
        Long deviceId = 3L;
        int times = 0;
        while (count++ < 10000) {
            times = times + 10;
            s = s + (times++) * 1000;
            PointEstimateDataBO bo = new PointEstimateDataBO();
            bo.setPointId("5M2DVC403MV-N-acc-ZeroPeak");
            Random random = new Random(500);
            bo.setActual(random.nextInt(100) * random.nextDouble());
            bo.setEstimate(random.nextInt(500) * random.nextDouble());
            bo.setResidual(random.nextInt(10) * random.nextDouble());
            bo.setTimestamp(s);
            dataList.add(bo);
            System.out.println(s);
            hBase.insertObject(H_BASE_TABLE_NPC_PHM_DATA, deviceId + ROW_KEY_SEPARATOR + bo.getTimestamp(), H_BASE_FAMILY_NPC_ESTIMATE, bo.getPointId(), bo, bo.getTimestamp());
            System.out.println("添加成功： " + bo);
        }
    }

}

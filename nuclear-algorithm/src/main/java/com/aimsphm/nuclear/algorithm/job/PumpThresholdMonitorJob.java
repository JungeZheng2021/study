package com.aimsphm.nuclear.algorithm.job;

import com.aimsphm.nuclear.algorithm.enums.AlgorithmTypeEnum;
import com.aimsphm.nuclear.algorithm.service.AlgorithmService;
import com.aimsphm.nuclear.common.annotation.DistributedLock;
import com.aimsphm.nuclear.common.entity.CommonDeviceDO;
import com.aimsphm.nuclear.common.enums.DeviceTypeEnum;
import com.aimsphm.nuclear.common.service.CommonDeviceService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.algorithm.job
 * @Description: <上充泵状态监测>
 * @Author: MILLA
 * @CreateDate: 2020/6/28 10:54
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/6/28 10:54
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Component
@Slf4j
@ConditionalOnProperty(prefix = "scheduled.config", name = "enable", havingValue = "true")
public class PumpThresholdMonitorJob implements BaseMonitorJob {

    @Resource
    private AlgorithmService algorithmService;

    @Resource
    private CommonDeviceService deviceService;

    /**
     * 设备状态监测算法
     * 测试：每7分钟执行一次
     * 线上：每小时的13分的时候执行一次
     */
//    @Scheduled(cron = "0 0/7 * * * ? ")
//    @Scheduled(cron = "0 13 * * * ? ")
    @Async
    @Scheduled(cron = "${scheduled.config.PumpThresholdMonitorJob:0 13 * * * ?}")
    @DistributedLock("FanMonitorJobLock")
    public void monitor() {
        execute(DeviceTypeEnum.PUMP.getType(), algorithmService, deviceService, AlgorithmTypeEnum.THRESHOLD_MONITOR);
    }
}

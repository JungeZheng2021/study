package com.aimsphm.nuclear.algorithm.job;

import com.aimsphm.nuclear.algorithm.enums.AlgorithmTypeEnum;
import com.aimsphm.nuclear.algorithm.service.AlgorithmService;
import com.aimsphm.nuclear.common.annotation.DistributedLock;
import com.aimsphm.nuclear.common.enums.DeviceTypeEnum;
import com.aimsphm.nuclear.common.service.CommonDeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Package: com.aimsphm.nuclear.algorithm.job
 * @Description: <风机状态监测>
 * @Author: MILLA
 * @CreateDate: 2020/6/28 10:54
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/6/28 10:54
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Component
@Slf4j
public class FanMonitorJob implements BaseMonitorJob {

    @Resource
    private AlgorithmService algorithmService;

    @Resource
    private CommonDeviceService deviceService;

    /**
     * 设备状态监测算法
     * 测试： 每11分的时候执行一次
     * 线上： 每小时的37分的时候执行一次
     */
//    @Scheduled(cron = "0 0/11 * * * ?")
    @Scheduled(cron = "${scheduled.config.FanMonitorJob:0 37 * * * ?}")
    @DistributedLock("FanMonitorJobLock")
    public void monitor() {
        execute(DeviceTypeEnum.FAN.getType(), algorithmService, deviceService, AlgorithmTypeEnum.STATE_MONITOR);
    }

}

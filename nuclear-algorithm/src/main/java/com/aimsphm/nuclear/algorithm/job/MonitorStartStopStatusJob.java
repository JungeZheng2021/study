package com.aimsphm.nuclear.algorithm.job;

import com.aimsphm.nuclear.algorithm.service.AlgorithmService;
import com.aimsphm.nuclear.common.annotation.DistributedLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Package: com.aimsphm.nuclear.history
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2020/6/28 10:54
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/6/28 10:54
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Component
@Slf4j
public class MonitorStartStopStatusJob {

    @Resource
    private AlgorithmService algorithmService;

    /**
     * 设备状态监测算法
     * 一分钟(每分钟的57秒)执行一次执行一次
     */
    @Scheduled(cron = "57 0/3 * * * ? ")
    @DistributedLock("monitorStartStopStatus")
    public void monitorStartStopStatus() {
        try {
            log.info("device  start and stop running..................");
            algorithmService.getDeviceStartAndStopMonitorInfo();
        } catch (Exception e) {
            log.error("invoke algorithm server failed: {}", e);
        } finally {
            log.info("device  start and stop finished..................");
        }
    }

}

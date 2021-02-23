package com.aimsphm.nuclear.algorithm.job;

import com.aimsphm.nuclear.algorithm.service.AlgorithmService;
import com.aimsphm.nuclear.common.annotation.DistributedLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 状态监测算法定时任务
 *
 * @author milla
 */
@Component
@Slf4j
public class MonitorJob {

    @Resource
    private AlgorithmService algorithmService;

    /**
     * 设备状态监测算法
     * 每小时的13分的时候执行一次
     */
    @Scheduled(cron = "0 13 * * * ?")
    @DistributedLock("MonitorJobDistributeLock")
    public void monitor() {
        try {
            log.info("device status running..................");
            algorithmService.getDeviceStateMonitorInfo();
        } catch (Exception e) {
            log.error("invoke algorithm server failed: {}", e);
        } finally {
            log.info("device status finished..................");
        }
    }

}

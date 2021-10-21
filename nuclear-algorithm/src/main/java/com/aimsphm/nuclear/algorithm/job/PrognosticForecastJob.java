package com.aimsphm.nuclear.algorithm.job;

import com.aimsphm.nuclear.algorithm.service.PrognosticForecastService;
import com.aimsphm.nuclear.common.annotation.DistributedLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * <p>
 * 功能描述:征兆预测任务
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020/6/28 10:54
 */
@Slf4j
@Component
public class PrognosticForecastJob {

    @Resource
    private PrognosticForecastService prognosticForecastService;

    /**
     * 设备状态监测算法
     * 测试： 每11分的时候执行一次
     * 线上： 每小时的37分的时候执行一次
     */
    @Scheduled(cron = "${scheduled.config.PrognosticForecastJob:29 0 * * * ?}")
    @DistributedLock("PrognosticForecastJob")
    public void monitor() {
        prognosticForecastService.prognosticForecast();
    }
}
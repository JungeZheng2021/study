package com.aimsphm.nuclear.algorithm.job;

import com.aimsphm.nuclear.algorithm.service.DownSampleService;
import com.aimsphm.nuclear.common.annotation.DistributedLock;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;

/**
 * <p>
 * 功能描述:性能预测降采样Job
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2021/07/27 13:55
 */
public class DownSampleJob {
    @Resource
    private DownSampleService sampleService;

    @Scheduled(cron = "${scheduled.config.FanStateJob:30 0/10 * * * ? }")
    @DistributedLock("DownSampleJobLock")
    public void execute() {
        sampleService.execute();
    }
}

package com.aimsphm.nuclear.algorithm.job;

import com.aimsphm.nuclear.algorithm.service.DownSampleService;
import com.aimsphm.nuclear.common.annotation.DistributedLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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
@Slf4j
@Component
public class DownSampleJob {
    @Resource
    private DownSampleService sampleService;

    @Scheduled(cron = "${scheduled.config.DownSampleJobLock:30 0/10 * * * ? }")
    @DistributedLock("DownSampleJobLock")
    public void execute() {
        sampleService.execute();
    }
}

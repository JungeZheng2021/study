package com.aimsphm.nuclear.algorithm.job;

import com.aimsphm.nuclear.algorithm.service.FeatureExtractionOperationService;
import com.aimsphm.nuclear.common.annotation.DistributedLock;
import com.aimsphm.nuclear.common.enums.PointTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * <p>
 * 功能描述:特征提取任务
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020/6/28 10:54
 */
@Component
@Slf4j
public class FeatureExtractionJob {

    @Resource
    private FeatureExtractionOperationService featureExtractionService;

    /**
     * 设备状态监测算法
     * 测试： 每11分的时候执行一次
     * 线上： 每小时的37分的时候执行一次
     */
    @Scheduled(cron = "${scheduled.config.FeatureExtractionJob:29 0 * * * ?}")
    @DistributedLock("FeatureExtractionJob")
    public void monitor() {
        featureExtractionService.operationFeatureExtractionData(PointTypeEnum.CALCULATE);
    }
}

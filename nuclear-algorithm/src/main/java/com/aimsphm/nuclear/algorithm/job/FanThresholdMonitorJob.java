package com.aimsphm.nuclear.algorithm.job;

import com.aimsphm.nuclear.algorithm.enums.AlgorithmTypeEnum;
import com.aimsphm.nuclear.algorithm.service.AlgorithmService;
import com.aimsphm.nuclear.common.annotation.DistributedLock;
import com.aimsphm.nuclear.common.enums.DeviceTypeEnum;
import com.aimsphm.nuclear.common.service.CommonDeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.aimsphm.nuclear.common.constant.RedisKeyConstant.REDIS_KEY_FAN;

/**
 * <p>
 * 功能描述:风机状态监测
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020/6/28 10:54
 */
@Component
@Slf4j
public class FanThresholdMonitorJob implements BaseMonitorJob {

    @Resource
    private CommonDeviceService deviceService;
    @Resource
    private AlgorithmService algorithmService;
    @Resource
    @Qualifier("redisTemplate")
    private RedisTemplate<String, Object> template;

    /**
     * 设备状态监测算法
     * 测试： 每11分的时候执行一次
     * 线上： 每小时的37分的时候执行一次
     */
    @Async
    @Scheduled(cron = "${scheduled.config.FanThresholdMonitorJob:29 0 * * * ?}")
    @DistributedLock("FanThresholdMonitorJob")
    public void monitor() {
        Boolean running = template.hasKey(REDIS_KEY_FAN);
        if (running) {
            return;
        }
        execute(DeviceTypeEnum.FAN.getType(), algorithmService, deviceService, AlgorithmTypeEnum.THRESHOLD_MONITOR);
    }
}

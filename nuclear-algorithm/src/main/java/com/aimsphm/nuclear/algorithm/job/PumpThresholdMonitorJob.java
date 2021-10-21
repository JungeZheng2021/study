package com.aimsphm.nuclear.algorithm.job;

import com.aimsphm.nuclear.algorithm.enums.AlgorithmTypeEnum;
import com.aimsphm.nuclear.algorithm.service.AlgorithmService;
import com.aimsphm.nuclear.common.annotation.DistributedLock;
import com.aimsphm.nuclear.common.enums.DeviceTypeEnum;
import com.aimsphm.nuclear.common.service.CommonDeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.aimsphm.nuclear.common.constant.RedisKeyConstant.REDIS_KEY_PUMP;

/**
 * <p>
 * 功能描述:上充泵启动状态
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020/6/28 10:54
 */
@Component
@Slf4j
@ConditionalOnProperty(prefix = "scheduled.config", name = "enable", havingValue = "true")
public class PumpThresholdMonitorJob implements BaseMonitorJob {

    @Resource
    private AlgorithmService algorithmService;

    @Resource
    private CommonDeviceService deviceService;
    @Resource
    @Qualifier("redisTemplate")
    private RedisTemplate<String, Object> redis;

    /**
     * 设备状态监测算法
     * 测试：每7分钟执行一次
     * 线上：每小时的13分的时候执行一次
     */
    @Async
    @Scheduled(cron = "${scheduled.config.PumpThresholdMonitorJob:0 13 * * * ?}")
    @DistributedLock("FanMonitorJobLock")
    public void monitor() {
        Boolean running = redis.hasKey(REDIS_KEY_PUMP);
        if (running) {
            return;
        }
        execute(DeviceTypeEnum.PUMP.getType(), algorithmService, deviceService, AlgorithmTypeEnum.THRESHOLD_MONITOR);
    }
}

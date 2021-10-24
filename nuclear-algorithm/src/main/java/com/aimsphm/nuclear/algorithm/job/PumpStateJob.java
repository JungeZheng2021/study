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
public class PumpStateJob implements BaseMonitorJob {
    @Resource
    @Qualifier("redisTemplate")
    private RedisTemplate<String, Object> redis;

    @Resource
    private AlgorithmService algorithmService;

    @Resource
    private CommonDeviceService deviceService;

    /**
     * 设备状态监测算法
     * 一分钟(每分钟的37秒)执行一次执行一次
     */
    @Async
    @Scheduled(cron = "${scheduled.config.PumpStateJob:37 0/1 * * * ?}")
    @DistributedLock("PumpStartStopStatusJobLock")
    public void monitorStartStopStatus() {
        redis.opsForValue().set(REDIS_KEY_PUMP, 1);
        try {
            execute(DeviceTypeEnum.PUMP.getType(), algorithmService, deviceService, AlgorithmTypeEnum.STATE_MONITOR);
            log.info("执行----慢");
        } catch (Exception e) {
            log.error("{}", e);
        }
        redis.delete(REDIS_KEY_PUMP);
    }
}

package com.aimsphm.nuclear.algorithm.job;

import com.aimsphm.nuclear.algorithm.enums.AlgorithmTypeEnum;
import com.aimsphm.nuclear.algorithm.service.AlgorithmService;
import com.aimsphm.nuclear.common.annotation.DistributedLock;
import com.aimsphm.nuclear.common.enums.DeviceTypeEnum;
import com.aimsphm.nuclear.common.service.CommonDeviceService;
import com.aimsphm.nuclear.common.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.aimsphm.nuclear.common.constant.RedisKeyConstant.REDIS_KEY_FAN;
import static com.aimsphm.nuclear.common.util.DateUtils.YEAR_MONTH_DAY_HH_MM_SS_SSS_M;

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
public class FanStateJob implements BaseMonitorJob {

    @Resource
    private AlgorithmService algorithmService;
    @Resource
    @Qualifier("redisTemplate")
    private RedisTemplate<String, Object> redis;
    @Resource
    private CommonDeviceService deviceService;

    /**
     * 设备状态监测算法
     * 一分钟(每分钟的10秒)执行一次执行一次
     */
    @Async
//    @Scheduled(cron = "30 0/10 * * * ? ")
    @Scheduled(cron = "${scheduled.config.FanStateJob:30 0/10 * * * ? }")
    @DistributedLock("FanStartStopStatusJobLock")
    public void monitorStartStopStatus() {
        redis.opsForValue().set(REDIS_KEY_FAN, 1);
        try {
            execute(DeviceTypeEnum.FAN.getType(), algorithmService, deviceService, AlgorithmTypeEnum.STATE_MONITOR);
            log.info("执行----慢： {}", DateUtils.formatCurrentDateTime(YEAR_MONTH_DAY_HH_MM_SS_SSS_M));
        } catch (Exception e) {
            e.printStackTrace();
        }
        redis.delete(REDIS_KEY_FAN);
    }
}

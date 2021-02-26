package com.aimsphm.nuclear.algorithm.job;

import com.aimsphm.nuclear.algorithm.enums.AlgorithmTypeEnum;
import com.aimsphm.nuclear.algorithm.service.AlgorithmService;
import com.aimsphm.nuclear.common.annotation.DistributedLock;
import com.aimsphm.nuclear.common.entity.CommonDeviceDO;
import com.aimsphm.nuclear.common.enums.DeviceTypeEnum;
import com.aimsphm.nuclear.common.service.CommonDeviceService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.algorithm.job
 * @Description: <风机启停监测>
 * @Author: MILLA
 * @CreateDate: 2020/6/28 10:54
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/6/28 10:54
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Component
@Slf4j
public class FanStartStopStatusJob implements BaseMonitorJob {

    @Resource
    private AlgorithmService algorithmService;

    @Resource
    private CommonDeviceService deviceService;

    /**
     * 设备状态监测算法
     * 一分钟(每分钟的17秒)执行一次执行一次
     */
    @Scheduled(cron = "13 0/1 * * * ? ")
    @DistributedLock("FanStartStopStatusJobLock")
    public void monitorStartStopStatus() {
        execute(DeviceTypeEnum.FAN.getType(), algorithmService, deviceService, AlgorithmTypeEnum.STATE_START_STOP);
    }
}

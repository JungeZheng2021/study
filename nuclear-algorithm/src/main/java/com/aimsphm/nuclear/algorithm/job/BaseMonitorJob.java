package com.aimsphm.nuclear.algorithm.job;

import com.aimsphm.nuclear.algorithm.enums.AlgorithmTypeEnum;
import com.aimsphm.nuclear.algorithm.service.AlgorithmService;
import com.aimsphm.nuclear.common.entity.CommonDeviceDO;
import com.aimsphm.nuclear.common.service.CommonDeviceService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;

import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.algorithm.job
 * @Description: <>
 * @Author: milla
 * @CreateDate: 2021/02/26 13:19
 * @UpdateUser: milla
 * @UpdateDate: 2021/02/26 13:19
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public interface BaseMonitorJob {
    Logger log = LoggerFactory.getLogger(BaseMonitorJob.class);

    /**
     * 执行调用
     *
     * @param deviceType       设备类型
     * @param algorithmService 算法接口
     * @param deviceService    设备接口
     * @param algorithmType    算法类型
     */
    default void execute(Integer deviceType, AlgorithmService algorithmService, CommonDeviceService deviceService, AlgorithmTypeEnum algorithmType) {
        try {
            log.info("device {} starting..................", algorithmType.getDesc());
            LambdaQueryWrapper<CommonDeviceDO> wrapper = Wrappers.lambdaQuery(CommonDeviceDO.class);
//        0:上充泵 1：风机
            wrapper.eq(CommonDeviceDO::getDeviceType, deviceType);
            List<CommonDeviceDO> list = deviceService.list(wrapper);
            if (CollectionUtils.isEmpty(list)) {
                log.info("device not exist deviceId:{}..................", deviceType);
                return;
            }
            //状态监测
            if (algorithmType.equals(AlgorithmTypeEnum.STATE_MONITOR)) {
                list.stream().forEach(deviceDO -> {
                    log.debug("device status running..................deviceId:{}", deviceDO.getDeviceName());
                    algorithmService.getDeviceStateMonitorInfo(deviceDO.getId());
                });
            }
            //启停状态
            if (algorithmType.equals(AlgorithmTypeEnum.STATE_START_STOP)) {
                list.stream().forEach(deviceDO -> {
                    log.debug("device start stop running..................deviceId:{}", deviceDO.getDeviceName());
                    algorithmService.getDeviceStartAndStopMonitorInfo(deviceDO.getId());
                });
            }
        } catch (Exception e) {
            log.error("device monitor failed: {}", e);
        } finally {
            log.info("device {} finished..................", algorithmType.getDesc());
        }
    }
}

package com.aimsphm.nuclear.algorithm.service;

import com.aimsphm.nuclear.algorithm.enums.AlgorithmTypeEnum;

/**
 * <p>
 * 功能描述:
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020/12/23 16:16
 */
public interface AlgorithmService {

    /**
     * 设备状态监测算法
     *
     * @param algorithmType
     * @param deviceId
     * @param algorithmPeriod
     * @return
     */
    void deviceStateMonitorInfo(AlgorithmTypeEnum algorithmType, Long deviceId, Integer algorithmPeriod);

    /**
     * 设备启停状态判断
     *
     * @param algorithmType
     * @param deviceId
     * @param algorithmPeriod
     * @return
     */
    void deviceThresholdMonitorInfo(AlgorithmTypeEnum algorithmType, Long deviceId, Integer algorithmPeriod);
}

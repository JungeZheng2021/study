package com.aimsphm.nuclear.algorithm.service;

/**
 * @Package: com.aimsphm.nuclear.algorithm.service
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2020/12/23 16:11
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/12/23 16:11
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public interface AlgorithmService {

    /**
     * 设备状态监测算法
     *
     * @return
     */
    void getDeviceStateMonitorInfo();

    /**
     * 设备启停状态判断
     */
    void getDeviceStartAndStopMonitorInfo();
}

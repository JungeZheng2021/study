package com.aimsphm.nuclear.core.service;

import java.util.Map;

/**
 * @Package: com.aimsphm.nuclear.core.service
 * @Description: <>
 * @Author: milla
 * @CreateDate: 2020/08/14 14:32
 * @UpdateUser: milla
 * @UpdateDate: 2020/08/14 14:32
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public interface VibrationAnalysisService {
    /**
     * 查询测点位置
     *
     * @param deviceId 设备id
     * @return
     */
    Map<String, String> listSensorLocation(Long deviceId);

    /**
     * 根据子系统编号查询测点位置
     *
     * @param subSystemId 子系统编号
     * @return
     */
    Map<String, String> listSensorLocationBySubSystemId(Long subSystemId);
}

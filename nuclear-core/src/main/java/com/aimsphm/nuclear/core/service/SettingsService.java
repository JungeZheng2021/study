package com.aimsphm.nuclear.core.service;

import com.aimsphm.nuclear.common.entity.CommonSensorDO;
import com.aimsphm.nuclear.common.entity.CommonSensorSettingsDO;

/**
 * @Package: com.aimsphm.nuclear.core.service
 * @Description: <系统设置模块>
 * @Author: MILLA
 * @CreateDate: 2021/01/21 11:05
 * @UpdateUser: MILLA
 * @UpdateDate: 2021/01/21 11:05
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public interface SettingsService {

    /**
     * 启/禁用 报警
     *
     * @param deviceId 设备id
     * @param enable   启用禁用标识
     * @return
     */
    boolean updateDeviceMonitorStatus(Long deviceId, boolean enable);

    /**
     * 设备报警重置
     *
     * @param deviceId 设备id
     * @return
     */
    boolean updateDeviceEventProduce(Long deviceId);

    /**
     * 修改传感器数据
     *
     * @param dto
     * @return
     */
    boolean modifyCommonSensor(CommonSensorDO dto);

    /**
     * 修改基本配置
     *
     * @param dto
     * @return
     */
    boolean saveOrModifyCommonSensorSettings(CommonSensorSettingsDO dto);
}

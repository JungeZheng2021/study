package com.aimsphm.nuclear.ext.service;

import com.aimsphm.nuclear.common.entity.JobDeviceStatusDO;
import com.aimsphm.nuclear.common.service.JobDeviceStatusService;

/**
 * @Package: com.aimsphm.nuclear.ext.service
 * @Description: <设备状态扩展服务类>
 * @Author: MILLA
 * @CreateDate: 2020-12-04
 * @UpdateUser: MILLA
 * @UpdateDate: 2020-12-04
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public interface JobDeviceStatusServiceExt extends JobDeviceStatusService {

    /**
     * 获取设备运行状态
     *
     * @param deviceId
     * @return
     */
    JobDeviceStatusDO getDeviceRunningStatus(Long deviceId);
}

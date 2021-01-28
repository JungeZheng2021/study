package com.aimsphm.nuclear.common.service;

import com.aimsphm.nuclear.common.entity.JobDeviceStatusDO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

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
public interface JobDeviceStatusService extends IService<JobDeviceStatusDO> {

    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return
     */
    Page<JobDeviceStatusDO> listJobDeviceStatusByPageWithParams(QueryBO<JobDeviceStatusDO> queryBO);

    /**
     * 获取设备运行状态
     *
     * @param deviceId
     * @return
     */
    JobDeviceStatusDO getDeviceRunningStatus(Long deviceId);

    /**
     * 计算设备的状态
     *
     * @param status
     * @param enableMonitor
     */
    void updateDeviceStatusWithCalculate(JobDeviceStatusDO status, Boolean enableMonitor);

    /**
     * 更新或者是保存设备状态
     *
     * @param status       原来的状态
     * @param healthStatus 新的状态
     */
    void saveOrUpdateDeviceStatus(JobDeviceStatusDO status, Integer healthStatus);

    /**
     * 获取设备当前的健康状态
     *
     * @param deviceId 设备id
     * @param isStop   是否停机
     * @return
     */
    Integer getDeviceCurrentStatus(Long deviceId, boolean isStop);


}

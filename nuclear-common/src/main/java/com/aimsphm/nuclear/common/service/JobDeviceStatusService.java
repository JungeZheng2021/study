package com.aimsphm.nuclear.common.service;

import com.aimsphm.nuclear.common.entity.CommonDeviceDO;
import com.aimsphm.nuclear.common.entity.JobDeviceStatusDO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.entity.bo.TimeRangeQueryBO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
     *  @param status
     * @param enableMonitor
     * @param healthStatus
     */
    void updateDeviceStatusWithCalculate(JobDeviceStatusDO status, CommonDeviceDO enableMonitor, Integer healthStatus);

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
     * @param deviceId     设备id
     * @param healthStatus 设备状态
     * @return
     */
    Integer getDeviceCurrentStatus(CommonDeviceDO deviceId, Integer healthStatus);


    /**
     * 计算统计设备的次数
     *
     * @param deviceId     设备id
     * @param rangeQueryBO 起止时间
     * @return
     */
    int countStopStatus(Long deviceId, TimeRangeQueryBO rangeQueryBO);
}

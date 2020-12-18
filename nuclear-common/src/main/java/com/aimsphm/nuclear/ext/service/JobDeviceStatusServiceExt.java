package com.aimsphm.nuclear.ext.service;

import com.aimsphm.nuclear.common.entity.CommonSystemDO;
import com.aimsphm.nuclear.common.entity.JobDeviceStatusDO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.service.JobDeviceStatusService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

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
}

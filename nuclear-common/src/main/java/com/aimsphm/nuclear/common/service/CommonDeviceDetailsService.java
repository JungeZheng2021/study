package com.aimsphm.nuclear.common.service;

import com.aimsphm.nuclear.common.entity.CommonDeviceDetailsDO;
import com.aimsphm.nuclear.common.entity.bo.CommonQueryBO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.ext.service
 * @Description: <设备详细信息扩展服务类>
 * @Author: MILLA
 * @CreateDate: 2020-11-17
 * @UpdateUser: MILLA
 * @UpdateDate: 2020-11-17
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public interface CommonDeviceDetailsService extends IService<CommonDeviceDetailsDO> {
    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return
     */
    Page<CommonDeviceDetailsDO> listCommonDeviceDetailsByPageWithParams(QueryBO<CommonDeviceDetailsDO> queryBO);

    /**
     * 获取设备信息
     *
     * @param query
     * @return
     */
    List<CommonDeviceDetailsDO> listDetailByConditions(CommonQueryBO query);

    /**
     * 更新最后启动时间
     *
     * @param deviceId
     */
    void updateLastStartTime(Long deviceId);
}

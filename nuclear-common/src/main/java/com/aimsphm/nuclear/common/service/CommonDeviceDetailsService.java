package com.aimsphm.nuclear.common.service;

import com.aimsphm.nuclear.common.entity.CommonDeviceDetailsDO;
import com.aimsphm.nuclear.common.entity.bo.CommonQueryBO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 功能描述:服务类
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020-11-17 14:30
 */
public interface CommonDeviceDetailsService extends IService<CommonDeviceDetailsDO> {
    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return 分页
     */
    Page<CommonDeviceDetailsDO> listCommonDeviceDetailsByPageWithParams(QueryBO<CommonDeviceDetailsDO> queryBO);

    /**
     * 获取设备信息
     *
     * @param query 查询条件
     * @return 集合
     */
    List<CommonDeviceDetailsDO> listDetailByConditions(CommonQueryBO query);

    /**
     * 更新最后启动时间
     *
     * @param deviceId 设备id
     */
    void updateLastStartTime(Long deviceId);

    /**
     * 根据sensorCode和字段名称获取配置
     *
     * @param fieldName 字段名称
     * @return map
     */
    Map<String, CommonDeviceDetailsDO> listDetailByFilename(String fieldName);
}

package com.aimsphm.nuclear.common.service;

import com.aimsphm.nuclear.common.entity.CommonSensorSettingsDO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.common.service
 * @Description: <传感器信息服务类>
 * @Author: MILLA
 * @CreateDate: 2021-01-21
 * @UpdateUser: MILLA
 * @UpdateDate: 2021-01-21
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public interface CommonSensorSettingsService extends IService<CommonSensorSettingsDO> {

    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return
     */
    Page<CommonSensorSettingsDO> listCommonSensorSettingsByPageWithParams(QueryBO<CommonSensorSettingsDO> queryBO);

    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return
     */
    List<CommonSensorSettingsDO> listCommonSensorSettingsWithParams(QueryBO<CommonSensorSettingsDO> queryBO);

    /**
     * 根据边缘id获取设置信息
     *
     * @param edgeId
     * @param category 边缘id
     * @return
     */
    CommonSensorSettingsDO getCommonSensorByEdgeId(Integer edgeId, Integer category);
}

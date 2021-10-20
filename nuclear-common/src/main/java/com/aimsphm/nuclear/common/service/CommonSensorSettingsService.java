package com.aimsphm.nuclear.common.service;

import com.aimsphm.nuclear.common.entity.CommonSensorSettingsDO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 功能描述:服务类
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020-01-21 14:30
 */
public interface CommonSensorSettingsService extends IService<CommonSensorSettingsDO> {

    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return 分页
     */
    Page<CommonSensorSettingsDO> listCommonSensorSettingsByPageWithParams(QueryBO<CommonSensorSettingsDO> queryBO);

    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return 集合
     */
    List<CommonSensorSettingsDO> listCommonSensorSettingsWithParams(QueryBO<CommonSensorSettingsDO> queryBO);

    /**
     * 根据边缘id获取设置信息
     *
     * @param edgeId   边缘id
     * @param category 类型
     * @return 对象
     */
    CommonSensorSettingsDO getCommonSensorByEdgeId(Integer edgeId, Integer category);
}

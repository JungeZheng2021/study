package com.aimsphm.nuclear.common.service;

import com.aimsphm.nuclear.common.entity.CommonSensorDO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.entity.vo.SensorVO;
import com.aimsphm.nuclear.data.feign.entity.dto.ConfigSettingsDTO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.ArrayList;
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
public interface CommonSensorService extends IService<CommonSensorDO> {

    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return
     */
    Page<CommonSensorDO> listCommonSensorByPageWithParams(QueryBO<CommonSensorDO> queryBO);

    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return
     */
    List<CommonSensorDO> listCommonSensorWithParams(QueryBO<CommonSensorDO> queryBO);

    /**
     * 获取列表-系统设置/振动分析
     *
     * @param entity
     * @return
     */
    List<SensorVO> listCommonSensorSettingsWithParams(CommonSensorDO entity);

    /**
     * 更新配置状态
     *
     * @param edgeCode
     * @param result
     */
    void updateConfigStatus(String edgeCode, ConfigSettingsDTO result);

    /**
     * 根据sensorCode列表获取传感器信息
     *
     * @param sensorCodeList sensorCode列表
     * @return
     */
    List<CommonSensorDO> listCommonSensorBySensorCodeList(ArrayList<String> sensorCodeList);
}

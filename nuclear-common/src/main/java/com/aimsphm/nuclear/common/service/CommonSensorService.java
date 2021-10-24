package com.aimsphm.nuclear.common.service;

import com.aimsphm.nuclear.common.entity.CommonSensorDO;
import com.aimsphm.nuclear.common.entity.CommonSensorSettingsDO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.entity.vo.SensorVO;
import com.aimsphm.nuclear.data.feign.entity.dto.ConfigSettingsDTO;
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
public interface CommonSensorService extends IService<CommonSensorDO> {

    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return 分页
     */
    Page<CommonSensorDO> listCommonSensorByPageWithParams(QueryBO<CommonSensorDO> queryBO);

    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return 集合
     */
    List<CommonSensorDO> listCommonSensorWithParams(QueryBO<CommonSensorDO> queryBO);

    /**
     * 获取列表-系统设置/振动分析
     *
     * @param entity 实体
     * @return 集合
     */
    List<SensorVO> listCommonSensorSettingsWithParams(CommonSensorDO entity);

    /**
     * 更新配置状态
     *
     * @param edgeCode 编码
     * @param result   结果
     */
    void updateConfigStatus(String edgeCode, ConfigSettingsDTO result);

    /**
     * 获取配置
     *
     * @param sensorCode 编码
     * @param category   类型
     * @return 对象
     */
    CommonSensorSettingsDO getSensorConfigBySensorCode(String sensorCode, Integer category);

    /**
     * 根据sensorCode列表获取传感器信息
     *
     * @param sensorCodeList sensorCode列表
     * @return 集合
     */
    List<CommonSensorDO> listCommonSensorBySensorCodeList(List<String> sensorCodeList);
}

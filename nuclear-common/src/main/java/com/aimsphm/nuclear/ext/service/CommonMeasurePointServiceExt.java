package com.aimsphm.nuclear.ext.service;

import com.aimsphm.nuclear.common.entity.CommonMeasurePointDO;
import com.aimsphm.nuclear.common.entity.vo.MeasurePointVO;
import com.aimsphm.nuclear.common.entity.vo.PointFeatureVO;
import com.aimsphm.nuclear.common.service.CommonMeasurePointService;

import java.util.List;
import java.util.Set;

/**
 * @Package: com.aimsphm.nuclear.ext.service
 * @Description: <测点信息扩展服务类>
 * @Author: MILLA
 * @CreateDate: 2020-11-17
 * @UpdateUser: MILLA
 * @UpdateDate: 2020-11-17
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public interface CommonMeasurePointServiceExt extends CommonMeasurePointService {

    /**
     * 根据测点id更新redis中的热点数据
     *
     * @param itemId 测点id
     * @param value  对应的值
     */
    void updateMeasurePointsInRedis(String itemId, Double value);

    /**
     * 根据测点id获取测点列表(考虑一个测点可能会被多个设备/系统共用)
     *
     * @param itemId 测点id
     * @return
     */
    List<MeasurePointVO> getMeasurePointsByTagId(String itemId);

    /**
     * 获取所有的特征值
     *
     * @return
     */
    Set<String> listFeatures();

    /**
     * 获取点存储在redis中的key
     *
     * @param vo 实体
     * @return
     */
    String getStoreKey(CommonMeasurePointDO vo);

    /**
     * 根据测点编号获取测点下面所有的特征信息
     *
     * @param sensorCode 测点编号
     * @return
     */
    PointFeatureVO listFeatures(String sensorCode);

    /**
     * 清除所有的时时点信息
     */
    void clearAllPointsData();

    /**
     * 根据设备id获取测点的列表
     *
     * @param deviceId 设备id
     * @param status
     * @return
     */
    List<CommonMeasurePointDO> listPointsByDeviceId(Long deviceId, Integer status);
}

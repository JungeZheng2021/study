package com.aimsphm.nuclear.common.service;

import com.aimsphm.nuclear.common.entity.CommonMeasurePointDO;
import com.aimsphm.nuclear.common.entity.bo.CommonQueryBO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.entity.vo.LabelVO;
import com.aimsphm.nuclear.common.entity.vo.MeasurePointVO;
import com.aimsphm.nuclear.common.entity.vo.PointFeatureVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

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
public interface CommonMeasurePointService extends IService<CommonMeasurePointDO> {
    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return
     */
    Page<CommonMeasurePointDO> listCommonMeasurePointByPageWithParams(QueryBO<CommonMeasurePointDO> queryBO);

    /**
     * 根据测点id更新redis中的热点数据
     *
     * @param itemId    测点id
     * @param value     对应的值
     * @param timestamp
     */
    void updateMeasurePointsInRedis(String itemId, Double value, Long timestamp);

    /**
     * 根据测点id获取测点信息
     *
     * @param pointId 测点Id
     * @return
     */
    CommonMeasurePointDO getPointByPointId(String pointId);

    /**
     * 判断测点是否需要降采样
     *
     * @param pointId 测点Id
     * @return true:降采样 false:不降采样
     */
    Boolean isNeedDownSample(String pointId);

    /**
     * 判断测点是否需要降采样
     *
     * @param point 测点
     * @return
     */
    Boolean isNeedDownSample(CommonMeasurePointDO point);

    /**
     * 根据测点id获取测点列表(考虑一个测点可能会被多个设备/系统共用)
     *
     * @param itemId 测点id
     * @return
     */
    List<MeasurePointVO> getMeasurePointsByPointId(String itemId);

    /**
     * 获取所有的特征值
     *
     * @return
     */
    Set<String> listFeatures();

    /**
     * 更新点位信息
     *
     * @param vo    点的基本信息
     * @param value 对应的value值
     */
    void store2Redis(MeasurePointVO vo, Double value);

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
     * 根据系统id、子系统id、设备id查询测点集合
     *
     * @param query 查询条件
     * @return
     */
    List<CommonMeasurePointDO> listPointsByConditions(CommonQueryBO query);

    /**
     * 获取所有测点的位置信息
     *
     * @param subSystemId
     * @return
     */
    List<LabelVO> listLocationInfo(Long subSystemId);

    /**
     * 根据测点id列表获取sensorCode列表
     *
     * @param pointIdList 测点id列表
     * @return
     */
    List<String> listSensorCodeByPointList(List<String> pointIdList);

    /**
     * 查询所有的油质测点
     *
     * @param deviceId 设备id
     * @return
     */
    List<CommonMeasurePointDO> listOilPoint(Long deviceId);
}

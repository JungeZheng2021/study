package com.aimsphm.nuclear.common.service;

import com.aimsphm.nuclear.common.entity.CommonMeasurePointDO;
import com.aimsphm.nuclear.common.entity.JobAlarmRealtimeDO;
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
 * @since 2020-12-24 14:30
 */
public interface JobAlarmRealtimeService extends IService<JobAlarmRealtimeDO> {

    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return 分页
     */
    Page<JobAlarmRealtimeDO> listJobAlarmRealtimeByPageWithParams(QueryBO<JobAlarmRealtimeDO> queryBO);

    /**
     * 根据起止时间获取实时报警的报警时间数据
     *
     * @param pointId 测点
     * @param start   开始
     * @param end     结束
     * @param modelId 模型id
     * @return 集合
     */
    List<JobAlarmRealtimeDO> listRealTime(String pointId, Long start, Long end, Long modelId);

    /**
     * 不分页查询数据
     *
     * @param queryBO 条件
     * @return 集合
     */
    List<JobAlarmRealtimeDO> listJobAlarmRealtimeWithParams(QueryBO queryBO);

    /**
     * 去重复的
     *
     * @param queryBO 查询条件
     * @return 集合
     */
    List<CommonMeasurePointDO> listJobAlarmRealtimeByPageWithParamsDistinct(QueryBO queryBO);

    /**
     * 获取测点报警记录
     *
     * @param queryBO  条件
     * @param pointIds 测点ids
     * @return map
     */
    Map<String, List<Long>> listJobAlarmRealtimeWithParams(QueryBO<JobAlarmRealtimeDO> queryBO, List<String> pointIds);
}

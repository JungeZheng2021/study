package com.aimsphm.nuclear.common.service;

import com.aimsphm.nuclear.common.entity.JobAlarmRealtimeDO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.common.service
 * @Description: <服务类>
 * @Author: MILLA
 * @CreateDate: 2020-12-24
 * @UpdateUser: MILLA
 * @UpdateDate: 2020-12-24
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public interface JobAlarmRealtimeService extends IService<JobAlarmRealtimeDO> {

    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return
     */
    Page<JobAlarmRealtimeDO> listJobAlarmRealtimeByPageWithParams(QueryBO<JobAlarmRealtimeDO> queryBO);

    /**
     * 根据起止时间获取实时报警的报警时间数据
     *
     * @param pointId
     * @param start
     * @param end
     * @param modelId
     * @return
     */
    List<JobAlarmRealtimeDO> listRealTime(String pointId, Long start, Long end, Long modelId);

    /**
     * 不分页查询数据
     *
     * @param queryBO
     * @return
     */
    List<JobAlarmRealtimeDO> listJobAlarmRealtimeWithParams(QueryBO queryBO);
}

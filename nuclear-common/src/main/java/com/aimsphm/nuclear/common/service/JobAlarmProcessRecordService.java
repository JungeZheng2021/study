package com.aimsphm.nuclear.common.service;

import com.aimsphm.nuclear.common.entity.JobAlarmProcessRecordDO;
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
 * @since 2020-12-05 14:30
 */
public interface JobAlarmProcessRecordService extends IService<JobAlarmProcessRecordDO> {

    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return 分页
     */
    Page<JobAlarmProcessRecordDO> listJobAlarmProcessRecordByPageWithParams(QueryBO<JobAlarmProcessRecordDO> queryBO);

    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return 集合
     */
    List<JobAlarmProcessRecordDO> listJobAlarmProcessRecordWithParams(QueryBO<JobAlarmProcessRecordDO> queryBO);
}

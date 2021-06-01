package com.aimsphm.nuclear.common.service;

import com.aimsphm.nuclear.common.entity.JobAlarmProcessRecordDO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.common.service
 * @Description: <服务类>
 * @Author: MILLA
 * @CreateDate: 2021-05-25
 * @UpdateUser: MILLA
 * @UpdateDate: 2021-05-25
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public interface JobAlarmProcessRecordService extends IService<JobAlarmProcessRecordDO> {
    
    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return
     */
    Page<JobAlarmProcessRecordDO> listJobAlarmProcessRecordByPageWithParams(QueryBO<JobAlarmProcessRecordDO> queryBO);

    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return
     */
    List<JobAlarmProcessRecordDO> listJobAlarmProcessRecordWithParams(QueryBO<JobAlarmProcessRecordDO> queryBO);
}

package com.aimsphm.nuclear.ext.service;

import com.aimsphm.nuclear.common.entity.JobAlarmEventDO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.service.JobAlarmEventService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * @Package: com.aimsphm.nuclear.ext.service
 * @Description: <报警事件扩展服务类>
 * @Author: MILLA
 * @CreateDate: 2020-12-05
 * @UpdateUser: MILLA
 * @UpdateDate: 2020-12-05
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public interface JobAlarmEventServiceExt extends JobAlarmEventService {

    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return
     */
    Page<JobAlarmEventDO> listJobAlarmEventByPageWithParams(QueryBO<JobAlarmEventDO> queryBO);
}

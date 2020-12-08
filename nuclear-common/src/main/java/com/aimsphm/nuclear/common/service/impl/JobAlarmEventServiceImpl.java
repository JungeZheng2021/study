package com.aimsphm.nuclear.common.service.impl;

import com.aimsphm.nuclear.common.entity.JobAlarmEventDO;
import com.aimsphm.nuclear.common.mapper.JobAlarmEventMapper;
import com.aimsphm.nuclear.common.service.JobAlarmEventService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * @Package: com.aimsphm.nuclear.common.service.impl
 * @Description: <报警事件信息服务实现类>
 * @Author: MILLA
 * @CreateDate: 2020-12-08
 * @UpdateUser: MILLA
 * @UpdateDate: 2020-12-08
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public class JobAlarmEventServiceImpl extends ServiceImpl<JobAlarmEventMapper, JobAlarmEventDO> implements JobAlarmEventService {

}

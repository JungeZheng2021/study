package com.aimsphm.nuclear.ext.service.impl;

import com.aimsphm.nuclear.ext.service.JobAlarmEventServiceExt;
import com.aimsphm.nuclear.common.service.impl.JobAlarmEventServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * @Package: com.aimsphm.nuclear.ext.service.impl
 * @Description: <报警事件扩展服务实现类>
 * @Author: MILLA
 * @CreateDate: 2020-12-05
 * @UpdateUser: MILLA
 * @UpdateDate: 2020-12-05
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Service
@ConditionalOnProperty(prefix = "spring.config", name = "enableServiceExtImpl", havingValue = "true")
public class JobAlarmEventServiceExtImpl extends JobAlarmEventServiceImpl implements JobAlarmEventServiceExt {

}

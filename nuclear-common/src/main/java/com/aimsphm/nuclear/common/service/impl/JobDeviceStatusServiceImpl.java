package com.aimsphm.nuclear.common.service.impl;

import com.aimsphm.nuclear.common.entity.JobDeviceStatusDO;
import com.aimsphm.nuclear.common.mapper.JobDeviceStatusMapper;
import com.aimsphm.nuclear.common.service.JobDeviceStatusService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * @Package: com.aimsphm.nuclear.common.service.impl
 * @Description: <设备状态服务实现类>
 * @Author: MILLA
 * @CreateDate: 2020-12-05
 * @UpdateUser: MILLA
 * @UpdateDate: 2020-12-05
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public class JobDeviceStatusServiceImpl extends ServiceImpl<JobDeviceStatusMapper, JobDeviceStatusDO> implements JobDeviceStatusService {

}

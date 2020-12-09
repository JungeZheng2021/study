package com.aimsphm.nuclear.common.service.impl;

import com.aimsphm.nuclear.common.entity.CommonSensorDO;
import com.aimsphm.nuclear.common.mapper.CommonSensorMapper;
import com.aimsphm.nuclear.common.service.CommonSensorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * @Package: com.aimsphm.nuclear.common.service.impl
 * @Description: <传感器信息服务实现类>
 * @Author: MILLA
 * @CreateDate: 2020-12-09
 * @UpdateUser: MILLA
 * @UpdateDate: 2020-12-09
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public class CommonSensorServiceImpl extends ServiceImpl<CommonSensorMapper, CommonSensorDO> implements CommonSensorService {

}

package com.aimsphm.nuclear.common.service.impl;

import com.aimsphm.nuclear.common.entity.CommonDeviceDO;
import com.aimsphm.nuclear.common.mapper.CommonDeviceMapper;
import com.aimsphm.nuclear.common.service.CommonDeviceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * @Package: com.aimsphm.nuclear.common.service.impl
 * @Description: <设备信息服务实现类>
 * @Author: MILLA
 * @CreateDate: 2020-11-30
 * @UpdateUser: MILLA
 * @UpdateDate: 2020-11-30
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public class CommonDeviceServiceImpl extends ServiceImpl<CommonDeviceMapper, CommonDeviceDO> implements CommonDeviceService {

}

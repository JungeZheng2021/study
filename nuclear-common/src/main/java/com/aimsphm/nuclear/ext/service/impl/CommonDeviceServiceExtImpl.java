package com.aimsphm.nuclear.ext.service.impl;

import com.aimsphm.nuclear.common.entity.CommonDeviceDO;
import com.aimsphm.nuclear.common.entity.vo.TreeVO;
import com.aimsphm.nuclear.common.service.impl.CommonDeviceServiceImpl;
import com.aimsphm.nuclear.ext.service.CommonDeviceServiceExt;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @Package: com.aimsphm.nuclear.ext.service.impl
 * @Description: <设备信息扩展服务实现类>
 * @Author: MILLA
 * @CreateDate: 2020-11-17
 * @UpdateUser: MILLA
 * @UpdateDate: 2020-11-17
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Service
@ConditionalOnProperty(prefix = "spring.config", name = "enableServiceExtImpl", havingValue = "true")
public class CommonDeviceServiceExtImpl extends CommonDeviceServiceImpl implements CommonDeviceServiceExt {
}

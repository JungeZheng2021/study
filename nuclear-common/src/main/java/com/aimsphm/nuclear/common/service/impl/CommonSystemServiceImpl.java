package com.aimsphm.nuclear.common.service.impl;

import com.aimsphm.nuclear.common.entity.CommonSystemDO;
import com.aimsphm.nuclear.common.mapper.CommonSystemMapper;
import com.aimsphm.nuclear.common.service.CommonSystemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * @Package: com.aimsphm.nuclear.common.service.impl
 * @Description: <系统信息服务实现类>
 * @Author: MILLA
 * @CreateDate: 2020-11-23
 * @UpdateUser: MILLA
 * @UpdateDate: 2020-11-23
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public class CommonSystemServiceImpl extends ServiceImpl<CommonSystemMapper, CommonSystemDO> implements CommonSystemService {

}

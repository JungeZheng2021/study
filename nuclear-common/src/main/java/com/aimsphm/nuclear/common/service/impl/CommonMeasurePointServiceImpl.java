package com.aimsphm.nuclear.common.service.impl;

import com.aimsphm.nuclear.common.entity.CommonMeasurePointDO;
import com.aimsphm.nuclear.common.mapper.CommonMeasurePointMapper;
import com.aimsphm.nuclear.common.service.CommonMeasurePointService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * @Package: com.aimsphm.nuclear.common.service.impl
 * @Description: <测点信息服务实现类>
 * @Author: MILLA
 * @CreateDate: 2020-12-08
 * @UpdateUser: MILLA
 * @UpdateDate: 2020-12-08
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public class CommonMeasurePointServiceImpl extends ServiceImpl<CommonMeasurePointMapper, CommonMeasurePointDO> implements CommonMeasurePointService {

}

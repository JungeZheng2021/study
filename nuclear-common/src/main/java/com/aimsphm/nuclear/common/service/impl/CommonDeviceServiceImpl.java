package com.aimsphm.nuclear.common.service.impl;

import com.aimsphm.nuclear.common.entity.CommonDeviceDO;
import com.aimsphm.nuclear.common.entity.bo.ConditionsQueryBO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.mapper.CommonDeviceMapper;
import com.aimsphm.nuclear.common.service.CommonDeviceService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.CaseFormat;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
public class CommonDeviceServiceImpl extends ServiceImpl<CommonDeviceMapper, CommonDeviceDO> implements CommonDeviceService {
    @Override
    public Page<CommonDeviceDO> listCommonDeviceByPageWithParams(QueryBO<CommonDeviceDO> queryBO) {
        if (Objects.nonNull(queryBO.getPage().getOrders()) && !queryBO.getPage().getOrders().isEmpty()) {
            queryBO.getPage().getOrders().stream().forEach(item -> item.setColumn(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, item.getColumn())));
        }
        return this.page(queryBO.getPage(), customerConditions(queryBO));
    }

    @Override
    public List<CommonDeviceDO> listCommonDeviceWithParams(QueryBO<CommonDeviceDO> queryBO) {
        return this.list(customerConditions(queryBO));
    }

    private LambdaQueryWrapper<CommonDeviceDO> customerConditions(QueryBO<CommonDeviceDO> queryBO) {
        LambdaQueryWrapper<CommonDeviceDO> wrapper = queryBO.lambdaQuery();
        ConditionsQueryBO query = queryBO.getQuery();
        if (Objects.nonNull(query.getEnd()) && Objects.nonNull(query.getEnd())) {
        }
        if (StringUtils.hasText(queryBO.getQuery().getKeyword())) {
        }
        return wrapper;
    }


}

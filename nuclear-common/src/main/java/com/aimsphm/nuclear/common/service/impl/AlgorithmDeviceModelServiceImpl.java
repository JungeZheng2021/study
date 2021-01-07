package com.aimsphm.nuclear.common.service.impl;

import com.aimsphm.nuclear.common.entity.AlgorithmDeviceModelDO;
import com.aimsphm.nuclear.common.entity.bo.ConditionsQueryBO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.mapper.AlgorithmDeviceModelMapper;
import com.aimsphm.nuclear.common.service.AlgorithmDeviceModelService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.CaseFormat;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;
/**
 * @Package: com.aimsphm.nuclear.common.service.impl
 * @Description: <算法模型关系信息服务实现类>
 * @Author: MILLA
 * @CreateDate: 2020-12-23
 * @UpdateUser: MILLA
 * @UpdateDate: 2020-12-23
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Service
@ConditionalOnProperty(prefix = "spring.config", name = "enableServiceExtImpl", havingValue = "true")
public class AlgorithmDeviceModelServiceImpl extends ServiceImpl<AlgorithmDeviceModelMapper, AlgorithmDeviceModelDO> implements AlgorithmDeviceModelService {

    @Override
    public Page<AlgorithmDeviceModelDO> listAlgorithmDeviceModelByPageWithParams(QueryBO<AlgorithmDeviceModelDO> queryBO) {
        if (Objects.nonNull(queryBO.getPage().getOrders()) && !queryBO.getPage().getOrders().isEmpty()) {
            queryBO.getPage().getOrders().stream().forEach(item -> item.setColumn(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, item.getColumn())));
        }
        LambdaQueryWrapper<AlgorithmDeviceModelDO> wrapper = queryBO.lambdaQuery();
        ConditionsQueryBO query = queryBO.getQuery();
        if (Objects.nonNull(query.getEnd()) && Objects.nonNull(query.getEnd())) {
        }
        if (StringUtils.hasText(queryBO.getQuery().getKeyword())) {
        }
        return this.page(queryBO.getPage(), wrapper);
    }
}

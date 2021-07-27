package com.aimsphm.nuclear.common.service.impl;

import com.aimsphm.nuclear.common.entity.CommonComponentDO;
import com.aimsphm.nuclear.common.entity.bo.ConditionsQueryBO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.mapper.CommonComponentMapper;
import com.aimsphm.nuclear.common.service.CommonComponentService;
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
 * @Package: com.aimsphm.nuclear.common.service.impl
 * @Description: <组件信息服务实现类>
 * @Author: MILLA
 * @CreateDate: 2021-06-03
 * @UpdateUser: MILLA
 * @UpdateDate: 2021-06-03
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Service
@ConditionalOnProperty(prefix = "spring.config", name = "enableServiceExtImpl", havingValue = "true")
public class CommonComponentServiceImpl extends ServiceImpl<CommonComponentMapper, CommonComponentDO> implements CommonComponentService {

    @Override
    public Page<CommonComponentDO> listCommonComponentByPageWithParams(QueryBO<CommonComponentDO> queryBO) {
        if (Objects.nonNull(queryBO.getPage().getOrders()) && !queryBO.getPage().getOrders().isEmpty()) {
            queryBO.getPage().getOrders().stream().forEach(item -> item.setColumn(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, item.getColumn())));
        }
        return this.page(queryBO.getPage(), customerConditions(queryBO));
    }

    /**
     * 拼装查询条件
     *
     * @param queryBO
     * @return
     */
    private LambdaQueryWrapper<CommonComponentDO> customerConditions(QueryBO<CommonComponentDO> queryBO) {
        LambdaQueryWrapper<CommonComponentDO> wrapper = queryBO.lambdaQuery();
        ConditionsQueryBO query = queryBO.getQuery();
        if (Objects.nonNull(query.getEnd()) && Objects.nonNull(query.getEnd())) {
        }
        if (StringUtils.hasText(queryBO.getQuery().getKeyword())) {
        }
        //最大的设备部件不需要显示
        wrapper.ne(CommonComponentDO::getParentComponentId, 0);
        return wrapper;
    }

    @Override
    public List<CommonComponentDO> listCommonComponentWithParams(QueryBO<CommonComponentDO> queryBO) {
        return this.list(customerConditions(queryBO));
    }
}

package com.aimsphm.nuclear.common.service.impl;

import com.aimsphm.nuclear.common.entity.CommonComponentDO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.mapper.CommonComponentMapper;
import com.aimsphm.nuclear.common.service.CommonComponentService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.CaseFormat;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 功能描述:组件信息服务实现类
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2021-06-03 14:30
 */
@Service
@ConditionalOnProperty(prefix = "spring.config", name = "enableServiceExtImpl", havingValue = "true")
public class CommonComponentServiceImpl extends ServiceImpl<CommonComponentMapper, CommonComponentDO> implements CommonComponentService {

    @Override
    public Page<CommonComponentDO> listCommonComponentByPageWithParams(QueryBO<CommonComponentDO> queryBO) {
        if (Objects.nonNull(queryBO.getPage().getOrders()) && !queryBO.getPage().getOrders().isEmpty()) {
            queryBO.getPage().getOrders().forEach(item -> item.setColumn(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, item.getColumn())));
        }
        return this.page(queryBO.getPage(), customerConditions(queryBO));
    }

    /**
     * 拼装查询条件
     *
     * @param queryBO 条件
     * @return 封装的查询条件
     */
    private LambdaQueryWrapper<CommonComponentDO> customerConditions(QueryBO<CommonComponentDO> queryBO) {
        LambdaQueryWrapper<CommonComponentDO> wrapper = queryBO.lambdaQuery();
        //最大的设备部件不需要显示
        wrapper.ne(CommonComponentDO::getParentComponentId, 0);
        return wrapper;
    }

    @Override
    public List<CommonComponentDO> listCommonComponentWithParams(QueryBO<CommonComponentDO> queryBO) {
        return this.list(customerConditions(queryBO));
    }
}

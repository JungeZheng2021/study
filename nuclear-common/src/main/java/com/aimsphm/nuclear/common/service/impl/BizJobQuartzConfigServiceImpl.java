package com.aimsphm.nuclear.common.service.impl;

import com.aimsphm.nuclear.common.entity.BizJobQuartzConfigDO;
import com.aimsphm.nuclear.common.entity.bo.ConditionsQueryBO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.mapper.BizJobQuartzConfigMapper;
import com.aimsphm.nuclear.common.service.BizJobQuartzConfigService;
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
 * @Description: <算法配置服务实现类>
 * @Author: MILLA
 * @CreateDate: 2021-02-23
 * @UpdateUser: MILLA
 * @UpdateDate: 2021-02-23
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Service
@ConditionalOnProperty(prefix = "spring.config", name = "enableServiceExtImpl", havingValue = "true")
public class BizJobQuartzConfigServiceImpl extends ServiceImpl<BizJobQuartzConfigMapper, BizJobQuartzConfigDO> implements BizJobQuartzConfigService {

    @Override
    public Page<BizJobQuartzConfigDO> listBizJobQuartzConfigByPageWithParams(QueryBO<BizJobQuartzConfigDO> queryBO) {
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
    private LambdaQueryWrapper<BizJobQuartzConfigDO> customerConditions(QueryBO<BizJobQuartzConfigDO> queryBO) {
        LambdaQueryWrapper<BizJobQuartzConfigDO> wrapper = queryBO.lambdaQuery();
        ConditionsQueryBO query = queryBO.getQuery();
        if (Objects.nonNull(query.getEnd()) && Objects.nonNull(query.getEnd())) {
        }
        if (StringUtils.hasText(queryBO.getQuery().getKeyword())) {
        }
        return wrapper;
    }

    @Override
    public List<BizJobQuartzConfigDO> listBizJobQuartzConfigWithParams(QueryBO<BizJobQuartzConfigDO> queryBO) {
        return this.list(customerConditions(queryBO));
    }
}

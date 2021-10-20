package com.aimsphm.nuclear.common.service.impl;

import com.aimsphm.nuclear.common.entity.CommonSetDO;
import com.aimsphm.nuclear.common.entity.CommonSiteDO;
import com.aimsphm.nuclear.common.entity.bo.ConditionsQueryBO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.entity.vo.TreeVO;
import com.aimsphm.nuclear.common.mapper.CommonSiteMapper;
import com.aimsphm.nuclear.common.service.CommonSetService;
import com.aimsphm.nuclear.common.service.CommonSiteService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.CaseFormat;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 功能描述:电厂信息扩展服务实现类
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020-11-17 14:30
 */
@Service
@ConditionalOnProperty(prefix = "spring.config", name = "enableServiceExtImpl", havingValue = "true")
public class CommonSiteServiceImpl extends ServiceImpl<CommonSiteMapper, CommonSiteDO> implements CommonSiteService {
    @Autowired
    private CommonSetService setServiceExt;

    @Override
    public Page<CommonSiteDO> listCommonSiteByPageWithParams(QueryBO<CommonSiteDO> queryBO) {
        if (Objects.nonNull(queryBO.getPage().getOrders()) && !queryBO.getPage().getOrders().isEmpty()) {
            queryBO.getPage().getOrders().forEach(item -> item.setColumn(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, item.getColumn())));
        }
        LambdaQueryWrapper<CommonSiteDO> wrapper = queryBO.lambdaQuery();
        ConditionsQueryBO query = queryBO.getQuery();
        if (Objects.nonNull(query.getStart()) && Objects.nonNull(query.getEnd())) {
        }
        if (StringUtils.hasText(queryBO.getQuery().getKeyword())) {
        }
        return this.page(queryBO.getPage(), wrapper);
    }

    @Override
    public TreeVO<Long, String> listCommonSetTree(Long siteId) {
        CommonSiteDO siteDO = this.getById(siteId);
        if (Objects.isNull(siteDO)) {
            return null;
        }
        TreeVO<Long, String> vo = new TreeVO(siteDO.getId(), siteDO.getSiteName());
        LambdaQueryWrapper<CommonSetDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CommonSetDO::getSiteId, siteId).orderByAsc(CommonSetDO::getSort);
        List<CommonSetDO> list = setServiceExt.list(wrapper);
        if (CollectionUtils.isEmpty(list)) {
            return vo;
        }
        List<TreeVO<Long, String>> children = list.stream().map(item -> setServiceExt.listCommonSetTree(item.getId())).collect(Collectors.toList());
        vo.setChildren(children);
        return vo;
    }
}

package com.aimsphm.nuclear.common.service.impl;

import com.aimsphm.nuclear.common.entity.CommonSetDO;
import com.aimsphm.nuclear.common.entity.CommonSystemDO;
import com.aimsphm.nuclear.common.entity.bo.ConditionsQueryBO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.entity.vo.TreeVO;
import com.aimsphm.nuclear.common.mapper.CommonSetMapper;
import com.aimsphm.nuclear.common.service.CommonSetService;
import com.aimsphm.nuclear.common.service.CommonSystemService;
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
 * @Package: com.aimsphm.nuclear.ext.service.impl
 * @Description: <机组信息扩展服务实现类>
 * @Author: MILLA
 * @CreateDate: 2020-11-17
 * @UpdateUser: MILLA
 * @UpdateDate: 2020-11-17
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Service
@ConditionalOnProperty(prefix = "spring.config", name = "enableServiceExtImpl", havingValue = "true")
public class CommonSetServiceImpl extends ServiceImpl<CommonSetMapper, CommonSetDO> implements CommonSetService {

    @Autowired
    private CommonSystemService systemServiceExt;

    @Override
    public Page<CommonSetDO> listCommonSetByPageWithParams(QueryBO<CommonSetDO> queryBO) {
        if (Objects.nonNull(queryBO.getPage().getOrders()) && !queryBO.getPage().getOrders().isEmpty()) {
            queryBO.getPage().getOrders().stream().forEach(item -> item.setColumn(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, item.getColumn())));
        }
        LambdaQueryWrapper<CommonSetDO> wrapper = queryBO.lambdaQuery();
        ConditionsQueryBO query = queryBO.getQuery();
        if (Objects.nonNull(query.getEnd()) && Objects.nonNull(query.getEnd())) {
        }
        if (StringUtils.hasText(queryBO.getQuery().getKeyword())) {
        }
        return this.page(queryBO.getPage(), wrapper);
    }

    @Override
    public TreeVO<Long, String> listCommonSetTree(Long setId) {
        CommonSetDO commonSetDO = this.getById(setId);
        if (Objects.isNull(commonSetDO)) {
            return null;
        }
        TreeVO<Long, String> vo = new TreeVO(commonSetDO.getId(), commonSetDO.getSetName());
        LambdaQueryWrapper<CommonSystemDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CommonSystemDO::getSetId, setId).orderByAsc(CommonSystemDO::getSort);
        List<CommonSystemDO> list = systemServiceExt.list(wrapper);
        if (CollectionUtils.isEmpty(list)) {
            return vo;
        }
        List<TreeVO<Long, String>> children = list.stream().map(item -> systemServiceExt.listCommonSystemTree(item.getId())).collect(Collectors.toList());
        vo.setChildren(children);
        return vo;
    }
}

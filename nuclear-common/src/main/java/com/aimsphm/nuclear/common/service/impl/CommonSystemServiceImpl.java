package com.aimsphm.nuclear.common.service.impl;

import com.aimsphm.nuclear.common.entity.CommonSubSystemDO;
import com.aimsphm.nuclear.common.entity.CommonSystemDO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.entity.vo.TreeVO;
import com.aimsphm.nuclear.common.mapper.CommonSystemMapper;
import com.aimsphm.nuclear.common.service.CommonSubSystemService;
import com.aimsphm.nuclear.common.service.CommonSystemService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.CaseFormat;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 功能描述:系统信息扩展服务实现类
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020-11-17 14:30
 */
@Service
@ConditionalOnProperty(prefix = "spring.config", name = "enableServiceExtImpl", havingValue = "true")
public class CommonSystemServiceImpl extends ServiceImpl<CommonSystemMapper, CommonSystemDO> implements CommonSystemService {

    @Autowired
    private CommonSubSystemService subSystemServiceExt;

    @Override
    public Page<CommonSystemDO> listCommonSystemByPageWithParams(QueryBO<CommonSystemDO> queryBO) {
        if (Objects.nonNull(queryBO.getPage().getOrders()) && !queryBO.getPage().getOrders().isEmpty()) {
            queryBO.getPage().getOrders().forEach(item -> item.setColumn(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, item.getColumn())));
        }
        LambdaQueryWrapper<CommonSystemDO> wrapper = queryBO.lambdaQuery();
        return this.page(queryBO.getPage(), wrapper);
    }

    @Override
    public TreeVO<Long, String> listCommonSystemTree(Long id) {
        CommonSystemDO systemDO = this.getById(id);
        if (Objects.isNull(systemDO)) {
            return null;
        }
        TreeVO<Long, String> vo = new TreeVO(systemDO.getId(), systemDO.getSystemName());
        LambdaQueryWrapper<CommonSubSystemDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CommonSubSystemDO::getSystemId, id).orderByAsc(CommonSubSystemDO::getSort);
        List<CommonSubSystemDO> list = subSystemServiceExt.list(wrapper);
        if (CollectionUtils.isEmpty(list)) {
            return vo;
        }
        List<TreeVO<Long, String>> children = list.stream().map(item -> subSystemServiceExt.listCommonSubSystemTree(item.getId())).collect(Collectors.toList());
        vo.setChildren(children);
        return vo;
    }
}

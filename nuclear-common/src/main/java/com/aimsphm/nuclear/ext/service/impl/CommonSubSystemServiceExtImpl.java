package com.aimsphm.nuclear.ext.service.impl;

import com.aimsphm.nuclear.common.entity.CommonDeviceDO;
import com.aimsphm.nuclear.common.entity.CommonSubSystemDO;
import com.aimsphm.nuclear.common.entity.bo.ConditionsQueryBO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.entity.vo.TreeVO;
import com.aimsphm.nuclear.common.service.impl.CommonSubSystemServiceImpl;
import com.aimsphm.nuclear.ext.service.CommonDeviceServiceExt;
import com.aimsphm.nuclear.ext.service.CommonSubSystemServiceExt;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
 * @Description: <子系统信息扩展服务实现类>
 * @Author: MILLA
 * @CreateDate: 2020-11-17
 * @UpdateUser: MILLA
 * @UpdateDate: 2020-11-17
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Service
@ConditionalOnProperty(prefix = "spring.config", name = "enableServiceExtImpl", havingValue = "true")
public class CommonSubSystemServiceExtImpl extends CommonSubSystemServiceImpl implements CommonSubSystemServiceExt {

    @Autowired
    private CommonDeviceServiceExt deviceServiceExt;

    @Override
    public Page<CommonSubSystemDO> listCommonSubSystemByPageWithParams(QueryBO<CommonSubSystemDO> queryBO) {
        if (Objects.nonNull(queryBO.getPage().getOrders()) && !queryBO.getPage().getOrders().isEmpty()) {
            queryBO.getPage().getOrders().stream().forEach(item -> item.setColumn(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, item.getColumn())));
        }
        LambdaQueryWrapper<CommonSubSystemDO> wrapper = queryBO.lambdaQuery();
        ConditionsQueryBO query = queryBO.getQuery();
        if (Objects.nonNull(query.getEnd()) && Objects.nonNull(query.getEnd())) {
        }
        if (StringUtils.hasText(queryBO.getQuery().getKeyword())) {
        }
        return this.page(queryBO.getPage(), wrapper);
    }

    @Override
    public TreeVO<Long, String> listCommonSubSystemTree(Long id) {
        CommonSubSystemDO subSystemDO = this.getById(id);
        if (Objects.isNull(subSystemDO)) {
            return null;
        }
        TreeVO<Long, String> vo = new TreeVO(subSystemDO.getId(), subSystemDO.getSubSystemName());
        LambdaQueryWrapper<CommonDeviceDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CommonDeviceDO::getSubSystemId, id).orderByAsc(CommonDeviceDO::getSort);
        List<CommonDeviceDO> list = deviceServiceExt.list(wrapper);
        if (CollectionUtils.isEmpty(list)) {
            return vo;
        }
        List<TreeVO<Long, String>> children = list.stream().map(item -> new TreeVO<>(item.getId(), item.getDeviceName())).collect(Collectors.toList());
        vo.setChildren(children);
        return vo;
    }
}

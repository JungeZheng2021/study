package com.aimsphm.nuclear.ext.service.impl;

import com.aimsphm.nuclear.common.entity.CommonSetDO;
import com.aimsphm.nuclear.common.entity.CommonSiteDO;
import com.aimsphm.nuclear.common.entity.CommonSystemDO;
import com.aimsphm.nuclear.common.entity.vo.TreeVO;
import com.aimsphm.nuclear.ext.service.CommonSetServiceExt;
import com.aimsphm.nuclear.ext.service.CommonSiteServiceExt;
import com.aimsphm.nuclear.common.service.impl.CommonSiteServiceImpl;
import com.aimsphm.nuclear.ext.service.CommonSystemServiceExt;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Package: com.aimsphm.nuclear.ext.service.impl
 * @Description: <电厂信息扩展服务实现类>
 * @Author: MILLA
 * @CreateDate: 2020-11-17
 * @UpdateUser: MILLA
 * @UpdateDate: 2020-11-17
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Service
@ConditionalOnProperty(prefix = "spring.config", name = "enableServiceExtImpl", havingValue = "true")
public class CommonSiteServiceExtImpl extends CommonSiteServiceImpl implements CommonSiteServiceExt {
    @Autowired
    private CommonSetServiceExt setServiceExt;

    @Override
    public TreeVO<Long, String> listCommonSetTree(Long id) {
        CommonSiteDO siteDO = this.getById(id);
        if (Objects.isNull(siteDO)) {
            return null;
        }
        TreeVO<Long, String> vo = new TreeVO(siteDO.getId(), siteDO.getSiteName());
        LambdaQueryWrapper<CommonSetDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CommonSetDO::getSiteId, id).orderByAsc(CommonSetDO::getImportance);
        List<CommonSetDO> list = setServiceExt.list(wrapper);
        if (CollectionUtils.isEmpty(list)) {
            return vo;
        }
        List<TreeVO<Long, String>> children = list.stream().map(item -> setServiceExt.listCommonSetTree(item.getId())).collect(Collectors.toList());
        vo.setChildren(children);
        return vo;
    }
}

package com.aimsphm.nuclear.ext.service.impl;

import com.aimsphm.nuclear.common.entity.CommonSubSystemDO;
import com.aimsphm.nuclear.common.entity.CommonSystemDO;
import com.aimsphm.nuclear.common.entity.vo.TreeVO;
import com.aimsphm.nuclear.ext.service.CommonSubSystemServiceExt;
import com.aimsphm.nuclear.ext.service.CommonSystemServiceExt;
import com.aimsphm.nuclear.common.service.impl.CommonSystemServiceImpl;
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
 * @Description: <系统信息扩展服务实现类>
 * @Author: MILLA
 * @CreateDate: 2020-11-17
 * @UpdateUser: MILLA
 * @UpdateDate: 2020-11-17
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Service
@ConditionalOnProperty(prefix = "spring.config", name = "enableServiceExtImpl", havingValue = "true")
public class CommonSystemServiceExtImpl extends CommonSystemServiceImpl implements CommonSystemServiceExt {

    @Autowired
    private CommonSubSystemServiceExt subSystemServiceExt;

    @Override
    public TreeVO<Long, String> listCommonSystemTree(Long id) {
        CommonSystemDO systemDO = this.getById(id);
        if (Objects.isNull(systemDO)) {
            return null;
        }
        TreeVO<Long, String> vo = new TreeVO(systemDO.getId(), systemDO.getSystemName());
        LambdaQueryWrapper<CommonSubSystemDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CommonSubSystemDO::getSystemId, id).orderByAsc(CommonSubSystemDO::getImportance);
        List<CommonSubSystemDO> list = subSystemServiceExt.list(wrapper);
        if (CollectionUtils.isEmpty(list)) {
            return vo;
        }
        List<TreeVO<Long, String>> children = list.stream().map(item -> subSystemServiceExt.listCommonSubSystemTree(item.getId())).collect(Collectors.toList());
        vo.setChildren(children);
        return vo;
    }
}

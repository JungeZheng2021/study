package com.aimsphm.nuclear.ext.service.impl;

import com.aimsphm.nuclear.common.entity.CommonDeviceDO;
import com.aimsphm.nuclear.common.entity.CommonSubSystemDO;
import com.aimsphm.nuclear.common.entity.vo.TreeVO;
import com.aimsphm.nuclear.ext.service.CommonDeviceServiceExt;
import com.aimsphm.nuclear.ext.service.CommonSubSystemServiceExt;
import com.aimsphm.nuclear.common.service.impl.CommonSubSystemServiceImpl;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
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
    public TreeVO<Long, String> listCommonSubSystemTree(Long id) {
        CommonSubSystemDO subSystemDO = this.getById(id);
        if (Objects.isNull(subSystemDO)) {
            return null;
        }
        TreeVO<Long, String> vo = new TreeVO(subSystemDO.getId(), subSystemDO.getSubSystemName());
        LambdaQueryWrapper<CommonDeviceDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CommonDeviceDO::getSubSystemId, id).orderByAsc(CommonDeviceDO::getImportance);
        List<CommonDeviceDO> list = deviceServiceExt.list(wrapper);
        if (CollectionUtils.isEmpty(list)) {
            return vo;
        }
        List<TreeVO<Long, String>> children = list.stream().map(item -> new TreeVO<>(item.getId(), item.getDeviceName())).collect(Collectors.toList());
        vo.setChildren(children);
        return vo;
    }
}

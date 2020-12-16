package com.aimsphm.nuclear.ext.service.impl;

import com.aimsphm.nuclear.common.entity.CommonDeviceDO;
import com.aimsphm.nuclear.common.entity.CommonDeviceDetailsDO;
import com.aimsphm.nuclear.common.entity.CommonSubSystemDO;
import com.aimsphm.nuclear.common.entity.bo.CommonQueryBO;
import com.aimsphm.nuclear.common.exception.CustomMessageException;
import com.aimsphm.nuclear.common.mapper.CommonDeviceDetailsMapper;
import com.aimsphm.nuclear.common.service.impl.CommonDeviceDetailsServiceImpl;
import com.aimsphm.nuclear.ext.service.CommonDeviceDetailsServiceExt;
import com.aimsphm.nuclear.ext.service.CommonDeviceServiceExt;
import com.aimsphm.nuclear.ext.service.CommonSubSystemServiceExt;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @Package: com.aimsphm.nuclear.ext.service.impl
 * @Description: <设备详细信息扩展服务实现类>
 * @Author: MILLA
 * @CreateDate: 2020-11-17
 * @UpdateUser: MILLA
 * @UpdateDate: 2020-11-17
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Service
@ConditionalOnProperty(prefix = "spring.config", name = "enableServiceExtImpl", havingValue = "true")
public class CommonDeviceDetailsServiceExtImpl extends CommonDeviceDetailsServiceImpl<CommonDeviceDetailsMapper, CommonDeviceDetailsDO> implements CommonDeviceDetailsServiceExt {
    @Autowired
    private CommonDeviceServiceExt deviceServiceExt;
    @Autowired
    private CommonSubSystemServiceExt subSystemServiceExt;

    @Override
    public List<CommonDeviceDetailsDO> listDetailByConditions(CommonQueryBO query) {
        LambdaQueryWrapper<CommonDeviceDetailsDO> wrapper = initWrapper(query);
        if (Objects.nonNull(query.getVisible())) {
            wrapper.last("and visible=" + query.getVisible());
        }
        return this.list(wrapper);
    }

    /**
     * 组装查询条件
     * 目前能支持到系统下公共设别明细
     *
     * @param query 查询条件
     * @return
     */
    private LambdaQueryWrapper<CommonDeviceDetailsDO> initWrapper(CommonQueryBO query) {
        LambdaQueryWrapper<CommonDeviceDetailsDO> wrapper = Wrappers.lambdaQuery(CommonDeviceDetailsDO.class);
        if (Objects.isNull(query.getSystemId()) && Objects.isNull(query.getSubSystemId()) && Objects.isNull(query.getDeviceId()) && Objects.isNull(query.getVisible())) {
            throw new CustomMessageException("参数不全");
        }
        if (Objects.nonNull(query.getSystemId())) {
            wrapper.eq(CommonDeviceDetailsDO::getSystemId, query.getSystemId());
            return wrapper;
        }
        if (Objects.nonNull(query.getSubSystemId())) {
            CommonSubSystemDO subSystem = subSystemServiceExt.getById(query.getSubSystemId());
            if (Objects.isNull(subSystem)) {
                throw new CustomMessageException("该子系统下没有数据");
            }
            wrapper.and(w -> w.eq(CommonDeviceDetailsDO::getSubSystemId, subSystem.getId())
                    .or().eq(CommonDeviceDetailsDO::getSystemId, subSystem.getSystemId()).isNull(CommonDeviceDetailsDO::getSubSystemId));
            return wrapper;
        }
        CommonDeviceDO device = deviceServiceExt.getById(query.getDeviceId());
        if (Objects.isNull(device)) {
            throw new CustomMessageException("该设备下没有数据");
        }
        wrapper.and(w -> w.eq(CommonDeviceDetailsDO::getDeviceId, device.getId())
                .or().eq(CommonDeviceDetailsDO::getSubSystemId, device.getSubSystemId()).isNull(CommonDeviceDetailsDO::getDeviceId)
                .or().eq(CommonDeviceDetailsDO::getSystemId, device.getSystemId()).isNull(CommonDeviceDetailsDO::getDeviceId).isNull(CommonDeviceDetailsDO::getSubSystemId)
        );
        return wrapper;
    }
}

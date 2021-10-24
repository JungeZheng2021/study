package com.aimsphm.nuclear.common.service.impl;

import com.aimsphm.nuclear.common.entity.CommonDeviceDO;
import com.aimsphm.nuclear.common.entity.CommonDeviceDetailsDO;
import com.aimsphm.nuclear.common.entity.CommonMeasurePointDO;
import com.aimsphm.nuclear.common.entity.CommonSubSystemDO;
import com.aimsphm.nuclear.common.entity.bo.CommonQueryBO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.exception.CustomMessageException;
import com.aimsphm.nuclear.common.mapper.CommonDeviceDetailsMapper;
import com.aimsphm.nuclear.common.service.CommonDeviceDetailsService;
import com.aimsphm.nuclear.common.service.CommonDeviceService;
import com.aimsphm.nuclear.common.service.CommonMeasurePointService;
import com.aimsphm.nuclear.common.service.CommonSubSystemService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.CaseFormat;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static com.aimsphm.nuclear.common.constant.RedisKeyConstant.REDIS_KEY_OIL_SETTINGS;

/**
 * <p>
 * 功能描述:设备详细信息扩展服务实现类
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020-11-17 14:30
 */
@Service
@ConditionalOnProperty(prefix = "spring.config", name = "enableServiceExtImpl", havingValue = "true")
public class CommonDeviceDetailsServiceImpl extends ServiceImpl<CommonDeviceDetailsMapper, CommonDeviceDetailsDO> implements CommonDeviceDetailsService {

    private static final String START_TIME = "start_time";
    @Resource
    private CommonDeviceService deviceServiceExt;
    @Resource
    private CommonSubSystemService subSystemServiceExt;
    @Resource
    private CommonMeasurePointService pointService;


    @Override
    public Page<CommonDeviceDetailsDO> listCommonDeviceDetailsByPageWithParams(QueryBO<CommonDeviceDetailsDO> queryBO) {
        if (Objects.nonNull(queryBO.getPage().getOrders()) && !queryBO.getPage().getOrders().isEmpty()) {
            queryBO.getPage().getOrders().forEach(item -> item.setColumn(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, item.getColumn())));
        }
        LambdaQueryWrapper<CommonDeviceDetailsDO> wrapper = queryBO.lambdaQuery();
        return this.page(queryBO.getPage(), wrapper);
    }

    @Override
    public List<CommonDeviceDetailsDO> listDetailByConditions(CommonQueryBO query) {
        LambdaQueryWrapper<CommonDeviceDetailsDO> wrapper = initWrapper(query);
        wrapper.last("and visible=" + (Objects.isNull(query.getVisible()) ? 1 : query.getVisible()));
        return this.list(wrapper);
    }

    @Override
    public void updateLastStartTime(Long deviceId) {
        LambdaUpdateWrapper<CommonDeviceDetailsDO> update = Wrappers.lambdaUpdate(CommonDeviceDetailsDO.class);
        update.eq(CommonDeviceDetailsDO::getDeviceId, deviceId).eq(CommonDeviceDetailsDO::getVisible, false).eq(CommonDeviceDetailsDO::getFieldNameEn, START_TIME)
                .set(CommonDeviceDetailsDO::getFieldValue, System.currentTimeMillis());
        this.update(update);
    }

    @Override
    @Cacheable(value = REDIS_KEY_OIL_SETTINGS, key = "#fieldName")
    public Map<String, CommonDeviceDetailsDO> listDetailByFilename(String fieldName) {
        Map<String, CommonDeviceDetailsDO> retVal = new HashMap<>(16);
        LambdaQueryWrapper<CommonDeviceDetailsDO> wrapper = Wrappers.lambdaQuery(CommonDeviceDetailsDO.class);
        wrapper.eq(CommonDeviceDetailsDO::getFieldNameEn, fieldName);
        List<CommonDeviceDetailsDO> list = this.list(wrapper);
        if (CollectionUtils.isEmpty(list)) {
            return retVal;
        }
        Map<Long, CommonDeviceDetailsDO> collect = list.stream().filter(x -> Objects.nonNull(x.getDeviceId())).collect(Collectors.toMap(CommonDeviceDetailsDO::getDeviceId, x -> x, (a, b) -> a));
        if (MapUtils.isEmpty(collect)) {
            return retVal;
        }
        Set<Long> deviceIds = collect.keySet();
        LambdaQueryWrapper<CommonMeasurePointDO> pointWrapper = Wrappers.lambdaQuery(CommonMeasurePointDO.class);
        pointWrapper.select(CommonMeasurePointDO::getDeviceId, CommonMeasurePointDO::getSensorCode).in(CommonMeasurePointDO::getDeviceId, deviceIds).
                groupBy(CommonMeasurePointDO::getSensorCode);
        List<CommonMeasurePointDO> points = pointService.list(pointWrapper);
        if (CollectionUtils.isEmpty(points)) {
            return retVal;
        }
        Map<Long, List<CommonMeasurePointDO>> map = points.stream().collect(Collectors.groupingBy(CommonMeasurePointDO::getDeviceId));
        for (Map.Entry<Long, CommonDeviceDetailsDO> x : collect.entrySet()) {
            Long deviceId = x.getKey();
            CommonDeviceDetailsDO detailsDO = x.getValue();
            List<CommonMeasurePointDO> pointDOS = map.get(deviceId);
            if (CollectionUtils.isEmpty(pointDOS)) {
                continue;
            }
            pointDOS.forEach(m -> retVal.put(m.getSensorCode(), detailsDO));
        }
        return retVal;
    }

    /**
     * 组装查询条件
     * 目前能支持到系统下公共设别明细
     *
     * @param query 查询条件
     * @return 封装后的条件
     */
    private LambdaQueryWrapper<CommonDeviceDetailsDO> initWrapper(CommonQueryBO query) {
        LambdaQueryWrapper<CommonDeviceDetailsDO> wrapper = Wrappers.lambdaQuery(CommonDeviceDetailsDO.class);
        boolean needAll = Objects.isNull(query.getSystemId()) && Objects.isNull(query.getSubSystemId()) && Objects.isNull(query.getDeviceId()) && Objects.isNull(query.getVisible());
        if (needAll) {
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

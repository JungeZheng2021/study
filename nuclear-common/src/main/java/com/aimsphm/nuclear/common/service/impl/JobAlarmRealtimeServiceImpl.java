package com.aimsphm.nuclear.common.service.impl;

import com.aimsphm.nuclear.common.entity.CommonMeasurePointDO;
import com.aimsphm.nuclear.common.entity.JobAlarmRealtimeDO;
import com.aimsphm.nuclear.common.entity.bo.CommonQueryBO;
import com.aimsphm.nuclear.common.entity.bo.ConditionsQueryBO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.mapper.JobAlarmRealtimeMapper;
import com.aimsphm.nuclear.common.service.CommonMeasurePointService;
import com.aimsphm.nuclear.common.service.JobAlarmRealtimeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.CaseFormat;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.hbase.thirdparty.com.google.common.collect.Maps;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 功能描述:服务实现类
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020-12-24 14:30
 */
@Service
@ConditionalOnProperty(prefix = "spring.config", name = "enableServiceExtImpl", havingValue = "true")
public class JobAlarmRealtimeServiceImpl extends ServiceImpl<JobAlarmRealtimeMapper, JobAlarmRealtimeDO> implements JobAlarmRealtimeService {

    @Resource
    private CommonMeasurePointService pointService;

    @Override
    public Page<JobAlarmRealtimeDO> listJobAlarmRealtimeByPageWithParams(QueryBO<JobAlarmRealtimeDO> queryBO) {
        if (Objects.nonNull(queryBO.getPage().getOrders()) && !queryBO.getPage().getOrders().isEmpty()) {
            queryBO.getPage().getOrders().forEach(item -> item.setColumn(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, item.getColumn())));
        }
        return this.page(queryBO.getPage(), customerConditions(queryBO));
    }

    @Override
    public List<JobAlarmRealtimeDO> listRealTime(String pointId, Long start, Long end, Long modelId) {
        LambdaQueryWrapper<JobAlarmRealtimeDO> wrapper = Wrappers.lambdaQuery(JobAlarmRealtimeDO.class);
        wrapper.eq(JobAlarmRealtimeDO::getPointId, pointId)
//                .eq(JobAlarmRealtimeDO::getAlarmType, AlarmTypeEnum.ALGORITHM.getValue())
                .ge(JobAlarmRealtimeDO::getGmtAlarmTime, new Date(start)).le(JobAlarmRealtimeDO::getGmtAlarmTime, new Date(end));
        if (Objects.nonNull(modelId) && modelId != -1L) {
            wrapper.eq(JobAlarmRealtimeDO::getModelId, modelId);
        }
        return this.list(wrapper);
    }

    /**
     * 拼装查询条件
     *
     * @param queryBO
     * @return
     */
    private LambdaQueryWrapper<JobAlarmRealtimeDO> customerConditions(QueryBO<JobAlarmRealtimeDO> queryBO) {
        LambdaQueryWrapper<JobAlarmRealtimeDO> wrapper = queryBO.lambdaQuery();
        ConditionsQueryBO query = queryBO.getQuery();
        if (Objects.nonNull(query.getStart()) && Objects.nonNull(query.getEnd())) {
            wrapper.between(JobAlarmRealtimeDO::getGmtAlarmTime, new Date(query.getStart()), new Date(query.getEnd()));
        }
        wrapper.orderByDesc(JobAlarmRealtimeDO::getGmtAlarmTime);
        return wrapper;
    }

    @Override
    public List<JobAlarmRealtimeDO> listJobAlarmRealtimeWithParams(QueryBO queryBO) {
        return this.list(customerConditions(queryBO));
    }

    @Override
    public List<CommonMeasurePointDO> listJobAlarmRealtimeByPageWithParamsDistinct(QueryBO queryBO) {
        LambdaQueryWrapper<JobAlarmRealtimeDO> wrapper = customerConditions(queryBO);
        wrapper.groupBy(JobAlarmRealtimeDO::getPointId);
        List<JobAlarmRealtimeDO> list = this.list(wrapper);
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        return pointService.listPointAliasAndName(list.stream().map(JobAlarmRealtimeDO::getPointId).collect(Collectors.toList()), new CommonQueryBO());
    }

    @Override
    public Map<String, List<Long>> listJobAlarmRealtimeWithParams(QueryBO<JobAlarmRealtimeDO> queryBO, List<String> pointIds) {
        JobAlarmRealtimeDO entity = queryBO.getEntity();
        if (Objects.nonNull(entity.getPointId())) {
            pointIds.add(entity.getPointId());
        }
        LambdaQueryWrapper<JobAlarmRealtimeDO> wrapper = customerConditions(queryBO);
        wrapper.isNotNull(JobAlarmRealtimeDO::getGmtAlarmTime);
        wrapper.in(JobAlarmRealtimeDO::getPointId, pointIds);
        List<JobAlarmRealtimeDO> list = this.list(wrapper);
        if (CollectionUtils.isEmpty(list)) {
            return Maps.newHashMap();
        }
        return list.stream().collect(Collectors.groupingBy(JobAlarmRealtimeDO::getPointId, Collectors.mapping(m -> m.getGmtAlarmTime().getTime(), Collectors.toList())));
    }
}

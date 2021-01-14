package com.aimsphm.nuclear.common.service.impl;

import com.aimsphm.nuclear.common.entity.*;
import com.aimsphm.nuclear.common.entity.bo.AlarmQueryBO;
import com.aimsphm.nuclear.common.entity.bo.CommonQueryBO;
import com.aimsphm.nuclear.common.entity.bo.JobAlarmEventBO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.exception.CustomMessageException;
import com.aimsphm.nuclear.common.mapper.JobAlarmEventMapper;
import com.aimsphm.nuclear.common.service.*;
import com.aimsphm.nuclear.common.util.DateUtils;
import com.aimsphm.nuclear.common.util.EasyExcelUtils;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.CaseFormat;
import org.assertj.core.util.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @Package: com.aimsphm.nuclear.ext.service.impl
 * @Description: <报警事件扩展服务实现类>
 * @Author: MILLA
 * @CreateDate: 2020-12-05
 * @UpdateUser: MILLA
 * @UpdateDate: 2020-12-05
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Service
@ConditionalOnProperty(prefix = "spring.config", name = "enableServiceExtImpl", havingValue = "true")
public class JobAlarmEventServiceImpl extends ServiceImpl<JobAlarmEventMapper, JobAlarmEventDO> implements JobAlarmEventService {
    @Resource
    private CommonDeviceService deviceService;
    @Resource
    private CommonMeasurePointService pointsService;

    @Resource
    private AlgorithmDeviceModelService deviceModelService;
    @Resource
    private AlgorithmModelPointService pointService;
    @Resource
    private AlgorithmModelService modelService;

    @Override
    public Page<JobAlarmEventDO> listJobAlarmEventByPageWithParams(QueryBO<JobAlarmEventDO> queryBO) {
        Wrapper<JobAlarmEventDO> wrapper = initialWrapper(queryBO);
        return this.page(queryBO.getPage(), wrapper);
    }

    @Override
    public void listJobAlarmEventWithParams(QueryBO queryBO, HttpServletResponse response) {
        Wrapper<JobAlarmEventDO> wrapper = initialWrapper(queryBO);
        List<JobAlarmEventDO> list = this.list(wrapper);
        try {
            if (CollectionUtils.isEmpty(list)) {
                throw new CustomMessageException("has no data");
            }
            AtomicInteger index = new AtomicInteger(1);
            String time = DateUtils.formatCurrentDateTime();
            List<JobAlarmEventBO> collect = list.stream().map(x -> {
                JobAlarmEventBO eventBO = new JobAlarmEventBO();
                BeanUtils.copyProperties(x, eventBO);
                eventBO.setId(Long.valueOf(index.getAndIncrement()));
                return eventBO;
            }).collect(Collectors.toList());
            EasyExcelUtils.Write2Website(response, collect, JobAlarmEventBO.class, null, String.format("报警事件-%s", time));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Wrapper<JobAlarmEventDO> initialWrapper(QueryBO<JobAlarmEventDO> queryBO) {
        JobAlarmEventDO entity = queryBO.getEntity();
        Integer eventStatus = entity.getAlarmStatus();
        if (Objects.nonNull(queryBO.getPage()) && Objects.nonNull(queryBO.getPage().getOrders()) && !queryBO.getPage().getOrders().isEmpty()) {
            queryBO.getPage().getOrders().stream().forEach(item -> item.setColumn(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, item.getColumn())));
        }
        LambdaQueryWrapper<JobAlarmEventDO> wrapper = queryBO.lambdaQuery();
        AlarmQueryBO query = (AlarmQueryBO) queryBO.getQuery();
        if (Objects.nonNull(query.getEnd()) && Objects.nonNull(query.getEnd())) {
            //last>start && first<end
            wrapper.ge(JobAlarmEventDO::getGmtLastAlarm, new Date(query.getStart())).le(JobAlarmEventDO::getGmtFirstAlarm, new Date(query.getEnd()));
        }
        if (StringUtils.hasText(query.getKeyword())) {
            wrapper.like(JobAlarmEventDO::getRemark, queryBO.getQuery().getKeyword());
        }
        if (Objects.nonNull(eventStatus)) {
            if (CollectionUtils.isEmpty(query.getAlarmStatusList())) {
                query.setAlarmStatusList(Lists.newArrayList(eventStatus));
            } else {
                query.getAlarmStatusList().add(eventStatus);
            }
            entity.setAlarmStatus(null);
        }
        if (!CollectionUtils.isEmpty(query.getAlarmStatusList())) {
            wrapper.in(JobAlarmEventDO::getAlarmStatus, query.getAlarmStatusList());
        }
        String pointIds = entity.getPointIds();
        if (StringUtils.hasText(pointIds)) {
            entity.setPointIds(null);
            wrapper.apply("FIND_IN_SET('" + pointIds + "',point_ids) > 0");
        }
        wrapper.orderByDesc(JobAlarmEventDO::getId);
        return wrapper;
    }


    @Override
    public List<CommonMeasurePointDO> listPointByConditions(CommonQueryBO queryBO) {
        if (Objects.nonNull(queryBO.getModelId())) {
            return listPointByModelId(queryBO.getModelId());
        }
        if (Objects.nonNull(queryBO.getDeviceId())) {
            return listPointByDeviceId(queryBO.getDeviceId());
        }
        if (Objects.nonNull(queryBO.getSubSystemId())) {
            LambdaQueryWrapper<CommonDeviceDO> wrapper = Wrappers.lambdaQuery(CommonDeviceDO.class);
            wrapper.eq(CommonDeviceDO::getSubSystemId, queryBO.getSubSystemId());
            List<CommonDeviceDO> list = deviceService.list(wrapper);
            if (CollectionUtils.isEmpty(list)) {
                return Lists.newArrayList();
            }
            List<CommonMeasurePointDO> pointList = Lists.newArrayList();
            list.stream().forEach(x -> {
                pointList.addAll(listPointByDeviceId(x.getId()));
            });
            return pointList;
        }
        return Lists.newArrayList();
    }

    private List<CommonMeasurePointDO> listPointByModelId(Long modelId) {
        LambdaQueryWrapper<AlgorithmModelPointDO> pointWrapper = Wrappers.lambdaQuery(AlgorithmModelPointDO.class);
        pointWrapper.eq(AlgorithmModelPointDO::getModelId, modelId);
        List<AlgorithmModelPointDO> pointList = pointService.list(pointWrapper);
        if (CollectionUtils.isEmpty(pointList)) {
            return Lists.newArrayList();
        }
        LambdaQueryWrapper<CommonMeasurePointDO> query = Wrappers.lambdaQuery(CommonMeasurePointDO.class);
        query.select(CommonMeasurePointDO::getPointId, CommonMeasurePointDO::getPointName);
        query.in(CommonMeasurePointDO::getId, pointList.stream().map(x -> x.getPointId()).collect(Collectors.toSet()));
        return pointsService.list(query);
    }


    private List<CommonMeasurePointDO> listPointByDeviceId(Long deviceId) {
        LambdaQueryWrapper<AlgorithmDeviceModelDO> wrapper = Wrappers.lambdaQuery(AlgorithmDeviceModelDO.class);
        wrapper.eq(AlgorithmDeviceModelDO::getDeviceId, deviceId);
        List<AlgorithmDeviceModelDO> deviceModelDOS = deviceModelService.list(wrapper);
        if (CollectionUtils.isEmpty(deviceModelDOS)) {
            return Lists.newArrayList();
        }
        LambdaQueryWrapper<AlgorithmModelDO> modelWrapper = Wrappers.lambdaQuery(AlgorithmModelDO.class);
        modelWrapper.in(AlgorithmModelDO::getDeviceModelId, deviceModelDOS.stream().map(x -> x.getId()).collect(Collectors.toSet()));
        List<AlgorithmModelDO> modelList = modelService.list(modelWrapper);
        if (CollectionUtils.isEmpty(modelList)) {
            return Lists.newArrayList();
        }
        LambdaQueryWrapper<AlgorithmModelPointDO> pointWrapper = Wrappers.lambdaQuery(AlgorithmModelPointDO.class);
        pointWrapper.in(AlgorithmModelPointDO::getModelId, modelList.stream().map(x -> x.getId()).collect(Collectors.toSet()));
        List<AlgorithmModelPointDO> pointList = pointService.list(pointWrapper);
        if (CollectionUtils.isEmpty(modelList)) {
            return Lists.newArrayList();
        }
        LambdaQueryWrapper<CommonMeasurePointDO> query = Wrappers.lambdaQuery(CommonMeasurePointDO.class);
        query.select(CommonMeasurePointDO::getPointId, CommonMeasurePointDO::getPointName);
        query.in(CommonMeasurePointDO::getId, pointList.stream().map(x -> x.getPointId()).collect(Collectors.toSet()));
        return pointsService.list(query);
    }
}

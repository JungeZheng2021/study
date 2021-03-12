package com.aimsphm.nuclear.common.service.impl;

import com.aimsphm.nuclear.common.entity.AlgorithmModelPointDO;
import com.aimsphm.nuclear.common.entity.CommonMeasurePointDO;
import com.aimsphm.nuclear.common.entity.JobAlarmEventDO;
import com.aimsphm.nuclear.common.entity.bo.AlarmQueryBO;
import com.aimsphm.nuclear.common.entity.bo.CommonQueryBO;
import com.aimsphm.nuclear.common.entity.bo.JobAlarmEventBO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.enums.EventStatusEnum;
import com.aimsphm.nuclear.common.exception.CustomMessageException;
import com.aimsphm.nuclear.common.mapper.JobAlarmEventMapper;
import com.aimsphm.nuclear.common.service.AlgorithmModelPointService;
import com.aimsphm.nuclear.common.service.CommonMeasurePointService;
import com.aimsphm.nuclear.common.service.JobAlarmEventService;
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
    private CommonMeasurePointService mPointService;
    @Resource
    private AlgorithmModelPointService pointService;

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

    @Override
    public Long getNewestEventIdByDeviceId(Long deviceId) {
        LambdaQueryWrapper<JobAlarmEventDO> wrapper = Wrappers.lambdaQuery(JobAlarmEventDO.class);
        wrapper.eq(JobAlarmEventDO::getDeviceId, deviceId).eq(JobAlarmEventDO::getAlarmStatus, EventStatusEnum.IN_ACTIVITY.getValue()).orderByDesc(JobAlarmEventDO::getId).last("limit 1");
        JobAlarmEventDO one = this.getOne(wrapper);
        if (Objects.nonNull(one)) {
            return one.getId();
        }
        return null;
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
            query.getAlarmStatusList().add(eventStatus);
            entity.setAlarmStatus(null);
        }
        if (Objects.nonNull(entity.getAlarmLevel())) {
            query.getAlarmLevelList().add(entity.getAlarmLevel());
            entity.setAlarmLevel(null);
        }
        if (!CollectionUtils.isEmpty(query.getAlarmStatusList())) {
            wrapper.in(JobAlarmEventDO::getAlarmStatus, query.getAlarmStatusList());
        }
        if (!CollectionUtils.isEmpty(query.getAlarmLevelList())) {
            wrapper.in(JobAlarmEventDO::getAlarmLevel, query.getAlarmLevelList());
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
        LambdaQueryWrapper<AlgorithmModelPointDO> mWrapper = Wrappers.lambdaQuery(AlgorithmModelPointDO.class);
        if (Objects.nonNull(queryBO.getModelId())) {
            mWrapper.eq(AlgorithmModelPointDO::getModelId, queryBO.getModelId());
        }
        if (Objects.nonNull(queryBO.getDeviceId())) {
            mWrapper.eq(AlgorithmModelPointDO::getDeviceId, queryBO.getDeviceId());
        }
        if (Objects.nonNull(queryBO.getSubSystemId())) {
            mWrapper.eq(AlgorithmModelPointDO::getSubSystemId, queryBO.getSubSystemId());
        }
        List<AlgorithmModelPointDO> list = pointService.list(mWrapper);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return mPointService.listByIds(list.stream().map(x -> x.getPointId()).collect(Collectors.toSet()));
    }
}

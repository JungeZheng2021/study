package com.aimsphm.nuclear.common.service.impl;

import com.aimsphm.nuclear.common.entity.JobAlarmThresholdDO;
import com.aimsphm.nuclear.common.entity.bo.AlarmQueryBO;
import com.aimsphm.nuclear.common.entity.bo.JobAlarmEventBO;
import com.aimsphm.nuclear.common.entity.bo.JobAlarmThresholdBO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.entity.vo.MeasurePointVO;
import com.aimsphm.nuclear.common.exception.CustomMessageException;
import com.aimsphm.nuclear.common.mapper.JobAlarmThresholdMapper;
import com.aimsphm.nuclear.common.service.JobAlarmThresholdService;
import com.aimsphm.nuclear.common.util.BigDecimalUtils;
import com.aimsphm.nuclear.common.util.DateUtils;
import com.aimsphm.nuclear.common.util.EasyExcelUtils;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.CaseFormat;
import org.assertj.core.util.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @Package: com.aimsphm.nuclear.common.service.impl
 * @Description: <阈值报警信息服务实现类>
 * @Author: MILLA
 * @CreateDate: 2021-01-04
 * @UpdateUser: MILLA
 * @UpdateDate: 2021-01-04
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Service
@ConditionalOnProperty(prefix = "spring.config", name = "enableServiceExtImpl", havingValue = "true")
public class JobAlarmThresholdServiceImpl extends ServiceImpl<JobAlarmThresholdMapper, JobAlarmThresholdDO> implements JobAlarmThresholdService {

    @Override
    public Page<JobAlarmThresholdDO> listJobAlarmThresholdByPageWithParams(QueryBO<JobAlarmThresholdDO> queryBO) {
        Wrapper<JobAlarmThresholdDO> wrapper = initialWrapper(queryBO);
        return this.page(queryBO.getPage(), wrapper);
    }

    private Wrapper<JobAlarmThresholdDO> initialWrapper(QueryBO<JobAlarmThresholdDO> queryBO) {
        JobAlarmThresholdDO entity = queryBO.getEntity();
        Integer eventStatus = entity.getAlarmStatus();
        if (Objects.nonNull(queryBO.getPage()) && Objects.nonNull(queryBO.getPage().getOrders()) && !queryBO.getPage().getOrders().isEmpty()) {
            queryBO.getPage().getOrders().stream().forEach(item -> item.setColumn(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, item.getColumn())));
        }
        LambdaQueryWrapper<JobAlarmThresholdDO> wrapper = queryBO.lambdaQuery();
        AlarmQueryBO query = (AlarmQueryBO) queryBO.getQuery();
        if (Objects.nonNull(query.getEnd()) && Objects.nonNull(query.getEnd())) {
            wrapper.ge(JobAlarmThresholdDO::getGmtLastAlarm, new Date(query.getStart())).le(JobAlarmThresholdDO::getGmtLastAlarm, new Date(query.getEnd()));
        }
        if (Objects.nonNull(eventStatus)) {
            if (CollectionUtils.isEmpty(query.getAlarmStatusList())) {
                query.setAlarmStatusList(Lists.newArrayList(eventStatus));
            } else {
                query.getAlarmStatusList().add(eventStatus);
            }
            entity.setAlarmStatus(null);
        }
        if (!CollectionUtils.isEmpty(query.getOperateStatusList())) {
            wrapper.in(JobAlarmThresholdDO::getOperateStatus, query.getOperateStatusList());
        }
        return wrapper;
    }

    @Async
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdateThresholdAlarmList(List<MeasurePointVO> vos) {
        List<MeasurePointVO> pointList = vos.stream().filter(x -> Objects.nonNull(x.getPointId()) && Objects.nonNull(x.getAlarmLevel())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(pointList)) {
            return;
        }
        //将已经消失的阈值报警更新状态
        updateThresholdAlarmList(pointList);
        //本次所有需要存储的报警点
        pointList.stream().forEach(item -> {
            LambdaQueryWrapper<JobAlarmThresholdDO> query = Wrappers.lambdaQuery(JobAlarmThresholdDO.class);
            query.eq(JobAlarmThresholdDO::getPointId, item.getPointId()).eq(JobAlarmThresholdDO::getAlarmLevel, item.getAlarmLevel())
                    .orderByDesc(JobAlarmThresholdDO::getId).last(" limit 1");
            JobAlarmThresholdDO one = this.getOne(query);
            //当前已经存在或者是已经被忽略了不保存
            boolean isNeedSave = Objects.nonNull(one) && (Objects.isNull(one.getGmtLastAlarm()) || one.getOperateStatus() == 3);
            if (isNeedSave) {
                return;
            }
//            重新保存
            JobAlarmThresholdDO alarmThreshold = new JobAlarmThresholdDO();
            BeanUtils.copyProperties(item, alarmThreshold);
            alarmThreshold.setAlarmReason(item.getStatusCause());
            //1：阈值 5： 算法
            alarmThreshold.setAlarmType(1);
            alarmThreshold.setGmtFirstAlarm(new Date());
            alarmThreshold.setPointId(item.getPointId());
            this.save(alarmThreshold);
        });
    }

    @Override
    public void listJobAlarmThresholdByPageWithParams(QueryBO queryBO, HttpServletResponse response) {
        Wrapper<JobAlarmThresholdDO> wrapper = initialWrapper(queryBO);
        List<JobAlarmThresholdDO> list = this.list(wrapper);
        try {
            if (CollectionUtils.isEmpty(list)) {
                throw new CustomMessageException("has no data");
            }
            AtomicInteger index = new AtomicInteger(1);
            String time = DateUtils.formatCurrentDateTime();
            List<JobAlarmThresholdBO> collect = list.stream().map(x -> {
                JobAlarmThresholdBO eventBO = new JobAlarmThresholdBO();
                BeanUtils.copyProperties(x, eventBO);
                eventBO.setId(Long.valueOf(index.getAndIncrement()));
                Double subtract = BigDecimalUtils.subtract(Objects.isNull(x.getGmtLastAlarm()) ? System.currentTimeMillis() : x.getGmtLastAlarm().getTime() / 1000, x.getGmtFirstAlarm().getTime() / 1000);
                eventBO.setDuration(subtract.toString());
                return eventBO;
            }).collect(Collectors.toList());
            EasyExcelUtils.Write2Website(response, collect, JobAlarmThresholdBO.class, null, String.format("阈值报警-%s", time));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 修改之前的报警为结束
     *
     * @param vos
     * @return
     */
    private void updateThresholdAlarmList(List<MeasurePointVO> vos) {
        List<String> collect = vos.stream().map(x -> x.getPointId()).collect(Collectors.toList());
        LambdaUpdateWrapper<JobAlarmThresholdDO> wrapper = Wrappers.lambdaUpdate(JobAlarmThresholdDO.class);
        //状态修改成已结束
        wrapper.set(JobAlarmThresholdDO::getAlarmStatus, 1);
        wrapper.set(JobAlarmThresholdDO::getGmtLastAlarm, new Date());
        wrapper.eq(JobAlarmThresholdDO::getAlarmStatus, 0).notIn(JobAlarmThresholdDO::getPointId, collect);
        wrapper.isNull(JobAlarmThresholdDO::getGmtLastAlarm);
        boolean update = this.update(wrapper);
        log.warn("更新状态：" + update);
    }
}

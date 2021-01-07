package com.aimsphm.nuclear.common.service.impl;

import com.aimsphm.nuclear.common.entity.JobAlarmRealtimeDO;
import com.aimsphm.nuclear.common.entity.bo.ConditionsQueryBO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.enums.AlarmTypeEnum;
import com.aimsphm.nuclear.common.mapper.JobAlarmRealtimeMapper;
import com.aimsphm.nuclear.common.service.JobAlarmRealtimeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.CaseFormat;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @Package: com.aimsphm.nuclear.common.service.impl
 * @Description: <服务实现类>
 * @Author: MILLA
 * @CreateDate: 2020-12-24
 * @UpdateUser: MILLA
 * @UpdateDate: 2020-12-24
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Service
@ConditionalOnProperty(prefix = "spring.config", name = "enableServiceExtImpl", havingValue = "true")
public class JobAlarmRealtimeServiceImpl extends ServiceImpl<JobAlarmRealtimeMapper, JobAlarmRealtimeDO> implements JobAlarmRealtimeService {

    @Override
    public Page<JobAlarmRealtimeDO> listJobAlarmRealtimeByPageWithParams(QueryBO<JobAlarmRealtimeDO> queryBO) {
        if (Objects.nonNull(queryBO.getPage().getOrders()) && !queryBO.getPage().getOrders().isEmpty()) {
            queryBO.getPage().getOrders().stream().forEach(item -> item.setColumn(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, item.getColumn())));
        }
        LambdaQueryWrapper<JobAlarmRealtimeDO> wrapper = queryBO.lambdaQuery();
        ConditionsQueryBO query = queryBO.getQuery();
        if (Objects.nonNull(query.getEnd()) && Objects.nonNull(query.getEnd())) {
        }
        if (StringUtils.hasText(queryBO.getQuery().getKeyword())) {
        }
        return this.page(queryBO.getPage(), wrapper);
    }

    @Override
    public List<JobAlarmRealtimeDO> listRealTime(String pointId, Long start, Long end, Long modelId) {
        LambdaQueryWrapper<JobAlarmRealtimeDO> wrapper = Wrappers.lambdaQuery(JobAlarmRealtimeDO.class);
        wrapper.eq(JobAlarmRealtimeDO::getPointId, pointId)
                .eq(JobAlarmRealtimeDO::getAlarmType, AlarmTypeEnum.ALGORITHM.getValue())
                .ge(JobAlarmRealtimeDO::getGmtAlarmTime, new Date(start)).le(JobAlarmRealtimeDO::getGmtAlarmTime, new Date(end));
        if (modelId != -1L) {
            wrapper.eq(JobAlarmRealtimeDO::getModelId, modelId);
        }
        return this.list(wrapper);
    }
}

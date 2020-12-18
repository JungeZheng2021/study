package com.aimsphm.nuclear.ext.service.impl;

import com.aimsphm.nuclear.common.entity.JobAlarmEventDO;
import com.aimsphm.nuclear.common.entity.bo.ConditionsQueryBO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.service.impl.JobAlarmEventServiceImpl;
import com.aimsphm.nuclear.ext.service.JobAlarmEventServiceExt;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.base.CaseFormat;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Objects;

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
public class JobAlarmEventServiceExtImpl extends JobAlarmEventServiceImpl implements JobAlarmEventServiceExt {

    @Override
    public Page<JobAlarmEventDO> listJobAlarmEventByPageWithParams(QueryBO<JobAlarmEventDO> queryBO) {
        if (Objects.nonNull(queryBO.getPage().getOrders()) && !queryBO.getPage().getOrders().isEmpty()) {
            queryBO.getPage().getOrders().stream().forEach(item -> item.setColumn(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, item.getColumn())));
        }
        LambdaQueryWrapper<JobAlarmEventDO> wrapper = queryBO.lambdaQuery();
        ConditionsQueryBO query = queryBO.getQuery();
        if (Objects.nonNull(query.getEnd()) && Objects.nonNull(query.getEnd())) {
            wrapper.ge(JobAlarmEventDO::getGmtLastAlarm, new Date(query.getStart())).le(JobAlarmEventDO::getGmtLastAlarm, new Date(query.getEnd()));
        }
        if (StringUtils.hasText(queryBO.getQuery().getKeyword())) {
            wrapper.like(JobAlarmEventDO::getAlarmContent, queryBO.getQuery().getKeyword());
        }
        return this.page(queryBO.getPage(), wrapper);
    }
}

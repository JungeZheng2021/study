package com.aimsphm.nuclear.common.service.impl;

import com.aimsphm.nuclear.common.entity.JobAlarmProcessRecordDO;
import com.aimsphm.nuclear.common.entity.bo.ConditionsQueryBO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.mapper.JobAlarmProcessRecordMapper;
import com.aimsphm.nuclear.common.service.JobAlarmProcessRecordService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.CaseFormat;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 功能描述:服务实现类
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2021-05-25 14:30
 */
@Service
@ConditionalOnProperty(prefix = "spring.config", name = "enableServiceExtImpl", havingValue = "true")
public class JobAlarmProcessRecordServiceImpl extends ServiceImpl<JobAlarmProcessRecordMapper, JobAlarmProcessRecordDO> implements JobAlarmProcessRecordService {

    @Override
    public Page<JobAlarmProcessRecordDO> listJobAlarmProcessRecordByPageWithParams(QueryBO<JobAlarmProcessRecordDO> queryBO) {
        if (Objects.nonNull(queryBO.getPage().getOrders()) && !queryBO.getPage().getOrders().isEmpty()) {
            queryBO.getPage().getOrders().forEach(item -> item.setColumn(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, item.getColumn())));
        }
        return this.page(queryBO.getPage(), customerConditions(queryBO));
    }

    /**
     * 拼装查询条件
     *
     * @param queryBO 条件
     * @return 封装的条件
     */
    private LambdaQueryWrapper<JobAlarmProcessRecordDO> customerConditions(QueryBO<JobAlarmProcessRecordDO> queryBO) {
        LambdaQueryWrapper<JobAlarmProcessRecordDO> wrapper = queryBO.lambdaQuery();
        ConditionsQueryBO query = queryBO.getQuery();
        if (Objects.nonNull(query.getStart()) && Objects.nonNull(query.getEnd())) {
            wrapper.between(JobAlarmProcessRecordDO::getGmtEventTime, new Date(query.getStart()), new Date(query.getEnd()));
        }
        return wrapper;
    }

    @Override
    public List<JobAlarmProcessRecordDO> listJobAlarmProcessRecordWithParams(QueryBO<JobAlarmProcessRecordDO> queryBO) {
        return this.list(customerConditions(queryBO));
    }
}

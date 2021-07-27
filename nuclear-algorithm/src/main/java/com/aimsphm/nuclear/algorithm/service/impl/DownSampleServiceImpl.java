package com.aimsphm.nuclear.algorithm.service.impl;

import com.aimsphm.nuclear.algorithm.service.DownSampleService;
import com.aimsphm.nuclear.common.entity.AlgorithmPrognosticFaultFeatureDO;
import com.aimsphm.nuclear.common.service.AlgorithmPrognosticFaultFeatureService;
import com.aimsphm.nuclear.common.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 功能描述:降采样实现类
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2021/07/27 14:00
 */
@Slf4j
@Service
public class DownSampleServiceImpl implements DownSampleService {
    @Resource
    private AlgorithmPrognosticFaultFeatureService prognosticFaultFeatureService;

    @Override
    public void execute() {
        List<AlgorithmPrognosticFaultFeatureDO> list = prognosticFaultFeatureService.list();
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        Set<String> pointIds = list.stream().filter(x -> Objects.nonNull(x.getComponentId())).map(x -> x.getSensorDesc()).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(pointIds)) {
            return;
        }
        //结束时间
        Long end = DateUtils.previousHourMaxValue();
        //开始时间
        Long start = DateUtils.previousHourMinValue();
    }
}

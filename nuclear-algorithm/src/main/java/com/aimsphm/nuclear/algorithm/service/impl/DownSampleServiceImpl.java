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

        //1.根据起止时间使用查询1个小时内的时时数据

        //2.根据algorithm_prognostic_fault_feature表中配置项，等间隔的取出查询出来数据中的几个

        //3.将数据插入biz_down_sample中，data(是个数组队列)的长度是algorithm_prognostic_fault_feature表配置中计算出来的个数，
        //   data数组有先进后出的特性：如果个数没有达到就一直添加进去，如果个数达到配置项计算的个数，就把最先进去的数据提出，然后在添加上最新的值
    }
}

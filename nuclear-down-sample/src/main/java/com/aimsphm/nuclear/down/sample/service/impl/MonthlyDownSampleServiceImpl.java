package com.aimsphm.nuclear.down.sample.service.impl;

import com.aimsphm.nuclear.common.entity.SparkDownSample;
import com.aimsphm.nuclear.common.entity.SparkDownSampleConfigDO;
import com.aimsphm.nuclear.common.entity.bo.HistoryQuerySingleWithFeatureBO;
import com.aimsphm.nuclear.common.entity.bo.TimeRangeQueryBO;
import com.aimsphm.nuclear.common.service.SparkDownSampleService;
import com.aimsphm.nuclear.down.sample.service.ExecuteDownSampleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 功能描述:
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2021/11/01 18:54
 */
@Slf4j
@Service("monthly")
public class MonthlyDownSampleServiceImpl implements ExecuteDownSampleService {

    @Resource
    private SparkDownSampleService downSampleService;

    @Override
    public void executeDownSample(List<SparkDownSampleConfigDO> list, TimeRangeQueryBO rangTime) {
        dataStore2ManyTable(log, list, rangTime);
    }

    @Override
    public List<List<Object>> queryDataByPoint(HistoryQuerySingleWithFeatureBO pointInfo) {
        return downSampleService.listDataFromDailyData(pointInfo);
    }


    @Override
    public void saveDownSampleResult(SparkDownSample sample) {
        downSampleService.save(sample);
    }

}

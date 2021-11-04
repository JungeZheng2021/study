package com.aimsphm.nuclear.down.sample.service.impl;

import com.aimsphm.nuclear.common.entity.SparkDownSample;
import com.aimsphm.nuclear.common.entity.SparkDownSampleConfigDO;
import com.aimsphm.nuclear.common.entity.bo.HistoryQuerySingleWithFeatureBO;
import com.aimsphm.nuclear.common.entity.bo.TimeRangeQueryBO;
import com.aimsphm.nuclear.common.enums.FrequencyEnum;
import com.aimsphm.nuclear.common.service.SparkDownSampleService;
import com.aimsphm.nuclear.common.util.DateUtils;
import com.aimsphm.nuclear.down.sample.entity.bo.ThreePointBO;
import com.aimsphm.nuclear.down.sample.service.DataQueryService;
import com.aimsphm.nuclear.down.sample.service.ExecuteDownSampleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * 功能描述:没小时执行的降采样任务
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2021/11/01 18:54
 */
@Slf4j
@Service("hourly")
public class HourlyDownSampleServiceImpl implements ExecuteDownSampleService {
    @Resource
    private DataQueryService dataQueryService;
    @Resource
    private SparkDownSampleService downSampleService;

    @Override
    public List<List<Object>> executeDownSample4HistoryData(SparkDownSampleConfigDO config, TimeRangeQueryBO rangTime, FrequencyEnum frequencyEnum) {
        return rawDataOperation4History(getHistoryQuerySingleWithFeatureBO(rangTime, config), config.getTargetNum());
    }


    @Override
    public void executeDownSample(List<SparkDownSampleConfigDO> list, TimeRangeQueryBO rangTime) {
        list.forEach(x -> {
            try {
                FrequencyEnum rate = FrequencyEnum.getByRate(x.getRate());
                if (Objects.isNull(rate)) {
                    return;
                }
                HistoryQuerySingleWithFeatureBO bo = getHistoryQuerySingleWithFeatureBO(rangTime, x);
                hourlyTask(bo, x.getTargetNum(), rate);
            } catch (Exception e) {
                log.error("lost point :{} ;cause:{}", x.getSensorCode(), e);
            }
        });
    }

    private HistoryQuerySingleWithFeatureBO getHistoryQuerySingleWithFeatureBO(TimeRangeQueryBO rangTime, SparkDownSampleConfigDO x) {
        HistoryQuerySingleWithFeatureBO bo = new HistoryQuerySingleWithFeatureBO();
        bo.setSensorCode(x.getSensorCode());
        bo.setFeature(x.getFeature());
        bo.setStart(rangTime.getStart());
        bo.setEnd(rangTime.getEnd());
        return bo;
    }

    public void hourlyTask(HistoryQuerySingleWithFeatureBO single, Integer targetNumber, FrequencyEnum rate) {
        if (single.getEnd() <= single.getStart()) {
            log.error("end is must greater than  start --> start:{},end:{}", single.getStart(), single.getEnd());
            return;
        }
        Map<TimeRangeQueryBO, ThreePointBO> data = initKeys(single.getStart(), single.getEnd(), targetNumber);
        rawDataOperation(single, data, rate);
    }

    @Override
    public List<List<Object>> queryDataByPoint(HistoryQuerySingleWithFeatureBO pointInfo) {
        List<List<Object>> lists = dataQueryService.listHoursData(pointInfo);
        if (CollectionUtils.isEmpty(lists)) {
            log.warn("hourly data is null :{},start:{} end:{}", pointInfo, DateUtils.format(pointInfo.getStart()), DateUtils.format(pointInfo.getEnd()));
        }
        return lists;
    }


    @Override
    public void saveDownSampleResult(SparkDownSample sample) {
        downSampleService.save(sample);
    }
}

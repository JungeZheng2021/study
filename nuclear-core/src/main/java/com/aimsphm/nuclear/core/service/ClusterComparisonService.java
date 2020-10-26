package com.aimsphm.nuclear.core.service;

import com.aimsphm.nuclear.core.entity.bo.DeviationPointBO;
import com.aimsphm.nuclear.core.entity.bo.DeviationStatisticBO;

import java.util.List;
import java.util.Map;
import com.aimsphm.nuclear.common.entity.MdSensor;

public interface ClusterComparisonService {

    public Map<String, List<DeviationPointBO>> genTrendMeanPoints(List<String> tags, long startTs, long endTs);

    public Map<String, List<DeviationPointBO>> genRelativeDeviationPoints(Map<String, List<DeviationPointBO>> originData,List<MdSensor> sortedSensorList);

    public Map<String, DeviationStatisticBO> genRelativeDeviationStatistics(Map<String, List<DeviationPointBO>> deviationData,List<MdSensor> sortedSensorList);
}

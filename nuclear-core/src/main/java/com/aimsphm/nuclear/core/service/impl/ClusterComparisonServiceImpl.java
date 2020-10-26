package com.aimsphm.nuclear.core.service.impl;

import com.aimsphm.nuclear.common.entity.MdSensor;
import com.aimsphm.nuclear.common.entity.dto.TrendFeatureCellDTO;
import com.aimsphm.nuclear.common.service.MdSensorService;
import com.aimsphm.nuclear.core.entity.bo.DeviationPointBO;
import com.aimsphm.nuclear.core.entity.bo.DeviationStatisticBO;
import com.aimsphm.nuclear.core.service.ClusterComparisonService;
import com.aimsphm.nuclear.core.feign.AlgorithmServiceFeignClient;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.math3.stat.StatUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import java.util.stream.Collectors;

@Service
public class ClusterComparisonServiceImpl implements ClusterComparisonService {

    @Autowired
    private AlgorithmServiceFeignClient feignClient;

    @Autowired
    private MdSensorService sensorService;

    @Override
    public Map<String, List<DeviationPointBO>> genTrendMeanPoints(List<String> tags, long startTs, long endTs) {

        Map<String, List<DeviationPointBO>> returnMap = new LinkedHashMap<>();



        Map<String, List<TrendFeatureCellDTO>> feignResult = feignClient.getTrendFeatureByTags(tags, startTs, endTs).getResData();

        LambdaQueryWrapper<MdSensor> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.in(MdSensor::getTagId,feignResult.keySet());
//
        queryWrapper.orderByAsc(MdSensor::getDeviceId);

        List<MdSensor> sortedSensorList =  sensorService.list(queryWrapper);

       // List<String> sortedTags = feignResult.keySet().stream().sorted().collect(Collectors.toList());

        for (MdSensor sensor : sortedSensorList) {
            String tag = sensor.getTagId();
            Map<Long, Boolean> pool = new ConcurrentHashMap<>();
            List<DeviationPointBO> tempList = feignResult.get(tag).stream().filter(x ->
                    pool.putIfAbsent(x.getTimestamp(), Boolean.TRUE) == null).map(x -> {
                DeviationPointBO dbo = new DeviationPointBO();
                dbo.setTimestamp(x.getTimestamp());
                dbo.setValue((Double) x.getMean());
                return dbo;
            }).sorted(Comparator.comparingLong(x -> x.getTimestamp())).collect(Collectors.toList());
            returnMap.put(tag, tempList);

        }
        return returnMap;
    }

    @Override
    public Map<String, List<DeviationPointBO>> genRelativeDeviationPoints(Map<String, List<DeviationPointBO>> originData,List<MdSensor> sortedSensorList) {
        Map<String, List<DeviationPointBO>> returnMap = new LinkedHashMap<>();



        //所有列表公用得Timestamp提取出来
        Collection<Long> timestampSet = new HashSet<>();
        int loop = 0;
        for (MdSensor sensor : sortedSensorList) {
            String tag = sensor.getTagId();
            List<DeviationPointBO> stepList = originData.get(tag);
            Set<Long> stepSet = new HashSet<>();
            stepList.forEach(x -> stepSet.add(x.getTimestamp()));
            if (loop == 0) {
                timestampSet = stepSet;
            } else {
                timestampSet = CollectionUtils.intersection(timestampSet, stepSet);
                //timestampSet.retainAll(stepSet);
            }
            loop++;

        }
        List<Long> sortedTsList = new ArrayList<>(timestampSet).stream().sorted().collect(Collectors.toList());

        //一组内没有时间重合的点，直接返回空Map
        if (sortedTsList.size() == 0) {
            Map<String, List<DeviationPointBO>> emptyMap = new HashMap<>();
            for (String tag : originData.keySet()) {
                emptyMap.put(tag, Lists.newArrayList());
            }
            return emptyMap;
        }

      /*  List<Integer> lenList = Lists.newArrayList();
        for (String tag : originData.keySet()) {
            lenList.add(originData.get(tag).size());
        }
        int len = Collections.min(lenList);*/

        Integer totalLines = originData.keySet().size();

        Map<Long, Double> avgValueMap = new LinkedHashMap<>();
        //List<DeviationPointBO> avgValueList = new ArrayList<DeviationPointBO>();
        Map<String, Map<Long, Double>> originDataMap = new LinkedHashMap<>();
        for (MdSensor sensor : sortedSensorList) {
            String tag = sensor.getTagId();
            List<DeviationPointBO> boList = originData.get(tag);
            Map<Long, Double> stepMap = new HashMap<>();
            for (DeviationPointBO bo : boList) {
                stepMap.putIfAbsent(bo.getTimestamp(), bo.getValue());
            }

            //Map<Long, Double> stepMap = originData.get(tag).stream().collect(Collectors.toMap(DeviationPointBO::getTimestamp, DeviationPointBO::getValue, (oldValue, newValue) -> newValue));
            originDataMap.put(tag, stepMap);

        }


        for (int i = 0; i < sortedTsList.size(); i++) {
            Double stepSum = 0D;
            Double stepAvg = 0D;
            Long timestamp = sortedTsList.get(i);
            for (MdSensor sensor : sortedSensorList) {
                String tag = sensor.getTagId();
                Map<Long, Double> tempMap = originDataMap.get(tag);
                Double currentValue = tempMap.get(timestamp);
                //timestamp = sortedTsList.get(i);
                if (currentValue != null) {//此处注意处理是什么值，或许是None？
                    stepSum = stepSum + currentValue;
                } else {
                    //avgValueMap.put(timestamp, null);
                    stepSum=null;//add for bug fix of null value
                    break;
                }

            }
            if(stepSum!=null) {//是null值的话，代表其中有一个是没有值的
                stepAvg = stepSum / totalLines;

                avgValueMap.put(timestamp, stepAvg);
            }
        }

        for (MdSensor sensor : sortedSensorList) {
            String tag = sensor.getTagId();
            List<DeviationPointBO> lineList = originData.get(tag);
            List<DeviationPointBO> stepResultList = Lists.newArrayList();
            for (DeviationPointBO p : lineList) {
                Long ts = p.getTimestamp();
                Double avg = avgValueMap.get(ts);
                if (avg != null && avg != 0D) {
                    Double realValue = p.getValue();
                    Double result = ((realValue - avg) / avg) * 100;
                    DeviationPointBO dbo = new DeviationPointBO();
                    dbo.setTimestamp(ts);
                    dbo.setValue(result);
                    stepResultList.add(dbo);
                } else {
                    DeviationPointBO dbo = new DeviationPointBO();
                    dbo.setTimestamp(ts);
                    dbo.setValue(null);
                    stepResultList.add(dbo);
                }
            }

            Map<Long, Boolean> pool = new ConcurrentHashMap<>();
            stepResultList.stream().filter(x ->
                    pool.putIfAbsent(x.getTimestamp(), Boolean.TRUE) == null).sorted(Comparator.comparingLong(x -> x.getTimestamp())).collect(Collectors.toList());
            returnMap.put(tag, stepResultList);
        }


        return returnMap;
    }

    @Override
    public Map<String, DeviationStatisticBO> genRelativeDeviationStatistics(Map<String, List<DeviationPointBO>> deviationData,List<MdSensor> sortedSensorList) {
        Map<String, DeviationStatisticBO> returnMap = new LinkedHashMap<>();


        //List<String> sortedTags = deviationData.keySet().stream().sorted().collect(Collectors.toList());
        for (MdSensor sensor: sortedSensorList) {
            DeviationStatisticBO deviationStatisticBO = new DeviationStatisticBO();
            String tag = sensor.getTagId();
            List<DeviationPointBO> lineList = deviationData.get(tag);
            List<Double> valueList = lineList.stream().filter(x -> x.getValue() != null).map(x -> x.getValue()).sorted(Comparator.comparingDouble(x -> x.doubleValue())).collect(Collectors.toList());
            if(valueList.size()!=0) {
                Double[] valueArray = new Double[valueList.size()];
                valueList.toArray(valueArray);
                //DeviationStatisticBO deviationStatisticBO = new DeviationStatisticBO();
                double[] primitiveArray = ArrayUtils.toPrimitive(valueArray);
                deviationStatisticBO.setAvgDeviation(StatUtils.mean(primitiveArray));
                deviationStatisticBO.setMax(getMax(primitiveArray));
                deviationStatisticBO.setVariance(StatUtils.variance(primitiveArray));
                deviationStatisticBO.setQuarterPercentile(genPercentile(25, primitiveArray));
                deviationStatisticBO.setThirdQuarterPercentile(genPercentile(75, primitiveArray));
                //需要绝对值对比
                List<Double> positiveValueList = lineList.stream().filter(x -> x.getValue() != null).map(x -> Math.abs(x.getValue())).sorted(Comparator.comparingDouble(x -> x.doubleValue())).collect(Collectors.toList());
                Double[] positiveValueArray = new Double[positiveValueList.size()];
                positiveValueList.toArray(positiveValueArray);

                deviationStatisticBO.setTenPercentageQuantile(calculateQuantileByValue(10D, ArrayUtils.toPrimitive(positiveValueArray)));
                deviationStatisticBO.setTwentyPercentageQuantile(calculateQuantileByValue(20D, ArrayUtils.toPrimitive(positiveValueArray)));
            }else{
                deviationStatisticBO.setAvgDeviation(null);
                deviationStatisticBO.setMax(null);
                deviationStatisticBO.setVariance(null);
                deviationStatisticBO.setQuarterPercentile(null);
                deviationStatisticBO.setThirdQuarterPercentile(null);

                deviationStatisticBO.setTenPercentageQuantile(null);
                deviationStatisticBO.setTwentyPercentageQuantile(null);
            }
            returnMap.put(tag, deviationStatisticBO);

        }
        return returnMap;
    }

    public Double genPercentile(int percentile, double[] vlaueArray) {
        if (vlaueArray.length == 0) return null;
        int len = vlaueArray.length;
        int idx = len * percentile / 100;
        return new Double(vlaueArray[idx]);
    }

    public Double calculateQuantileByValue(Double target, double[] valueArray) {
        Double returnValue = 0D;
        int len = valueArray.length;
        int count = 0;
        for (int i = 0; i < len; i++) {
            if (valueArray[i] > target) {
                count++;
            }
        }
        returnValue = (new Double(count) / len) * 100;
        return returnValue;
    }

    public double getMax(double[] valueArray){
       if(valueArray.length!=0){
          double pivot = valueArray[0];
          for(int i=0;i<valueArray.length;i++){
              if(Math.abs(valueArray[i])>Math.abs(pivot)){
                  pivot=valueArray[i];
              }
          }
          return pivot;
       }else{
           return 0;
       }
    }
}

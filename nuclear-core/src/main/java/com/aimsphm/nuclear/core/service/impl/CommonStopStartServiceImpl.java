package com.aimsphm.nuclear.core.service.impl;

import com.aimsphm.nuclear.common.entity.MdSensor;
import com.aimsphm.nuclear.common.entity.MdVirtualSensor;
import com.aimsphm.nuclear.common.entity.TxDeviceStopStartRecord;
import com.aimsphm.nuclear.common.entity.dto.Cell;
import com.aimsphm.nuclear.common.entity.vo.CommonSensorVO;
import com.aimsphm.nuclear.common.service.MdSensorService;
import com.aimsphm.nuclear.common.service.MdVirtualSensorService;
import com.aimsphm.nuclear.common.util.BeanCopyUtils;
import com.aimsphm.nuclear.common.util.CommonAlgoUtil;
import com.aimsphm.nuclear.common.util.CommonUtil;
import com.aimsphm.nuclear.core.entity.bo.DeviationPointBO;
import com.aimsphm.nuclear.core.entity.bo.DeviationStatisticBO;
import com.aimsphm.nuclear.core.feign.AlgorithmServiceFeignClient;
import com.aimsphm.nuclear.core.service.ClusterComparisonService;
import com.aimsphm.nuclear.core.service.CommonStopStartService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.math3.stat.StatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class CommonStopStartServiceImpl implements CommonStopStartService {
    @Autowired
    MdSensorService mdSensorService;
    @Autowired
    MdVirtualSensorService mdVirtualSensorService;
    @Autowired
    AlgorithmServiceFeignClient algorithmServiceFeignClient;
    @Autowired
    ClusterComparisonService clusterComparisonService;

    @Override
    public List<CommonSensorVO> getAllSubSystemSensors(Long subSystemId, Long deviceId) {
        List<MdSensor> sensorListO = null;
        if (ObjectUtils.isEmpty(deviceId)) {
            sensorListO = mdSensorService.getMdSensorBySubSystemId(subSystemId);
        } else {
            sensorListO = mdSensorService.getMdSensorsByDeviceId(deviceId);
        }
//        Supplier<CommonSensorVO> supplier = new Supplier<CommonSensorVO>() {
//            @Override
//            public CommonSensorVO get() {
//                return new CommonSensorVO();
//            }
//        };

        List<CommonSensorVO> cSensorListO = BeanCopyUtils.copyListProperties(sensorListO, CommonSensorVO::new, (mS, cS) -> {
            // 这里可以定义特定的转换规则
            cS.setIsVirtual(false);
        });
        List<MdVirtualSensor> sensorListV = null;
        if (ObjectUtils.isEmpty(deviceId)) {
            sensorListV = mdVirtualSensorService.getMdSensorBySubSystemId(subSystemId);
        } else {
            sensorListV = mdVirtualSensorService.getMdSensorByDeviceId(deviceId);
        }
        List<CommonSensorVO> cSensorListV = BeanCopyUtils.copyListProperties(sensorListV, CommonSensorVO::new, (mS, cS) -> {
            // 这里可以定义特定的转换规则
            cS.setIsVirtual(true);
        });
        cSensorListO.addAll(cSensorListV);
        return cSensorListO;
    }

    @Override
    public Map<String, Map> analyseStopStart(CommonSensorVO xCommonSensorVO, CommonSensorVO yCommonSensorVO, Long onset, Long offset, List<TxDeviceStopStartRecord> records, Boolean startFlag) {
        List<TxDeviceStopStartRecord> filteredRecords = filterTxDeviceStopStartRecords(records, startFlag);
        List cells = Lists.newArrayList();
        Map<String, List<List<Double>>> cellMap = Maps.newHashMap();
        List<Cell> baseCell = Lists.newArrayList();
        for (TxDeviceStopStartRecord tr : filteredRecords) {
            String notes = "";

            notes = startFlag ? tr.getStartBrief() : tr.getStopBrief();
            if(notes == null)
            {
                notes = "";
            }
            Long lunchTime = startFlag ? tr.getStartBeginTime().getTime() : tr.getStopBeginTime().getTime();

            Long onsettime = lunchTime - onset;
            Long offsettime = lunchTime + offset;
            List<Cell> xcell = getByCommonSensorVO(xCommonSensorVO, onsettime, offsettime);
            List<Cell> ycell = getByCommonSensorVO(yCommonSensorVO, onsettime, offsettime);
            List<Long> timestamps = CommonAlgoUtil.alignTime(false, Lists.newArrayList(xcell, ycell));
            Map basexCellMap = xcell.stream().collect(Collectors.toMap(Cell::getTimestamp, Cell::getValue));
            Map baseyCellMap = ycell.stream().collect(Collectors.toMap(Cell::getTimestamp, Cell::getValue));
            List<List<Double>> list = Lists.newArrayList();
            for (Long time : timestamps) {
                Double xvalue = (Double) basexCellMap.get(time);
                Double yvalue = (Double) baseyCellMap.get(time);
                List<Double> object = Lists.newArrayList();
                object.add(xvalue);
                object.add(yvalue);
                list.add(object);
            }
            list = list.stream().sorted(Comparator.comparingDouble(x -> x.get(0))).collect(Collectors.toList());
            cellMap.put(notes, list);

        }
        Map<String, Map> resultMap = Maps.newHashMap();
        resultMap.put("chart", cellMap);
        return resultMap;
    }

    @Override
    public List<Cell> getByCommonSensorVO(CommonSensorVO yCommonSensorVO, Long start, Long end) {
        Integer step = 0;
        long duration = end - start;
        if (duration <= 1 * 3600 * 1000l) {
            step = 1;
        } else if (duration > 1 * 3600 * 1000l && duration <= 3 * 3600 * 1000l) {
            step = 10;
        } else if (duration > 1 * 3600 * 1000l && duration <= 7 * 24 * 3600 * 1000l) {
            step = 60;
        } else if (duration > 7 * 24 * 3600 * 1000l && duration <= 30 * 24 * 3600 * 1000l) {
            step = 600;
        } else {
            step = 3600;
        }
        if (yCommonSensorVO.getIsVirtual()) {

            return algorithmServiceFeignClient.getVSDataByVSId(yCommonSensorVO.getId(), start, end, step).getResData();
        } else {
            return algorithmServiceFeignClient.getTagData(yCommonSensorVO.getTagId(), start, end, step).getResData();
        }
    }

    @Override
    public Map<String, Object> analyseStopStartXTime(CommonSensorVO yCommonSensorVO, Long onset, Long offset, List<TxDeviceStopStartRecord> records, Long baseRecordId, Boolean startFlag) {
        List<TxDeviceStopStartRecord> filteredRecords = filterTxDeviceStopStartRecords(records, startFlag);

        AtomicReference<TxDeviceStopStartRecord> baseTxDeviceStopStartRecord = new AtomicReference<>(filteredRecords.stream().findFirst().orElse(null));
        filteredRecords.forEach(tr -> {
            if (tr.getId().equals(baseRecordId)) {
                baseTxDeviceStopStartRecord.set(tr);
            }
        });
        if (ObjectUtils.isEmpty(baseTxDeviceStopStartRecord.get())) {
            return Maps.newHashMap();
        }
        Long baseTime = startFlag ? baseTxDeviceStopStartRecord.get().getStartBeginTime().getTime() : baseTxDeviceStopStartRecord.get().getStopBeginTime().getTime();
        List cells = Lists.newArrayList();
        Map<String, List<Cell>> cellMap = Maps.newHashMap();
        Map<String, Object> tcellMap = Maps.newHashMap();
        List<Cell> baseCell = Lists.newArrayList();
        List<String> recordNames = Lists.newArrayList();
        for (TxDeviceStopStartRecord tr : filteredRecords) {
            String notes = "";

            notes = startFlag ? tr.getStartBrief() : tr.getStopBrief();
            if(notes == null)
            {
                notes = "";
            }
            Long lunchTime = startFlag ? tr.getStartBeginTime().getTime() : tr.getStopBeginTime().getTime();
            Long deviation = baseTime - lunchTime;

            Long onsettime = lunchTime - onset;
            Long offsettime = lunchTime + offset;

            List<Cell> cell = getByCommonSensorVO(yCommonSensorVO, onsettime, offsettime);
            if (cell == null) {
                cell = Lists.newArrayList();
            }
//            if (deviation != 0) {
                cell.stream().forEach(c -> c.setTimestamp((c.getTimestamp() -lunchTime)/1000l));
//            }
            cells.add(cell);
            if (tr.getId().equals(baseRecordId)) {
                baseCell = cell;
            } else {
                cellMap.put(notes, cell);
                recordNames.add(notes);
            }

        }

        List<Long> timelist = CommonAlgoUtil.alignTime(false, cells);

//        if (!ObjectUtils.isEmpty(baseRecordId)) {
//            Map baseCellMap = baseCell.stream().collect(Collectors.toMap(Cell::getTimestamp, Cell::getValue));
//            for (Map.Entry<String, List<Cell>> entry : cellMap.entrySet()) {
//                String key = entry.getKey();
//                List<Cell> value = entry.getValue();
//                value.forEach(e -> {
//                    Object dvalue = baseCellMap.get(e.getTimestamp());
//                    if (dvalue != null) {
//                        e.setValue((Double) e.getValue() - (Double) dvalue);
//                    }
//                });
//            }
//        }
//        List rsp = txTrendFeatureService.getTrendFeatureCell(tagId, start, end).stream().filter(tc -> {
//            Object result = cm.putIfAbsent(tc.getTimestamp(), Boolean.TRUE);
//            if (tc.getTimestamp() > cutofftime) {
//                return false;
//            }
//            if (result == null) {
//                return true;
//            } else {
//                return false;
//            }
//        }).map(tc -> {
//            List<Object> object = Lists.newArrayList();
//            object.add(tc.getTimestamp());
//            object.add(tc.getMean());
//            return object;
//        }).collect(Collectors.toList());
        Map<String, List<DeviationPointBO>> originMap = new LinkedHashMap<>();
        Map<String, Map<Long, Object>> originDataMap = new LinkedHashMap<>();
        Map baseCellMap = baseCell.stream().collect(Collectors.toMap(Cell::getTimestamp, Cell::getValue));
        for (Map.Entry<String, List<Cell>> entry : cellMap.entrySet()) {
            String key = entry.getKey();
            List<Cell> value = entry.getValue();
            List<DeviationPointBO> tempList = value.stream().map(x -> {
                DeviationPointBO dbo = new DeviationPointBO();
                dbo.setTimestamp(x.getTimestamp());
                dbo.setValue((Double) x.getValue());
                return dbo;
            })
//                    .sorted(Comparator.comparingLong(x -> x.getTimestamp()))
                    .collect(Collectors.toList());
            Map<Long, Object> tvMapRaw = value.stream().collect(Collectors.toMap(Cell::getTimestamp, Cell::getValue));
            HashMap<Long, Object> tvMap = Maps.newHashMap();
            tvMap.putAll(tvMapRaw);
            if (!ObjectUtils.isEmpty(baseRecordId)) {
                value.forEach(e -> {
                    Object dvalue = baseCellMap.get(e.getTimestamp());
                    if (dvalue != null) {
                        e.setValue((Double) e.getValue() - (Double) dvalue);
                    }
                });
            }
            List<Object> timedataList = value.stream().map(tc -> {
                List<Object> object = Lists.newArrayList();
                object.add(tc.getTimestamp());
                object.add(tc.getValue());
                return object;
            }).collect(Collectors.toList());
            originMap.put(key, tempList);
            originDataMap.put(key, tvMap);
            tcellMap.put(key, timedataList);
        }
        Map<Long, Double> avgValueMap = new LinkedHashMap<>();
        Integer totalLines = recordNames.size();
        for (int i = 0; i < timelist.size(); i++) {
            Long timestamp = timelist.get(i);
            Double stepSum = 0D;
            Double stepAvg = 0D;
            for (String recordName : recordNames) {

                Map<Long, Object> tempMap = originDataMap.get(recordName);
                Double currentValue = (Double) tempMap.get(timestamp);
                //timestamp = sortedTsList.get(i);
                if (currentValue != null) {//此处注意处理是什么值，或许是None？
                    stepSum = stepSum + currentValue;
                } else {
                    //avgValueMap.put(timestamp, null);
                    break;
                }
            }
            stepAvg = stepSum / totalLines;
            avgValueMap.put(timestamp, stepAvg);
        }
        for (String recordName : recordNames) {
            List<DeviationPointBO> lineList = originMap.get(recordName);
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
//            stepResultList.stream().filter(x ->
//                    pool.putIfAbsent(x.getTimestamp(), Boolean.TRUE) == null).sorted(Comparator.comparingLong(x -> x.getTimestamp())).collect(Collectors.toList());
            originMap.put(recordName, stepResultList);
        }


        List<String> labelList = new ArrayList<>();
        List<List<Double>> dataList = new ArrayList<>();
        for (String tag : originMap.keySet()) {
            List<DeviationPointBO> deviationListByTag = originMap.get(tag);
            List<Double> stepList = deviationListByTag.stream().map(x -> x.getValue()).filter(x -> x != null).collect(Collectors.toList());
            dataList.add(stepList);
            labelList.add(tag);
        }
        Map<String, Object> returnBoxPlotMap = new LinkedHashMap<>();
        returnBoxPlotMap.put("data", dataList);
        returnBoxPlotMap.put("labelList", labelList);
//        Map<String, DeviationStatisticBO> statisticBOMap = clusterComparisonService.genRelativeDeviationStatistics(deviationMap, genSortedSensorList(deviationMap.keySet()));
        Map<String, DeviationStatisticBO> statisticBOMap = new LinkedHashMap<>();


        //List<String> sortedTags = deviationData.keySet().stream().sorted().collect(Collectors.toList());
        for (String tag : originMap.keySet()) {
            DeviationStatisticBO deviationStatisticBO = new DeviationStatisticBO();
            List<DeviationPointBO> lineList = originMap.get(tag);
            List<Double> valueList = lineList.stream().filter(x -> x.getValue() != null).map(x -> x.getValue()).sorted(Comparator.comparingDouble(x -> x.doubleValue())).collect(Collectors.toList());
            if (valueList.size() != 0) {
                Double[] valueArray = new Double[valueList.size()];
                valueList.toArray(valueArray);
                //DeviationStatisticBO deviationStatisticBO = new DeviationStatisticBO();
                double[] primitiveArray = ArrayUtils.toPrimitive(valueArray);
                deviationStatisticBO.setAvgDeviation(StatUtils.mean(primitiveArray));
                deviationStatisticBO.setMax(getMax(primitiveArray));
                deviationStatisticBO.setVariance(StatUtils.variance(primitiveArray));
                deviationStatisticBO.setQuarterPercentile(CommonUtil.genPercentile(25, primitiveArray));
                deviationStatisticBO.setThirdQuarterPercentile(CommonUtil.genPercentile(75, primitiveArray));
                //需要绝对值对比
                List<Double> positiveValueList = lineList.stream().filter(x -> x.getValue() != null).map(x -> Math.abs(x.getValue())).sorted(Comparator.comparingDouble(x -> x.doubleValue())).collect(Collectors.toList());
                Double[] positiveValueArray = new Double[positiveValueList.size()];
                positiveValueList.toArray(positiveValueArray);

                deviationStatisticBO.setTenPercentageQuantile(CommonUtil.calculateQuantileByValue(10D, ArrayUtils.toPrimitive(positiveValueArray)));
                deviationStatisticBO.setTwentyPercentageQuantile(CommonUtil.calculateQuantileByValue(20D, ArrayUtils.toPrimitive(positiveValueArray)));
            } else {
                deviationStatisticBO.setAvgDeviation(null);
                deviationStatisticBO.setMax(null);
                deviationStatisticBO.setVariance(null);
                deviationStatisticBO.setQuarterPercentile(null);
                deviationStatisticBO.setThirdQuarterPercentile(null);

                deviationStatisticBO.setTenPercentageQuantile(null);
                deviationStatisticBO.setTwentyPercentageQuantile(null);
            }
            statisticBOMap.put(tag, deviationStatisticBO);

        }

        Map<String, Object> resultMap = Maps.newHashMap();
        resultMap.put("chart", tcellMap);
        resultMap.put("boxPlot", returnBoxPlotMap);
        resultMap.put("statistic", statisticBOMap);

        resultMap.put("baseTime",baseTime);

        return resultMap;
    }

    public double getMax(double[] valueArray) {
        if (valueArray.length != 0) {
            double pivot = valueArray[0];
            for (int i = 0; i < valueArray.length; i++) {
                if (Math.abs(valueArray[i]) > Math.abs(pivot)) {
                    pivot = valueArray[i];
                }
            }
            return pivot;
        } else {
            return 0;
        }
    }

    private List<MdSensor> genSortedSensorList(Collection<String> TagCollection) {
        LambdaQueryWrapper<com.aimsphm.nuclear.common.entity.MdSensor> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.in(com.aimsphm.nuclear.common.entity.MdSensor::getTagId, TagCollection);
//
        queryWrapper.orderByAsc(com.aimsphm.nuclear.common.entity.MdSensor::getDeviceId);

        return mdSensorService.list(queryWrapper);
    }

    public List<TxDeviceStopStartRecord> filterTxDeviceStopStartRecords(List<TxDeviceStopStartRecord> records, Boolean startFlag) {
        return records.stream().filter(rs -> {
            if (startFlag) {
                return !ObjectUtils.isEmpty(rs.getStartBeginTime());
            } else {
                return !ObjectUtils.isEmpty(rs.getStopBeginTime());
            }
        }).sorted(Comparator.comparing(startFlag ? TxDeviceStopStartRecord::getStartBeginTime : TxDeviceStopStartRecord::getStopBeginTime).reversed()).collect(Collectors.toList());
    }
}

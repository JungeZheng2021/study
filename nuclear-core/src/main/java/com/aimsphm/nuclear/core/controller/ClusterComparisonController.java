package com.aimsphm.nuclear.core.controller;

import com.aimsphm.nuclear.common.entity.MdSensor;
import com.aimsphm.nuclear.common.response.ResponseUtils;
import com.aimsphm.nuclear.common.service.MdSensorService;
import com.aimsphm.nuclear.core.entity.bo.DeviationPointBO;
import com.aimsphm.nuclear.core.entity.bo.DeviationStatisticBO;
import com.aimsphm.nuclear.core.service.ClusterComparisonService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Mao
 * @since 2020-05-07
 */
@Slf4j
@RestController
@RequestMapping("clusterComparison")
@Api(tags = "集群对比通用功能接口")
public class ClusterComparisonController {

    @Autowired
    private ClusterComparisonService clusterComparisonService;

    @Autowired
    private MdSensorService sensorService;

    @GetMapping("getTrendFeatureComparison")
    @ApiOperation(value = "根据开始和结束时间拿到相应传感器的历史特征值集合", notes = "开始结束时间为毫秒的时间戳形式")
    public Object getTrendFeatureByTags(@RequestParam List<String> tags, @RequestParam Long start, @RequestParam Long end) {
        log.info("参数列表：测点集合：{},开始时间:{},结束时间：{}",tags,start,end);
        Map<String, List<DeviationPointBO>> originMap = clusterComparisonService.genTrendMeanPoints(tags,start, end);
        log.info("参数列表：调用结果：{}",originMap.size());
        Map<String,List<List<Object>>> returnMap = new LinkedHashMap<>();
        List<MdSensor> sortedSensorList = genSortedSensorList(originMap.keySet());
        for(MdSensor sensor:sortedSensorList){
            String tag = sensor.getTagId();
            List<DeviationPointBO> originListByTag = originMap.get(tag);
            if(CollectionUtils.isEmpty(originListByTag)){
                continue;
            }
            List<List<Object>> stepList = originListByTag.stream().map(x->{
                List<Object> lst= new ArrayList<Object>();
                lst.add(x.getTimestamp());
                lst.add(x.getValue());
                return lst;
            }).collect(Collectors.toList());
            returnMap.put(tag,stepList);
        }
        return ResponseUtils.success(returnMap);
    }

    @GetMapping("getTrendFeatureDeviation")
    @ApiOperation(value = "根据开始和结束时间计算相应传感器的特征偏差", notes = "开始结束时间为毫秒的时间戳形式")
    public Object getTrendFeatureDeviationByTags(@RequestParam List<String> tags, @RequestParam Long start, @RequestParam Long end) {
        Map<String, List<DeviationPointBO>> originMap = clusterComparisonService.genTrendMeanPoints(tags,start, end);
        List<MdSensor> sortedSensorList = genSortedSensorList(originMap.keySet());

        Map<String, List<DeviationPointBO>> deviationMap = clusterComparisonService.genRelativeDeviationPoints(originMap,sortedSensorList);

        Map<String,List<List<Object>>> returnMap = new LinkedHashMap<>();
        for(MdSensor sensor:sortedSensorList){
            String tag = sensor.getTagId();
            List<DeviationPointBO> deviationListByTag = deviationMap.get(tag);
            List<List<Object>> stepList = deviationListByTag.stream().map(x->{
                List<Object> lst= new ArrayList<Object>();
                lst.add(x.getTimestamp());
                lst.add(x.getValue());
                return lst;
            }).collect(Collectors.toList());
            returnMap.put(tag,stepList);
        }
        return ResponseUtils.success(returnMap);
    }

    @GetMapping("getTrendFeatureBoxPlot")
    @ApiOperation(value = "根据开始和结束时间得到箱型图的数组", notes = "开始结束时间为毫秒的时间戳形式")
    public Object getTrendFeatureBoxPlotByTags(@RequestParam List<String> tags, @RequestParam Long start, @RequestParam Long end) {
        Map<String, List<DeviationPointBO>> originMap = clusterComparisonService.genTrendMeanPoints(tags,start, end);
        Map<String, List<DeviationPointBO>> deviationMap = clusterComparisonService.genRelativeDeviationPoints(originMap,genSortedSensorList(originMap.keySet()));

        Map<String,Object> returnMap = new HashMap<>();
        List<String> deviceList=new ArrayList<>();
        List<List<Double>> dataList = new ArrayList<>();
        for(String tag:deviationMap.keySet()){
            List<DeviationPointBO> deviationListByTag = deviationMap.get(tag);
            List<Double> stepList = deviationListByTag.stream().map(x->x.getValue()).filter(x->x!=null).collect(Collectors.toList());
            dataList.add(stepList);
            deviceList.add(tag);
        }

        returnMap.put("data",dataList);
        returnMap.put("deviceList",deviceList);
        return ResponseUtils.success(returnMap);
    }

    @GetMapping("getTrendFeatureStatistics")
    @ApiOperation(value = "根据开始和结束时间计算相应传感器的偏差统计", notes = "开始结束时间为毫秒的时间戳形式")
    public Object getTrendFeatureStatisticsByTags(@RequestParam List<String> tags, @RequestParam Long start, @RequestParam Long end) {
        Map<String, List<DeviationPointBO>> originMap = clusterComparisonService.genTrendMeanPoints(tags,start, end);
        Map<String, List<DeviationPointBO>> deviationMap = clusterComparisonService.genRelativeDeviationPoints(originMap,genSortedSensorList(originMap.keySet()));
        Map<String, DeviationStatisticBO> statisticBOMap = clusterComparisonService.genRelativeDeviationStatistics(deviationMap,genSortedSensorList(deviationMap.keySet()));


        return ResponseUtils.success(statisticBOMap);
    }

    //一次性拿到页面所有得数据
    @GetMapping("getTrendFeatureCombined")
    @ApiOperation(value = "综合接口，一次性返回相应传感器的特征趋势集合，偏差集合，箱型图数据以及偏差统计", notes = "开始结束时间为毫秒的时间戳形式")
    public Object getTrendFeatureCombinedByTags(@RequestParam List<String> tags, @RequestParam Long start, @RequestParam Long end) {

        Map<String,Object> returnMap = new HashMap<>();



       //拿到滑动平均
        Map<String, List<DeviationPointBO>> originMap = clusterComparisonService.genTrendMeanPoints(tags,start, end);

        //把tags根据DeviceID排个序

        List<MdSensor> sortedSensorList =  this.genSortedSensorList(originMap.keySet());


        Map<String,List<List<Object>>> meanDataReturnMap = new LinkedHashMap<>();
       /* for(String tag:originMap.keySet()){
            List<DeviationPointBO> originListByTag = originMap.get(tag);
            List<List<Object>> stepList = originListByTag.stream().map(x->{
                List<Object> lst= new ArrayList<Object>();
                lst.add(x.getTimestamp());
                lst.add(x.getValue());
                return lst;
            }).collect(Collectors.toList());
            meanDataReturnMap.put(tag,stepList);
        }*/

        for(MdSensor sensor:sortedSensorList){
            String tagId = sensor.getTagId();
            List<DeviationPointBO> originListByTag = originMap.get(tagId);
            List<List<Object>> stepList = originListByTag.stream().map(x->{
                List<Object> lst= new ArrayList<Object>();
                lst.add(x.getTimestamp());
                lst.add(x.getValue());
                return lst;
            }).collect(Collectors.toList());
            meanDataReturnMap.put(tagId,stepList);
        }

         //拿到偏差
        Map<String, List<DeviationPointBO>> deviationMap = clusterComparisonService.genRelativeDeviationPoints(originMap,sortedSensorList);

        Map<String,List<List<Object>>> deviationReturnMap = new LinkedHashMap<>();





        for(MdSensor sensor:sortedSensorList){
            String tagId = sensor.getTagId();
            List<DeviationPointBO> deviationListByTag = deviationMap.get(tagId);
            List<List<Object>> stepDeviationList = deviationListByTag.stream().map(x->{
                List<Object> lst= new ArrayList<Object>();
                lst.add(x.getTimestamp());
                lst.add(x.getValue());
                return lst;
            }).collect(Collectors.toList());
            deviationReturnMap.put(tagId,stepDeviationList);
        }

        //拿到plotBox得值
        Map<String,Object> returnBoxPlotMap = new LinkedHashMap<>();
        List<String> deviceList=new ArrayList<>();
        List<List<Double>> dataList = new ArrayList<>();
       /* for(String tag:deviationMap.keySet()){
            List<DeviationPointBO> deviationListBoxPlotByTag = deviationMap.get(tag);
            List<Double> stepListBoxPlot = deviationListBoxPlotByTag.stream().map(x->x.getValue()).filter(x->x!=null).collect(Collectors.toList());
            dataList.add(stepListBoxPlot);
            deviceList.add(tag);
        }*/
        for(MdSensor sensor:sortedSensorList){
            String tagId = sensor.getTagId();
            List<DeviationPointBO> deviationListBoxPlotByTag = deviationMap.get(tagId);
            List<Double> stepListBoxPlot = deviationListBoxPlotByTag.stream().map(x->x.getValue()).filter(x->x!=null).collect(Collectors.toList());
            dataList.add(stepListBoxPlot);
            deviceList.add(tagId);
        }

        returnBoxPlotMap.put("data",dataList);
        returnBoxPlotMap.put("deviceList",deviceList);

        //拿到统计值
        Map<String, DeviationStatisticBO> statisticBOMap = clusterComparisonService.genRelativeDeviationStatistics(deviationMap,sortedSensorList);

        /**
         * 一下皆为排序
         */



        //Map<String,List<List<Object>>> sortedMeanDataReturnMap = new HashMap<>();
        //meanDataReturnMap.entrySet().stream().sorted(Comparator.comparing(x->x.getKey())).forEachOrdered(e->sortedMeanDataReturnMap.put(e.getKey(),e.getValue()));
        returnMap.put("meanPoints",meanDataReturnMap);
       // Map<String,List<List<Object>>> sortedDeviationReturnMap = new HashMap<>();
        //deviationReturnMap.entrySet().stream().sorted(Comparator.comparing(x->x.getKey())).forEachOrdered(e->sortedDeviationReturnMap.put(e.getKey(),e.getValue()));
        returnMap.put("deviationPoints",deviationReturnMap);
        returnMap.put("boxPlot",returnBoxPlotMap);

        /*Map<String, DeviationStatisticBO> sortedStatisticBOMap = new HashMap<>();
        statisticBOMap.entrySet().stream().sorted(Comparator.comparing(x->x.getKey())).forEachOrdered(e->sortedStatisticBOMap.put(e.getKey(),e.getValue()));*/
        returnMap.put("statistics",statisticBOMap);

        return ResponseUtils.success(returnMap);
    }

    private List<MdSensor> genSortedSensorList(Collection<String> TagCollection){
        LambdaQueryWrapper<MdSensor> queryWrapper = new LambdaQueryWrapper<>();
        //增加判空逻辑
        if(!TagCollection.isEmpty()){
            queryWrapper.in(MdSensor::getTagId,TagCollection);
        }
        queryWrapper.orderByAsc(MdSensor::getDeviceId);

        return sensorService.list(queryWrapper);
    }
}

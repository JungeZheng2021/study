package com.aimsphm.nuclear.core.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import com.aimsphm.nuclear.common.constant.CommonConstant;
import com.aimsphm.nuclear.common.entity.MdSensor;
import com.aimsphm.nuclear.common.entity.MdSubSystem;
import com.aimsphm.nuclear.common.enums.DeviceTypeEnum;
import com.aimsphm.nuclear.common.enums.SensorTypeEnum;
import com.aimsphm.nuclear.common.service.HotSpotDataService;
import com.aimsphm.nuclear.core.service.MdSubSystemService;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aimsphm.nuclear.common.entity.vo.MeasurePointVO;
import com.aimsphm.nuclear.common.service.AlgorithmCacheService;
import com.aimsphm.nuclear.common.service.MdSensorService;
import com.aimsphm.nuclear.common.service.TxSensorTrendService;
import com.aimsphm.nuclear.core.service.SensorTrendService;
import com.aimsphm.nuclear.core.vo.SensorTrendVO;
import com.google.common.collect.Maps;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

@Service
public class SensorTrendServiceImpl implements SensorTrendService {

    @Autowired
    HotSpotDataService hotSpotDataService;

    @Autowired
    MdSensorService mdSensorService;
    @Autowired
    MdSubSystemService mdSubSystemService;

    @Autowired
    AlgorithmCacheService algorithmCacheService;

    @Autowired
    TxSensorTrendService txSensorTrendService;

    //    @Override
//    public List<Map<String, Object>> getTrendHotSpotBySubSystemId(Long subSystemId) {
//        Set<String> tagIds = mdSensorService.getMdSensorBySubSystemId(subSystemId).stream().filter(s -> s.getClusterAnalysis() && !ObjectUtils.isEmpty(s.getSensorDesc())).map(ms -> ms.getTagId())
//                .distinct().collect(Collectors.toSet());
//        List<MeasurePointVO> measurePointVO = hotSpotDataService.getPoints(tagIds).stream().filter(mp -> mp != null)
//                .collect(Collectors.toList());
//        measurePointVO.stream().forEach(mv -> {
//            if (ObjectUtils.isEmpty(mv.getRelatedGroup())) {
//                mv.setRelatedGroup("其他分组");
//            }
//        });
//        Map<String, List<MeasurePointVO>> measurePointVOMapRaw = measurePointVO.stream()
//                .collect(Collectors.groupingBy(MeasurePointVO::getRelatedGroup));
//        Map<String, List<MeasurePointVO>> measurePointVOMap = new TreeMap<String, List<MeasurePointVO>>(
//                new Comparator<String>() {
//                    @Override
//                    public int compare(String obj1, String obj2) {
//
//                        return obj1.compareTo(obj2);
//                    }
//                });
//        measurePointVOMap.putAll(measurePointVOMapRaw);
//        Iterator it = measurePointVOMap.keySet().iterator();
//        List<Map<String, Object>> list = Lists.newArrayList();
//
//        while (it.hasNext()) {
//            String key = it.next().toString();
//            List<MeasurePointVO> points = measurePointVOMap.get(key);
//            Map<String, List<MeasurePointVO>> voMap = points.stream().filter(v -> !ObjectUtils.isEmpty(v.getSensorDesc())).sorted((u1, u2) ->
//            {
//                if (ObjectUtils.isEmpty(u1.getParentTag()) ) {
//                    return -1;
//                }
//                if(ObjectUtils.isEmpty(u2.getParentTag()))
//                {
//                   return  1;
//                }
//                if (ObjectUtils.isEmpty(u1.getParentTag()) && ObjectUtils.isEmpty(u2.getParentTag())) {
//                    return 0;
//                }
//                return u1.getParentTag().compareTo(u2.getParentTag());
//            })
//                    .collect(Collectors.groupingBy(MeasurePointVO::getSensorDesc));
//            Map map = Maps.newHashMap();
//            map.put("type", key);
//            map.put("points", voMap);
//            list.add(map);
//        }
//        return list;
//    }
    @Override
    public List<Map<String, Object>> getTrendHotSpotBySubSystemId(Long subSystemId) {
        MdSubSystem subSystem = mdSubSystemService.getById(subSystemId);
        boolean turbineSortedByGroup = false;
        boolean sortedByGroup = false;
        if (subSystem != null) {
            if (subSystem.getSubSystemType().equals(DeviceTypeEnum.Turbine.getValue())) {
                //special handle
                turbineSortedByGroup = true;
            }
            if (subSystem.getSubSystemType().equals(DeviceTypeEnum.Rotating.getValue())) {
                sortedByGroup = true;
            }
        }
        //redis中的key做了tagId+subSystemId+deviceId的组装
        Set<String> tagIds = mdSensorService.getMdSensorBySubSystemId(subSystemId).stream().filter(s -> s.getClusterAnalysis() && !ObjectUtils.isEmpty(s.getSensorDesc())).map(
                ms -> ms.getTagId().concat(CommonConstant.REDIS_KEY_UNDERLINE + ms.getSubSystemId())
                        .concat(ms.getDeviceId() == null ? StringUtils.EMPTY : CommonConstant.REDIS_KEY_UNDERLINE + ms.getDeviceId()))
                .collect(Collectors.toSet());
        List<MeasurePointVO> measurePointVO = hotSpotDataService.getPoints(tagIds).stream().filter(mp -> mp != null)
                .collect(Collectors.toList());
        measurePointVO.stream().forEach(mv -> {
            if (mv.getRelatedGroup() != null) {
                mv.setRelatedGroup(mv.getRelatedGroup().split("_")[0]);
            }

        });
        Map<String, List<MeasurePointVO>> measurePointVOMapRaw = null;
        boolean finalSortedByGroup = sortedByGroup;
        if (!finalSortedByGroup) {
            measurePointVOMapRaw = measurePointVO.stream()
                    .collect(Collectors.groupingBy(MeasurePointVO::getDesc));
        } else {
            measurePointVOMapRaw = measurePointVO.stream()
                    .collect(Collectors.groupingBy(MeasurePointVO::getRelatedGroup));
        }


        Map<String, List<MeasurePointVO>> measurePointVOMap = new TreeMap<String, List<MeasurePointVO>>(
                new Comparator<String>() {
                    @Override
                    public int compare(String obj1, String obj2) {
                        if (!finalSortedByGroup) {
                            return SensorTypeEnum.getValue(obj1).compareTo(SensorTypeEnum.getValue(obj2));
                        } else {
                            return obj1.compareTo(obj2);
                        }
                    }
                });
        measurePointVOMap.putAll(measurePointVOMapRaw);
        Iterator it = measurePointVOMap.keySet().iterator();
        List<Map<String, Object>> list = Lists.newArrayList();
        boolean finalTurbineSortedByGroup = turbineSortedByGroup;
        while (it.hasNext()) {
            String key = it.next().toString();
            List<MeasurePointVO> points = measurePointVOMap.get(key);
            Map<String, List<MeasurePointVO>> voMap = points.stream().filter(v -> !ObjectUtils.isEmpty(v.getSensorDesc())).sorted((u1, u2) ->
            {
                if (finalTurbineSortedByGroup) {
                    if (u1.getRelatedGroup() != null && u2.getRelatedGroup() != null) {
                        if (Character.isDigit(u1.getRelatedGroup().charAt(0)) && Character.isDigit(u2.getRelatedGroup().charAt(0))) {
                            return Integer.parseInt(u1.getRelatedGroup().replaceAll("[^0-9]*", "")) - (Integer.parseInt(u2.getRelatedGroup().replaceAll("[^0-9]*", "")));
                        }
                    }
                }
                if (ObjectUtils.isEmpty(u1.getParentTag()) || ObjectUtils.isEmpty(u2.getParentTag())) {


                    return -1;
                }
                return u1.getParentTag().compareTo(u2.getParentTag());
            })
                    .collect(Collectors.groupingBy(MeasurePointVO::getSensorDesc));
            Map<String, List<MeasurePointVO>> measurePointVOMap2 = new TreeMap<String, List<MeasurePointVO>>(
                    new Comparator<String>() {
                        @Override
                        public int compare(String obj1, String obj2) {
                            if (finalSortedByGroup) {
                                return getOrderFromSensorDesc(obj1).compareTo(getOrderFromSensorDesc(obj2));
                            } else {
                                return obj1.compareTo(obj2);
                            }
                        }
                    });
            measurePointVOMap2.putAll(voMap);
            Map map = Maps.newHashMap();
            map.put("type", key);
            if (finalSortedByGroup) {
                map.put("points", measurePointVOMap2);
            } else {
                map.put("points", voMap);
            }
            list.add(map);
        }
        return list;
    }

    private Integer getOrderFromSensorDesc(String sensorDesc) {
        MdSensor mdSensor = mdSensorService.getMdSensorsBySensorDesc(sensorDesc);
        if (mdSensor == null || mdSensor.getTorder() == null) {
            return -99;
        }
        return mdSensor.getTorder();
    }

    private Integer getOrderFromGroupName(String groupName) {
        if ("转速和电信号".equals(groupName)) {
            return 0;
        } else if ("主泵冷却相关测点".equals(groupName)) {
            return 1;
        } else if ("轴承模型".equals(groupName)) {
            return 2;
        } else if ("叶轮模型".equals(groupName)) {
            return 3;
        } else if ("其它公共测点".equals(groupName)) {
            return 4;
        } else if ("报警相关测点".equals(groupName)) {
            return 5;
        }
//        "1号轴承" -> {ArrayList@12941}  size = 5
//        "2号轴承" -> {ArrayList@12943}  size = 5
//        "其他分组" -> {ArrayList@12945}  size = 9
//        "系统公用一般测点" -> {ArrayList@12947}  size = 2
//        "系统公用总览测点" -> {ArrayList@12949}  size = 9
//        "高压缸缸体" -> {ArrayList@12951}  size = 7
//        "系统公用核心测点" -> {ArrayList@12953}  size = 2
        else if ("1号轴承".equals(groupName)) {
            return 1;
        } else if ("2号轴承".equals(groupName)) {
            return 2;
        } else if ("3号轴承".equals(groupName)) {
            return 3;
        } else if ("4号轴承".equals(groupName)) {
            return 4;
        } else if ("5号轴承".equals(groupName)) {
            return 5;
        } else if ("6号轴承".equals(groupName)) {
            return 6;
        } else if ("7号轴承".equals(groupName)) {
            return 7;
        } else if ("8号轴承".equals(groupName)) {
            return 8;
        } else if ("9号轴承".equals(groupName)) {
            return 9;
        } else if ("10号轴承".equals(groupName)) {
            return 10;
        } else if ("11号轴承".equals(groupName)) {
            return 11;
        } else if ("轴向测点".equals(groupName)) {
            return 12;
        } else if ("高压缸缸体".equals(groupName)) {
            return 13;
        } else if ("系统公用核心测点".equals(groupName)) {
            return 14;
        } else if ("系统公用核心测点".equals(groupName)) {
            return 15;
        } else if ("系统公用总览测点".equals(groupName)) {
            return 16;
        } else if ("系统公用一般测点".equals(groupName)) {
            return 17;
        } else if ("泵测点".equals(groupName)) {
            return 18;
        } else if ("电机测点".equals(groupName)) {
            return 19;
        } else if ("主给水泵测点".equals(groupName)) {
            return 20;
        } else if ("前置泵测点".equals(groupName)) {
            return 21;
        } else if ("变速箱测点".equals(groupName)) {
            return 22;
        } else if ("基本测点".equals(groupName)) {
            return 23;
        } else if ("油单元测点".equals(groupName)) {
            return 24;
        }

        return 9999;
    }

    @Override
    public List<Map<String, Object>> getTrendHotSpot(Long deviceId, String keyword) {
        List<MeasurePointVO> pumpPointsByDeviceId = hotSpotDataService.getHotPointsByDeviceId(deviceId, null);
        if (CollectionUtils.isEmpty(pumpPointsByDeviceId)) {
            return null;
        }
        List<MeasurePointVO> measurePointVO = pumpPointsByDeviceId.stream().filter(item -> {
            if (StringUtils.isBlank(keyword)) {
                return Objects.nonNull(item);
            }
            return Objects.nonNull(item) && StringUtils.isNotBlank(item.getTagName()) && item.getTagName().contains(keyword);
        }).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(measurePointVO)) {
            return null;
        }
        measurePointVO.stream().forEach(mv -> {
            if (mv.getRelatedGroup() != null) {
                mv.setRelatedGroup(mv.getRelatedGroup().split("_")[0]);
            }
            if (ObjectUtils.isEmpty(mv.getRelatedGroup())) {
                mv.setRelatedGroup("其他分组");
            }
        });
        Map<String, List<MeasurePointVO>> measurePointVOMapRaw = measurePointVO.stream().collect(Collectors.groupingBy(MeasurePointVO::getRelatedGroup));
        if (MapUtils.isEmpty(measurePointVOMapRaw)) {
            return null;
        }
        Map<String, List<MeasurePointVO>> measurePointVOMap = new TreeMap<>(Comparator.comparing(this::getOrderFromGroupName));
        Iterator it = measurePointVOMap.keySet().iterator();
        List<Map<String, Object>> list = Lists.newArrayList();
        while (it.hasNext()) {
            String key = it.next().toString();
            List<MeasurePointVO> points = measurePointVOMap.get(key);
            points = points.stream().sorted(Comparator.comparing(MeasurePointVO::getTagName)).collect(Collectors.toList());
            Map map = Maps.newHashMap();
            map.put("type", key);
            map.put("points", points);
            list.add(map);
        }
        return list;
    }

    @Override
    public Map<String, SensorTrendVO> getTrendHotSpotDetails(Long deviceId) {
        List<String> tagIds = mdSensorService.getMdSensorsByDeviceId(deviceId).stream().map(d -> d.getTagId())
                .collect(Collectors.toList());
        Map<String, SensorTrendVO> sensorTVos = getTrendHotSpotDetails(tagIds);
        return sensorTVos;
    }

    @Override
    public Map<String, SensorTrendVO> getSubSystemTrendHotSpotDetails(Long subSystemId) {
        List<String> tagIds = mdSensorService.getMdSensorBySubSystemId(subSystemId).stream().map(d -> d.getTagId())
                .collect(Collectors.toList());
        Map<String, SensorTrendVO> sensorTVos = getTrendHotSpotDetails(tagIds);
        return sensorTVos;
    }

    @Override
    public Map<String, SensorTrendVO> getTrendHotSpotDetails(List<String> tagIds) {
        List<SensorTrendVO> txSensorTrends = algorithmCacheService.getTrendRecognition(tagIds).stream()
                .filter(tt -> tt != null).map(tt -> {
                    SensorTrendVO sv = new SensorTrendVO();
                    BeanUtils.copyProperties(tt, sv);
                    return sv;
                }).collect(Collectors.toList());
        Map<String, SensorTrendVO> sensorTVos = txSensorTrends.stream()
                .collect(Collectors.toMap(SensorTrendVO::getTagId, stv -> stv));
        return sensorTVos;
    }
}

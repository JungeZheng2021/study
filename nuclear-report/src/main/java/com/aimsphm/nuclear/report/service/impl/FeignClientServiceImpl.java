package com.aimsphm.nuclear.report.service.impl;

import com.aimsphm.nuclear.report.service.FeignClientService;
import org.springframework.stereotype.Service;


/**
 * @Package: com.aimsphm.nuclear.report.service.impl
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2020/6/12 14:43
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/6/12 14:43
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Service
public class FeignClientServiceImpl implements FeignClientService {
//    @Autowired
//    private MdDeviceMapper deviceMapper;
//
//    @Autowired
//    private CoreServiceFeignClient coreFeignClient;
//    @Autowired
//    private AlgorithmServiceFeignClient algorithmFeignClient;
//
//    @Autowired
//    private HistoryServiceFeignClient historyFeignClient;
//    @Autowired
//    private VibrationAnalysisFeignClient analysisFeignClient;
//
//    /**
//     * 数据结构中的key
//     */
//    public static final String DATA_SON = "son";
//
//    @Override
//    public Map<String, String> getSensorTrendData(String tagList) {
//        ReturnResponse<Map<String, SensorTrendVO>> response = coreFeignClient.getTrendHotSpotDetails(tagList);
//        if (response == null || response.getResData() == null || response.getResData().isEmpty() || !RestResponseCode.OK.getCode().equals(response.getResCode())) {
//            return null;
//        }
//        Map<String, SensorTrendVO> resData = response.getResData();
//        Set<Map.Entry<String, SensorTrendVO>> entries = resData.entrySet();
//        Map<String, String> data = Maps.newHashMap();
//        for (Iterator<Map.Entry<String, SensorTrendVO>> it = entries.iterator(); it.hasNext(); ) {
//            Map.Entry<String, SensorTrendVO> next = it.next();
//            String key = next.getKey();
//            SensorTrendVO value = next.getValue();
//            if (value == null) {
//                continue;
//            }
//            data.put(key, value.desc(value.getTrend()));
//        }
//        return data;
//
//    }
//
//
//    /**
//     * 根据测点集合-折线图
//     *
//     * @param tags  测点集合
//     * @param query
//     * @return 折线图数据
//     */
//    @Override
//    public List<List<List<Object>>> getSensorTrendLineData(List<String> tags, ReportQueryBO query) {
//        Map<String, List<List<Object>>> resData = this.getSensorTrendLineDataMap(tags, query);
//        if (MapUtils.isEmpty(resData)) {
//            return null;
//        }
//        return tags.stream().map(pointId -> resData.get(pointId)).collect(Collectors.toList());
//    }
//
//    @Override
//    public Map<String, List<List<Object>>> getSensorTrendLineDataMap(List<String> tagList, ReportQueryBO query) {
//        ReturnResponse<Map<String, List<List<Object>>>> response = coreFeignClient.getTrendFeatureByTags(tagList, query.getStartTime(), query.getEndTime());
//        if (response == null || response.getResData() == null || !RestResponseCode.OK.getCode().equals(response.getResCode())) {
//            return null;
//        }
//        return response.getResData();
//    }
//
//    @Override
//    public Map<String, List<Object>> listVibrationAnalysisData(List<VibrationAnalysisQueryBO> tagList) {
//        return analysisFeignClient.listVibrationAnalysisData(tagList);
//    }
//
//    @Override
//    public Map<String, HBaseTimeSeriesDataDTO> getNewestRmsDataByMultiTag(VibrationRmsDataQueryBO query) {
//        return analysisFeignClient.getNewestRmsDataByMultiTag(query);
//    }
//
//    @Override
//    public List<List<Object>> getTrendDataByNonePiPoints(String tagList, Long startTime, Long endTime, String featureType, Integer isWifi) {
//        ReturnResponse<Map<String, Object>> response = analysisFeignClient.getTrendDataByNonePiPoints(tagList, startTime, endTime, featureType, isWifi);
//        if (response == null || response.getResData() == null || !RestResponseCode.OK.getCode().equals(response.getResCode())) {
//            return null;
//        }
//        Map<String, Object> resData = response.getResData();
//        String[] split = tagList.split(SYMBOL_COMMA_EN);
//        List<List<Object>> retVal = new ArrayList<>();
//        for (String tag : split) {
//            Object o = resData.get(tag);
//            if (Objects.isNull(o)) {
//                retVal.add(null);
//                continue;
//            }
//            Map<String, Object> data = (Map<String, Object>) o;
//            if (Objects.isNull(data)) {
//                retVal.add(null);
//                continue;
//            }
//            List<Object> pointList = (List<Object>) data.get("pointList");
//            retVal.add(pointList);
//        }
//        return retVal;
//    }
//
//    @Override
//    public List<List<Object>> getLineImageWithTrendFeatureData(String tagList, ReportQueryBO query) {
//        String[] split = tagList.split(SYMBOL_COMMA_EN);
//        List<String> tags = Arrays.asList(split);
//        ReturnResponse<Map<String, List<TrendFeatureCellDTO>>> response = algorithmFeignClient.getTrendFeatureByTags(tags, query.getStartTime(), query.getEndTime());
//        if (response == null || response.getResData() == null || !RestResponseCode.OK.getCode().equals(response.getResCode())) {
//            return null;
//        }
//        Map<String, List<TrendFeatureCellDTO>> resData = response.getResData();
//        if (MapUtils.isEmpty(resData)) {
//            return null;
//        }
//        List<List<Object>> retVal = new ArrayList<>();
//        for (String tag : tags) {
//            List<TrendFeatureCellDTO> value = resData.get(tag);
//            if (CollectionUtils.isEmpty(value)) {
//                retVal.add(null);
//                continue;
//            }
//            List<Object> collect = value.stream().map(vo -> new Object[]{vo.getTimestamp(), vo.getMean()}).sorted(Comparator.comparing(left -> ((Long) left[0]))).collect(Collectors.toList());
//            retVal.add(collect);
//        }
//        return retVal;
//    }
//
//    @Override
//    public TxPumpsnapshot getDeviceSnapshot(Long deviceId) {
//        ReturnResponse<TxPumpsnapshot> response = coreFeignClient.getRunningStatus(deviceId);
//        if (response == null || response.getResData() == null || !RestResponseCode.OK.getCode().equals(response.getResCode())) {
//            return null;
//        }
//        return response.getResData();
//    }
//
//    @Override
//    public List<List<Object[]>> getAlarmBarData(ReportQueryBO query) {
//        List<MdDeviceVO> deviceList = deviceMapper.selectDeviceBySubSystemId(query.getSubSystemId());
//        if (CollectionUtils.isEmpty(deviceList)) {
//            return null;
//        }
//        Map<String, List<List<MeasurePointTimesScaleVO>>> data = Maps.newLinkedHashMap();
//        for (int i = 0, len = deviceList.size(); i < len; i++) {
//            MdDeviceVO mdDevice = deviceList.get(i);
//            if (mdDevice == null) {
//                continue;
//            }
//            ReturnResponse<List<List<MeasurePointTimesScaleVO>>> response = coreFeignClient.statisticsWarmingPoints(mdDevice.getId(), query.getStartTime(), query.getEndTime());
//            if (response == null || response.getResData() == null || !RestResponseCode.OK.getCode().equals(response.getResCode())) {
//                continue;
//            }
//            data.put(mdDevice.getDeviceName(), response.getResData());
//        }
//        return null;
//    }
//
//    @Override
//    public Map.Entry<String, Double> getTagMaxDataByTagListWithTime(List<String> tagList, Long startTime, Long endTime) {
//        List<HistoryQueryResult> list = this.getTagHistoryDataByTagList(tagList, startTime, endTime);
//        if (CollectionUtils.isEmpty(list)) {
//            return null;
//        }
//        Map<String, Double> collect = list.stream().collect(Collectors.toMap(HistoryQueryResult::getTag, data -> {
//            double max = data.getPointList().stream().flatMapToDouble(o ->
//                    DoubleStream.of(Double.parseDouble(o.get(1) == null ? "0" : o.get(1).toString()))).max().orElse(Integer.MIN_VALUE);
//            return max;
//        }));
//        if (collect.isEmpty()) {
//            return null;
//        }
//        return collect.entrySet().stream().max(Map.Entry.comparingByValue()).get();
//    }
//
//    @Override
//    public List<HistoryQueryResult> getTagHistoryDataByTagList(List<String> tagList, Long startTime, Long endTime) {
//
//        ReturnResponse<Map<String, List<HistoryQueryResult>>> response = historyFeignClient.getTagHistoryDaTaByTagList(tagList, startTime, endTime);
//        if (response == null || response.getResData() == null || !RestResponseCode.OK.getCode().equals(response.getResCode())) {
//            return null;
//        }
//        return response.getResData().get(DATA_SON);
//    }
}

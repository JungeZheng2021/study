package com.aimsphm.nuclear.report.service.impl;

import com.aimsphm.nuclear.common.constant.SymbolConstant;
import com.aimsphm.nuclear.common.entity.*;
import com.aimsphm.nuclear.common.entity.bo.*;
import com.aimsphm.nuclear.common.entity.vo.*;
import com.aimsphm.nuclear.common.enums.AlarmTypeEnum;
import com.aimsphm.nuclear.common.enums.DeviceHealthEnum;
import com.aimsphm.nuclear.common.enums.PointCategoryEnum;
import com.aimsphm.nuclear.common.response.ResponseData;
import com.aimsphm.nuclear.common.service.*;
import com.aimsphm.nuclear.common.util.BigDecimalUtils;
import com.aimsphm.nuclear.common.util.DateUtils;
import com.aimsphm.nuclear.ext.service.MonitoringService;
import com.aimsphm.nuclear.report.constant.PlaceholderConstant;
import com.aimsphm.nuclear.report.enums.ReportCategoryEnum;
import com.aimsphm.nuclear.report.feign.HistoryServerFeignClient;
import com.aimsphm.nuclear.report.service.ReportDataService;
import com.aimsphm.nuclear.report.service.ReportFileService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.aimsphm.nuclear.common.constant.ReportConstant.*;
import static com.aimsphm.nuclear.common.response.ResponseData.SUCCESS_CODE;
import static com.aimsphm.nuclear.common.util.DateUtils.YEAR_MONTH_DAY_HH_MM_I;
import static com.aimsphm.nuclear.common.util.DateUtils.format;
import static com.aimsphm.nuclear.report.constant.PlaceholderConstant.*;


/**
 * @Package: com.aimsphm.nuclear.report.service.impl
 * @Description: <汽机报告数据准备类>
 * @Author: MILLA
 * @CreateDate: 2020/6/12 14:02
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/6/12 14:02
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Slf4j
@Service("RcvData")
public class FanReportDataServiceImpl implements ReportDataService {


    @Resource
    private ReportFileService fileService;

    @Resource
    private JobAlarmEventService eventService;

    @Resource
    private CommonDeviceService deviceService;

    @Resource
    private BizReportConfigService configService;

    @Resource
    private MonitoringService monitoringService;

    @Resource
    private CommonMeasurePointService pointService;

    @Resource
    private JobAlarmRealtimeService realtimeService;

    @Resource
    private HistoryServerFeignClient historyServerClient;


    @Override
    public Map<String, Object> getAllReportData(ReportQueryBO query) {
        //定义map
        Map<String, Object> data = new ConcurrentHashMap<>(16);

        Long deviceId = query.getDeviceId();
        TimeRangeQueryBO range = new TimeRangeQueryBO();
        range.setStart(query.getStartTime());
        range.setEnd(query.getEndTime());
        CommonDeviceDO device = deviceService.getById(deviceId);
        data.put(PlaceholderConstant.DEVICE_CODE, device.getDeviceCode());
        //起止时间
        data.put(PlaceholderConstant.RANGE_DATE, DateUtils.format(DateUtils.YEAR_MONTH_DAY_ZH, query.getStartTime()) + "至" + DateUtils.format(DateUtils.YEAR_MONTH_DAY_ZH, query.getEndTime()));
        //报告名称
        data.put(PlaceholderConstant.REPORT_NAME, StringUtils.isNotBlank(query.getReportName()) ? query.getReportName() : device.getDeviceCode() + "性能监测报告");
        //设备运行状态
        storeRunningStatus(deviceId, data);
        //设备运行统计
        Map<Integer, Long> integerLongMap = monitoringService.listRunningDuration(deviceId, range);
        //设备预警统计
        List<List<LabelVO>> lists = monitoringService.listWarningPoint(deviceId, range);
        //油质测点表
        storeOilPointValue(deviceId, data);
        List<BizReportConfigDO> list = configService.listConfigByDeviceId(deviceId);
        if (CollectionUtils.isEmpty(list)) {
            return data;
        }
        for (BizReportConfigDO config : list) {
            if (StringUtils.isBlank(config.getPlaceholder())) {
                continue;
            }
            //已经查询的数据截图
            baseData4Image(config, lists, integerLongMap);
            //趋势图
            trendData4Image(config, query);
            //动态融合诊断
            dynamicDiagnose4Image(data, config, query);
            data.put(config.getPlaceholder(), config);
        }
        return data;
    }

    /**
     * 动态阈值查询的时间范围[默认是最后依次报警的前后十二个小时]
     */
    @Value("#{${config.dynamic-threshold-time-gap:12*60*60*1000}}")
    private Long dynamicThresholdGap;

    /**
     * 获取动态诊断信息
     *
     * @param data
     * @param config
     * @param query
     * @return
     */
    private void dynamicDiagnose4Image(Map<String, Object> data, BizReportConfigDO config, ReportQueryBO query) {
        if (!ReportCategoryEnum.LINE_DYNAMIC_THRESHOLD.getCategory().equals(config.getCategory())) {
            return;
        }
        List<JobAlarmEventDO> eventDOS = eventService.listPointsWithAlarm(config.getSubSystemId(), config.getDeviceId(), query.getStartTime(), query.getEndTime());
        if (CollectionUtils.isEmpty(eventDOS)) {
            return;
        }
        List<ReportAlarmEventVO> imageList = new ArrayList<>();
        List<ReportFaultReasoningVO> reasoningList = new ArrayList<>();
//        AtomicInteger i = new AtomicInteger();
        eventDOS.stream().forEach(x -> {
//            if (i.get() > 1) {
//                return;
//            }
//            i.getAndIncrement();
            String pointIds = x.getPointIds();
            if (StringUtils.isBlank(pointIds)) {
                return;
            }
            log.info("execute ....{}", pointIds);
            List<String> pointIdList = Arrays.asList(pointIds.split(SYMBOL_COMMA_EN));
            Map<String, Long> modelIdPointId = pointService.listPointByDeviceIdInModel(pointIdList);

            HashSet<String> pointIdSet = new HashSet<>(pointIdList);
            Map<String, List<BizReportConfigDO>> images = new HashMap<>(16);
            pointIdSet.stream().forEach(pointId -> getDynamicDiagnoseImage(pointId, images, modelIdPointId, x, config, query));
            if (MapUtils.isNotEmpty(images)) {
                ReportAlarmEventVO vo = new ReportAlarmEventVO();
                vo.setEventName(x.getEventName());
                vo.setImages(images);
                imageList.add(vo);
            }

            //最后报警时间
            Date gmtLastAlarm = x.getGmtLastAlarm();
            log.info("融合诊断： pointIdList: {}, start: {},end: {}", pointIdList, DateUtils.format(query.getStartTime()), DateUtils.format(query.getEndTime()));
            ResponseData<List<FaultReasoningVO>> response = historyServerClient.faultReasoning(pointIdList, x.getDeviceId(), gmtLastAlarm.getTime());
            if (Objects.nonNull(response) && CollectionUtils.isNotEmpty(response.getData()) && SUCCESS_CODE.equals(response.getCode())) {
                ReportFaultReasoningVO item = new ReportFaultReasoningVO();
                item.setEventName(x.getEventName());
                item.setReasoningList(response.getData());
                reasoningList.add(item);
            }
        });
        data.put(PARAGRAPH_GRAPH_DATA_ITEMS, imageList);
        data.put(PARAGRAPH_DIAGNOSIS_RESULTS, reasoningList);
    }

    private Map<String, List<BizReportConfigDO>> getDynamicDiagnoseImage(String pointId, Map<String, List<BizReportConfigDO>> result, Map<String, Long> modelIdPointId, JobAlarmEventDO x, BizReportConfigDO config, ReportQueryBO query) {
        //测点在不在模型中
        Long modelId = modelIdPointId.get(pointId);
        EventDataVO eventVO = new EventDataVO();
        QueryBO<JobAlarmRealtimeDO> queryBO = initialQuery(x, query, AlarmTypeEnum.THRESHOLD);
        //历史数据-阈值
        Map<String, List<Long>> realtimeList = realtimeService.listJobAlarmRealtimeWithParams(queryBO, Lists.newArrayList(pointId));
        //历史数据
        Map<String, HistoryDataWithThresholdVO> data = listHistoryFromServer(queryBO.getQuery(), query.getDeviceId(), Lists.newArrayList(pointId));
        if (MapUtils.isEmpty(data) || Objects.isNull(data.get(pointId)) || CollectionUtils.isEmpty(data.get(pointId).getChartData())) {
            return result;
        }
        log.info("实时数据查询： pointId: {}, start: {},end: {}", pointId, DateUtils.format(query.getStartTime()), DateUtils.format(query.getEndTime()));
        HistoryDataWithThresholdVO vo = data.get(pointId);
        eventVO.setAlias(vo.getAlias());
        eventVO.setPointId(vo.getPointId());
        //报警测点-阈值
        eventVO.setAlarmData(realtimeList.get(pointId));
        //实际值
        eventVO.setActualData(vo.getChartData());
        eventVO.setAlarmType(AlarmTypeEnum.THRESHOLD.getValue());
        setImages(result, config, eventVO, "实时数据");
        //参数自回归
        if (Objects.nonNull(modelId) && modelId != -1) {
            //算法数据
            Map<String, EventDataVO> eventData = listRealtimeHistoryFromServer(queryBO.getQuery(), query.getDeviceId(), Lists.newArrayList(pointId));
            EventDataVO dataVO = eventData.get(pointId);
            if (Objects.isNull(dataVO) || CollectionUtils.isEmpty(dataVO.getActualData())) {
                return null;
            }
            clearThresholdInfo(dataVO);
            log.info("算法数据： pointId: {}, start: {},end: {}", pointId, DateUtils.format(query.getStartTime()), DateUtils.format(query.getEndTime()));
            //报警测点-算法
            QueryBO<JobAlarmRealtimeDO> doQueryBO = initialQuery(x, query, AlarmTypeEnum.ALGORITHM);
            Map<String, List<Long>> realtimeEvent = realtimeService.listJobAlarmRealtimeWithParams(doQueryBO, Lists.newArrayList(pointId));
            if (MapUtils.isNotEmpty(realtimeEvent)) {
                dataVO.setAlarmData(realtimeEvent.get(pointId));
                dataVO.setAlarmType(AlarmTypeEnum.ALGORITHM.getValue());
                setImages(result, config, dataVO, "参数自回归估计值");
            }
            //残差值
            dataVO.setAlarmType(51);
            setImages(result, config, dataVO, "参数自回归残差值");
        }
        return result;
    }

    private void clearThresholdInfo(EventDataVO dataVO) {
        dataVO.setEarlyWarningLow(null);
        dataVO.setEarlyWarningHigh(null);
        dataVO.setThresholdHigh(null);
        dataVO.setThresholdHigher(null);
        dataVO.setThresholdLow(null);
        dataVO.setThresholdLower(null);
    }

    private void setImages(Map<String, List<BizReportConfigDO>> result, BizReportConfigDO config, EventDataVO eventVO, String imageName) {
        try {
            config.setTitle(imageName);
            File img = fileService.getImageFileWithData(config, eventVO, null);
            if (Objects.nonNull(img)) {
                BizReportConfigDO item = new BizReportConfigDO();
                //图片title
                item.setTitle(eventVO.getAlias() + imageName);
                item.setImage(img);
                //测点code
                item.setPointIds(eventVO.getPointId());
                //测点别名
                item.setRemark(eventVO.getAlias());
                List<BizReportConfigDO> dos = result.get(eventVO.getPointId());
                if (Objects.isNull(dos)) {
                    dos = new ArrayList<>();
                    result.put(eventVO.getPointId(), dos);
                }
                dos.add(item);
            }
        } catch (InterruptedException e) {
            log.error("get data  img failed...{}", e.getCause());
        }
    }

    private QueryBO<JobAlarmRealtimeDO> initialQuery(JobAlarmEventDO x, ReportQueryBO query, AlarmTypeEnum typeEnum) {
        ConditionsQueryBO rangeDate = new ConditionsQueryBO();
        rangeDate.setStart(query.getStartTime());
        long start = x.getGmtLastAlarm().getTime() - dynamicThresholdGap;
        long end = x.getGmtLastAlarm().getTime() + dynamicThresholdGap;
        long now = System.currentTimeMillis();
        end = end > now ? now : end;
        rangeDate.setStart(start);
        rangeDate.setEnd(end);
        JobAlarmRealtimeDO realtimeDO = new JobAlarmRealtimeDO();
        realtimeDO.setAlarmType(typeEnum.getValue());
        return new QueryBO(realtimeDO, rangeDate);
    }

    /**
     * 获取配置的趋势图[是降采样数据]
     *
     * @param config 配置信息
     * @param query  查询条件
     * @return
     */
    private void trendData4Image(BizReportConfigDO config, ReportQueryBO query) {
        String pointIds = config.getPointIds();
        if (!ReportCategoryEnum.LINE.getCategory().equals(config.getCategory()) || StringUtils.isBlank(pointIds)) {
            return;
        }
        List<String> pointIdList = Arrays.asList(pointIds.split(SYMBOL_COMMA_EN));
        TimeRangeQueryBO rangeDate = new TimeRangeQueryBO();
        rangeDate.setStart(query.getStartTime());
        rangeDate.setEnd(query.getEndTime());
        log.info("历史趋势数据查询： pointId: {}, start: {},end: {}", pointIdList, DateUtils.format(query.getStartTime()), DateUtils.format(query.getEndTime()));
        Map<String, HistoryDataWithThresholdVO> data = listHistoryFromServer(rangeDate, query.getDeviceId(), pointIdList);
        if (MapUtils.isEmpty(data)) {
            return;
        }
        List<List<List<Object>>> collect = pointIdList.stream().map(x -> {
            HistoryDataWithThresholdVO vo = data.get(x);
            if (Objects.isNull(vo) || CollectionUtils.isEmpty(vo.getChartData())) {
                return null;
            }
            return vo.getChartData();
        }).collect(Collectors.toList());
        try {
            File imag = this.fileService.getImageFileWithData(config, collect, null);
            if (Objects.nonNull(imag)) {
                config.setImage(imag);
            }
        } catch (InterruptedException e) {
            log.error("get history trend imag failed: {}", e.getCause());
        }
    }

    private Map<String, HistoryDataWithThresholdVO> listHistoryFromServer(TimeRangeQueryBO rangDate, Long deviceId, List<String> pointIdList) {
        try {
            HistoryQueryMultiBO bo = operateQueryParams(rangDate, deviceId, pointIdList);
            ResponseData<Map<String, HistoryDataWithThresholdVO>> response = historyServerClient.listHistoryWithPointList(bo);
            if (Objects.isNull(response) || Objects.isNull(response.getData()) || !SUCCESS_CODE.equals(response.getCode())) {
                return null;
            }
            return response.getData();
        } catch (Exception e) {
            log.error("history trend data failed ....{}", pointIdList);
        }
        return null;
    }

    private HistoryQueryMultiBO operateQueryParams(TimeRangeQueryBO rangDate, Long deviceId, List<String> pointIdList) {
        HistoryQueryMultiBO bo = new HistoryQueryMultiBO();
        bo.setPointIds(pointIdList);
        bo.setEnd(rangDate.getEnd());
        bo.setStart(rangDate.getStart());
        bo.setDeviceId(deviceId);
        return bo;
    }

    private Map<String, EventDataVO> listRealtimeHistoryFromServer(TimeRangeQueryBO rangDate, Long deviceId, List<String> pointIdList) {
        try {
            HistoryQueryMultiBO bo = operateQueryParams(rangDate, deviceId, pointIdList);
            ResponseData<Map<String, EventDataVO>> response = historyServerClient.listDataWithPointList(bo);
            if (Objects.isNull(response) || Objects.isNull(response.getData()) || !SUCCESS_CODE.equals(response.getCode())) {
                return null;
            }
            return response.getData();
        } catch (Exception e) {
            log.error("history realtime data ");
        }
        return null;
    }

    private void baseData4Image(BizReportConfigDO config, List<List<LabelVO>> lists, Map<Integer, Long> integerLongMap) {
        if (Objects.isNull(config.getSort())) {
            return;
        }
        File image = null;
        try {
            switch (config.getSort()) {
                //运行状态柱状图
                case 1:
                    image = getRunningDurationImage(config, integerLongMap);
                    break;
                case 2:
                    ////测点类型饼图
                    List<LabelVO> typeList = CollectionUtils.isNotEmpty(lists) ? lists.get(0) : Lists.newArrayList();
                    if (CollectionUtils.isNotEmpty(typeList)) {
                        image = fileService.getImageFileWithData(config, typeList, null);
                    }
                    break;
                case 3:
//                    事件级别饼图
                    List<LabelVO> levelList = CollectionUtils.isNotEmpty(lists) && lists.size() >= 2 ? lists.get(1) : Lists.newArrayList();
                    if (CollectionUtils.isNotEmpty(levelList)) {
                        image = fileService.getImageFileWithData(config, levelList, null);
                    }
                    break;
                case 4:
                    //算法报警趋势//趋势柱状图
                    List<LabelVO> trendList = CollectionUtils.isNotEmpty(lists) && lists.size() >= 3 ? lists.get(2) : Lists.newArrayList();
                    image = getWarningImage(config, trendList);
                    break;
                default:
                    break;
            }
        } catch (InterruptedException e) {
            log.error("get a error :{},{}", config, e);
        }
        config.setImage(image);
    }


    /**
     * 算法报警趋势-柱状图
     *
     * @param config
     * @param trendList
     * @return
     */
    private File getWarningImage(BizReportConfigDO config, List<LabelVO> trendList) throws InterruptedException {
        if (CollectionUtils.isEmpty(trendList)) {
            return null;
        }
        List<Object> charData = trendList.stream().filter(x -> Objects.nonNull(x.getValue())).map(LabelVO::getValue).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(charData)) {
            return null;
        }
        return fileService.getImageFileWithData(config, charData, null);
    }

    /**
     * 周期内运行状态-柱状图
     *
     * @param config
     * @param data
     * @return
     * @throws InterruptedException
     */
    private File getRunningDurationImage(BizReportConfigDO config, Map<Integer, Long> data) throws InterruptedException {
        if (MapUtils.isEmpty(data)) {
            return null;
        }
        List<Double> charData = Lists.newArrayList(
                format(MapUtils.getLong(data, DeviceHealthEnum.ALARM.getValue(), 0L), ChronoUnit.DAYS),
                format(MapUtils.getLong(data, DeviceHealthEnum.WARNING.getValue(), 0L), ChronoUnit.DAYS),
                format(MapUtils.getLong(data, DeviceHealthEnum.HEALTH.getValue(), 0L), ChronoUnit.DAYS),
                format(MapUtils.getLong(data, DeviceHealthEnum.STOP.getValue(), 0L), ChronoUnit.DAYS)
        );
        return fileService.getImageFileWithData(config, charData, null);
    }

    private void storeRunningStatus(Long deviceId, Map<String, Object> data) {
        DeviceStatusVO status = monitoringService.getRunningStatus(deviceId);
        if (Objects.isNull(status)) {
            return;
        }
        data.put(RUNNING_STATUS, DeviceHealthEnum.getDesc(status.getStatus()));
        data.put(RUNNING_START_TIME, Objects.nonNull(status.getStartTime()) ? DateUtils.format(YEAR_MONTH_DAY_HH_MM_I, status.getStartTime()) : WORD_BLANK);
        data.put(RUNNING_CONTINUE_TIME, format(status.getContinuousRunningTime(), ChronoUnit.DAYS) + " 天");
        data.put(RUNNING_TOTAL_TIME, format(status.getTotalRunningTime(), ChronoUnit.DAYS) + " 天");
        data.put(RUNNING_STOP_TIMES, status.getStopTimes() + " 次");
    }

    /**
     * 油质测点分析
     *
     * @param deviceId
     * @param data
     */
    @Override
    public void storeOilPointValue(Long deviceId, Map<String, Object> data) {
        LambdaQueryWrapper<CommonMeasurePointDO> wrapper = Wrappers.lambdaQuery(CommonMeasurePointDO.class);
        wrapper.eq(CommonMeasurePointDO::getDeviceId, deviceId).eq(CommonMeasurePointDO::getCategory, PointCategoryEnum.OIL_QUALITY.getValue());
        List<MeasurePointVO> pointVOList = monitoringService.listPointByWrapper(wrapper);
        if (CollectionUtils.isEmpty(pointVOList)) {
            return;
        }
        pointVOList.stream().forEach(point -> {
            String key = SymbolConstant.HASH.concat(point.getFeatureType()).concat(CONNECTOR).concat(point.getFeature());
            data.put(key.concat(SymbolConstant.HASH), BigDecimalUtils.format(point.getValue(), 4));
            data.put(key.concat(CONNECTOR + FIX_DECIDE).concat(SymbolConstant.HASH), point.getStatusDesc());
            data.put(key.concat(CONNECTOR + FIX_TH).concat(SymbolConstant.HASH), getThresholdDesc(point));
        });
    }

    /**
     * 获取阈值描述
     *
     * @param point
     * @return
     */
    private String getThresholdDesc(MeasurePointVO point) {
        StringBuilder sb = new StringBuilder();
        if (Objects.nonNull(point.getEarlyWarningLow())) {
            checkFirst(sb);
            sb.append("低预警：").append(point.getEarlyWarningLow());
        }
        if (Objects.nonNull(point.getEarlyWarningHigh())) {
            checkFirst(sb);
            sb.append("高预警：").append(point.getEarlyWarningHigh());
        }
        if (Objects.nonNull(point.getThresholdLow())) {
            checkFirst(sb);
            sb.append("低报：").append(point.getThresholdLow());
        }
        if (Objects.nonNull(point.getThresholdHigh())) {
            checkFirst(sb);
            sb.append("高报：").append(point.getThresholdHigh());
        }
        if (Objects.nonNull(point.getThresholdLower())) {
            checkFirst(sb);
            sb.append("低低报：").append(point.getThresholdLower());
        }
        if (Objects.nonNull(point.getThresholdHigher())) {
            checkFirst(sb);
            sb.append("高高报：").append(point.getThresholdHigher());
        }
        return sb.toString();
    }

    private void checkFirst(StringBuilder sb) {
        if (sb.length() > 0) {
            sb.append(";");
        }
    }
}
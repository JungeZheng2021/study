package com.aimsphm.nuclear.report.service.impl;

import com.aimsphm.nuclear.algorithm.entity.dto.FaultReportMarkResponseDTO;
import com.aimsphm.nuclear.algorithm.entity.dto.FaultReportResponseDTO;
import com.aimsphm.nuclear.common.constant.SymbolConstant;
import com.aimsphm.nuclear.common.entity.AlgorithmRulesConclusionDO;
import com.aimsphm.nuclear.common.entity.BizReportConfigDO;
import com.aimsphm.nuclear.common.entity.CommonMeasurePointDO;
import com.aimsphm.nuclear.common.entity.bo.ReportQueryBO;
import com.aimsphm.nuclear.common.entity.bo.TimeRangeQueryBO;
import com.aimsphm.nuclear.common.entity.vo.DeviceStatusVO;
import com.aimsphm.nuclear.common.entity.vo.LabelVO;
import com.aimsphm.nuclear.common.entity.vo.MeasurePointVO;
import com.aimsphm.nuclear.common.enums.DeviceHealthEnum;
import com.aimsphm.nuclear.common.enums.PointCategoryEnum;
import com.aimsphm.nuclear.common.service.*;
import com.aimsphm.nuclear.common.util.DateUtils;
import com.aimsphm.nuclear.ext.service.MonitoringService;
import com.aimsphm.nuclear.ext.service.RedisDataService;
import com.aimsphm.nuclear.report.constant.PlaceholderConstant;
import com.aimsphm.nuclear.report.entity.vo.GraphDataItemVO;
import com.aimsphm.nuclear.report.entity.vo.PieDataItemVO;
import com.aimsphm.nuclear.report.enums.FigureTypeEnum;
import com.aimsphm.nuclear.report.enums.ReportCategoryEnum;
import com.aimsphm.nuclear.report.service.ReportDataService;
import com.aimsphm.nuclear.report.service.ReportFileService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.aimsphm.nuclear.common.constant.ReportConstant.*;
import static com.aimsphm.nuclear.common.constant.SymbolConstant.SLASH_ZH;
import static com.aimsphm.nuclear.common.util.DateUtils.*;
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
    private MonitoringService monitoringService;

    @Resource
    private BizReportConfigService configService;

    @Resource
    private JobAlarmThresholdService thresholdService;

    @Resource
    private ReportFileService fileService;

    @Resource
    private JobAlarmEventService eventService;

    @Resource
    private BizDiagnosisResultService diagnosisResultService;

    @Override
    public Map<String, Object> getAllReportData(ReportQueryBO query) {
        //定义map
        Map<String, Object> data = new ConcurrentHashMap<>(16);

        Long deviceId = query.getDeviceId();
        TimeRangeQueryBO range = new TimeRangeQueryBO();
        range.setStart(query.getStartTime());
        range.setEnd(query.getEndTime());

        //创建时间
        data.put(PlaceholderConstant.CREATE_DATE, DateUtils.formatCurrentDateTime(YEAR_MONTH_DAY_HH_MM_SS_ZH));
        //报告名称
        data.put(PlaceholderConstant.REPORT_NAME, StringUtils.isNotBlank(query.getReportName()) ? query.getReportName() : WORD_BLANK);
        //设备运行状态
        storeRunningStatus(deviceId, data);
        //设备运行统计
        Map<Integer, Long> integerLongMap = monitoringService.listRunningDuration(deviceId, range);
        //设备预警统计
        List<List<LabelVO>> lists = monitoringService.listWarningPoint(deviceId, range);
        //测点类型
        List<LabelVO> typeList = CollectionUtils.isNotEmpty(lists) ? lists.get(0) : Lists.newArrayList();
        //报警级别
        List<LabelVO> levelList = CollectionUtils.isNotEmpty(lists) && lists.size() >= 3 ? lists.get(2) : Lists.newArrayList();
        //算法报警趋势
        List<LabelVO> trendList = CollectionUtils.isNotEmpty(lists) && lists.size() >= 4 ? lists.get(3) : Lists.newArrayList();
        //油质测点表
        storeOilPointValue(deviceId, data);
        //阈值报警
        List<Object[]> alarmThresholdList = thresholdService.listCurrentThresholdAlarm(deviceId);
        if (CollectionUtils.isNotEmpty(alarmThresholdList)) {
            data.put(TABLE_ALARM_THRESHOLD, alarmThresholdList);
        }
        List<BizReportConfigDO> list = configService.listConfigByDeviceId(deviceId);
        if (CollectionUtils.isEmpty(list)) {
            return data;
        }
        for (BizReportConfigDO config : list) {
            if (StringUtils.isBlank(config.getPlaceholder())) {
                continue;
            }
            File image = null;
            try {
                switch (config.getSort()) {
                    //运行状态
                    case 1:
                        image = getRunningDurationImage(config, integerLongMap);
                        break;
                    //算法报警
                    case 2:
                        image = getAlarmStatisticsImage(config, typeList, levelList);
                        break;
                    //报警统计
                    case 3:
                        image = getWarningImage(config, trendList);
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                log.error("get a error :{},{}", config, e);
            }
            if (Objects.nonNull(image)) {
                config.setImage(image);
            }
            data.put(config.getPlaceholder(), config);
        }
        //振动图谱分析
        storeDiagnosisValue(deviceId, data);
        return data;
    }

    /**
     * 振动图谱分析
     * 目前逻辑： 查询该设备下最新活动的事件，无结果的话不做任何操作，有的话调用故障推理算法，并将算法结果展示在成图谱分析和故障推理结果
     * 有可能改成：执行一遍状态监测算法，如果有报警输出，再执行故障推理算法，无的话，不做任何操作
     *
     * @param deviceId
     * @param data
     */
    private void storeDiagnosisValue(Long deviceId, Map<String, Object> data) {
        Long eventId = eventService.getNewestEventIdByDeviceId(deviceId);
        if (Objects.isNull(eventId)) {
            return;
        }
        Map<String, List<FaultReportResponseDTO>> reportData = diagnosisResultService.saveRulesConclusion(eventId, 1);
//        List<FaultReportResponseDTO> list = JSON.parseArray(s, FaultReportResponseDTO.class);
        if (MapUtils.isEmpty(reportData)) {
            return;
        }
        List<FaultReportResponseDTO> list = Lists.newArrayList();
        reportData.entrySet().stream().filter(x -> CollectionUtils.isNotEmpty(x.getValue())).forEach(x -> list.addAll(x.getValue()));
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        List<GraphDataItemVO> graphItems = Lists.newArrayList();
        for (int i = 0, len = list.size(); i < len; i++) {
            GraphDataItemVO vo = new GraphDataItemVO();
            FaultReportResponseDTO item = list.get(i);
            String figureType = FigureTypeEnum.getDesc(item.getFigureType());
            String sensorCode = item.getSensorCode();
            List<List<Double>> curve = item.getCurve();
            List<FaultReportMarkResponseDTO> mark = item.getMark();
            String title = i + 1 + SLASH_ZH.concat(sensorCode).concat(figureType).concat(FIX_EXCEPTION);
            vo.setTitle(title);
            List<String> marks = Lists.newArrayList();
            if (CollectionUtils.isNotEmpty(mark)) {
                mark.stream().filter(x -> CollectionUtils.isNotEmpty(x.getAnnotation())).forEach(x -> marks.addAll(x.getAnnotation()));
                String desc = PRE_MARK.concat(String.join(SLASH_ZH, marks));
                vo.setDesc(desc);
            }
            List<List<List<Double>>> charData = new ArrayList<>();
            charData.add(curve);
            File image = getGraphImage(figureType, charData);
            vo.setImage(image);
            graphItems.add(vo);
        }
        data.put(PARAGRAPH_GRAPH_DATA_ITEMS, graphItems);
        List<AlgorithmRulesConclusionDO> results = diagnosisResultService.lastRulesConclusionWithEventId(eventId);
        if (CollectionUtils.isNotEmpty(results)) {
            data.put(PARAGRAPH_DIAGNOSIS_RESULTS, results);
        }
    }

    /**
     * 获取图谱的异常图片
     *
     * @param figureType
     * @param charData
     * @return
     */
    private File getGraphImage(String figureType, List<List<List<Double>>> charData) {
        try {
            BizReportConfigDO config = new BizReportConfigDO();
            config.setCategory(ReportCategoryEnum.LINE_SPECTRUM.getCategory());
            config.setLegend(figureType);
            return fileService.getImageFileWithData(config, charData, null);
        } catch (InterruptedException e) {
            log.error("获取频谱图片报错：{}", e);
        }
        return null;
    }

    private File getAlarmStatisticsImage(BizReportConfigDO config, List<LabelVO> typeList, List<LabelVO> levelList) throws InterruptedException {
        List<PieDataItemVO> charData = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(typeList)) {
            PieDataItemVO itemVO = new PieDataItemVO();
            itemVO.setData(typeList);
            itemVO.setTitle("测点类型");
            itemVO.setUnit("次");
            charData.add(itemVO);
        }
        if (CollectionUtils.isNotEmpty(levelList)) {
            PieDataItemVO itemVO = new PieDataItemVO();
            itemVO.setData(levelList);
            itemVO.setTitle("报警级别");
            itemVO.setUnit("次");
            charData.add(itemVO);
        }
        if (CollectionUtils.isEmpty(charData)) {
            return null;
        }
        return fileService.getImageFileWithData(config, charData, null);
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
    private void storeOilPointValue(Long deviceId, Map<String, Object> data) {
        LambdaQueryWrapper<CommonMeasurePointDO> wrapper = Wrappers.lambdaQuery(CommonMeasurePointDO.class);
        wrapper.eq(CommonMeasurePointDO::getDeviceId, deviceId).eq(CommonMeasurePointDO::getCategory, PointCategoryEnum.OIL_QUALITY.getValue());
        List<MeasurePointVO> pointVOList = monitoringService.listPointByWrapper(wrapper);
        if (CollectionUtils.isEmpty(pointVOList)) {
            return;
        }
        pointVOList.stream().forEach(point -> {
            String key = SymbolConstant.HASH.concat(point.getFeatureType()).concat(CONNECTOR).concat(point.getFeature());
            data.put(key.concat(SymbolConstant.HASH), point.getValue());
            data.put(key.concat(CONNECTOR + FIX_DECIDE).concat(SymbolConstant.HASH), point.getStatusDesc());
        });
    }
//    测试数据
//    String s =
//                "[\n" +
//                        "        {\n" +
//                        "            \"sensorCode\":\"6M2DVC403MV-N\",\n" +
//                        "            \"figureType\":\"envelopeSpectrum\",\n" +
//                        "            \"curve\":[\n" +
//                        "                [\n" +
//                        "                    0,\n" +
//                        "                    6.729337952756514e-17\n" +
//                        "                ],\n" +
//                        "                [\n" +
//                        "                    0.625,\n" +
//                        "                    4.520986294844187e-17\n" +
//                        "                ],\n" +
//                        "                [\n" +
//                        "                    1.25,\n" +
//                        "                    4.764164238798577e-17\n" +
//                        "                ],\n" +
//                        "                [\n" +
//                        "                    1.875,\n" +
//                        "                    4.139547596052102e-17\n" +
//                        "                ],\n" +
//                        "                [\n" +
//                        "                    2.5,\n" +
//                        "                    3.0080554834491434e-17\n" +
//                        "                ],\n" +
//                        "                [\n" +
//                        "                    3.125,\n" +
//                        "                    6.845341933219464e-17\n" +
//                        "                ],\n" +
//                        "                [\n" +
//                        "                    3.75,\n" +
//                        "                    3.831880668577914e-17\n" +
//                        "                ],\n" +
//                        "                [\n" +
//                        "                    4.375,\n" +
//                        "                    6.771969193450171e-17\n" +
//                        "                ],\n" +
//                        "                [\n" +
//                        "                    1255.625,\n" +
//                        "                    4.212149517108385e-17\n" +
//                        "                ],\n" +
//                        "                [\n" +
//                        "                    1256.25,\n" +
//                        "                    2.2705804234531582e-17\n" +
//                        "                ],\n" +
//                        "                [\n" +
//                        "                    1256.875,\n" +
//                        "                    4.356402612254552e-17\n" +
//                        "                ],\n" +
//                        "                [\n" +
//                        "                    1260.625,\n" +
//                        "                    1.1396187569950748e-16\n" +
//                        "                ],\n" +
//                        "                [\n" +
//                        "                    1261.25,\n" +
//                        "                    7.603454666560213e-17\n" +
//                        "                ],\n" +
//                        "                [\n" +
//                        "                    1261.875,\n" +
//                        "                    3.017589574357483e-17\n" +
//                        "                ],\n" +
//                        "                [\n" +
//                        "                    1262.5,\n" +
//                        "                    7.338445660264668e-18\n" +
//                        "                ],\n" +
//                        "                [\n" +
//                        "                    1263.125,\n" +
//                        "                    1.4501759194018695e-17\n" +
//                        "                ],\n" +
//                        "                [\n" +
//                        "                    1263.75,\n" +
//                        "                    2.655476563723834e-17\n" +
//                        "                ],\n" +
//                        "                [\n" +
//                        "                    1264.375,\n" +
//                        "                    6.330708592517079e-17\n" +
//                        "                ],\n" +
//                        "                [\n" +
//                        "                    1265,\n" +
//                        "                    9.30131944573396e-17\n" +
//                        "                ],\n" +
//                        "                [\n" +
//                        "                    1265.625,\n" +
//                        "                    7.953770399274342e-17\n" +
//                        "                ],\n" +
//                        "                [\n" +
//                        "                    1266.25,\n" +
//                        "                    2.9448059839322125e-17\n" +
//                        "                ],\n" +
//                        "                [\n" +
//                        "                    1266.875,\n" +
//                        "                    4.0775871749976656e-17\n" +
//                        "                ],\n" +
//                        "                [\n" +
//                        "                    1267.5,\n" +
//                        "                    3.148973235442091e-17\n" +
//                        "                ],\n" +
//                        "                [\n" +
//                        "                    1268.125,\n" +
//                        "                    1.6725814363778495e-17\n" +
//                        "                ],\n" +
//                        "                [\n" +
//                        "                    1268.75,\n" +
//                        "                    1.440734773794269e-17\n" +
//                        "                ],\n" +
//                        "                [\n" +
//                        "                    1269.375,\n" +
//                        "                    1.1697720971325402e-17\n" +
//                        "                ],\n" +
//                        "                [\n" +
//                        "                    1270,\n" +
//                        "                    1.7908964812190707e-17\n" +
//                        "                ],\n" +
//                        "                [\n" +
//                        "                    1270.625,\n" +
//                        "                    3.14492540902047e-17\n" +
//                        "                ],\n" +
//                        "                [\n" +
//                        "                    1271.25,\n" +
//                        "                    3.048005052410712e-17\n" +
//                        "                ],\n" +
//                        "                [\n" +
//                        "                    1271.875,\n" +
//                        "                    2.063043448453698e-17\n" +
//                        "                ],\n" +
//                        "                [\n" +
//                        "                    1272.5,\n" +
//                        "                    3.192755129031135e-17\n" +
//                        "                ],\n" +
//                        "                [\n" +
//                        "                    1273.125,\n" +
//                        "                    2.3316069015928837e-17\n" +
//                        "                ],\n" +
//                        "                [\n" +
//                        "                    1273.75,\n" +
//                        "                    1.520265198492203e-17\n" +
//                        "                ],\n" +
//                        "                [\n" +
//                        "                    1274.375,\n" +
//                        "                    1.1340972216514965e-17\n" +
//                        "                ],\n" +
//                        "                [\n" +
//                        "                    1275,\n" +
//                        "                    1.3019172525862781e-17\n" +
//                        "                ],\n" +
//                        "                [\n" +
//                        "                    1275.625,\n" +
//                        "                    1.5900905185076648e-17\n" +
//                        "                ],\n" +
//                        "                [\n" +
//                        "                    1276.25,\n" +
//                        "                    1.9435577863989444e-17\n" +
//                        "                ],\n" +
//                        "                [\n" +
//                        "                    1276.875,\n" +
//                        "                    1.553687436422944e-17\n" +
//                        "                ],\n" +
//                        "                [\n" +
//                        "                    1277.5,\n" +
//                        "                    1.6788496233895393e-17\n" +
//                        "                ],\n" +
//                        "                [\n" +
//                        "                    1278.125,\n" +
//                        "                    2.3612352031627774e-17\n" +
//                        "                ],\n" +
//                        "                [\n" +
//                        "                    1278.75,\n" +
//                        "                    7.479967911960386e-18\n" +
//                        "                ],\n" +
//                        "                [\n" +
//                        "                    1279.375,\n" +
//                        "                    1.4194478517987257e-18\n" +
//                        "                ]\n" +
//                        "            ],\n" +
//                        "            \"mark\":[\n" +
//                        "                {\n" +
//                        "                    \"markType\":\"somePoint\",\n" +
//                        "                    \"coordinate\":[\n" +
//                        "                        [\n" +
//                        "                            31.25,\n" +
//                        "                            1\n" +
//                        "                        ]\n" +
//                        "                    ],\n" +
//                        "                    \"annotation\":[\n" +
//                        "                        \"故障频率\"\n" +
//                        "                    ]\n" +
//                        "                }\n" +
//                        "            ]\n" +
//                        "        }\n" +
//                        "    ]\n";
}
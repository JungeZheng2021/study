package com.aimsphm.nuclear.core.controller;


import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.aimsphm.nuclear.common.entity.MdSensor;
import com.aimsphm.nuclear.common.entity.TxAlarmRealtime;
import com.aimsphm.nuclear.common.entity.bo.AlarmEventQueryPageBO;
import com.aimsphm.nuclear.common.entity.bo.AlarmRealtimeQueryPageBO;
import com.aimsphm.nuclear.common.enums.*;
import com.aimsphm.nuclear.common.excelutil.EasyExcelHelper;
import com.aimsphm.nuclear.common.service.MdDeviceService;
import com.aimsphm.nuclear.common.service.MdSensorService;
import com.aimsphm.nuclear.core.excel.entity.AlarmEventExcelModel;
import com.aimsphm.nuclear.core.excel.entity.AlarmRealTimeExcelModel;
import com.aimsphm.nuclear.core.service.CommonAlarmService;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import com.aimsphm.nuclear.common.entity.TxAlarmEvent;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RestController;
import com.aimsphm.nuclear.common.service.TxAlarmEventService;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.aimsphm.nuclear.common.pojo.QueryObject;
import com.aimsphm.nuclear.common.response.ResponseUtils;
import com.aimsphm.nuclear.common.util.CommonUtil;

import javax.servlet.http.HttpServletResponse;

/**
 * @author lu.yi
 * @since 2020-04-28
 */
@Slf4j
@RestController
@RequestMapping("txAlarmEvent")
@Api(tags = "通用报警事件相关")
public class TxAlarmEventController {

    @Autowired
    private TxAlarmEventService iTxAlarmEventService;

    @Autowired
    private CommonAlarmService commonAlarmService;

    @Autowired
    private MdDeviceService mdDeviceService;
    @Autowired
    private MdSensorService mdSensorService;

    @PostMapping("list")
    @ApiOperation(value = "获取报警事件列表")
    public Object getTxAlarmEventList(@RequestBody QueryObject<TxAlarmEvent> queryObject) {
        QueryWrapper<TxAlarmEvent> queryWrapper = CommonUtil.initQueryWrapper(queryObject);
        return ResponseUtils.success(iTxAlarmEventService.page(queryObject.getPage() == null ? new Page() : queryObject.getPage(), queryWrapper));
    }

    @PostMapping
    @ApiOperation(value = "新增报警接口")
    public Object saveTxAlarmEvent(@RequestBody TxAlarmEvent TxAlarmEvent) {
        if(AlgorithmStatusEnum.HANDLED.getValue().equals(TxAlarmEvent.getAlarmStatus()))
        {
            TxAlarmEvent.setStopFlag(true);
        }else{
            TxAlarmEvent.setStopFlag(false);
        }
        return ResponseUtils.success(iTxAlarmEventService.save(TxAlarmEvent));
    }

    @PutMapping
    @ApiOperation(value = "修改报警事件接口")
    public Object modifyTxAlarmEvent(@RequestBody TxAlarmEvent TxAlarmEvent) {
        if(AlgorithmStatusEnum.HANDLED.getValue().equals(TxAlarmEvent.getAlarmStatus()))
        {
            TxAlarmEvent.setStopFlag(true);
        }else{
            TxAlarmEvent.setStopFlag(false);
        }
        return ResponseUtils.success(iTxAlarmEventService.updateById(TxAlarmEvent));
    }

    @DeleteMapping
    @ApiOperation(value = "删除报警事件接口")
    public Object delTxAlarmEvent(@RequestBody String ids) {
        return ResponseUtils.success(iTxAlarmEventService.removeByIds(Arrays.asList(ids.split(","))));
    }

    @ApiOperation(value = "获取报警事件模糊查询接口")
    @PostMapping("search")
    public Object searchAlarms(@RequestBody AlarmEventQueryPageBO alarmEventQueryPageBO) {
        return ResponseUtils.success(commonAlarmService.searchTxAlarmEvents(alarmEventQueryPageBO));
    }

    @ApiOperation(value = "获取实时报警模糊查询接口")
    @PostMapping("searchRealTime")
    public Object searchRTAlarms(@RequestBody AlarmRealtimeQueryPageBO alarmRealtimeQueryPageBO) {
        Object obj = commonAlarmService.searchTxAlarmRealTime(alarmRealtimeQueryPageBO);
        return ResponseUtils.success(obj);
    }


    @ApiOperation(value = "导出报警事件excel")
    @GetMapping("exportAlarmEvents")
    public void exportAlarmEvents(HttpServletResponse response, AlarmEventQueryPageBO alarmEventQueryPageBO) throws IOException {
//        AlarmEventQueryPageBO alarmEventQueryPageBO = new AlarmEventQueryPageBO();
//        alarmEventQueryPageBO.setCurrent(1);
//        alarmEventQueryPageBO.setSize(10);
//        DefaultConverterLoader.loadDefaultWriteConverter();
        Page<TxAlarmEvent> page = commonAlarmService.searchTxAlarmEvents(alarmEventQueryPageBO);
        List<AlarmEventExcelModel> aemlist = Lists.newArrayList();
        List<TxAlarmEvent> alist = page.getRecords();
        for (int i = 0; i < alist.size(); i++) {
            AlarmEventExcelModel aem = new AlarmEventExcelModel();
            TxAlarmEvent te = alist.get(i);
            BeanUtils.copyProperties(te, aem);

            aem.setSeq(i);
            if (!ObjectUtils.isEmpty(aem.getAlarmLevel())) {
                aem.setAlarmLevelContent(AlgorithmLevelEnum.getByValue(aem.getAlarmLevel().byteValue()).getDesc());
            }
            if (!ObjectUtils.isEmpty(aem.getAlarmStatus())) {
                aem.setAlarmStatusContent(AlgorithmStatusEnum.getByValue(aem.getAlarmStatus()).getDesc());
            }
            if (aem.getStopFlag()) {
                aem.setStopFlagConetent("已结束");
            } else {
                aem.setStopFlagConetent("未结束");
            }
            if (!ObjectUtils.isEmpty(aem.getAlarmReason())) {
                aem.setAlarmReasonContent(AlarmReasonEnum.getByValue(aem.getAlarmReason()).getDesc());
            }
            aemlist.add(aem);
        }
        EasyExcelHelper.WriteExcel(response, aemlist, AlarmEventExcelModel.class, "报警事件列表", "报警事件");
    }

    @ApiOperation(value = "导出实时报警excel")
    @GetMapping("exportAlarmRealTimes")
    public void exportAlarmRealTimes(HttpServletResponse response, AlarmRealtimeQueryPageBO alarmRealtimeQueryPageBO) throws IOException {
        Page<TxAlarmRealtime> page = commonAlarmService.searchTxAlarmRealTime(alarmRealtimeQueryPageBO);
        List<AlarmRealTimeExcelModel> aemlist = Lists.newArrayList();
        List<TxAlarmRealtime> alist = page.getRecords();
        for (int i = 0; i < alist.size(); i++) {
            AlarmRealTimeExcelModel aem = new AlarmRealTimeExcelModel();
            TxAlarmRealtime te = alist.get(i);
            BeanUtils.copyProperties(te, aem);

            aem.setSeq(i);
            if (!ObjectUtils.isEmpty(aem.getAlarmType())) {
                aem.setAlarmTypeContent(AlarmTypeEnum.getByValue(aem.getAlarmType()).getDesc());
            }
            if (!ObjectUtils.isEmpty(aem.getDeviceId())) {
                aem.setDeviceName(mdDeviceService.getOnlyNameById(aem.getDeviceId()));
            }
            if (!ObjectUtils.isEmpty(aem.getSensorTagid())) {
                MdSensor mdSensor = mdSensorService.getMdSensorsByTagIds(aem.getSensorTagid());
                String sensorName = mdSensor != null ? mdSensor.getSensorName() : "";
                aem.setSensorName(sensorName);
            }
            if (!ObjectUtils.isEmpty(aem.getTrend())) {
                if (aem.getTrend() == 20) {
                    aem.setTrendContent("上升");
                } else if (aem.getTrend() == 10) {
                    aem.setTrendContent("下降");
                } else {
                    aem.setTrendContent("其他");
                }
            }
            if (!ObjectUtils.isEmpty(aem.getEvaluation())) {
                aem.setEvaluationContent(AlarmEvaluationEnum.getByValue(aem.getEvaluation()).getDesc());
            }
            aemlist.add(aem);
        }
        EasyExcelHelper.WriteExcel(response, aemlist, AlarmRealTimeExcelModel.class, "实时报警列表", "实时报警");
    }
}

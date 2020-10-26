package com.aimsphm.nuclear.core.job;

import com.aimsphm.nuclear.common.annotation.DistributedLock;
import com.aimsphm.nuclear.common.entity.TxAlarmRealtime;
import com.aimsphm.nuclear.common.entity.vo.MeasurePointVO;
import com.aimsphm.nuclear.common.enums.AlarmEvaluationEnum;
import com.aimsphm.nuclear.common.mapper.MdDeviceMapper;
import com.aimsphm.nuclear.common.service.HotSpotDataService;
import com.aimsphm.nuclear.common.service.TxAlarmRealtimeService;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 换热器定时任务
 *
 * @author milla
 */
@Component
@Slf4j
public class ExchangerJob {
    @Autowired
    HotSpotDataService spotDataService;
    @Autowired
    MdDeviceMapper mdDeviceMapper;
    @Autowired
    TxAlarmRealtimeService realtimeService;

    //定时器 每10秒执行一次
    @Scheduled(cron = "*/10 * * * * ?")
    //分布式执行锁
    @DistributedLock("ExchangerJobDistributeLock")
    public void monitor() {
        try {
            Set<Long> deviceIds = Sets.newHashSet();
            //换热器
            List<Long> exchangerList = mdDeviceMapper.selectDeviceIdsBySubSystemId(6L);
            //阀
            List<Long> valveList = mdDeviceMapper.selectDeviceIdsBySubSystemId(7L);

            if (CollectionUtils.isNotEmpty(exchangerList)) {
                deviceIds.addAll(exchangerList);
            }
            if (CollectionUtils.isNotEmpty(valveList)) {
                deviceIds.addAll(valveList);
            }
            if (CollectionUtils.isEmpty(deviceIds)) {
                return;
            }
            for (Long deviceId : deviceIds) {
                List<MeasurePointVO> vo = spotDataService.getWarmingPumpPointsByDeviceId(deviceId);
                if (CollectionUtils.isEmpty(vo)) {
                    continue;
                }
                List<TxAlarmRealtime> collect = vo.stream().filter(o -> Objects.nonNull(o) && StringUtils.isNotBlank(o.getParentTag())).flatMap(o -> {
//                    List<TxAlarmRealtime> collect = vo.stream().flatMap(o -> {
                    TxAlarmRealtime realtime = new TxAlarmRealtime();
                    realtime.setDeviceId(deviceId);//所属设备-设备id
                    realtime.setDeviceName(o.getTagName());//设备名称
                    realtime.setSensorTagName(o.getTagName());//测点名称
                    realtime.setIsAlgorithmAlarm(false);//非算法报警
                    realtime.setAlarmTime(new Date());//报警时间
                    realtime.setAlarmType(1);//报警类型-阈值报警
                    realtime.setSubSystemId(Long.parseLong(o.getParentTag()));
                    String statusCause = o.getStatusCause();
                    realtime.setModelId(-1L);
                    realtime.setAlarmCode(String.valueOf(o.getStatus()));//是阈值报警，将报警级别写到alarmCode中
                    realtime.setSensorTagid(o.getTag());
                    if (StringUtils.isNotBlank(statusCause)) {
                        String evaluationContent = statusCause.substring(0, statusCause.indexOf(":") != -1 ? statusCause.indexOf(":") : statusCause.length());
                        realtime.setEvaluation(AlarmEvaluationEnum.getByDesc(evaluationContent).getValue());
                        //                    测点趋势
                        if (evaluationContent.contains("高")) {
                            realtime.setTrend(20);//上升
                        }
                        if (evaluationContent.contains("低")) {
                            realtime.setTrend(10);//下降
                        }
                    }
                    return Stream.of(realtime);
                }).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(collect)) {
                    continue;
                }
                boolean b = realtimeService.saveBatch(collect);
                log.info("定时任务插入实时报警结果:{}", b);
            }
        } catch (Exception e) {
            log.error("定时插入实时报警失败：{}", e);
        } finally {
        }
    }

}

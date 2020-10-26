package com.aimsphm.nuclear.pump.service.impl;

import com.aimsphm.nuclear.common.entity.*;
import com.aimsphm.nuclear.common.entity.vo.MeasurePointVO;
import com.aimsphm.nuclear.common.mapper.MdDeviceMapper;
import com.aimsphm.nuclear.common.mapper.TxAlarmEventMapper;
import com.aimsphm.nuclear.common.service.HotSpotDataService;
import com.aimsphm.nuclear.core.vo.PumpPanoramaVO;
import com.aimsphm.nuclear.core.vo.RotaryMonitorVO;
import com.aimsphm.nuclear.pump.service.SystemPanoramaService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Package: com.aimsphm.nuclear.pump.service.impl
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2020/4/3 13:35
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/4/3 13:35
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Service
public class SystemPanoramaServiceImpl implements SystemPanoramaService {

    @Autowired
    private HotSpotDataService redis;

    @Autowired
    private MdDeviceMapper deviceMapper;

    private final String commPointKey = "comm";

    /**
     * 最大值测点的前缀
     */
    private final String maxSensorPre = "P";

    @Override
    public Map<String, MeasurePointVO> getMonitorInfo(Long deviceId) {
        Assert.notNull(deviceId, "deviceId can not be null ");

        List<MeasurePointVO> pointList = redis.getHotPointsByDeviceId(deviceId, true);

        if (CollectionUtils.isNotEmpty(pointList)) {
            List<MeasurePointVO> points = pointList.stream().filter(o -> Objects.nonNull(o)).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(points)) {
                return points.stream().filter((item -> StringUtils.hasText(item.getLocationCode()))).collect(Collectors.toMap(o -> o.getLocationCode(), point -> point));
            }
        }
        return null;
    }

    @Override
    public RotaryMonitorVO getMonitorInfoOfRotary(Long deviceId) {
        RotaryMonitorVO vo = new RotaryMonitorVO();
        List<MeasurePointVO> pointList = redis.getHotPointsByDeviceId(deviceId, true);

        //需要展示的测点信息
        if (CollectionUtils.isNotEmpty(pointList)) {
            List<MeasurePointVO> points = pointList.stream().filter(o -> Objects.nonNull(o)).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(points)) {
                Map<String, MeasurePointVO> collect = points.stream().filter((item -> StringUtils.hasText(item.getLocationCode()))).collect(Collectors.toMap(o -> o.getLocationCode(), point -> point));
                vo.setPoints(collect);
                vo.setMaxPoints(getMaxPoints(points));
            }
        }
        return vo;
    }

    /**
     * 获取最大值测点
     *
     * @param points
     * @return
     */
    private List<MeasurePointVO> getMaxPoints(List<MeasurePointVO> points) {
        //环境最大温度-设备下所有测点信息
        ArrayList<MeasurePointVO> objects = new ArrayList<>();
        //传感器最大温度-根据sensorDesc前两个字符分组
        //非pi测点，且是分驱动端和非驱动端的
        List<MeasurePointVO> maxSensorList = points.stream().filter(item -> !item.getPiSensor() && StringUtils.hasText(item.getSensorDes()) && item.getSensorDes().length() > 2 && item.getSensorDes().startsWith(maxSensorPre))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(maxSensorList)) {
            return objects;
        }
        //传感器最大温度-根据sensorDesc前两个字符分组
        Map<String, MeasurePointVO> collect = maxSensorList.stream().collect(Collectors.groupingBy((item) -> item.getSensorDes().substring(0, 2),
                Collectors.collectingAndThen(Collectors.maxBy((one, two) -> compareTemp(one, two, 1)), Optional::get)));

        Collection<MeasurePointVO> values = collect.values();
        if (CollectionUtils.isNotEmpty(values)) {
            objects.addAll(values);
        }
        return objects;
    }

    private static <T> int compareTemp(T left, T right, int i) {
        MeasurePointVO one = (MeasurePointVO) left;
        MeasurePointVO two = (MeasurePointVO) right;
        //0 ，获取环境温度最大值
        if (i == 0) {
            return (int) ((one.getTemp1() == null ? 0 : one.getTemp1()) - (two.getTemp1() == null ? 0 : two.getTemp1()));
        } else {
            //其他获取传感器本身温度最大值
            return (int) ((one.getTemp2() == null ? 0 : one.getTemp2()) - (two.getTemp2() == null ? 0 : two.getTemp2()));
        }
    }


    @Override
    public boolean getExchangerOrValveStatus(Long deviceId) {

        MdDevice mdDevice = deviceMapper.selectById(deviceId);
        if (Objects.isNull(mdDevice)) {
            return false;
        }
        List<MeasurePointVO> pointList = redis.getHotPointsBySubSystemId(mdDevice.getSubSystemId(), true);
        if (CollectionUtils.isEmpty(pointList)) {
            return false;
        }
        Map<String, MeasurePointVO> collect = pointList.stream().filter(Objects::nonNull).collect(Collectors.toMap(vo -> vo.getTag(), vo -> vo));

        if (collect.isEmpty() || collect == null) {
            return false;
        }
        //阀的设备状态
        //阀的状态开关测点
        MeasurePointVO valve = collect.get("10SGS-V250A-CLSD");
        if (Objects.nonNull(valve) && String.valueOf(deviceId).equals(valve.getParentTag())) {
            //1：开启 0：关闭
            return valve.getValue().intValue() == 1;
        }
        //换热器A入口温度
        MeasurePointVO entryA = collect.get("10SWS-TE005A");
        //换热器A出口温度
        MeasurePointVO exitA = collect.get("10SWS-TE007A");
        //换热器B入口温度
        MeasurePointVO entryB = collect.get("10SWS-TE005B");
        //换热器B出口温度
        MeasurePointVO exitB = collect.get("10SWS-TE007B");
        //换热器的设备状态
        if (Objects.nonNull(entryA) && Objects.nonNull(exitA) && Objects.nonNull(entryB) && Objects.nonNull(exitB)) {
            double subA = (exitA.getValue() == null ? 0.0 : exitA.getValue()) - (entryA.getValue() == null ? 0.0 : entryA.getValue());
            double subB = (exitB.getValue() == null ? 0.0 : exitB.getValue()) - (entryB.getValue() == null ? 0.0 : entryB.getValue());
            if (String.valueOf(deviceId).equals(entryA.getParentTag())) {
                boolean b = Math.abs(subA) > Math.abs(subB);
                return b;
            }
            if (String.valueOf(deviceId).equals(entryB.getParentTag())) {
                return Math.abs(subB) > Math.abs(subA);
            }
        }
        return false;
    }

    @Override
    public Double getExchangerFlowMax() {
        List<MeasurePointVO> points = redis.getPoints(Sets.newHashSet("10SWS-FT004A_6_19", "10SWS-FT004B_6_20"));
        List<MeasurePointVO> collect = points.stream().filter(o -> Objects.nonNull(o) && Objects.nonNull(o.getValue())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(collect)) {
            return null;
        }
        return collect.stream().max(Comparator.comparing(MeasurePointVO::getValue)).get().getValue();
    }


    @Override
    public PumpPanoramaVO getPanoramaInfo(Long subSystemId) {
        Assert.notNull(subSystemId, "subSystemId can not be null ");
        PumpPanoramaVO vo = new PumpPanoramaVO();
        List<TxPumpsnapshot> deviceList = redis.listPumpSnapshot(subSystemId);

        sortedAndSetHealthInfo(deviceList, vo.getHealthInfo());
        vo.setDevices(deviceList);
        List<MeasurePointVO> pointList = redis.getHotPointsBySubSystemId(subSystemId, true);
        if (CollectionUtils.isEmpty(pointList)) {
            return vo;
        }
        List<MeasurePointVO> points = pointList.stream().filter(o -> Objects.nonNull(o)).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(points)) {
            List<Map<String, MeasurePointVO>> pointDataList = Lists.newArrayList();
            Map<String, Map<String, MeasurePointVO>> pointVOMap = points.stream().filter((a) -> StringUtils.hasText(a.getLocationCode())).collect(Collectors.groupingBy(
                    (a) -> StringUtils.hasText(a.getParentTag()) ? a.getParentTag() : commPointKey,
                    Collectors.mapping(o -> o, Collectors.toMap(o -> o.getLocationCode(), point -> point))));
            Map<String, MeasurePointVO> other = pointVOMap.remove(commPointKey);
            vo.setCommPoints(other);
            Set<Map.Entry<String, Map<String, MeasurePointVO>>> entries = pointVOMap.entrySet();
            List<Map.Entry<String, Map<String, MeasurePointVO>>> collect = entries.stream().sorted(Comparator.comparingInt(a -> Integer.parseInt(a.getKey()))).collect(Collectors.toList());
            for (Iterator<Map.Entry<String, Map<String, MeasurePointVO>>> it = collect.iterator(); it.hasNext(); ) {
                Map.Entry<String, Map<String, MeasurePointVO>> next = it.next();
                pointDataList.add(next.getValue());
            }
            vo.setPoints(pointDataList);
        }
        return vo;
    }

    private void sortedAndSetHealthInfo(List<TxPumpsnapshot> deviceList, int[] healthInfo) {
        if (CollectionUtils.isNotEmpty(deviceList)) {
            //排序
            for (int i = 0, len = deviceList.size(); i < len; i++) {
                Object obj = deviceList.get(i);
                if (Objects.isNull(obj)) {
                    healthInfo[0] += 1;
                    continue;
                }
                Integer healthStatus = -1;
                if (obj instanceof TxPumpsnapshot) {
                    healthStatus = ((TxPumpsnapshot) obj).getHealthStatus();
                }
                if (obj instanceof TxTurbinesnapshot) {
                    healthStatus = ((TxTurbinesnapshot) obj).getHealthStatus();
                }
                if (obj instanceof TxRotatingsnapshot) {
                    healthStatus = ((TxRotatingsnapshot) obj).getHealthStatus();
                }
                if (healthStatus == -1) {
                    healthInfo[0] += 1;
                    continue;
                }
                // healthStatus 0:健康 1:待观察 2:预警 3:报警 4:停机
                //数组状态分别为 --> 健康,待观察,停机,预警,报警
                switch (healthStatus) {
                    case 0:
                        healthInfo[0] += 1;
                        break;
                    case 1:
                        healthInfo[1] += 1;
                        break;
                    case 4:
                        healthInfo[2] += 1;
                        break;
                    case 2:
                        healthInfo[3] += 1;
                        break;
                    case 3:
                        healthInfo[4] += 1;
                        break;
                    default:
                        break;
                }

            }
        }
    }

    @Autowired
    private TxAlarmEventMapper alarmEventMapper;

    @Override
    public List<TxAlarmEvent> getWarningNewest(Long queryId, Integer top, boolean type) {
        //默认取前10条记录
        if (Objects.isNull(top)) {
            top = 10;
        }
        QueryWrapper<TxAlarmEvent> queryWrapper = new QueryWrapper<>();
        if (type) {
            queryWrapper.lambda().eq(TxAlarmEvent::getDeviceId, queryId);
        } else {
            //默认按照子系统查询-告警信息列表-先按照告警级别倒序，然后按照时间倒序展示
            queryWrapper.lambda().eq(TxAlarmEvent::getSubSystemId, queryId);
            queryWrapper.lambda().eq(TxAlarmEvent::getStopFlag, 0);
            queryWrapper.orderBy(true, false, "alarm_level");
        }
        queryWrapper.orderBy(true, false, "last_alarm");
        String sql = "limit 0," + top;
        queryWrapper.last(sql);
        return alarmEventMapper.selectList(queryWrapper);
    }
}

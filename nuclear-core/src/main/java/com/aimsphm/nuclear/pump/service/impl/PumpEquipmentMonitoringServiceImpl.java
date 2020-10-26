package com.aimsphm.nuclear.pump.service.impl;

import com.aimsphm.nuclear.common.entity.*;
import com.aimsphm.nuclear.common.entity.vo.MeasurePointTimesScaleVO;
import com.aimsphm.nuclear.common.entity.vo.MeasurePointTimesVO;
import com.aimsphm.nuclear.common.entity.vo.MeasurePointVO;
import com.aimsphm.nuclear.common.enums.AlgoTypeEnum;
import com.aimsphm.nuclear.common.enums.AlgorithmLevelEnum;
import com.aimsphm.nuclear.common.enums.SensorTypeEnum;
import com.aimsphm.nuclear.common.exception.CustomMessageException;
import com.aimsphm.nuclear.common.mapper.MdDeviceMapper;
import com.aimsphm.nuclear.common.mapper.MdSensorMapper;
import com.aimsphm.nuclear.common.mapper.TxAlarmEventMapper;
import com.aimsphm.nuclear.common.service.AlgorithmCacheService;
import com.aimsphm.nuclear.common.service.HotSpotDataService;
import com.aimsphm.nuclear.common.service.MdDeviceDetailsService;
import com.aimsphm.nuclear.common.service.TxRotatingsnapshotService;
import com.aimsphm.nuclear.common.util.BigDecimalUtils;
import com.aimsphm.nuclear.common.util.DateUtils;
import com.aimsphm.nuclear.core.constant.CoreConstants;
import com.aimsphm.nuclear.core.entity.MdRuntimeBaseCfg;
import com.aimsphm.nuclear.core.mapper.MdRuntimeBaseCfgMapper;
import com.aimsphm.nuclear.core.service.MdRuntimeBaseCfgService;
import com.aimsphm.nuclear.pump.service.PumpEquipmentMonitoringService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.google.common.base.CaseFormat;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.aimsphm.nuclear.common.constant.ReportConstant.BLANK;
import static com.aimsphm.nuclear.common.service.impl.MdDeviceDetailsServiceImpl.LAST_START_TIME;
import static com.aimsphm.nuclear.common.service.impl.MdDeviceDetailsServiceImpl.LAST_STOP_TIME;

/**
 * @Package: com.aimsphm.nuclear.pump.service.impl
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2020/4/14 18:28
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/4/14 18:28
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Service
public class PumpEquipmentMonitoringServiceImpl implements PumpEquipmentMonitoringService {
    @Autowired
    private HotSpotDataService redis;
    @Autowired
    private MdSensorMapper sensorMapper;
    @Autowired
    private TxAlarmEventMapper eventMapper;
    @Autowired
    private MdRuntimeBaseCfgService iMdRuntimeBaseCfgService;

    @Autowired
    private AlgorithmCacheService cacheService;

    @Autowired
    private TxRotatingsnapshotService rotatingsnapshotService;

    @Autowired
    private MdDeviceMapper deviceMapper;

    /**
     * 默认保存1位小数
     */
    public static final int DIGIT = 1;

    private final List<String> temperatureTitle = Lists.newArrayList("温度超过35℃", "温度超过36.4℃");
    private final List<String> speedTitle = Lists.newArrayList("100%转速", "88%转速", "50%转速", "23.6%转");
    private final List<String> dateDistributionTitle = Lists.newArrayList("一天", "三天", "一周", "一个月", "三个月");
    /**
     * 汽机统计的title
     */
    private final List<String> statusTitle = Lists.newArrayList("正常", "待观察", "预警", "报警");
    /**
     * 旋机时常统计title
     */
    private final List<String> rotaryTitle = Lists.newArrayList("本次运行", "", "", "总运行");

    @Autowired
    private MdDeviceDetailsService detailsService;
    @Autowired
    private MdRuntimeBaseCfgMapper configMapper;

    @Override
    public TxPumpsnapshot getRunningStatus(Long deviceId) {
        TxPumpsnapshot pumpSnapshot = redis.getPumpSnapshot(deviceId);
        if (Objects.isNull(pumpSnapshot)) {
            return null;
        }
        Map<String, Date> times = detailsService.getLatTimes(deviceId);
        //上次停机时间
        if (Objects.nonNull(times) && Objects.isNull(pumpSnapshot.getLastStopTime())) {
            pumpSnapshot.setLastStopTime(times.get(LAST_STOP_TIME));
        }
        updateSnapshot(pumpSnapshot, deviceId);
        return pumpSnapshot;
    }

    /**
     * 查询时候添加上基础数据
     *
     * @param snapshot 快照数据
     * @param deviceId
     */
    private void updateSnapshot(Object snapshot, Long deviceId) {
        List<MdRuntimeBaseCfg> configList = iMdRuntimeBaseCfgService.getAllConfigByDeviceId(deviceId);
        if (CollectionUtils.isEmpty(configList)) {
            return;
        }
        Class aClass = snapshot.getClass();
        Method[] declaredMethods = aClass.getDeclaredMethods();
        for (MdRuntimeBaseCfg config : configList) {
            try {
                //单位  1 小时 2 分钟 3 秒  4 次
                Integer unit = config.getUnit();
                String parameterName = config.getInitialParameterName();
                String name = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, parameterName);
                String dataType = config.getInitialParameterDataType();
                //基础值
                String baseValue = config.getInitialParameterValue();
                Field declaredField = snapshot.getClass().getDeclaredField(name);
                if (declaredField == null || StringUtils.isBlank(name) || StringUtils.isBlank(dataType) || StringUtils.isBlank(baseValue)) {
                    return;
                }
                String methodSuffix = name.substring(0, 1).toUpperCase() + (name.length() > 1 ? name.substring(1) : BLANK);
                String setMethod = "set" + methodSuffix;
                String getMethod = "get" + methodSuffix;

                Method getValueMethod = aClass.getDeclaredMethod(getMethod);
                Object getValue = getValueMethod.invoke(snapshot);

                Long multipleNumber = 1L;
                //小时换成秒
                if (unit == 1) {
                    multipleNumber = multipleNumber * 3600;
                    //分钟换成秒
                } else if (unit == 2) {
                    multipleNumber = multipleNumber * 60;
                }

                setParam(snapshot, declaredMethods, multipleNumber, getValue, setMethod, baseValue);

            } catch (Exception e) {
                e.printStackTrace();
                throw new CustomMessageException(e.getCause());
            }
        }
    }

    private void setParam(Object snapshot, Method[] declaredMethods, Long multipleNumber, Object getValue, String methodName, String baseValue) throws InvocationTargetException, IllegalAccessException {
        for (int i = 0, len = declaredMethods.length; i < len; i++) {
            Method method = declaredMethods[i];
            if (!StringUtils.equals(methodName, method.getName())) {
                continue;
            }
            Class<?>[] parameterTypes = method.getParameterTypes();
            Class<?> type = parameterTypes[0];
            Object value = getValueByParamType(type, multipleNumber, getValue, baseValue);
            method.invoke(snapshot, value);
        }
    }

    private Object getValueByParamType(Class<?> type, Long multipleNumber, Object getValue, String baseValue) {
        if (Integer.class.getSimpleName().equalsIgnoreCase(type.getSimpleName())) {
            return Math.toIntExact(Integer.parseInt(baseValue) * multipleNumber.intValue() + (Objects.isNull(getValue) ? 0L : (Integer) getValue));
        } else if (Long.class.getSimpleName().equalsIgnoreCase(type.getSimpleName())) {
            Double value = Double.parseDouble(baseValue) * multipleNumber + (Objects.isNull(getValue) ? 0L : (Long) getValue);
            return value.longValue();
        } else if (Double.class.getSimpleName().equalsIgnoreCase(type.getSimpleName())) {
            return Double.parseDouble(baseValue) * multipleNumber + (Objects.isNull(getValue) ? 0L : (Double) getValue);
        } else {
            throw new CustomMessageException("this data type only support int long double");
        }
    }


    @Override
    public List<MeasurePointVO> listWarmingPoint(Long deviceId) {
        return redis.getWarmingPumpPointsByDeviceId(deviceId);
    }


    @Override
    public List<List<MeasurePointTimesScaleVO>> statisticsWarmingPoints(Long deviceId, Long startTime, Long endTime) {
        endTime = endTime == null ? System.currentTimeMillis() : endTime;
        //测点类型占比
        List<MeasurePointTimesScaleVO> pointTypeScale = statisticsWarmingPointsByPointType(deviceId, startTime, endTime);
        //报警类型占比
        List<MeasurePointTimesScaleVO> alarmTypeScale = statisticsAlarmByAlarmType(deviceId, startTime, endTime);
        //报警级别占比
        List<MeasurePointTimesScaleVO> levelTypeScale = statisticsAlarmByLevelType(deviceId, startTime, endTime);
        return Lists.newArrayList(pointTypeScale, alarmTypeScale, levelTypeScale);
    }

    @Override
    public List<List<MeasurePointTimesScaleVO>> turbineStatisticsWarmingPoints(Long deviceId, Long startTime, Long endTime) {
        endTime = endTime == null ? System.currentTimeMillis() : endTime;
        //测点类型占比
        List<MeasurePointTimesScaleVO> pointTypeScale = tbStatisticsWarmingPointsByPointType(deviceId, startTime, endTime);
        //报警类型占比
        List<MeasurePointTimesScaleVO> alarmTypeScale = statisticsAlarmByAlarmType(deviceId, startTime, endTime);
        //报警级别占比
        List<MeasurePointTimesScaleVO> levelTypeScale = statisticsAlarmByLevelType(deviceId, startTime, endTime);
        return Lists.newArrayList(pointTypeScale, alarmTypeScale, levelTypeScale);
    }

    private List<MeasurePointTimesScaleVO> tbStatisticsWarmingPointsByPointType(Long deviceId, Long startTime, Long endTime) {
        List<MeasurePointTimesScaleVO> pointList = sensorMapper.selectTbWarmingPointsByDeviceId(deviceId, startTime, endTime);
        return pointList;
    }

    @Override
    public List<MeasurePointTimesScaleVO> turbineStatisticsRunningStatus(Long deviceId) {
        List<MeasurePointTimesScaleVO> list = Lists.newArrayList(new MeasurePointTimesScaleVO(), new MeasurePointTimesScaleVO(), new MeasurePointTimesScaleVO());
        TxAlarmsnapshot status = cacheService.getAlarmSnapshot(deviceId);
        //运行统计
        if (Objects.nonNull(status)) {
            MeasurePointTimesScaleVO speed = list.get(0);
            speed.setName(statusTitle);
            speed.setValue(Lists.newArrayList(status.getNormalStateRuntime(), status.getWatchingStateRuntime(), status.getWarningStateRuntime(), status.getAlarmingStateRuntime()));
            MeasurePointTimesScaleVO temperature = list.get(1);
            temperature.setName(statusTitle);
            temperature.setValue(Lists.newArrayList(status.getOverallNormalStateRuntime(), status.getOverallWatchingStateRuntime(), status.getOverallWarningStateRuntime(), status.getOverallAlarmingStateRuntime()));
        }
        //报警分布占比
        List<Long> dateDistribution = eventMapper.selectWarmingPointsByDateDistribution(deviceId);
        if (CollectionUtils.isNotEmpty(dateDistribution)) {
            MeasurePointTimesScaleVO vo = list.get(2);
            vo.setName(dateDistributionTitle);
            vo.setValue(dateDistribution);
        }
        return list;
    }


    @Override
    public TxTurbinesnapshot getRunningStatusTurbine(Long deviceId) {
        TxTurbinesnapshot snapshot = null;
        Object obj = redis.getDeviceSnapshot(deviceId);
        if (Objects.isNull(obj)) {
            return null;
        }
        if (obj instanceof TxTurbinesnapshot) {
            snapshot = (TxTurbinesnapshot) obj;
        }
        if (Objects.isNull(snapshot)) {
            return null;
        }
        Map<String, Date> times = detailsService.getLatTimes(deviceId);
        //上次停机时间
        if (Objects.nonNull(times) && Objects.isNull(snapshot.getLastStopTime())) {
            snapshot.setLastStopTime(times.get(LAST_STOP_TIME));
        }
        //上次启动时间
        if (Objects.nonNull(times) && Objects.isNull(snapshot.getLastStartTime())) {
            snapshot.setLastStopTime(times.get(LAST_START_TIME));
        }
        updateSnapshot(snapshot, deviceId);
        return snapshot;
    }

    @Override
    public Object getMonitorStatusOfRotary(Long deviceId) {
        TxRotatingsnapshot snapshot = null;
        Object obj = redis.getDeviceSnapshot(deviceId);
        if (Objects.isNull(obj)) {
            return null;
        }
        if (obj instanceof TxRotatingsnapshot) {
            snapshot = (TxRotatingsnapshot) obj;
        }
        if (Objects.isNull(snapshot)) {
            return null;
        }
        Map<String, Date> times = detailsService.getLatTimes(deviceId);
        //上次停机时间
        if (Objects.nonNull(times) && Objects.isNull(snapshot.getLastStopTime())) {
            snapshot.setLastStopTime(times.get(LAST_STOP_TIME));
        }
        //上次启动时间
        if (Objects.nonNull(times) && Objects.isNull(snapshot.getLastStartTime())) {
            snapshot.setLastStopTime(times.get(LAST_START_TIME));
        }
        return snapshot;
    }

    @Override
    public MeasurePointTimesScaleVO getMonitorStatisticsOfRotary(Long deviceId) {
        MeasurePointTimesScaleVO vo = new MeasurePointTimesScaleVO();
        //初始化title
        initRotaryTitle();
        vo.setName(rotaryTitle);
        List<Double> values = Lists.newArrayList(0D, 0D, 0D, 0D);
        vo.setValue(values);
        statisticsRunningTime(deviceId, values);
        return vo;
    }

    private void statisticsRunningTime(Long deviceId, List<Double> values) {
        LambdaQueryWrapper<TxRotatingsnapshot> query = new LambdaQueryWrapper<>();
        query.eq(TxRotatingsnapshot::getDeviceId, deviceId).orderByDesc(TxRotatingsnapshot::getSnapshotTime).last(" limit 1 ");
        TxRotatingsnapshot snapshot = rotatingsnapshotService.getOne(query);
        if (Objects.isNull(snapshot)) {
            return;
        }
        //健康状态
        Integer status = snapshot.getHealthStatus();
        //最新的启动时间
        Date lastStartTime = snapshot.getLastStartTime();

        List<MdRuntimeBaseCfg> configs = configMapper.getRotaryRuntimeConfig(deviceId);

        Map<String, MdRuntimeBaseCfg> collect = configs.stream().collect(Collectors.toMap(item -> item.getInitialParameterName(), item -> item, (a, b) -> a));
        //更新或者是添加配置
        saveOrUpdateBaseConfig(collect, deviceId, lastStartTime);

        //启动时间
        MdRuntimeBaseCfg startTime = collect.get(CoreConstants.START_TIME);
        //总运行时间
        MdRuntimeBaseCfg allTime = collect.get(CoreConstants.TOTAL_RUNNING_DURATION);
        //本年度运行时间
        MdRuntimeBaseCfg currentYearTime = collect.get(CoreConstants.CURRENT_YEAR_RUNNING_DURATION);
        //上年度运行时间
        MdRuntimeBaseCfg previousYearTime = collect.get(CoreConstants.PREVIOUS_YEAR_RUNNING_DURATION);

        //本次运行时间
        Double currentRunTime = currentRunTime(status, startTime, lastStartTime, values);

        //总运行时间
        allRuntime(status, startTime, lastStartTime, allTime, values, currentRunTime);
        //当年度
        Double currentYearRuntime = currentYearRuntime(status, startTime, lastStartTime, currentYearTime, values, currentRunTime);
        //新的一年将本年度的基础值重置为0
        if (LocalDate.now().getDayOfYear() == 1) {
            updateYearRuntimeConfig(deviceId);
            updateYearRuntimeConfig(deviceId, currentYearRuntime);
        }
        //上年度
        previousYearTime(previousYearTime, values);
    }

    /**
     * 保存本年度的数据到上年度的配置中
     *
     * @param deviceId           设备id
     * @param currentYearRuntime 本年度运行时常
     */
    protected void updateYearRuntimeConfig(Long deviceId, Double currentYearRuntime) {
        LambdaUpdateWrapper<MdRuntimeBaseCfg> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(MdRuntimeBaseCfg::getInitialParameterValue, currentYearRuntime * 24);
        wrapper.eq(MdRuntimeBaseCfg::getDeviceId, deviceId);
        wrapper.eq(MdRuntimeBaseCfg::getInitialParameterName, CoreConstants.PREVIOUS_YEAR_RUNNING_DURATION);
        MdRuntimeBaseCfg cfg = new MdRuntimeBaseCfg();
        configMapper.update(cfg, wrapper);
    }

    /**
     * 更新本年度的基础配置
     *
     * @param deviceId
     */
    protected void updateYearRuntimeConfig(Long deviceId) {
        LambdaUpdateWrapper<MdRuntimeBaseCfg> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(MdRuntimeBaseCfg::getInitialParameterValue, 0);
        wrapper.eq(MdRuntimeBaseCfg::getDeviceId, deviceId);
        wrapper.eq(MdRuntimeBaseCfg::getInitialParameterName, CoreConstants.CURRENT_YEAR_RUNNING_DURATION);
        MdRuntimeBaseCfg cfg = new MdRuntimeBaseCfg();
        configMapper.update(cfg, wrapper);
    }

    private void previousYearTime(MdRuntimeBaseCfg previousYearTime, List<Double> values) {
        if (Objects.isNull(previousYearTime) || StringUtils.isBlank(previousYearTime.getInitialParameterValue())) {
            return;
        }
        double v = new BigDecimal(previousYearTime.getInitialParameterValue()).multiply(new BigDecimal("3600000")).doubleValue();
        values.set(2, BigDecimalUtils.divide(v, 24 * 3600 * 1000));

    }

    private Double currentYearRuntime(Integer status, MdRuntimeBaseCfg startTime, Date lastStartTime, MdRuntimeBaseCfg thisYearTime, List<Double> values, Double thisRunTime) {
        //启动时间
        Long t2 = lastStartTime == null ? 0L : lastStartTime.getTime();
        if (Objects.nonNull(startTime) && Objects.nonNull(startTime.getStartDateTime())) {
            t2 = startTime.getStartDateTime().getTime();
        }
        //当年度运行时间
        Long t3 = 0L;
        Double yearTotalTime = 0D;
        if (Objects.nonNull(thisYearTime) && Objects.nonNull(thisYearTime.getStartDateTime())) {
            t3 = startTime.getStartDateTime().getTime();
            yearTotalTime = new BigDecimal(thisYearTime.getInitialParameterValue()).multiply(new BigDecimal("3600000")).doubleValue();
        }
        //停机状态
        if (Objects.isNull(status) || status == 4) {
            Double divide = BigDecimalUtils.divide(yearTotalTime, 24 * 3600 * 1000);
            values.set(1, divide);
            return divide;
        }
        //正常状态
        if (t3 > t2) {
            yearTotalTime += thisRunTime;
        } else {
            yearTotalTime = Double.valueOf(System.currentTimeMillis() - t3);
        }
        //换算成天
        Double divide = BigDecimalUtils.divide(yearTotalTime, 24 * 3600 * 1000);
        values.set(1, divide);
        return divide;
    }


    private Double allRuntime(Integer status, MdRuntimeBaseCfg startTime, Date lastStartTime, MdRuntimeBaseCfg allTime, List<Double> values, Double thisRunTime) {
        //启动时间
        Long t2 = lastStartTime == null ? 0L : lastStartTime.getTime();
        if (Objects.nonNull(startTime) && Objects.nonNull(startTime.getStartDateTime())) {
            t2 = startTime.getStartDateTime().getTime();
        }
        //总运行时间
        Long t1 = 0L;
        Double allTotalTime = 0D;
        if (Objects.nonNull(allTime) && Objects.nonNull(allTime.getStartDateTime())) {
            t1 = startTime.getStartDateTime().getTime();
            allTotalTime = new BigDecimal(allTime.getInitialParameterValue()).multiply(new BigDecimal("3600000")).doubleValue();
        }
        //停机状态
        if (Objects.isNull(status) || status == 4) {
            Double divide = BigDecimalUtils.divide(allTotalTime, 24 * 3600 * 1000);
            values.set(3, divide);
            return divide;
        }
        //正常状态
        if (t2 > t1) {
            allTotalTime += thisRunTime;
        } else {
            allTotalTime = Double.valueOf(System.currentTimeMillis() - t1);
        }
        //换算成天
        Double divide = BigDecimalUtils.divide(allTotalTime, 24 * 3600 * 1000);
        values.set(3, divide);
        return divide;
    }

    /**
     * 本次运行时常
     *
     * @param status
     * @param startTime
     * @param lastStartTime
     * @param values
     * @return
     */
    private Double currentRunTime(Integer status, MdRuntimeBaseCfg startTime, Date lastStartTime, List<Double> values) {
        //停机状态
        if (Objects.isNull(status) || status == 4) {
            return 0D;
        }
        Long time;
        if (startTime == null || startTime.getStartDateTime() == null) {
            time = System.currentTimeMillis() - lastStartTime.getTime();
        } else {
            time = System.currentTimeMillis() - startTime.getStartDateTime().getTime();
        }
        //换算成天
        Double divide = BigDecimalUtils.divide(time, 24 * 3600 * 1000);
        values.set(0, divide);
        return divide;
    }

    protected void saveOrUpdateBaseConfig(Map<String, MdRuntimeBaseCfg> configs, Long deviceId, Date lastStartTime) {
        MdDevice device = deviceMapper.selectById(deviceId);
        if (Objects.isNull(device)) {
            return;
        }
        MdRuntimeBaseCfg cfg = new MdRuntimeBaseCfg();
        BeanUtils.copyProperties(device, cfg);
        Set<String> keys = configs.keySet();
        cfg.setInitialParameterDataType(Double.class.getSimpleName());
        cfg.setDeviceId(deviceId);
        if (!keys.contains(CoreConstants.START_TIME)) {
            cfg.setInitialParameterName(CoreConstants.START_TIME);
            cfg.setParameterDisplayName("启动时间");
            cfg.setInitialParameterValue("0");
            cfg.setStartDateTime(new Date());
            cfg.setUnit(5);
            configMapper.insert(cfg);
        }

        cfg.setUnit(4);
        if (!keys.contains(CoreConstants.TOTAL_RUNNING_DURATION)) {
            cfg.setInitialParameterName(CoreConstants.TOTAL_RUNNING_DURATION);
            cfg.setParameterDisplayName("总运行时间(小时)");
            cfg.setInitialParameterValue("0");
            cfg.setStartDateTime(Objects.isNull(lastStartTime) ? new Date() : lastStartTime);
            configMapper.insert(cfg);
        }
        if (!keys.contains(CoreConstants.CURRENT_YEAR_RUNNING_DURATION)) {
            cfg.setInitialParameterName(CoreConstants.CURRENT_YEAR_RUNNING_DURATION);
            cfg.setParameterDisplayName("本年度运行时间(小时)");
            cfg.setInitialParameterValue("0");
            cfg.setStartDateTime(Objects.isNull(lastStartTime) ? new Date() : lastStartTime);
            configMapper.insert(cfg);
        }

        if (!keys.contains(CoreConstants.PREVIOUS_YEAR_RUNNING_DURATION)) {
            cfg.setInitialParameterName(CoreConstants.PREVIOUS_YEAR_RUNNING_DURATION);
            cfg.setParameterDisplayName("上年度运行时间(小时)");
            cfg.setInitialParameterValue("0");
            cfg.setStartDateTime(Objects.isNull(lastStartTime) ? new Date() : lastStartTime);
            configMapper.insert(cfg);
        }
    }

    private void initRotaryTitle() {
        //当前年
        rotaryTitle.set(1, DateUtils.formatCurrentDateTime(DateUtils.YEAR_ZH).concat("运行"));
        //上一年
        rotaryTitle.set(2, DateUtils.formatPreviousYear(DateUtils.YEAR_ZH).concat("运行"));
    }

    @Override
    public List<MeasurePointTimesScaleVO> statisticsRunningStatus(Long deviceId) {
        List<MeasurePointTimesScaleVO> list = Lists.newArrayList(new MeasurePointTimesScaleVO(), new MeasurePointTimesScaleVO(), new MeasurePointTimesScaleVO());
        //运行统计
        TxPumpsnapshot status = this.getRunningStatus(deviceId);
        if (Objects.nonNull(status)) {
            MeasurePointTimesScaleVO speed = list.get(0);
            speed.setName(speedTitle);
            speed.setValue(Lists.newArrayList(status.getGearShiftSpeedG100(), status.getGearShiftSpeedG88(), status.getGearShiftSpeedG50(), status.getGearShiftSpeedG23()));
            MeasurePointTimesScaleVO temperature = list.get(1);
            temperature.setName(temperatureTitle);
            temperature.setValue(Lists.newArrayList(status.getOverD35Temp(), status.getOverD36Temp()));
        }
        //报警分布占比
        List<Long> dateDistribution = eventMapper.selectWarmingPointsByDateDistribution(deviceId);
        if (CollectionUtils.isNotEmpty(dateDistribution)) {
            MeasurePointTimesScaleVO vo = list.get(2);
            vo.setName(dateDistributionTitle);
            vo.setValue(dateDistribution);
        }
        return list;
    }

    private List<MeasurePointTimesScaleVO> statisticsAlarmByLevelType(Long deviceId, Long startTime, Long endTime) {
        List<MeasurePointTimesVO> pointList = eventMapper.selectAlarmByLevelType(deviceId, startTime, endTime);
        return operateDataVO(pointList, 1);
    }

    private List<MeasurePointTimesScaleVO> operateDataVO(List<MeasurePointTimesVO> pointList, int type) {
        List<MeasurePointTimesScaleVO> list = Lists.newArrayList();
        if (CollectionUtils.isEmpty(pointList)) {
            return list;
        }

        for (int i = 0, len = pointList.size(); i < len; i++) {
            MeasurePointTimesVO point = pointList.get(i);
            MeasurePointTimesScaleVO vo = new MeasurePointTimesScaleVO();
            //算法级别
            if (type == 1) {
                AlgorithmLevelEnum typeEnum = AlgorithmLevelEnum.getByValue(point.getType());
                if (Objects.nonNull(typeEnum)) {
                    vo.setName(typeEnum.getDesc());
                }
            }
            //算法类型[是否是算法报警]
            if (type == 2) {
                AlgoTypeEnum typeEnum = AlgoTypeEnum.getByValue(point.getType());
                if (Objects.nonNull(typeEnum)) {
                    vo.setName(typeEnum.getDesc());
                }
            }
            vo.setValue(point.getTimes());
            list.add(vo);
        }

        return list;
    }

    private List<MeasurePointTimesScaleVO> statisticsAlarmByAlarmType(Long deviceId, Long startTime, Long endTime) {
        List<MeasurePointTimesVO> pointList = eventMapper.selectAlarmByAlarmType(deviceId, startTime, endTime);
        return operateDataVO(pointList, 2);
    }

    private List<MeasurePointTimesScaleVO> statisticsWarmingPointsByPointType(Long deviceId, Long startTime, Long
            endTime) {
        List<MeasurePointTimesScaleVO> list = Lists.newArrayList();
        List<MeasurePointTimesVO> pointList = sensorMapper.selectWarmingPointsByDeviceId(deviceId, startTime, endTime);
        if (CollectionUtils.isEmpty(pointList)) {
            return list;
        }
        Long total = 0L;
        Map<Byte, Long> data = Maps.newHashMap();
        for (MeasurePointTimesVO vo : pointList) {
            byte type = vo.getType();
            type = isTypeInStatistics(type);
            Long times = vo.getTimes();
            total = total + times;
            //振动3,4,5需要统计为3
            data.put(type, MapUtils.getLong(data, type, 0L) + times);
        }
        if (MapUtils.isEmpty(data)) {
            return list;
        }
        for (Iterator<Map.Entry<Byte, Long>> it = data.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<Byte, Long> item = it.next();
            Byte key = item.getKey();
            MeasurePointTimesScaleVO vo = new MeasurePointTimesScaleVO();
            vo.setName(SensorTypeEnum.getDesc(key));
            vo.setValue(item.getValue());
            list.add(vo);
        }
        return list;
    }

    private byte isTypeInStatistics(byte type) {
        //温度
        if (type == SensorTypeEnum.Temp.getValue()) {
            return type;
        }
        //振动
        if (type == SensorTypeEnum.Vibration.getValue() || type == SensorTypeEnum.VibrationF1X.getValue() || type == SensorTypeEnum.VibrationF2X.getValue()) {
            return SensorTypeEnum.Vibration.getValue();
        }
        //转速和电信号
        if (type == SensorTypeEnum.SpeedOrElectric.getValue()) {
            return type;
        }

        return SensorTypeEnum.SOMETHING_ELSE.getValue();
    }
}

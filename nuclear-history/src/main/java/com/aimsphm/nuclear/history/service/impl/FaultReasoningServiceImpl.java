package com.aimsphm.nuclear.history.service.impl;

import com.aimsphm.nuclear.algorithm.entity.dto.SymptomParamDTO;
import com.aimsphm.nuclear.algorithm.entity.dto.SymptomResponseDTO;
import com.aimsphm.nuclear.algorithm.service.AlgorithmHandlerService;
import com.aimsphm.nuclear.algorithm.service.BizDiagnosisService;
import com.aimsphm.nuclear.common.entity.AlgorithmNormalFaultFeatureDO;
import com.aimsphm.nuclear.common.entity.CommonMeasurePointDO;
import com.aimsphm.nuclear.common.entity.CommonSensorComponentDO;
import com.aimsphm.nuclear.common.entity.vo.FaultReasoningVO;
import com.aimsphm.nuclear.common.enums.TimeUnitEnum;
import com.aimsphm.nuclear.common.service.AlgorithmNormalFaultFeatureService;
import com.aimsphm.nuclear.common.service.CommonMeasurePointService;
import com.aimsphm.nuclear.common.service.CommonSensorComponentService;
import com.aimsphm.nuclear.common.util.HBaseUtil;
import com.aimsphm.nuclear.history.service.FaultReasoningService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.aimsphm.nuclear.common.constant.HBaseConstant.H_BASE_FAMILY_NPC_PI_REAL_TIME;
import static com.aimsphm.nuclear.common.constant.HBaseConstant.H_BASE_TABLE_NPC_PHM_DATA;
import static com.aimsphm.nuclear.common.constant.ReportConstant.BLANK;
import static com.aimsphm.nuclear.common.constant.SymbolConstant.UNDERLINE;

/**
 * @Package: com.aimsphm.nuclear.algorithm.service.impl
 * @Description: <>
 * @Author: milla
 * @CreateDate: 2021/06/04 11:29
 * @UpdateUser: milla
 * @UpdateDate: 2021/06/04 11:29
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Slf4j
@Service
public class FaultReasoningServiceImpl implements FaultReasoningService {
    @Resource(name = "SYMPTOM")
    private AlgorithmHandlerService symptomService;
    @Resource
    private CommonMeasurePointService pointService;
    @Resource
    private HBaseUtil hBase;
    @Resource
    private AlgorithmNormalFaultFeatureService featureService;
    @Resource
    private CommonSensorComponentService sensorComponentService;
    @Resource
    private BizDiagnosisService diagnosisService;


    @Override
    public List<FaultReasoningVO> faultReasoningVO(List<String> pointIds, Long deviceId, Long gmtLastAlarm) {
        if (CollectionUtils.isEmpty(pointIds) || Objects.isNull(deviceId)) {
            return new ArrayList<>();
        }
        //征兆集合
        SymptomResponseDTO symptomResponseDTO = this.symptomJudgment(pointIds, gmtLastAlarm);
        if (Objects.isNull(symptomResponseDTO) || CollectionUtils.isEmpty(symptomResponseDTO.getSymptomList())) {
            log.error("征兆判断算法报错：{}", symptomResponseDTO);
            return new ArrayList<>();
        }
        return diagnosisService.faultReasoning(symptomResponseDTO, deviceId);
    }


    @Override
    public SymptomResponseDTO symptomJudgment(List<String> pointIds, Long gmtLastAlarm) {
        //如果没有最后一次报警时间，就使用当前时间
        Long end = Objects.isNull(gmtLastAlarm) ? System.currentTimeMillis() : gmtLastAlarm;
        LambdaQueryWrapper<CommonMeasurePointDO> query = Wrappers.lambdaQuery(CommonMeasurePointDO.class);
        query.in(CommonMeasurePointDO::getPointId, pointIds);
        List<CommonMeasurePointDO> points = pointService.list(query);
        if (CollectionUtils.isEmpty(points)) {
            return null;
        }
        LambdaQueryWrapper<CommonSensorComponentDO> sensorQuery = Wrappers.lambdaQuery(CommonSensorComponentDO.class);
        sensorQuery.in(CommonSensorComponentDO::getSensorCode, points.stream().map(CommonMeasurePointDO::getSensorCode).collect(Collectors.toSet()));
        List<CommonSensorComponentDO> sensorComponentList = sensorComponentService.list(sensorQuery);
        if (CollectionUtils.isEmpty(sensorComponentList)) {
            return null;
        }
        LambdaQueryWrapper<AlgorithmNormalFaultFeatureDO> wrapper = Wrappers.lambdaQuery(AlgorithmNormalFaultFeatureDO.class);
        wrapper.in(AlgorithmNormalFaultFeatureDO::getComponentId, sensorComponentList.stream().map(CommonSensorComponentDO::getComponentId).collect(Collectors.toSet()));
        List<AlgorithmNormalFaultFeatureDO> list = featureService.list(wrapper);
        if (CollectionUtils.isEmpty(list)) {
            log.error("没有对应的规则");
            return null;
        }
        SymptomParamDTO params = new SymptomParamDTO();
        params.setFeatureInfo(list);
        Map<String, List<List>> data = new HashMap<>(16);
        list.forEach(x -> {
            String pointId = x.getSensorDesc();
            Long componentId = x.getComponentId();
            String key = componentId + UNDERLINE + pointId;
            data.put(key, null);
            String sensorCode = x.getSensorCode();
            if (StringUtils.isBlank(sensorCode)) {
                return;
            }
            String family = H_BASE_FAMILY_NPC_PI_REAL_TIME;
            if (!StringUtils.equals(pointId, sensorCode)) {
                family = pointId.replace(sensorCode, BLANK).substring(1);
            }
            String timeRange = x.getTimeRange();
            if (StringUtils.isBlank(timeRange)) {
                return;
            }
            Long gapValue = TimeUnitEnum.getGapValue(timeRange);
            if (Objects.isNull(gapValue)) {
                return;
            }
            try {
                List<List> lists = hBase.listDataSWith3600Columns(H_BASE_TABLE_NPC_PHM_DATA, sensorCode, end - gapValue, end, family);
                data.put(key, lists);
            } catch (IOException e) {
                log.error("query history data failed.....");
            }
        });
        params.setFeatureValue(data);
        return (SymptomResponseDTO) symptomService.getInvokeCustomerData(params);
    }
}

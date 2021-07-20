package com.aimsphm.nuclear.history.service.impl;

import com.aimsphm.nuclear.algorithm.entity.dto.*;
import com.aimsphm.nuclear.algorithm.service.AlgorithmHandlerService;
import com.aimsphm.nuclear.algorithm.service.BizDiagnosisService;
import com.aimsphm.nuclear.common.entity.*;
import com.aimsphm.nuclear.common.entity.vo.AlgorithmNormalFaultFeatureVO;
import com.aimsphm.nuclear.common.entity.vo.FaultReasoningVO;
import com.aimsphm.nuclear.common.entity.vo.SymptomCorrelationVO;
import com.aimsphm.nuclear.common.enums.TimeUnitEnum;
import com.aimsphm.nuclear.common.service.*;
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
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.aimsphm.nuclear.common.constant.HBaseConstant.H_BASE_FAMILY_NPC_PI_REAL_TIME;
import static com.aimsphm.nuclear.common.constant.HBaseConstant.H_BASE_TABLE_NPC_PHM_DATA;
import static com.aimsphm.nuclear.common.constant.ReportConstant.BLANK;

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
    public List<FaultReasoningVO> faultReasoningVO(List<String> pointIds, Long deviceId) {
        if (CollectionUtils.isEmpty(pointIds) || Objects.isNull(deviceId)) {
            return null;
        }
        //征兆集合
        SymptomResponseDTO symptomResponseDTO = this.symptomJudgment(pointIds);
        if (Objects.isNull(symptomResponseDTO) || CollectionUtils.isEmpty(symptomResponseDTO.getSymptomList())) {
            return null;
        }
        return diagnosisService.faultReasoning(symptomResponseDTO, deviceId);
    }


    @Override
    public SymptomResponseDTO symptomJudgment(List<String> pointIds) {
        LambdaQueryWrapper<CommonMeasurePointDO> query = Wrappers.lambdaQuery(CommonMeasurePointDO.class);
        query.in(CommonMeasurePointDO::getPointId, pointIds);
        List<CommonMeasurePointDO> points = pointService.list(query);
        if (CollectionUtils.isEmpty(points)) {
            return null;
        }
        LambdaQueryWrapper<CommonSensorComponentDO> sensorQuery = Wrappers.lambdaQuery(CommonSensorComponentDO.class);
        sensorQuery.in(CommonSensorComponentDO::getSensorCode, points.stream().map(x -> x.getSensorCode()).collect(Collectors.toSet()));
        List<CommonSensorComponentDO> sensorComponentList = sensorComponentService.list(sensorQuery);
        if (CollectionUtils.isEmpty(sensorComponentList)) {
            return null;
        }
        LambdaQueryWrapper<AlgorithmNormalFaultFeatureDO> wrapper = Wrappers.lambdaQuery(AlgorithmNormalFaultFeatureDO.class);
        wrapper.in(AlgorithmNormalFaultFeatureDO::getComponentId, sensorComponentList.stream().map(x -> x.getComponentId()).collect(Collectors.toSet()));
        List<AlgorithmNormalFaultFeatureDO> list = featureService.list(wrapper);
        if (CollectionUtils.isEmpty(list)) {
            log.error("没有对应的规则");
            return null;
        }
        SymptomParamDTO params = new SymptomParamDTO();
        params.setFeatureInfo(list);
        List<List<List<Object>>> collect = list.stream().map(x -> {
            String pointId = x.getSensorDesc();
            String sensorCode = x.getSensorCode();
            if (StringUtils.isBlank(sensorCode)) {
                return null;
            }
            String family = H_BASE_FAMILY_NPC_PI_REAL_TIME;
            if (!StringUtils.equals(pointId, sensorCode)) {
                family = pointId.replace(sensorCode, BLANK).substring(1);
            }
            String timeRange = x.getTimeRange();
            if (StringUtils.isBlank(timeRange)) {
                return null;
            }
            long end = System.currentTimeMillis();
            Long gapValue = TimeUnitEnum.getGapValue(timeRange);
            if (Objects.isNull(gapValue)) {
                return null;
            }
            try {
                return hBase.listDataWith3600Columns(H_BASE_TABLE_NPC_PHM_DATA, sensorCode, end - gapValue, end, family);
            } catch (IOException e) {
                log.error("query history data failed.....");
            }
            return null;
        }).collect(Collectors.toList());
        params.setFeatureValue(collect);
        return (SymptomResponseDTO) symptomService.getInvokeCustomerData(params);
    }
}

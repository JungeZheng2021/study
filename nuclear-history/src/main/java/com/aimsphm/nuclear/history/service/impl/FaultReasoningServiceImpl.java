package com.aimsphm.nuclear.history.service.impl;

import com.aimsphm.nuclear.algorithm.entity.dto.*;
import com.aimsphm.nuclear.algorithm.service.AlgorithmHandlerService;
import com.aimsphm.nuclear.common.entity.*;
import com.aimsphm.nuclear.common.entity.dto.HBaseTimeSeriesDataDTO;
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
    @Resource(name = "DIAGNOSIS-RE")
    private AlgorithmHandlerService symptomService;
    @Resource
    private CommonMeasurePointService pointService;
    @Resource
    private CommonDeviceService deviceService;
    @Resource
    private CommonComponentService componentService;
    @Resource
    private AlgorithmNormalRuleService ruleService;
    @Resource
    private HBaseUtil hBase;
    @Resource
    private AlgorithmNormalRuleFeatureService ruleFeatureService;

    @Resource
    private AlgorithmNormalFaultFeatureService featureService;
    @Resource
    private AlgorithmNormalFeatureFeatureService featureFeatureService;
    @Resource
    private CommonSensorComponentService sensorComponentService;

    @Override
    public FaultReasoningResponseDTO faultReasoning(List<String> pointIds, Long deviceId) {
        CommonDeviceDO device = deviceService.getById(deviceId);
        if (Objects.isNull(device)) {
            return null;
        }
        FaultReasoningParamDTO params = new FaultReasoningParamDTO();
        params.setDeviceType(device.getDeviceType());
        //征兆集合
        SymptomResponseDTO symptomResponseDTO = this.symptomJudgment(pointIds);
        if (Objects.isNull(symptomResponseDTO) || CollectionUtils.isEmpty(symptomResponseDTO.getSymptomList())) {
            return null;
        }
        List<FaultReasoningParamVO.SymptomVO> symSet = symptomResponseDTO.getSymptomList().stream().map(x -> new FaultReasoningParamVO.SymptomVO(new Long(x))).collect(Collectors.toList());
        params.setSymSet(symSet);
        //所有的关联规则
        LambdaQueryWrapper<AlgorithmNormalRuleDO> wrapper = Wrappers.lambdaQuery(AlgorithmNormalRuleDO.class);
        wrapper.eq(AlgorithmNormalRuleDO::getDeviceType, device.getDeviceType());
        List<AlgorithmNormalRuleDO> ruleList = ruleService.list(wrapper);
        if (CollectionUtils.isNotEmpty(ruleList)) {
            List<FaultReasoningParamDTO.RefRuleSetElem> refRuleSet = ruleList.stream().map(x -> {
                FaultReasoningParamDTO.RefRuleSetElem item = new FaultReasoningParamDTO.RefRuleSetElem();
                item.setFaultId(x.getId());
                item.setMechanismCode(x.getConclusionId());
                item.setRuleType(x.getRuleType());
                //setSymSet
                setSymSetByRuleId(item, x.getId());
                //设置部件
                setComponentListByComponentId(item, x.getComponentId());
                return item;
            }).collect(Collectors.toList());
            params.setRefRuleSet(refRuleSet);
        }
        //征兆之间的相似关系
        List<SymptomCorrelationVO> symCorr = featureFeatureService.listSymptomCorrelationVO(device);
        params.setSymCorr(symCorr);

        //征兆相关信息
        LambdaQueryWrapper<AlgorithmNormalFaultFeatureDO> query = Wrappers.lambdaQuery(AlgorithmNormalFaultFeatureDO.class);
        query.eq(AlgorithmNormalFaultFeatureDO::getDeviceType, device.getDeviceType());
        List<AlgorithmNormalFaultFeatureDO> symInfoSet = featureService.list(query);
        params.setSymInfoSet(symInfoSet);

        FaultReasoningResponseDTO data = (FaultReasoningResponseDTO) symptomService.getInvokeCustomerData(params);
        return data;
    }

    @Override
    public List<FaultReasoningVO> faultReasoningVO(List<String> pointIds, Long deviceId) {
        FaultReasoningResponseDTO responseDTO = this.faultReasoning(pointIds, deviceId);
        List<FaultReasoningResponseDTO.ReasonResult> reasonResultList = responseDTO.getReasonResultList();
        return reasonResultList.stream().map(x -> {
            FaultReasoningVO reasoningVO = new FaultReasoningVO();
            FaultReasoningResponseDTO.FaultInfo faultInfo = x.getFaultInfo();
            Long faultId = faultInfo.getFaultId();
            AlgorithmNormalRuleDO ruleDO = ruleService.getById(faultId);
            reasoningVO.setFaultInfo(ruleDO);
            return reasoningVO;
        }).collect(Collectors.toList());

    }

    private void setComponentListByComponentId(FaultReasoningParamDTO.RefRuleSetElem item, Long componentId) {
        CommonComponentDO component = componentService.getById(componentId);
        if (Objects.isNull(component)) {
            return;
        }
        item.getComponentList().add(component.getId());
        if (Objects.nonNull(component.getParentComponentId())) {
            setComponentListByComponentId(item, component.getParentComponentId());
        }
    }

    private void setSymSetByRuleId(FaultReasoningParamDTO.RefRuleSetElem item, Long ruleId) {
        LambdaQueryWrapper<AlgorithmNormalRuleFeatureDO> featureWrapper = Wrappers.lambdaQuery();
        featureWrapper.eq(AlgorithmNormalRuleFeatureDO::getRuleId, ruleId);
        List<AlgorithmNormalRuleFeatureDO> list = ruleFeatureService.list(featureWrapper);
        if (CollectionUtils.isNotEmpty(list)) {
            List<FaultReasoningParamDTO.RefRuleSetElem.SymSetElem> symList = list.stream().map(m -> {
                FaultReasoningParamDTO.RefRuleSetElem.SymSetElem symSetElem = new FaultReasoningParamDTO.RefRuleSetElem.SymSetElem();
                symSetElem.setSymId(m.getFeatureId());
                symSetElem.setSymCorr(m.getCorrelation());
                symSetElem.setSymCorrLevel(m.getCorrelationLevel());
                return symSetElem;
            }).collect(Collectors.toList());
            item.setSymSet(symList);
        }
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
            return null;
        }
        SymptomParamDTO params = new SymptomParamDTO();
        params.setFeatureInfo(list);
        List<List<HBaseTimeSeriesDataDTO>> collect = list.stream().map(x -> {
            String pointId = x.getSensorDesc();
            String sensorCode = x.getSensorCode();
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
                return hBase.listObjectDataWith3600Columns(H_BASE_TABLE_NPC_PHM_DATA, sensorCode, end - gapValue, end, family);
            } catch (IOException e) {
                log.error("query history data failed.....");
            }
            return null;
        }).collect(Collectors.toList());
        params.setFeatureValue(collect);
        return (SymptomResponseDTO) symptomService.getInvokeCustomerData(params);
    }
}

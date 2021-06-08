package com.aimsphm.nuclear.algorithm.service.impl;

import com.aimsphm.nuclear.algorithm.entity.dto.FaultReasoningParamDTO;
import com.aimsphm.nuclear.algorithm.entity.dto.FaultReasoningParamVO;
import com.aimsphm.nuclear.algorithm.entity.dto.FaultReasoningResponseDTO;
import com.aimsphm.nuclear.algorithm.entity.dto.SymptomResponseDTO;
import com.aimsphm.nuclear.algorithm.service.AlgorithmHandlerService;
import com.aimsphm.nuclear.algorithm.service.FaultReasoningService;
import com.aimsphm.nuclear.algorithm.service.FeatureExtractionOperationService;
import com.aimsphm.nuclear.common.entity.*;
import com.aimsphm.nuclear.common.entity.vo.SymptomCorrelationVO;
import com.aimsphm.nuclear.common.service.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
@Service
public class FaultReasoningServiceImpl implements FaultReasoningService {
    @Resource(name = "DIAGNOSIS-RE")
    private AlgorithmHandlerService symptomService;
    @Resource
    private CommonDeviceService deviceService;
    @Resource
    private CommonComponentService componentService;
    @Resource
    private AlgorithmNormalRuleService ruleService;
    @Resource
    private AlgorithmNormalRuleFeatureService ruleFeatureService;

    @Resource
    private AlgorithmNormalFaultFeatureService featureService;
    @Resource
    private AlgorithmNormalFeatureFeatureService featureFeatureService;
    @Resource
    private FeatureExtractionOperationService featureExtractionService;

    @Override
    public FaultReasoningResponseDTO faultReasoning(List<String> pointIds, Long deviceId) {
        CommonDeviceDO device = deviceService.getById(deviceId);
        if (Objects.isNull(device)) {
            return null;
        }
        FaultReasoningParamDTO params = new FaultReasoningParamDTO();
        params.setDeviceType(device.getDeviceType());
        //征兆集合
        SymptomResponseDTO symptomResponseDTO = featureExtractionService.symptomJudgment(pointIds);
        if (Objects.isNull(symptomResponseDTO) && CollectionUtils.isEmpty(symptomResponseDTO.getSymptomList())) {
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
}

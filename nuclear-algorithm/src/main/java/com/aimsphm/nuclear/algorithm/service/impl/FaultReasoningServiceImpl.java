package com.aimsphm.nuclear.algorithm.service.impl;

import com.aimsphm.nuclear.algorithm.entity.dto.FaultReasoningParamDTO;
import com.aimsphm.nuclear.algorithm.entity.dto.FaultReasoningParamVO;
import com.aimsphm.nuclear.algorithm.entity.dto.FaultReasoningResponseDTO;
import com.aimsphm.nuclear.algorithm.service.AlgorithmHandlerService;
import com.aimsphm.nuclear.algorithm.service.FaultReasoningService;
import com.aimsphm.nuclear.algorithm.service.FeatureExtractionOperationService;
import com.aimsphm.nuclear.common.entity.AlgorithmNormalFaultFeatureDO;
import com.aimsphm.nuclear.common.entity.AlgorithmNormalRuleDO;
import com.aimsphm.nuclear.common.entity.CommonDeviceDO;
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
    private CommonMeasurePointService pointService;
    @Resource
    private AlgorithmNormalRuleService ruleService;

    @Resource
    private AlgorithmNormalFaultFeatureService featureService;
    @Resource
    private AlgorithmNormalFeatureFeatureService featureFeatureService;
    @Resource
    private FeatureExtractionOperationService featureExtractionService;

    @Override
    public void faultReasoning(List<String> pointIds, Long deviceId) {
        CommonDeviceDO device = deviceService.getById(deviceId);
        if (Objects.isNull(device)) {
            return;
        }
        FaultReasoningParamDTO params = new FaultReasoningParamDTO();
        params.setDeviceType(device.getDeviceType());
        //征兆集合
        List<Integer> integers = featureExtractionService.symptomJudgment(pointIds);
        if (CollectionUtils.isEmpty(integers)) {
            return;
        }
        List<FaultReasoningParamVO.Symptom> symSet = integers.stream().map(x -> new FaultReasoningParamVO.Symptom(new Long(x))).collect(Collectors.toList());
        params.setSymSet(symSet);
        //所有的关联规则
        LambdaQueryWrapper<AlgorithmNormalRuleDO> wrapper = Wrappers.lambdaQuery(AlgorithmNormalRuleDO.class);
        wrapper.eq(AlgorithmNormalRuleDO::getDeviceType, device.getDeviceType());
        List<AlgorithmNormalRuleDO> refRuleSet = ruleService.list(wrapper);
        params.setRefRuleSet(refRuleSet);
        //征兆之间的相似关系
        List<SymptomCorrelationVO> symCorr = featureFeatureService.listSymptomCorrelationVO(device);
        params.setSymCorr(symCorr);

        //征兆相关信息
        LambdaQueryWrapper<AlgorithmNormalFaultFeatureDO> query = Wrappers.lambdaQuery(AlgorithmNormalFaultFeatureDO.class);
        query.eq(AlgorithmNormalFaultFeatureDO::getDeviceType, device.getDeviceType());
        List<AlgorithmNormalFaultFeatureDO> symInfoSet = featureService.list(query);
        params.setSymInfoSet(symInfoSet);

        FaultReasoningResponseDTO data = (FaultReasoningResponseDTO) symptomService.getInvokeCustomerData(params);
        System.out.println(data);
    }
}

package com.aimsphm.nuclear.algorithm.service.impl;

import com.aimsphm.nuclear.algorithm.entity.dto.*;
import com.aimsphm.nuclear.algorithm.service.AlgorithmHandlerService;
import com.aimsphm.nuclear.algorithm.service.BizDiagnosisService;
import com.aimsphm.nuclear.common.entity.*;
import com.aimsphm.nuclear.common.entity.vo.AlgorithmNormalFaultFeatureVO;
import com.aimsphm.nuclear.common.entity.vo.FaultReasoningVO;
import com.aimsphm.nuclear.common.entity.vo.SymptomCorrelationVO;
import com.aimsphm.nuclear.common.enums.DataStatusEnum;
import com.aimsphm.nuclear.common.exception.CustomMessageException;
import com.aimsphm.nuclear.common.response.ResponseData;
import com.aimsphm.nuclear.common.service.*;
import com.aimsphm.nuclear.data.feign.DataServiceFeignClient;
import com.aimsphm.nuclear.data.feign.entity.dto.ConfigSettingsDTO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static com.aimsphm.nuclear.common.constant.RedisKeyConstant.REDIS_WAVE_DATA_TYPE;
import static com.aimsphm.nuclear.common.constant.SymbolConstant.COMMA;

/**
 * @Package: com.aimsphm.nuclear.common.service.impl
 * @Description: <故障诊断服务类 - 通用>
 * @Author: MILLA
 * @CreateDate: 2021-02-01
 * @UpdateUser: MILLA
 * @UpdateDate: 2021-02-01
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Slf4j
@Service
@ConditionalOnProperty(prefix = "spring.config", name = "enableAlgorithm", havingValue = "true")
public class BizDiagnosisServiceImpl implements BizDiagnosisService {
    @Resource(name = "DIAGNOSIS-RE")
    private AlgorithmHandlerService faultReasoningHandler;
    @Resource
    private CommonMeasurePointService pointService;
    @Resource
    private AlgorithmNormalRuleService ruleService;

    @Resource
    private AlgorithmNormalFaultFeatureService featureService;
    @Resource
    private AlgorithmNormalFeatureFeatureService featureFeatureService;

    @Resource
    private AlgorithmRulesService rulesService;
    @Resource
    private CommonDeviceService deviceService;

    @Resource
    private AlgorithmRulesParameterService parameterService;
    @Resource
    private CommonComponentService componentService;
    @Resource
    private AlgorithmNormalRuleFeatureService ruleFeatureService;
    @Resource
    private CommonSensorService sensorService;
    @Resource
    private AlgorithmNormalFaultConclusionService conclusionService;
    @Resource(name = "DIAGNOSIS")
    private AlgorithmHandlerService handlerService;

    @Autowired(required = false)
    private DataServiceFeignClient dataFeignClient;

    @Resource
    private StringRedisTemplate redis;

    @Override
    public Map<String, List<FaultReportResponseDTO>> faultDiagnosis(List<String> pointIdList, BizDiagnosisResultDO result, Integer isReportType) {
        try {
            List<String> sensorCodeList = pointService.listSensorCodeByPointList(pointIdList);
            if (CollectionUtils.isEmpty(sensorCodeList)) {
                result.setStatus(DataStatusEnum.FAILED.getValue());
                result.setRemark("配置信息不完整");
                return null;
            }
            List<AlgorithmRulesDO> ruleList = rulesService.listRulesBySensorCodeList(sensorCodeList);
            if (CollectionUtils.isEmpty(ruleList)) {
                result.setStatus(DataStatusEnum.FAILED.getValue());
                result.setRemark("配置信息不完整");
                return null;
            }
            Map<Long, AlgorithmRulesDO> collect = ruleList.stream().collect(Collectors.toMap(x -> x.getId(), x -> x));
            List<AlgorithmRulesParameterDO> paramList = parameterService.listParamByRuleList(new ArrayList<>(collect.keySet()));
            if (CollectionUtils.isEmpty(paramList)) {
                result.setStatus(DataStatusEnum.FAILED.getValue());
                result.setRemark("配置信息不完整");
                return null;
            }
            //含有波形数据
            Map<String, String> codeAndType = paramList.stream().filter(x -> Objects.nonNull(x.getParameterType()) && x.getParameterType() == 1)
                    .collect(Collectors.toMap(x -> x.getSensorCode(), x -> x.getSensorSignalType(), (a, b) -> a));
            if (MapUtils.isEmpty(codeAndType)) {
                log.warn("sensorCode or signalType is null in config files ...");
                result.setStatus(DataStatusEnum.FAILED.getValue());
                result.setRemark("配置信息不完整");
                return null;
            }
            //发送信息要求设置参数信息 - 如果没有获取到会阻塞
            sendMsgAndCheckParamsIsExist(codeAndType);
            //调用算法
            Map<Integer, List<AlgorithmRulesParameterDO>> params = paramList.stream().collect(Collectors.groupingBy(x -> x.getRuleId()));
            List<RuleParamDTO> ruleParamList = params.entrySet().stream().map(x -> {
                Integer ruleId = x.getKey();
                AlgorithmRulesDO rule = collect.get(Long.valueOf(ruleId));
                if (Objects.isNull(rule)) {
                    return null;
                }
                RuleParamDTO dto = new RuleParamDTO();
                dto.setParameter(x.getValue());
                dto.setRuleId(ruleId);
                dto.setRule(rule.getRule());
                dto.setSensorCode(rule.getSensorCode());
                return dto;
            }).filter(Objects::nonNull).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(ruleParamList)) {
                result.setStatus(DataStatusEnum.FAILED.getValue());
                result.setRemark("配置信息不完整");
                return null;
            }
            FaultDiagnosisParamDTO paramDTO = new FaultDiagnosisParamDTO();
            paramDTO.setRules(ruleParamList);
            paramDTO.setReturnReport(isReportType);
            FaultDiagnosisResponseDTO response = (FaultDiagnosisResponseDTO) handlerService.getInvokeCustomerData(paramDTO);
            resetRedisFlag(codeAndType);
            setFaultDiagnosisResponse(response, result);
            return response.getReportFigure();
        } catch (Exception e) {
            log.error("fault diagnosis failed......{}", e);
            result.setStatus(DataStatusEnum.FAILED.getValue());
            result.setRemark("调用算法报错");
        }
        return null;
    }

    @Override
    public List<FaultReasoningVO> faultReasoning(SymptomResponseDTO responseDTO, Long deviceId) {
        CommonDeviceDO device = deviceService.getById(deviceId);
        if (Objects.isNull(device)) {
            return null;
        }
        if (Objects.isNull(responseDTO) || CollectionUtils.isEmpty(responseDTO.getSymptomList())) {
            return null;
        }
        //征兆集合
        FaultReasoningParamDTO params = new FaultReasoningParamDTO();
        params.setDeviceType(device.getDeviceType());
        List<FaultReasoningParamVO.SymptomVO> symSet = responseDTO.getSymptomList().stream().map(x -> new FaultReasoningParamVO.SymptomVO(new Long(x))).collect(Collectors.toList());
        params.setSymSet(symSet);
        //所有的关联规则
        LambdaQueryWrapper<AlgorithmNormalRuleDO> wrapper = Wrappers.lambdaQuery(AlgorithmNormalRuleDO.class);
        wrapper.eq(AlgorithmNormalRuleDO::getDeviceType, device.getDeviceType());
        List<AlgorithmNormalRuleDO> ruleList = ruleService.list(wrapper);
        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(ruleList)) {
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

        FaultReasoningResponseDTO data = (FaultReasoningResponseDTO) faultReasoningHandler.getInvokeCustomerData(params);
        return faultReasoningVO(data);
    }

    private List<FaultReasoningVO> faultReasoningVO(FaultReasoningResponseDTO responseDTO) {
        if (Objects.isNull(responseDTO) || CollectionUtils.isEmpty(responseDTO.getReasonResultList())) {
            return null;
        }
        List<FaultReasoningResponseDTO.ReasonResult> reasonResultList = responseDTO.getReasonResultList();
        return reasonResultList.stream().map(x -> {
            FaultReasoningVO reasoningVO = new FaultReasoningVO();
            //推荐程度
            reasoningVO.setRecommend(x.getRecommend());
            //基本详情
            FaultReasoningResponseDTO.FaultInfo faultInfo = x.getFaultInfo();
            if (Objects.isNull(faultInfo)) {
                return null;
            }
            Long faultId = faultInfo.getFaultId();
            AlgorithmNormalRuleDO ruleDO = ruleService.getById(faultId);
            reasoningVO.setFaultInfo(ruleDO);
            if (Objects.nonNull(ruleDO)) {
                reasoningVO.setRuleDesc(ruleDO.getRuleDesc());
            }
            //故障特征
            List<FaultReasoningParamVO.SymptomVO> symSet = faultInfo.getSymSet();
            if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(symSet)) {
                List<AlgorithmNormalFaultFeatureVO> collect = symSet.stream().map(y -> {
                    AlgorithmNormalFaultFeatureVO featureVO = featureService.getAlgorithmNormalFaultFeatureByComponentId(y.getSymId());
                    return featureVO;
                }).collect(Collectors.toList());
                reasoningVO.setFeatures(collect);
            }
            Integer mechanismCode = x.getFaultInfo().getMechanismCode();
            AlgorithmNormalFaultConclusionDO conclusionDO = conclusionService.getById(mechanismCode);
            reasoningVO.setConclusion(conclusionDO);
            return reasoningVO;
        }).collect(Collectors.toList());
    }

    private void setSymSetByRuleId(FaultReasoningParamDTO.RefRuleSetElem item, Long ruleId) {
        LambdaQueryWrapper<AlgorithmNormalRuleFeatureDO> featureWrapper = Wrappers.lambdaQuery();
        featureWrapper.eq(AlgorithmNormalRuleFeatureDO::getRuleId, ruleId);
        List<AlgorithmNormalRuleFeatureDO> list = ruleFeatureService.list(featureWrapper);
        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(list)) {
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

    private void resetRedisFlag(Map<String, String> codeAndType) {
        codeAndType.forEach((sensorCode, type) -> {
            String key = String.format(REDIS_WAVE_DATA_TYPE, sensorCode, type);
            redis.delete(key);
        });
    }

    /**
     * @param response
     * @param result
     * @return
     */
    public void setFaultDiagnosisResponse(FaultDiagnosisResponseDTO response, BizDiagnosisResultDO result) {
        if (CollectionUtils.isEmpty(response.getActivation())) {
            log.warn("the response is null....");
            result.setStatus(DataStatusEnum.FAILED.getValue());
            result.setRemark("输出结果是空");
            return;
        }
        result.setGmtDiagnosis(new Date());
        String ruleIds = org.apache.commons.lang3.StringUtils.join(response.getActivation(), COMMA);
        result.setDiagnosisResult(ruleIds);
        result.setStatus(DataStatusEnum.SUCCESS.getValue());
    }

    private void sendMsgAndCheckParamsIsExist(Map<String, String> codeAndType) {
        ArrayList<String> sensorCodeList = new ArrayList<>(codeAndType.keySet());
        List<CommonSensorDO> sensorList = sensorService.listCommonSensorBySensorCodeList(sensorCodeList);
        if (CollectionUtils.isEmpty(sensorList)) {
            throw new CustomMessageException("sensorCode config has no edgeCode");
        }
        Map<String, List<CommonSensorDO>> collect = sensorList.stream().filter(x -> StringUtils.hasText(x.getEdgeCode())).collect(Collectors.groupingBy(x -> x.getEdgeCode()));
        Set<String> isSuccess = new HashSet<>();
        int retryCount = -1;
        //循环下发指令
        try {
            while (isSuccess.size() < collect.size()) {
                checkRetryTime(retryCount++);
                collect.forEach((edgeCode, v) -> {
                    if (isSuccess.contains(edgeCode)) {
                        return;
                    }
                    List<String> list = v.stream().map(x -> x.getSensorCode()).collect(Collectors.toList());
                    ConfigSettingsDTO settings = new ConfigSettingsDTO();
                    settings.setVibrationWaveUploadRequest(list);
                    ResponseData<Boolean> response = dataFeignClient.invokeService(edgeCode, settings);
                    if (response.getData()) {
                        isSuccess.add(edgeCode);
                    }
                });
                pauseTime(isSuccess.size() < collect.size(), 1000L);
            }
        } catch (Exception e) {
            throw new CustomMessageException("send message failed");
        }
        pauseTime(true, 60000L);
        //循环获取key是否有值
        isSuccess.clear();
        retryCount = -1;
        while (isSuccess.size() < codeAndType.size()) {
            checkRetryTime(retryCount++);
            codeAndType.forEach((sensorCode, type) -> {
                if (isSuccess.contains(sensorCode)) {
                    return;
                }
                String key = String.format(REDIS_WAVE_DATA_TYPE, sensorCode, type);
                if (redis.hasKey(key)) {
                    isSuccess.add(key);
                }
            });
            pauseTime(isSuccess.size() < codeAndType.size(), 5000L);
            log.info("执行判断参数是否存在....{}", retryCount);
        }
    }

    /**
     * 暂停一段时间
     *
     * @param isPause 是否需要暂停
     * @param times   暂停的时间[毫秒值]
     */
    private void pauseTime(boolean isPause, Long times) {
        log.info("等等待...");
        if (isPause) {
            try {
                Thread.sleep(Objects.isNull(times) ? 1000L : times);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkRetryTime(int retryCount) {
        //重试3次之后就算是失败
        if (retryCount == 3) {
            log.error("retry {} times ,but still failed", retryCount);
            throw new CustomMessageException("retry " + retryCount + "times ,but still failed");
        }
    }
}

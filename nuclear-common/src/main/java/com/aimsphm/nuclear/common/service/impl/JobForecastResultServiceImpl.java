package com.aimsphm.nuclear.common.service.impl;

import com.aimsphm.nuclear.algorithm.entity.dto.SymptomResponseDTO;
import com.aimsphm.nuclear.algorithm.service.BizDiagnosisService;
import com.aimsphm.nuclear.common.entity.CommonComponentDO;
import com.aimsphm.nuclear.common.entity.CommonMeasurePointDO;
import com.aimsphm.nuclear.common.entity.JobForecastResultDO;
import com.aimsphm.nuclear.common.entity.bo.ConditionsQueryBO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.entity.vo.FaultReasoningVO;
import com.aimsphm.nuclear.common.entity.vo.JobForecastResultVO;
import com.aimsphm.nuclear.common.mapper.JobForecastResultMapper;
import com.aimsphm.nuclear.common.service.CommonMeasurePointService;
import com.aimsphm.nuclear.common.service.JobForecastResultService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.CaseFormat;
import org.assertj.core.util.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.aimsphm.nuclear.common.constant.SymbolConstant.COMMA;

/**
 * @Package: com.aimsphm.nuclear.common.service.impl
 * @Description: <预测结果信息服务实现类>
 * @Author: MILLA
 * @CreateDate: 2021-07-15
 * @UpdateUser: MILLA
 * @UpdateDate: 2021-07-15
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Service
@ConditionalOnProperty(prefix = "spring.config", name = "enableServiceExtImpl", havingValue = "true")
public class JobForecastResultServiceImpl extends ServiceImpl<JobForecastResultMapper, JobForecastResultDO> implements JobForecastResultService {

    @Resource
    private BizDiagnosisService diagnosisService;
    @Resource
    private CommonMeasurePointService pointService;

    @Override
    public Page<JobForecastResultDO> listJobForecastResultByPageWithParams(QueryBO<JobForecastResultDO> queryBO) {
        if (Objects.nonNull(queryBO.getPage().getOrders()) && !queryBO.getPage().getOrders().isEmpty()) {
            queryBO.getPage().getOrders().stream().forEach(item -> item.setColumn(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, item.getColumn())));
        }
        return this.page(queryBO.getPage(), customerConditions(queryBO));
    }

    /**
     * 拼装查询条件
     *
     * @param queryBO
     * @return
     */
    private LambdaQueryWrapper<JobForecastResultDO> customerConditions(QueryBO<JobForecastResultDO> queryBO) {
        LambdaQueryWrapper<JobForecastResultDO> wrapper = queryBO.lambdaQuery();
        ConditionsQueryBO query = queryBO.getQuery();
        if (Objects.nonNull(query.getEnd()) && Objects.nonNull(query.getEnd())) {
        }
        if (StringUtils.hasText(queryBO.getQuery().getKeyword())) {
        }
        return wrapper;
    }

    @Override
    public List<JobForecastResultDO> listJobForecastResultWithParams(QueryBO<JobForecastResultDO> queryBO) {
        return this.list(customerConditions(queryBO));
    }

    @Override
    public JobForecastResultVO listJobForecastResultByIds(Long deviceId, Long componentId) {
        LambdaQueryWrapper<JobForecastResultDO> query = Wrappers.lambdaQuery(JobForecastResultDO.class);
        query.eq(JobForecastResultDO::getDeviceId, deviceId);
        query.eq(JobForecastResultDO::getComponentId, componentId);
        List<JobForecastResultDO> list = this.list(query);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        JobForecastResultVO vo = new JobForecastResultVO();
        List<JobForecastResultVO.ForecastDataVO> dataList = Lists.newArrayList();
        vo.setDataList(dataList);
        AtomicReference<JobForecastResultDO> first = new AtomicReference<>();
        list.stream().forEach(x -> {
            JobForecastResultVO.ForecastDataVO dataVO = new JobForecastResultVO.ForecastDataVO();
            CommonMeasurePointDO point = pointService.getPointByPointId(x.getPointId());
            if (Objects.isNull(point)) {
                return;
            }
            BeanUtils.copyProperties(point, dataVO);
            dataVO.setPointId(x.getPointId());
            dataVO.setTrendData(JSON.parseArray(x.getTrendData(), List.class));
            dataVO.setHistoryData(JSON.parseArray(x.getHistoryData(), List.class));
            dataVO.setForecastData(JSON.parseArray(x.getForecastData(), List.class));
            dataList.add(dataVO);
            first.set(x);
        });
        JobForecastResultDO result = first.get();
        vo.setDeviceId(result.getDeviceId());
        vo.setComponentId(result.getComponentId());
        vo.setForecastRange(result.getForecastRange());
        vo.setGmtForecast(result.getGmtForecast());
        SymptomResponseDTO dto = new SymptomResponseDTO();
        if (StringUtils.hasText(result.getSymptomIds())) {
            List<Integer> collect = null;
            try {
                collect = Arrays.stream(result.getSymptomIds().split(COMMA)).map(x -> Integer.parseInt(x)).collect(Collectors.toList());
            } catch (Exception e) {
                log.error("convent data failed ...{}", e);
            }
            dto.setSymptomList(collect);
        }
        List<FaultReasoningVO> faultReasoningVOS = diagnosisService.faultReasoning(dto, deviceId);
        vo.setForecastList(faultReasoningVOS);
        return vo;
    }

    @Override
    public List<CommonComponentDO> listCommonComponentByDeviceId(Long deviceId) {
        return this.getBaseMapper().listCommonComponentByDeviceId(deviceId);
    }
}

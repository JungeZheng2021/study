package com.aimsphm.nuclear.history.service.impl;

import com.aimsphm.nuclear.algorithm.entity.dto.AnalysisVibrationParamDTO;
import com.aimsphm.nuclear.algorithm.entity.dto.AnalysisVibrationResponseDTO;
import com.aimsphm.nuclear.algorithm.entity.dto.MovingAverageParamDTO;
import com.aimsphm.nuclear.algorithm.entity.dto.PredictionParamDTO;
import com.aimsphm.nuclear.algorithm.enums.AlgorithmTypeEnum;
import com.aimsphm.nuclear.algorithm.service.AlgorithmHandlerService;
import com.aimsphm.nuclear.algorithm.util.WhetherTreadLocal;
import com.aimsphm.nuclear.common.entity.CommonMeasurePointDO;
import com.aimsphm.nuclear.common.entity.bo.DataAnalysisQueryBO;
import com.aimsphm.nuclear.common.entity.bo.DataAnalysisQueryMultiBO;
import com.aimsphm.nuclear.common.entity.bo.HistoryQueryMultiBO;
import com.aimsphm.nuclear.common.entity.dto.HBaseParamDTO;
import com.aimsphm.nuclear.common.service.CommonMeasurePointService;
import com.aimsphm.nuclear.history.entity.vo.HistoryDataVO;
import com.aimsphm.nuclear.history.service.AlgorithmQueryService;
import com.aimsphm.nuclear.history.service.HBaseService;
import com.aimsphm.nuclear.history.service.HistoryQueryService;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.aimsphm.nuclear.common.constant.HBaseConstant.H_BASE_FAMILY_NPC_VIBRATION_RAW;
import static com.aimsphm.nuclear.common.constant.HBaseConstant.H_BASE_TABLE_NPC_PHM_DATA;

/**
 * @Package: com.aimsphm.nuclear.history.service
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2020/12/22 13:35
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/12/22 13:35
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Service
public class AlgorithmQueryServiceImpl implements AlgorithmQueryService {
    @Resource
    private HistoryQueryService historyService;
    @Resource
    private Map<String, AlgorithmHandlerService> handler;
    @Resource
    private CommonMeasurePointService pointService;
    @Resource
    private HBaseService hBaseService;

    @Override
    public Map<String, HistoryDataVO> listMovingAverageInfo(HistoryQueryMultiBO multi) {
        WhetherTreadLocal.INSTANCE.setWhether(false);
        AlgorithmHandlerService algorithm = handler.get(AlgorithmTypeEnum.MOVING_AVERAGE.getType());
        Map<String, HistoryDataVO> data = historyService.listHistoryDataWithPointIdsByScan(multi);
        data.entrySet().stream().forEach(x -> {
            HistoryDataVO value = x.getValue();
            if (Objects.nonNull(value) && CollectionUtils.isNotEmpty(value.getChartData())) {
                MovingAverageParamDTO dto = new MovingAverageParamDTO();
                dto.setSignal(value.getChartData());
                dto.setPointId(x.getKey());
                List<List<Object>> retVal = (List<List<Object>>) algorithm.getInvokeCustomerData(dto);
                value.setChartData(retVal);
            }
        });
        return data;
    }

    @Override
    public Map<String, HistoryDataVO> listPredictionInfo(HistoryQueryMultiBO multi) {
        //是否需要获取阈值
        WhetherTreadLocal.INSTANCE.setWhether(false);
        AlgorithmHandlerService algorithm = handler.get(AlgorithmTypeEnum.TREND_FORECAST.getType());
        Map<String, HistoryDataVO> data = historyService.listHistoryDataWithPointIdsByScan(multi);
        data.entrySet().stream().forEach(x -> {
            HistoryDataVO value = x.getValue();
            if (Objects.nonNull(value) && CollectionUtils.isNotEmpty(value.getChartData())) {
                PredictionParamDTO dto = new PredictionParamDTO();
                dto.setSignal(value.getChartData());
                dto.setPointId(x.getKey());
                BeanUtils.copyProperties(dto, multi);
                List<List<Object>> retVal = (List<List<Object>>) algorithm.getInvokeCustomerData(dto);
                value.setChartData(retVal);
            }
        });
        return data;
    }

    @Override
    public Map<String, List<List<List<Object>>>> listVibrationAnalysisData(DataAnalysisQueryMultiBO query) {
        Map<String, List<List<List<Object>>>> result = Maps.newHashMap();
        Assert.notNull(query, "params can not be null");
        AlgorithmTypeEnum type = AlgorithmTypeEnum.getByValue(query.getType());
        Assert.notNull(type, "this algorithm is not supported");
        List<HBaseParamDTO> list = query.getList();
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        List<HBaseParamDTO> params = list.stream().filter(x -> StringUtils.hasText(x.getPointId()) && Objects.nonNull(x.getTimestamp())).map(item -> {
            CommonMeasurePointDO point = pointService.getPointByPointId(item.getPointId());
            HBaseParamDTO paramDTO = new HBaseParamDTO();
            paramDTO.setPointId(point.getSensorCode());
            paramDTO.setFamily(H_BASE_FAMILY_NPC_VIBRATION_RAW);
            paramDTO.setTimestamp(item.getTimestamp());
            return paramDTO;
        }).collect(Collectors.toList());
        DataAnalysisQueryBO analysisQuery = new DataAnalysisQueryBO();
        analysisQuery.setParams(params);
        analysisQuery.setTableName(H_BASE_TABLE_NPC_PHM_DATA);
        Map<String, Map<Long, Object>> retVal = hBaseService.listArrayData(analysisQuery);
        AlgorithmHandlerService algorithm = handler.get(AlgorithmTypeEnum.DATA_ANALYSIS.getType());

        list.stream().filter(x -> StringUtils.hasText(x.getPointId()) && Objects.nonNull(x.getTimestamp())).forEach(item -> {
            CommonMeasurePointDO point = pointService.getPointByPointId(item.getPointId());
            if (Objects.isNull(point)) {
                return;
            }
            Map<Long, Object> map = retVal.get(point.getSensorCode());
            if (MapUtils.isEmpty(map)) {
                return;
            }
            Object o = map.get(item.getTimestamp());
            if (Objects.isNull(o)) {
                return;
            }
            AnalysisVibrationParamDTO param = new AnalysisVibrationParamDTO();
            param.setSignal((Double[]) o);
            //TODO 以下参数还需要从表中获取
            param.setFs(25000);
            param.setMinFrequency(0);
            param.setMaxFrequency(10000);
            param.setSignalType(3);
            param.setMaxLevel(3);
            param.setType(type.getType());
            AnalysisVibrationResponseDTO response = (AnalysisVibrationResponseDTO) algorithm.getInvokeCustomerData(param);
            result.put(point.getPointId(), response.getCurve());
        });
        return result;
    }
}

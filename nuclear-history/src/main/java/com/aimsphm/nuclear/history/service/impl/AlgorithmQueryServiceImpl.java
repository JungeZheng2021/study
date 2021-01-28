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
import com.aimsphm.nuclear.common.enums.PointFeatureEnum;
import com.aimsphm.nuclear.common.service.CommonMeasurePointService;
import com.aimsphm.nuclear.history.entity.vo.HistoryDataVO;
import com.aimsphm.nuclear.history.service.AlgorithmQueryService;
import com.aimsphm.nuclear.history.service.HBaseService;
import com.aimsphm.nuclear.history.service.HistoryQueryService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
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
        if (CollectionUtils.isEmpty(query.getPointIds()) || CollectionUtils.isEmpty(query.getTimestamps())) {
            return null;
        }
        Assert.isTrue(query.getPointIds().size() == query.getTimestamps().size(), "pointIds's size cant match timestamps's size");
        List<String> pointIds = query.getPointIds();
        List<Long> timestamps = query.getTimestamps();
        List<HBaseParamDTO> params = Lists.newArrayList();
        List<CommonMeasurePointDO> pointList = new ArrayList<>(pointIds.size());
        for (int i = 0; i < pointIds.size(); i++) {
            String pointId = pointIds.get(i);
            Long timestamp = timestamps.get(i);
            CommonMeasurePointDO point = pointService.getPointByPointId(pointId);
            pointList.add(point);
            if (Objects.isNull(point)) {
                continue;
            }
            HBaseParamDTO paramDTO = new HBaseParamDTO();
            paramDTO.setPointId(point.getSensorCode());
            paramDTO.setFamily(H_BASE_FAMILY_NPC_VIBRATION_RAW);
            paramDTO.setTimestamp(timestamp);
            params.add(paramDTO);
        }
        DataAnalysisQueryBO analysisQuery = new DataAnalysisQueryBO();
        analysisQuery.setParams(params);
        analysisQuery.setTableName(H_BASE_TABLE_NPC_PHM_DATA);
        Map<String, Map<Long, Object>> retVal = hBaseService.listArrayData(analysisQuery);
        AlgorithmHandlerService algorithm = handler.get(AlgorithmTypeEnum.DATA_ANALYSIS.getType());
        for (int i = 0; i < pointIds.size(); i++) {
            Long timestamp = timestamps.get(i);
            CommonMeasurePointDO point = pointList.get(i);
            if (Objects.isNull(point)) {
                continue;
            }
            Map<Long, Object> map = retVal.get(point.getSensorCode());
            if (MapUtils.isEmpty(map)) {
                continue;
            }
            Object o = map.get(timestamp);
            if (Objects.isNull(o)) {
                continue;
            }
            AnalysisVibrationParamDTO param = new AnalysisVibrationParamDTO();
            param.setSignal((Double[]) o);
            param.setFs(25000);

            //TODO 以下参数还需要从表中获取
            //根据sensorCode查询以下配置
            param.setMinFrequency(0);
            param.setMaxFrequency(10000);

            if (PointFeatureEnum.ACC.getValue().equals(point.getFeatureType())) {
                param.setSignalType(3);
            }
            if (PointFeatureEnum.VEC.getValue().equals(point.getFeatureType())) {
                param.setSignalType(2);
            }
            //信号类型为空的话不执行的调用
            if (Objects.isNull(param.getSignalType())) {
                continue;
            }
            param.setMaxLevel(3);
            param.setType(type.getType());
            AnalysisVibrationResponseDTO response = (AnalysisVibrationResponseDTO) algorithm.getInvokeCustomerData(param);
            result.put(point.getPointId(), response.getCurve());
        }
        return result;
    }
}

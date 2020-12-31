package com.aimsphm.nuclear.history.service.impl;

import com.aimsphm.nuclear.algorithm.entity.dto.MovingAverageParamDTO;
import com.aimsphm.nuclear.algorithm.entity.dto.PredictionParamDTO;
import com.aimsphm.nuclear.algorithm.enums.AlgorithmTypeEnum;
import com.aimsphm.nuclear.algorithm.service.AlgorithmHandlerService;
import com.aimsphm.nuclear.algorithm.util.WhetherTreadLocal;
import com.aimsphm.nuclear.common.entity.bo.HistoryQueryMultiBO;
import com.aimsphm.nuclear.history.entity.vo.HistoryDataVO;
import com.aimsphm.nuclear.history.service.AlgorithmQueryService;
import com.aimsphm.nuclear.history.service.HistoryQueryService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
}

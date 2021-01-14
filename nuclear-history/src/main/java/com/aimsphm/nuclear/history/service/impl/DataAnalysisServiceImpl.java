package com.aimsphm.nuclear.history.service.impl;

import com.aimsphm.nuclear.common.entity.CommonMeasurePointDO;
import com.aimsphm.nuclear.common.entity.bo.DataAnalysisQueryBO;
import com.aimsphm.nuclear.common.entity.bo.HistoryQueryMultiBO;
import com.aimsphm.nuclear.common.entity.dto.HBaseParamDTO;
import com.aimsphm.nuclear.common.service.CommonMeasurePointService;
import com.aimsphm.nuclear.history.entity.vo.HistoryDataVO;
import com.aimsphm.nuclear.history.service.DataAnalysisService;
import com.aimsphm.nuclear.history.service.HBaseService;
import com.aimsphm.nuclear.history.service.HistoryQueryService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.aimsphm.nuclear.common.constant.HBaseConstant.H_BASE_FAMILY_NPC_VIBRATION_RAW;
import static com.aimsphm.nuclear.common.constant.HBaseConstant.H_BASE_TABLE_NPC_PHM_DATA;
import static java.util.Objects.nonNull;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.springframework.util.StringUtils.hasText;

/**
 * @Package: com.aimsphm.nuclear.history.service.impl
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2021/01/08 16:11
 * @UpdateUser: MILLA
 * @UpdateDate: 2021/01/08 16:11
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Slf4j
@Service
public class DataAnalysisServiceImpl implements DataAnalysisService {
    @Resource
    private HistoryQueryService service;
    @Resource
    private HBaseService hBaseService;
    @Resource
    private CommonMeasurePointService pointService;

    @Override
    public Map<String, HistoryDataVO> listAnalysisDataWithPointList(HistoryQueryMultiBO queryMultiBO) {
        Map<String, HistoryDataVO> historyData = service.listHistoryDataWithPointIdsByScan(queryMultiBO);
        if (MapUtils.isEmpty(historyData)) {
            return null;
        }
        List<HBaseParamDTO> params = Lists.newArrayList();
        Map<String, HistoryDataVO> data = Maps.newHashMap();
        historyData.entrySet().stream().filter(x -> checkFilter(x)).forEach(x -> {
            CommonMeasurePointDO point = pointService.getPointByPointId(x.getKey());
            if (Objects.isNull(point)) {
                return;
            }
            List<HBaseParamDTO> hBaseParamDTOS = assembleHBaseColumnList(point, x.getValue());
            if (CollectionUtils.isEmpty(hBaseParamDTOS)) {
                return;
            }
            params.addAll(hBaseParamDTOS);
            data.put(point.getSensorCode(), x.getValue());
        });
        DataAnalysisQueryBO analysisQuery = new DataAnalysisQueryBO();
        analysisQuery.setParams(params);
        analysisQuery.setTableName(H_BASE_TABLE_NPC_PHM_DATA);
        Map<String, Map<Long, Object>> retVal = hBaseService.listArrayData(analysisQuery);
        operateResult(data, retVal);
        return data;
    }

    private void operateResult(Map<String, HistoryDataVO> data, Map<String, Map<Long, Object>> retVal) {
        data.entrySet().stream().filter(x -> checkFilter(x)).forEach(x -> {
            String sensorCode = x.getKey();
            List<List<Object>> chartData = x.getValue().getChartData();
            if (!retVal.containsKey(sensorCode)) {
                chartData.stream().forEach(list -> list.add(false));
                return;
            }
            Map<Long, Object> timestampValue = retVal.get(sensorCode);
            chartData.stream().forEach(list -> list.add(timestampValue.containsKey(list.get(0))));
        });
    }

    private boolean checkFilter(Map.Entry<String, HistoryDataVO> x) {
        return hasText(x.getKey()) && nonNull(x.getValue()) && isNotEmpty(x.getValue().getChartData());
    }

    public List<HBaseParamDTO> assembleHBaseColumnList(CommonMeasurePointDO point, HistoryDataVO data) {
        List<List<Object>> chartData = data.getChartData();
        return chartData.stream().map(x -> {
            Long timestamp = (Long) x.get(0);
            HBaseParamDTO itemBO = new HBaseParamDTO();
            itemBO.setPointId(point.getSensorCode());
            itemBO.setTimestamp(timestamp);
            itemBO.setFamily(H_BASE_FAMILY_NPC_VIBRATION_RAW);
            return itemBO;
        }).collect(Collectors.toList());
    }
}

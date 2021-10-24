package com.aimsphm.nuclear.history.service.impl;

import com.aimsphm.nuclear.common.entity.CommonMeasurePointDO;
import com.aimsphm.nuclear.common.entity.bo.HistoryQueryMultiBO;
import com.aimsphm.nuclear.common.entity.dto.HBaseParamDTO;
import com.aimsphm.nuclear.common.entity.vo.HistoryDataVO;
import com.aimsphm.nuclear.common.service.BizOriginalDataService;
import com.aimsphm.nuclear.common.service.CommonMeasurePointService;
import com.aimsphm.nuclear.history.service.DataAnalysisService;
import com.aimsphm.nuclear.history.service.HBaseService;
import com.aimsphm.nuclear.history.service.HistoryQueryService;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.aimsphm.nuclear.common.constant.HBaseConstant.H_BASE_FAMILY_NPC_VIBRATION_RAW;
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
    private HBaseService hBaseService;
    @Resource
    private HistoryQueryService service;
    @Resource
    private BizOriginalDataService dataService;
    @Resource
    private CommonMeasurePointService pointService;

    @Override
    public Map<String, HistoryDataVO> listAnalysisDataWithPointList(HistoryQueryMultiBO bo) {
        Map<String, HistoryDataVO> historyData = service.listHistoryDataWithPointIdsByScan(bo);
        if (MapUtils.isEmpty(historyData)) {
            return null;
        }
        Map<String, HistoryDataVO> data = Maps.newHashMap();
        Map<String, List<Long>> retVal = Maps.newHashMap();
        historyData.entrySet().stream().filter(this::checkFilter).forEach(x -> {
            CommonMeasurePointDO point = pointService.getPointByPointId(x.getKey());
            if (Objects.isNull(point)) {
                return;
            }
            List<List<Object>> chartData = x.getValue().getChartData();
            Long start = (Long) chartData.get(0).get(0);
            Long end = (Long) chartData.get(chartData.size() - 1).get(0);
            List<Long> timestamp = dataService.listBizOriginalDataByParams(point.getSensorCode(), start, end);
            retVal.put(point.getSensorCode(), timestamp);
            data.put(point.getSensorCode(), x.getValue());
        });
        operateResult(data, retVal);
        return data;
    }

    private void operateResult(Map<String, HistoryDataVO> data, Map<String, List<Long>> retVal) {
        data.entrySet().stream().filter(this::checkFilter).forEach(x -> {
            String sensorCode = x.getKey();
            List<List<Object>> chartData = x.getValue().getChartData();
            if (!retVal.containsKey(sensorCode) || CollectionUtils.isEmpty(retVal.get(sensorCode))) {
                return;
            }
            //组装标注信息
            List<Long> timestampValue = retVal.get(sensorCode);
            List<List<Object>> collect = chartData.stream().filter(list -> timestampValue.contains(list.get(0))).map(list -> {
                List<Object> item = new ArrayList<>();
                item.addAll(list);
                item.add(true);
                return item;
            }).collect(Collectors.toList());
            x.getValue().setLabelData(collect);
        });
    }

    private boolean checkFilter(Map.Entry<String, HistoryDataVO> x) {
        return hasText(x.getKey()) && nonNull(x.getValue()) && isNotEmpty(x.getValue().getChartData());
    }

    public List<HBaseParamDTO> assembleOriginalDataTimestamp(CommonMeasurePointDO point, HistoryDataVO data) {
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

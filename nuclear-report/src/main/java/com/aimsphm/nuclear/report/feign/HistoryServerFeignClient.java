package com.aimsphm.nuclear.report.feign;

import com.aimsphm.nuclear.common.entity.bo.HistoryQueryMultiBO;
import com.aimsphm.nuclear.common.entity.vo.EventDataVO;
import com.aimsphm.nuclear.common.entity.vo.FaultReasoningVO;
import com.aimsphm.nuclear.common.entity.vo.HistoryDataWithThresholdVO;
import com.aimsphm.nuclear.common.response.ResponseData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;


/**
 * <p>
 * 功能描述:
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2021/08/09 19:49
 */
@Component
@FeignClient(name = "history/history")//, fallback = HistoryServerFeignFallback.class)
public interface HistoryServerFeignClient {
    /**
     * 故障推理
     *
     * @param pointIds     测点列表
     * @param deviceId     设备id
     * @param gmtLastAlarm 最后一次报警时间
     * @return
     */
    @GetMapping("algorithm/fault/reasoning")
    ResponseData<List<FaultReasoningVO>> faultReasoning(@RequestParam("pointIds") List<String> pointIds, @RequestParam Long deviceId, @RequestParam Long gmtLastAlarm);

    /**
     * 查询历史数据
     *
     * @param queryMultiBO 多个测点
     * @return
     */
    @GetMapping("multiple")
    ResponseData<Map<String, HistoryDataWithThresholdVO>> listHistoryWithPointList(@SpringQueryMap HistoryQueryMultiBO queryMultiBO);

    /**
     * 查询多个测点实测值、估计值、报警测点、残差值
     *
     * @param queryMultiBO 查询条件
     * @return
     */
    @GetMapping("multiple/realtime")
    ResponseData<Map<String, EventDataVO>> listDataWithPointList(@SpringQueryMap HistoryQueryMultiBO queryMultiBO);

}
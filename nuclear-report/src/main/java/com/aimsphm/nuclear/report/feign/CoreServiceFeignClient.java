package com.aimsphm.nuclear.report.feign;

import com.aimsphm.nuclear.common.entity.bo.TimeRangeQueryBO;
import com.aimsphm.nuclear.common.entity.vo.DeviceStatusVO;
import com.aimsphm.nuclear.common.entity.vo.LabelVO;
import com.aimsphm.nuclear.common.response.ResponseData;
import com.aimsphm.nuclear.report.feign.fallback.CoreServiceFeignFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

/**
 * @Package: com.aimsphm.nuclear.history
 * @Description: <算法调用服务类>
 * @Author: MILLA
 * @CreateDate: 2020/6/28 10:54
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/6/28 10:54
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Component
@FeignClient(name = "core")//, fallback = CoreServiceFeignFallback.class)
public interface CoreServiceFeignClient {
    /**
     * 报警信息统计
     *
     * @param deviceId
     * @param range
     * @return
     */
    @GetMapping("service/monitor/{deviceId}/statistics/warning")
    ResponseData<Map<Integer, Long>> listRunningDuration(@PathVariable(value = "deviceId") Long deviceId, @SpringQueryMap TimeRangeQueryBO range);

    /**
     * 报警信息统计
     *
     * @param deviceId
     * @param range
     * @return
     */
    @GetMapping("service/monitor/{deviceId}/statistics/warning")
    ResponseData<List<List<LabelVO>>> listWarningPoint(@PathVariable(value = "deviceId") Long deviceId, @SpringQueryMap TimeRangeQueryBO range);

    /**
     * 设备运行状态
     *
     * @param deviceId
     * @return
     */
    @GetMapping("service/monitor/{deviceId}/running/status")
    ResponseData<DeviceStatusVO> getRunningStatus(@PathVariable(value = "deviceId") Long deviceId);
}
package com.aimsphm.nuclear.report.feign.fallback;

import com.aimsphm.nuclear.common.entity.bo.TimeRangeQueryBO;
import com.aimsphm.nuclear.common.entity.vo.DeviceStatusVO;
import com.aimsphm.nuclear.common.entity.vo.LabelVO;
import com.aimsphm.nuclear.common.response.ResponseData;
import com.aimsphm.nuclear.report.feign.CoreServiceFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Package: com.aimsphm.nuclear.data.feign
 * @Description: <服务调用失败处理>
 * @Author: MILLA
 * @CreateDate: 2020/4/2 17:52
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/4/2 17:52
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Slf4j
//@Component
public class CoreServiceFeignFallback implements CoreServiceFeignClient {

    @Override
    public ResponseData<Map<Integer, Long>> listRunningDuration(Long deviceId, TimeRangeQueryBO range) {
        log.error("报告调用core服务异常...............");
        return null;
    }

    @Override
    public ResponseData<List<List<LabelVO>>> listWarningPoint(Long deviceId, TimeRangeQueryBO range) {
        log.error("报告调用core服务异常...............");
        return null;
    }

    @Override
    public ResponseData<DeviceStatusVO> getRunningStatus(Long deviceId) {
        log.error("报告调用core服务异常...............");
        return null;
    }
}

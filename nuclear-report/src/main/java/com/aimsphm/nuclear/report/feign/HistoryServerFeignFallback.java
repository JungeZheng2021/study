//package com.aimsphm.nuclear.report.feign;
//
//import com.aimsphm.nuclear.common.entity.bo.HistoryQueryMultiBO;
//import com.aimsphm.nuclear.common.entity.vo.FaultReasoningVO;
//import com.aimsphm.nuclear.common.entity.vo.HistoryDataVO;
//import com.aimsphm.nuclear.common.response.ResponseData;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.util.Map;
//
///**
// * @Package: com.aimsphm.nuclear.data.feign
// * @Description: <服务调用失败处理>
// * @Author: MILLA
// * @CreateDate: 2020/4/2 17:52
// * @UpdateUser: MILLA
// * @UpdateDate: 2020/4/2 17:52
// * @UpdateRemark: <>
// * @Version: 1.0
// */
//@Slf4j
//@Component
//@ConditionalOnProperty(prefix = "spring.config", name = "enableDataService", havingValue = "true")
//public class HistoryServerFeignFallback implements HistoryServerFeignClient {
//
//    @Override
//    public ResponseData<List<FaultReasoningVO>> faultReasoning(List<String> pointIds, Long deviceId, Long gmtLastAlarm) {
//        return null;
//    }
//
//    @Override
//    public ResponseData<Map<String, HistoryDataVO>> listHistoryWithPointList(HistoryQueryMultiBO queryMultiBO) {
//        return null;
//    }
//}

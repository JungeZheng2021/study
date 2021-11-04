package com.aimsphm.nuclear.history.feign;

import com.aimsphm.nuclear.common.entity.bo.HistoryQueryFilledBO;
import com.aimsphm.nuclear.common.response.ResponseData;
import com.aimsphm.nuclear.history.feign.fallback.DownSampleServiceFeignFallback;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * <p>
 * 功能描述:降采样服务调用
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2021/11/01 09:50
 */
@Component
@FeignClient(name = "down-sample", fallback = DownSampleServiceFeignFallback.class)
@ConditionalOnProperty(prefix = "spring.config", name = "enableFilled", havingValue = "true")
public interface DownSampleServiceFeignClient {
    /**
     * 历史查询补点
     *
     * @param bo 补点实体
     * @return 补点的结果集
     */
    @GetMapping(value = "history/filled")
    ResponseData<List<List<Object>>> historyQueryDataFilled(@SpringQueryMap HistoryQueryFilledBO bo);
}
package com.aimsphm.nuclear.history.feign.fallback;

import com.aimsphm.nuclear.common.entity.bo.HistoryQueryFilledBO;
import com.aimsphm.nuclear.common.response.ResponseData;
import com.aimsphm.nuclear.history.feign.DownSampleServiceFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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
@Slf4j
@Component
public class DownSampleServiceFeignFallback implements DownSampleServiceFeignClient {

    @Override
    public ResponseData<List<List<Object>>> historyQueryDataFilled(HistoryQueryFilledBO bo) {
        log.error("Invoke server failed........ rangTime:{}", bo);
        return null;
    }
}

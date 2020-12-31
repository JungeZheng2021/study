package com.aimsphm.nuclear.algorithm.service.impl;

import com.aimsphm.nuclear.algorithm.entity.bo.*;
import com.aimsphm.nuclear.algorithm.entity.dto.AlarmEventDTO;
import com.aimsphm.nuclear.algorithm.entity.dto.StateMonitorParamDTO;
import com.aimsphm.nuclear.algorithm.entity.dto.StateMonitorResponseDTO;
import com.aimsphm.nuclear.algorithm.enums.AlgorithmTypeEnum;
import com.aimsphm.nuclear.algorithm.service.AlgorithmAsyncService;
import com.aimsphm.nuclear.algorithm.service.AlgorithmHandlerService;
import com.aimsphm.nuclear.common.entity.*;
import com.aimsphm.nuclear.common.entity.dto.HBaseTimeSeriesDataDTO;
import com.aimsphm.nuclear.common.service.*;
import com.aimsphm.nuclear.common.util.HBaseUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

import static com.aimsphm.nuclear.common.constant.HBaseConstant.*;
import static com.aimsphm.nuclear.common.constant.SymbolConstant.DASH;

/**
 * @Package: com.aimsphm.nuclear.algorithm.service.impl
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2020/12/23 16:16
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/12/23 16:16
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Slf4j
@Service
public class AlgorithmAsyncServiceImpl implements AlgorithmAsyncService {

    @Resource
    private HBaseUtil hBase;

    @Async
    @Override
    public void listPointDataFromHBase(String family, String sensorCode, PointDataBO data, CountDownLatch countDownLatch) {
        log.info("请求数据.family-{}  .sensorCode-{}", family, sensorCode);
        long start = System.currentTimeMillis();
        try {
            List<HBaseTimeSeriesDataDTO> cells = hBase.listDataWithLimit(H_BASE_TABLE_NPC_PHM_DATA, family, sensorCode, 1, 60, 60);
            log.info("请求数据返回值-.family-{}  .sensorCode-{} 返回值数据量 {}  耗时-- {} 毫秒", family, sensorCode, cells.size(), (System.currentTimeMillis() - start));
            data.setCells(cells);
        } catch (Exception e) {
            data.setCells(Lists.newArrayList());
            log.error("get point data failed ...family：{},pre:{}, --{}", family, sensorCode, e);
        } finally {
            countDownLatch.countDown();
        }
    }
}

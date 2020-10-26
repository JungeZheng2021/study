package com.aimsphm.nuclear.common.service.impl;

import com.aimsphm.nuclear.common.entity.TxSensorTrend;
import com.aimsphm.nuclear.common.mapper.TxSensorTrendMapper;
import com.aimsphm.nuclear.common.service.TxSensorTrendService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
/**
 * 
 *
 * @author lu.yi
 * @since 2020-04-11
 */
@Slf4j
@Service
@ConditionalOnProperty(prefix = "spring.mybatisPlusConfig", name = "enable", havingValue = "true",matchIfMissing= false)
public class TxSensorTrendServiceImpl extends ServiceImpl<TxSensorTrendMapper, TxSensorTrend> implements TxSensorTrendService {

    @Autowired
    private TxSensorTrendMapper TxSensorTrendMapper;

}
package com.aimsphm.nuclear.common.service.impl;

import com.aimsphm.nuclear.common.entity.TxReportTagConfig;
import com.aimsphm.nuclear.common.mapper.TxReportTagConfigMapper;
import com.aimsphm.nuclear.common.service.TxReportTagConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * 报告生成测点配置表
 *
 * @author MILLA
 * @since 2020-06-12
 */
@Slf4j
@Service
@ConditionalOnProperty(prefix = "spring.mybatisPlusConfig", name = "enable", havingValue = "true", matchIfMissing = false)
public class TxReportTagConfigServiceImpl extends ServiceImpl<TxReportTagConfigMapper, TxReportTagConfig> implements TxReportTagConfigService {

    @Autowired
    private TxReportTagConfigMapper TxReportTagConfigMapper;

}
package com.aimsphm.nuclear.common.service.impl;

import com.aimsphm.nuclear.common.entity.MdModelsensor;
import com.aimsphm.nuclear.common.mapper.MdModelsensorMapper;
import com.aimsphm.nuclear.common.service.MdModelsensorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
/**
 * 
 *
 * @author lu.yi
 * @since 2020-04-13
 */
@Slf4j
@Service
@ConditionalOnProperty(prefix = "spring.mybatisPlusConfig", name = "enable", havingValue = "true",matchIfMissing= false)
public class MdModelsensorServiceImpl extends ServiceImpl<MdModelsensorMapper, MdModelsensor> implements MdModelsensorService {

    @Autowired
    private MdModelsensorMapper MdModelsensorMapper;

}
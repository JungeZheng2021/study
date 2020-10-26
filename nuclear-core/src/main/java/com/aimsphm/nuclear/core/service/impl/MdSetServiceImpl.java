package com.aimsphm.nuclear.core.service.impl;

import com.aimsphm.nuclear.core.entity.MdSet;
import com.aimsphm.nuclear.core.mapper.MdSetMapper;
import com.aimsphm.nuclear.core.service.MdSetService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * 
 *
 * @author lu.yi
 * @since 2020-03-18
 */
@Slf4j
@Service
public class MdSetServiceImpl extends ServiceImpl<MdSetMapper, MdSet> implements MdSetService {

    @Autowired
    private MdSetMapper MdSetMapper;

}
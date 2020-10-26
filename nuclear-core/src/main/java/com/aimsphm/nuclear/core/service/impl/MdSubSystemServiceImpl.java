package com.aimsphm.nuclear.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aimsphm.nuclear.common.entity.MdSubSystem;
import com.aimsphm.nuclear.core.mapper.MdSubSystemMapper;
import com.aimsphm.nuclear.core.service.MdSubSystemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import lombok.extern.slf4j.Slf4j;
/**
 * 
 *
 * @author lu.yi
 * @since 2020-03-18
 */
@Slf4j
@Service
public class MdSubSystemServiceImpl extends ServiceImpl<MdSubSystemMapper, MdSubSystem> implements MdSubSystemService {

    @Autowired
    private MdSubSystemMapper MdSubSystemMapper;

}
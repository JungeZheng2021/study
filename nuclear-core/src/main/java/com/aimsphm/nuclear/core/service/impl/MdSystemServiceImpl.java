package com.aimsphm.nuclear.core.service.impl;

import com.aimsphm.nuclear.core.entity.MdSystem;
import com.aimsphm.nuclear.core.mapper.MdSystemMapper;
import com.aimsphm.nuclear.core.service.MdSystemService;
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
public class MdSystemServiceImpl extends ServiceImpl<MdSystemMapper, MdSystem> implements MdSystemService {

    @Autowired
    private MdSystemMapper MdSystemMapper;

}
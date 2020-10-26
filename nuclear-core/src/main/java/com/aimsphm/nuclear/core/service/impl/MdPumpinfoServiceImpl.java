package com.aimsphm.nuclear.core.service.impl;

import com.aimsphm.nuclear.core.entity.MdPumpinfo;
import com.aimsphm.nuclear.core.mapper.MdPumpinfoMapper;
import com.aimsphm.nuclear.core.service.MdPumpinfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author lu.yi
 * @since 2020-03-18
 */
@Slf4j
@Service
public class MdPumpinfoServiceImpl extends ServiceImpl<MdPumpinfoMapper, MdPumpinfo> implements MdPumpinfoService {

    @Autowired
    private MdPumpinfoMapper MdPumpinfoMapper;

    @Override
    @Transactional
    public int updatePumpInfo(MdPumpinfo info) throws Exception {

        int count = MdPumpinfoMapper.updateOptimisticLock(info, new Date());
        if(count==0){
            throw new Exception("consistence error");
        }
        return count;
    }
}
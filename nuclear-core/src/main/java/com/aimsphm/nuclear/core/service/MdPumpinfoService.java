package com.aimsphm.nuclear.core.service;

import com.aimsphm.nuclear.core.entity.MdPumpinfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 
 *
 * @author lu.yi
 * @since 2020-03-18
 */
public interface MdPumpinfoService extends IService<MdPumpinfo> {

    public int updatePumpInfo(MdPumpinfo info) throws Exception;
}
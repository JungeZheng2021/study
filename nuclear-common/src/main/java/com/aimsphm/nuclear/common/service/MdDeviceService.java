package com.aimsphm.nuclear.common.service;

import com.aimsphm.nuclear.common.constant.CommonConstant;
import com.aimsphm.nuclear.common.entity.MdDevice;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.cache.annotation.Cacheable;

import java.io.Serializable;
import java.util.List;

/**
 * 
 *
 * @author lu.yi
 * @since 2020-03-18
 */
public interface MdDeviceService extends IService<MdDevice> {

    String  getNameById(Serializable Id);

 
    String getOnlyNameById(Serializable id);

     List<MdDevice> getDevicesBySubSystemId(Long subSystemId);

    Integer getTypeById(Serializable id);
}